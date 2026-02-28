#!/usr/bin/env node

import fs from "node:fs/promises";
import path from "node:path";

const ACS_YEAR = "2023";
const ACS_BASE = `https://api.census.gov/data/${ACS_YEAR}/acs/acs5`;
const ROOT = process.cwd();
const CITIES_PATH = path.join(ROOT, "app/src/main/resources/data/cities.json");
const OUTPUT_PATH = path.join(
  ROOT,
  "app/src/main/resources/data/city_economic_facts.json",
);

const STATE_FIPS = {
  AL: "01",
  AK: "02",
  AZ: "04",
  AR: "05",
  CA: "06",
  CO: "08",
  CT: "09",
  DE: "10",
  DC: "11",
  FL: "12",
  GA: "13",
  HI: "15",
  ID: "16",
  IL: "17",
  IN: "18",
  IA: "19",
  KS: "20",
  KY: "21",
  LA: "22",
  ME: "23",
  MD: "24",
  MA: "25",
  MI: "26",
  MN: "27",
  MS: "28",
  MO: "29",
  MT: "30",
  NE: "31",
  NV: "32",
  NH: "33",
  NJ: "34",
  NM: "35",
  NY: "36",
  NC: "37",
  ND: "38",
  OH: "39",
  OK: "40",
  OR: "41",
  PA: "42",
  RI: "44",
  SC: "45",
  SD: "46",
  TN: "47",
  TX: "48",
  UT: "49",
  VT: "50",
  VA: "51",
  WA: "53",
  WV: "54",
  WI: "55",
  WY: "56",
};

const CITY_ALIAS = {
  "nashville|tn": "nashville davidson",
  "washington|dc": "washington",
  "las vegas|nv": "las vegas",
  "st louis|mo": "st louis",
  "st paul|mn": "saint paul",
  "saint paul|mn": "st paul",
};

const ACS_VARIABLES = [
  "NAME",
  "B19013_001E", // median household income
  "B25064_001E", // median gross rent
  "B25003_001E", // total occupied housing units
  "B25003_003E", // renter-occupied housing units
  "B25070_008E", // gross rent 35.0 to 39.9 percent of income
  "B25070_009E", // gross rent 40.0 to 49.9 percent of income
  "B25070_010E", // gross rent 50 percent or more of income
];

function normalizeName(raw) {
  return raw
    .toLowerCase()
    .split(",")[0]
    .replace(/\([^)]*\)/g, " ")
    .replace(/[.'`-]/g, " ")
    .replace(
      /\b(city|town|village|borough|cdp|municipality|metro|metropolitan|government|balance|county|urban|consolidated|charter|township)\b/g,
      " ",
    )
    .replace(/\s+/g, " ")
    .trim();
}

function parseNum(v) {
  const n = Number(v);
  if (!Number.isFinite(n) || n < 0) {
    return null;
  }
  return n;
}

function safePct(numerator, denominator) {
  if (
    numerator === null ||
    denominator === null ||
    denominator <= 0 ||
    !Number.isFinite(numerator) ||
    !Number.isFinite(denominator)
  ) {
    return null;
  }
  return Math.round((numerator / denominator) * 1000) / 10;
}

function parseRows(rawRows) {
  if (!Array.isArray(rawRows) || rawRows.length <= 1) {
    return [];
  }
  const headers = rawRows[0];
  return rawRows.slice(1).map((row) => {
    const item = {};
    headers.forEach((h, i) => {
      item[h] = row[i];
    });
    return item;
  });
}

async function fetchJson(url) {
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error(`Request failed (${res.status}) for ${url}`);
  }
  return res.json();
}

function buildAcsUrl(stateFips, forPlaces = true) {
  const params = new URLSearchParams();
  params.set("get", ACS_VARIABLES.join(","));
  if (forPlaces) {
    params.set("for", "place:*");
    params.set("in", `state:${stateFips}`);
  } else {
    params.set("for", `state:${stateFips}`);
  }
  return `${ACS_BASE}?${params.toString()}`;
}

function buildPlaceLookup(stateFips, rows) {
  const map = new Map();
  for (const r of rows) {
    const placeCode = r.place;
    if (!placeCode) {
      continue;
    }
    const normalized = normalizeName(r.NAME || "");
    if (!normalized) {
      continue;
    }
    const key = `${stateFips}|${normalized}`;
    if (!map.has(key)) {
      map.set(key, []);
    }
    map.get(key).push(r);
  }
  return map;
}

