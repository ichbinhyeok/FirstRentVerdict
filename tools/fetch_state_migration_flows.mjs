#!/usr/bin/env node

import fs from "node:fs/promises";
import path from "node:path";

const ROOT = process.cwd();
const OUTPUT_PATH = path.join(
  ROOT,
  "app/src/main/resources/data/state_migration_flows.json",
);

const INFLOW_URL = "https://www.irs.gov/pub/irs-soi/stateinflow2122.csv";
const FIPS_TO_STATE = {
  "01": "AL",
  "02": "AK",
  "04": "AZ",
  "05": "AR",
  "06": "CA",
  "08": "CO",
  "09": "CT",
  "10": "DE",
  "11": "DC",
  "12": "FL",
  "13": "GA",
  "15": "HI",
  "16": "ID",
  "17": "IL",
  "18": "IN",
  "19": "IA",
  "20": "KS",
  "21": "KY",
  "22": "LA",
  "23": "ME",
  "24": "MD",
  "25": "MA",
  "26": "MI",
  "27": "MN",
  "28": "MS",
  "29": "MO",
  "30": "MT",
  "31": "NE",
  "32": "NV",
  "33": "NH",
  "34": "NJ",
  "35": "NM",
  "36": "NY",
  "37": "NC",
  "38": "ND",
  "39": "OH",
  "40": "OK",
  "41": "OR",
  "42": "PA",
  "44": "RI",
  "45": "SC",
  "46": "SD",
  "47": "TN",
  "48": "TX",
  "49": "UT",
  "50": "VT",
  "51": "VA",
  "53": "WA",
  "54": "WV",
  "55": "WI",
  "56": "WY",
};
const VALID_STATE_CODES = new Set(Object.values(FIPS_TO_STATE));

function parseCsv(text) {
  const lines = text
    .split(/\r?\n/)
    .map((l) => l.replace(/^\uFEFF/, "").replace(/"/g, ""))
    .filter((l) => l.trim().length > 0);
  if (lines.length <= 1) {
    return [];
  }
  const headers = lines[0].split(",");
  return lines.slice(1).map((line) => {
    const cols = line.split(",");
    const obj = {};
    headers.forEach((h, i) => {
      obj[h] = (cols[i] || "").trim();
    });
    return obj;
  });
}

function toNum(v) {
  const n = Number(v);
  return Number.isFinite(n) ? n : 0;
}

function isStateLike(code) {
  return /^[A-Z]{2}$/.test(code || "");
}

async function main() {
  const res = await fetch(INFLOW_URL);
  if (!res.ok) {
    throw new Error(`Failed to download IRS inflow CSV (${res.status})`);
  }
  const csv = await res.text();
  const rows = parseCsv(csv);

  const byDest = new Map();

  for (const r of rows) {
    const toState = FIPS_TO_STATE[r.y2_statefips];
    const fromState = r.y1_state;
    const toFips = r.y2_statefips;
    const fromFips = r.y1_statefips;

    if (!isStateLike(toState) || !isStateLike(fromState)) {
      continue;
    }
    if (!VALID_STATE_CODES.has(toState) || !VALID_STATE_CODES.has(fromState)) {
      continue;
    }
    if (toState === fromState) {
      continue;
    }
    if (["96", "97", "98"].includes(fromFips) || ["96", "97", "98"].includes(toFips)) {
      continue;
    }

    if (!byDest.has(toState)) {
      byDest.set(toState, []);
    }
    byDest.get(toState).push({
      from_state: fromState,
      returns: toNum(r.n1),
      exemptions: toNum(r.n2),
      agi_thousands: toNum(r.AGI),
    });
  }

  const flows = [...byDest.entries()]
    .sort((a, b) => a[0].localeCompare(b[0]))
    .map(([toState, arr]) => {
      const sorted = arr.sort((a, b) => b.returns - a.returns);
      const totalReturns = sorted.reduce((acc, x) => acc + x.returns, 0);
      const topOrigins = sorted.slice(0, 12).map((x) => ({
        ...x,
        share_pct: totalReturns > 0 ? Math.round((x.returns / totalReturns) * 1000) / 10 : 0,
      }));
      return {
        to_state: toState,
        total_inbound_returns: totalReturns,
        top_origins: topOrigins,
      };
    });

  const out = {
    version: new Date().toISOString().slice(0, 10),
    dataset: "IRS SOI State-to-State Migration 2021-2022",
    source_url: INFLOW_URL,
    notes: "Top inbound origin states by number of tax returns for each destination state.",
    states: flows,
  };

  await fs.writeFile(OUTPUT_PATH, `${JSON.stringify(out, null, 2)}\n`, "utf8");
  console.log(`Wrote ${OUTPUT_PATH}`);
  console.log(`States covered: ${flows.length}`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
