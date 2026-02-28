# City Economic Facts Enrichment (ACS)

This project now supports a place-level economic facts dataset for each city:

- Median household income
- Median gross rent
- Renter share of occupied households
- Rent-burdened renter share (35%+ of income)
- Annual rent-to-income ratio

## Source

- US Census ACS 5-year API (2023)
- Endpoint base: `https://api.census.gov/data/2023/acs/acs5`
- API docs: `https://www.census.gov/programs-surveys/acs/data/data-via-api.html`

## Generate the dataset

From repo root:

```powershell
$env:NODE_OPTIONS='--use-system-ca'
& 'C:/Users/Administrator/FirstRentVerdict/tools/node-v24.14.0-win-x64/node.exe' tools/fetch_city_economic_facts.mjs
```

Output file:

- `app/src/main/resources/data/city_economic_facts.json`

## Runtime wiring

- Loader: `JsonDataLoader#loadCityEconomicFacts`
- Repository accessor: `VerdictDataRepository#getCityEconomicFact`
- Content integration: `CityContentGenerator`

If the dataset is missing, the app falls back to existing rent/deposit/moving/pet data without failing startup.

## IRS migration enrichment

To generate state-level inbound migration corridors (used to prioritize moving pair sitemap origins):

```powershell
$env:NODE_OPTIONS='--use-system-ca'
& 'C:/Users/Administrator/FirstRentVerdict/tools/node-v24.14.0-win-x64/node.exe' tools/fetch_state_migration_flows.mjs
```

Output file:

- `app/src/main/resources/data/state_migration_flows.json`

Source:

- `https://www.irs.gov/pub/irs-soi/stateinflow2122.csv`