function chooseBestPlace(city, state, stateFips, placeRows, placeLookup) {
  const cityNorm = normalizeName(city);
  const alias = CITY_ALIAS[`${city.toLowerCase()}|${state.toLowerCase()}`];
  const target = alias || cityNorm;
  const exact = placeLookup.get(`${stateFips}|${target}`);
  if (exact && exact.length > 0) {
    return exact[0];
  }

  let best = null;
  let bestScore = -1;
  for (const row of placeRows) {
    const cand = normalizeName(row.NAME || "");
    if (!cand) {
      continue;
    }

    let score = 0;
    if (cand === target) {
      score = 100;
    } else if (cand.startsWith(target) || target.startsWith(cand)) {
      score = 80;
    } else if (cand.includes(target) || target.includes(cand)) {
      score = 70;
    } else {
      const targetWords = new Set(target.split(" "));
      const candWords = new Set(cand.split(" "));
      let overlap = 0;
      for (const w of targetWords) {
        if (candWords.has(w)) {
          overlap++;
        }
      }
      if (overlap > 0) {
        score = Math.round((overlap / Math.max(targetWords.size, candWords.size)) * 60);
      }
    }

    if (score > bestScore) {
      bestScore = score;
      best = row;
    }
  }

  if (bestScore < 40) {
    return null;
  }
  return best;
}

async function main() {
  const citiesRaw = JSON.parse(await fs.readFile(CITIES_PATH, "utf8"));
  const cityEntries = citiesRaw.cities || [];

  const states = [...new Set(cityEntries.map((c) => c.state))].filter((s) => STATE_FIPS[s]);

  const stateData = new Map();
  for (const state of states) {
    const fips = STATE_FIPS[state];
    const url = buildAcsUrl(fips, true);
    const rows = parseRows(await fetchJson(url));
    stateData.set(state, {
      fips,
      rows,
      lookup: buildPlaceLookup(fips, rows),
    });
  }

  const cities = cityEntries.map((entry) => {
    const statePack = stateData.get(entry.state);
    if (!statePack) {
      return {
        city: entry.city,
        state: entry.state,
        year: Number(ACS_YEAR),
        census_place_name: null,
        census_place_geoid: null,
        median_household_income: null,
        median_gross_rent: null,
        occupied_households: null,
        renter_households: null,
        renter_share_pct: null,
        rent_burdened_35_plus_households: null,
        rent_burdened_35_plus_share_pct: null,
        annual_rent_to_income_pct: null,
        sources: [
          `${ACS_BASE}`,
          "https://www.census.gov/programs-surveys/acs/data/data-via-api.html",
        ],
        missing_reason: "State FIPS mapping unavailable",
      };
    }

    const chosen = chooseBestPlace(
      entry.city,
      entry.state,
      statePack.fips,
      statePack.rows,
      statePack.lookup,
    );

    if (!chosen) {
      return {
        city: entry.city,
        state: entry.state,
        year: Number(ACS_YEAR),
        census_place_name: null,
        census_place_geoid: null,
        median_household_income: null,
        median_gross_rent: null,
        occupied_households: null,
        renter_households: null,
        renter_share_pct: null,
        rent_burdened_35_plus_households: null,
        rent_burdened_35_plus_share_pct: null,
        annual_rent_to_income_pct: null,
        sources: [
          `${ACS_BASE}`,
          "https://www.census.gov/programs-surveys/acs/data/data-via-api.html",
        ],
        missing_reason: "No matching Census place found",
      };
    }

    const income = parseNum(chosen.B19013_001E);
    const medianRent = parseNum(chosen.B25064_001E);
    const occupied = parseNum(chosen.B25003_001E);
    const renters = parseNum(chosen.B25003_003E);
    const b35 = parseNum(chosen.B25070_008E) ?? 0;
    const b40 = parseNum(chosen.B25070_009E) ?? 0;
    const b50 = parseNum(chosen.B25070_010E) ?? 0;
    const burdened = renters === null ? null : b35 + b40 + b50;
    const annualRentToIncome =
      income && medianRent ? Math.round(((medianRent * 12) / income) * 1000) / 10 : null;

    return {
      city: entry.city,
      state: entry.state,
      year: Number(ACS_YEAR),
      census_place_name: chosen.NAME,
      census_place_geoid: `${chosen.state}${chosen.place}`,
      median_household_income: income,
      median_gross_rent: medianRent,
      occupied_households: occupied,
      renter_households: renters,
      renter_share_pct: safePct(renters, occupied),
      rent_burdened_35_plus_households: burdened,
      rent_burdened_35_plus_share_pct: safePct(burdened, renters),
      annual_rent_to_income_pct: annualRentToIncome,
      sources: [
        `${ACS_BASE}?get=${ACS_VARIABLES.join(",")}&for=place:*&in=state:${statePack.fips}`,
        "https://www.census.gov/programs-surveys/acs/data/data-via-api.html",
      ],
      missing_reason: null,
    };
  });

  const matched = cities.filter((c) => !c.missing_reason).length;
  const output = {
    version: new Date().toISOString().slice(0, 10),
    dataset: `ACS ${ACS_YEAR} 5-year`,
    notes: "City economic facts for SEO uniqueness and market context blocks.",
    coverage: {
      total_cities: cities.length,
      matched_cities: matched,
      unmatched_cities: cities.length - matched,
    },
    cities,
  };

  await fs.writeFile(OUTPUT_PATH, `${JSON.stringify(output, null, 2)}\n`, "utf8");
  console.log(`Wrote ${OUTPUT_PATH}`);
  console.log(`Matched ${matched}/${cities.length} cities.`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
