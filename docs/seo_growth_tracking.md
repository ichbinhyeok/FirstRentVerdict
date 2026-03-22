# SEO Growth Tracking (FirstRentVerdict)

이 문서는 Search Console/Analytics/라이브 점검 결과를 날짜별로 추적하고, 개선 작업과 결과를 연결하기 위한 운영 로그다.

## 1) 목표와 판단 기준

- 목표: `노출 -> 클릭 -> 상호작용` 흐름을 안정적으로 늘린다.
- 현재 판단: 주제 실패보다는 `인덱싱/크롤 분산/CTR` 실행 이슈가 우선 병목이다.
- 기대 효과: 개선안을 모두 적용하면 **개선 확률은 높다**. 다만 검색 반영은 보통 2~8주 지연이 발생할 수 있다.

## 2) 핵심 KPI 정의

- `Impressions`: Search Console 검색 노출
- `Clicks`: Search Console 클릭
- `CTR`: 클릭률
- `Avg Position`: 평균 게재 순위
- `Indexed`: 색인 생성됨 페이지 수
- `Not Indexed`: 색인 제외 페이지 수
- `Discovered - currently not indexed`: 발견되었으나 미색인 페이지 수

## 3) 스냅샷 로그

| Date | Data Window | Impr. | Clicks | CTR | Avg Pos | Indexed | Not Indexed | Discovered-Not-Indexed | Notes |
|---|---|---:|---:|---:|---:|---:|---:|---:|---|
| 2026-03-05 | 2026-01-23 ~ 2026-01-31 (GSC export) | 80 | 1 | 1.25% | 9.28 | 17 | 92 | 89 | 라이브 점검: robots/sitemap 200, sitemap URL 1,810개 |
| 2026-03-09 | 2026-02-06 ~ 2026-03-06 (GSC API) | 241 | 4 | 1.66% | 5.87 | - | - | - | 최근 28일 자체는 개선. 다만 최근 7일은 표본이 작고, 재크롤 반영이 섞여 있음 |
| 2026-03-22 | 2026-02-19 ~ 2026-03-19 (GSC MCP) | 290 | 4 | 1.38% | 10.09 | 대표 URL indexed | sitemap 제출 192 | moving-from 샘플 unknown | 최근 7일은 전주 대비 하락. research 허브는 2026-03-21 크롤, legacy `first-month-cost` 샘플은 아직 구 상태 잔존 |

## 4) 현재 문제 진단 (2026-03-05)

1. 인덱싱 병목이 큼  
`Indexed 17` 대비 `Not Indexed 92`, 그중 `Discovered - currently not indexed 89`.

2. Sitemap이 큰 편 (초기 사이트 대비)  
총 1,810 URL 중 `moving-from` 유형이 900개로 절반 비중.

3. URL 정규화 일관성 부족  
일부 경로는 trailing slash에서 200, 일부는 404.

4. 레거시 라우트 재크롤 가능성  
`no-cosigner`, `salary-needed`, `compare` 등은 410 처리되어 있으나, 구 URL 유입이 계속되면 크롤 예산 소모.

5. CTR 병목  
상위권(약 5~10위) 근처 페이지 중 무클릭 페이지 다수.

## 4-1) 추가 진단 업데이트 (2026-03-09)

1. 2026-03-05 배포는 라이브에 이미 반영됨  
`/RentVerdict/research/move-in-cost-index` 200, `/sitemap.xml` 200, `/robots.txt` 200, `3000 savings`와 `moving-from` 샘플은 라이브 HTML 기준 noindex 확인.

2. 구글 반영은 아직 부분적  
리서치 페이지는 Search Console URL 검사 기준 `2026-03-05`에 이미 크롤 및 색인됨. 반면 `3000 savings` 샘플은 마지막 크롤이 `2026-02-25`라 noindex 변경을 아직 못 봤고, `moving-from` 샘플은 `URL is unknown to Google`.

3. 최근 저노출은 “실패”보다 “재크롤 지연 + 적은 표본” 가능성이 큼  
최근 28일은 이전 28일 대비 개선(클릭 1 -> 4, 노출 89 -> 240, 평균 순위 10.74 -> 5.86). 하지만 최근 7일은 노출이 작아 일별 변동성이 큼.

4. 기술 보정 포인트 발견  
리서치 페이지 JSON-LD에서 `Bad escape sequence in string` 오류가 확인됨. 원인은 날짜/URL 문자열 escape 방식이며 2026-03-09 코드 수정으로 보정.

5. 레거시 `first-month-cost` 경로는 410보다 301이 더 실용적  
구 URL이 아직 검색 노출 흔적을 남기고 있어, 관련 신호를 리서치 허브로 모으기 위해 `/RentVerdict/research/move-in-cost-index` 로 영구 리다이렉트 전환.

## 4-2) 추가 진단 업데이트 (2026-03-22)

1. Search Console MCP 연결 복구 후 실측 확인  
`sc-domain:movecostinfo.com` 기준 최근 28일은 `4 clicks / 290 impressions / CTR 1.38% / avg position 10.09`.

2. 최근 7일은 실제로 악화  
`2026-03-12 ~ 2026-03-19` 는 직전 7일 대비 클릭 `1 -> 0`, 노출 `47 -> 25`, 평균 순위 `21.77 -> 37.60` 으로 악화. 다만 절대 표본이 작아 변동성도 큼.

3. 대표 research 허브는 정상 색인  
`/RentVerdict/research/move-in-cost-index` 는 `Submitted and indexed`, 마지막 크롤은 `2026-03-21`, Google/User canonical 일치.

4. noindex / 크롤 억제 전략은 의도대로 작동 중  
`moving-from` 샘플은 `URL is unknown to Google`. 이는 현재 noindex + sitemap 제외 전략과 일치.

5. 레거시 `first-month-cost` 신호는 아직 완전히 정리되지 않음  
샘플 `https://movecostinfo.com/RentVerdict/first-month-cost/1000/dc` 는 GSC URL 검사 기준 아직 `Submitted and indexed`, 마지막 크롤 `2026-02-10`. 즉 라이브 301 변경은 반영됐지만 Google 재처리는 아직 덜 끝남.

6. 현재 병목은 “CTR” 하나가 아니라 “미국 타깃 랭킹 부족 + 레거시 잔존 + 표본 부족”  
국가별로는 한국 `3 clicks / 126 impressions / position 1.40`, 미국 `1 click / 127 impressions / position 19.02`. 미국 타깃 쿼리 다수는 아직 `60~90위` 구간에 머묾.

## 5) 개선 백로그 (우선순위)

| Priority | Task | Expected Impact | Status | Target Date |
|---|---|---|---|---|
| P0 | Sitemap 축소/재우선순위 (핵심 URL 우선) | 크롤/인덱싱 집중 | DONE (2026-03-05) | 2026-03-10 |
| P0 | URL 정규화 (slash 정책 통일 + 301) | 중복/404 변형 감소 | DONE (2026-03-05) | 2026-03-10 |
| P0 | 저효율 조합형 페이지 단계적 noindex 검토 | 인덱싱 신호 집중 | DONE (1차, 2026-03-05) | 2026-03-12 |
| P0 | 대표 URL 재색인 요청 (`/RentVerdict/research/move-in-cost-index`, `/RentVerdict/`, `/RentVerdict/cities`) | 수정 반영 가속 | TODO (manual UI) | 2026-03-22 |
| P0 | 레거시 `first-month-cost` -> research 301 배포 확인 | 구 URL 신호 회수 | DONE (code, 2026-03-09) | 2026-03-10 |
| P0 | 리서치 페이지 JSON-LD 오류 수정 배포 확인 | Rich result 파싱 정상화 | DONE (code, 2026-03-09) | 2026-03-10 |
| P0 | legacy `first-month-cost` 샘플 재처리 확인 | 구 URL 잔존 신호 제거 | DOING | 2026-04-05 |
| P1 | 상위 노출 무클릭 페이지 title/meta 개편 | CTR 개선 | DONE (1차, 2026-03-05) | 2026-03-12 |
| P1 | 내부링크 강화 (핵심 허브 -> 핵심 랜딩) | 크롤 경로 개선 | DONE (1차, 2026-03-05) | 2026-03-14 |
| P1 | 링크 가능한 리서치 자산 발행 (월간 Move-In Index) | E-E-A-T/외부 인용 자산 | DONE (2026-03-05) | 2026-03-14 |
| P1 | 미국 실쿼리 대응용 랜딩 정렬 (`pet deposit`, `rental market`, `average rent`) | 미국 타깃 랭킹 개선 | DOING (code, 2026-03-22) | 2026-04-05 |
| P1 | Wichita / St. Petersburg sitemap 우선 편입 | 실쿼리 도시 크롤 집중 | DONE (code, 2026-03-22) | 2026-03-22 |
| P2 | GA4 리포트 자동 집계 연결 | 행동 데이터 확인 | BLOCKED (권한) | 2026-03-14 |

## 6) 적용 이력 (Changes Log)

| Date | Change | Type | Verified |
|---|---|---|---|
| 2026-03-05 | 테스트 트래픽 GA4 차단 가드 추가 (`localhost`, 자동화 브라우저, `frv_test`, `frv_analytics=off`) | Analytics Hygiene | Yes (test pass) |
| 2026-03-05 | Sitemap 기본 정책 축소: relocation-pair 제외, credit-good 제외, savings는 5000만 포함 | Crawl/Index Focus | Yes (test pass) |
| 2026-03-05 | Wave Sitemap 2차: city-limit(30) + priority-city-slugs 적용, 핵심 파형 크롤 전략 반영 | Crawl/Index Focus | Yes (test pass) |
| 2026-03-05 | trailing slash URL 301 정규화 필터 추가 (`/`, `/RentVerdict/` 제외) | URL Canonicalization | Yes (test pass) |
| 2026-03-05 | `moving-from` 페이지 기본 noindex, savings는 `3000/10000` noindex | Indexing Control | Yes (test pass) |
| 2026-03-05 | 홈/소개/방법론/도시 랜딩 메타 카피 개선 + 허브 내부링크 강화 | CTR & Internal Linking | Yes (test pass) |
| 2026-03-05 | 리서치 페이지 추가: `/RentVerdict/research/move-in-cost-index` | Authority Asset | Yes (test pass) |
| 2026-03-09 | 리서치 페이지 JSON-LD escape 수정 (`datePublished`, `dateModified`, `mainEntityOfPage`) | Structured Data | Yes (`VerdictControllerTest`) |
| 2026-03-09 | 레거시 `/RentVerdict/first-month-cost/{rent}/{state}` 를 research 허브로 301 전환 | Legacy URL Consolidation | Yes (`VerdictControllerTest`) |
| 2026-03-22 | GSC MCP 연결 복구 및 `sc-domain:movecostinfo.com` 실측 점검 | Search Console Ops | Yes (live MCP query) |
| 2026-03-22 | `Average Pet Deposit` / `Rental Market` 중심 메타 정렬 | Query Alignment | Pending deploy |
| 2026-03-22 | sitemap 우선 도시에 `st-petersburg-fl`, `wichita-ks` 추가 | Crawl Focus | Pending deploy |

## 7) 실험 추적 (Experiment Log)

| Start Date | Experiment | Hypothesis | Success Metric | Check Date | Result |
|---|---|---|---|---|---|
| 2026-03-05 | P0 실행 전 베이스라인 확보 | 개선 후 인덱싱/노출 상승 | Indexed 상승, Discovered-Not-Indexed 하락 | 2026-03-19 | Pending |
| 2026-03-09 | 리서치 허브 신호 집중 (JSON-LD + legacy redirect) | 대표 research URL의 재크롤/재색인/노출 안정화 | 대표 URL 크롤 갱신, Rich Result 오류 해소 | 2026-03-19 | Pending |
| 2026-03-22 | 미국 실쿼리 정렬 (`average pet deposit`, `rental market`, `average rent`) | 도시/펫 랜딩이 미국 쿼리에서 2페이지 안쪽으로 접근 | 미국 기준 노출 증가, 해당 URL 재크롤, 평균순위 개선 | 2026-04-05 | Pending |

## 8) 업데이트 루틴 (권장)

매주 2회(예: 화/금) 아래 순서로 갱신:

1. GSC MCP 최근 28일 갱신: Impr/Clicks/CTR/Pos
2. GSC MCP 최근 7일 vs 직전 7일 비교
3. URL 검사: research 허브, legacy 샘플, noindex 샘플, moving-from 샘플
4. sitemap 제출 수 / 다운로드 일시 / indexed 상태 기록
5. 국가 분리(`usa`, `kor`)로 타깃 노출 왜곡 여부 확인
6. 이번 주 배포 변경사항과 KPI 변화를 연결 기록
7. 백로그 상태(`TODO -> DOING -> DONE`) 갱신

## 9) 다음 체크 포인트

- 2026-03-22: Search Console UI에서 대표 URL 재색인 요청
- 2026-04-05: meta/query alignment 배포 후 첫 반영 구간 확인
- 2026-04-05: legacy `first-month-cost` 샘플이 여전히 indexed 인지 재확인
- 2026-04-12: 미국(`usa`) 기준 쿼리/페이지 노출이 실제로 늘었는지 재확인

## 10) 페르소나 검증 로그

### 2026-03-05 (자동 검증)

- Playwright `persona-beta.spec.ts` 실행: Desktop + Mobile 총 8/8 PASS
- API 시뮬레이션 페르소나 매트릭스: 8개 케이스 중 7개 정상 응답, 1개 의도된 validation 400

| Persona Case | Input Summary | Expected Direction | Result |
|---|---|---|---|
| A_HighCash_Local_NYC_Good | NYC, high cash, local, good credit | 승인 | APPROVED |
| B_LowCash_Local_NYC_Good | NYC, low cash, local, good credit | 거절 | DENIED |
| C_MidCash_Local_Austin_Fair | Austin, mid cash, fair credit | 승인/경계 | APPROVED |
| D_Pet_Local_SF_Good | SF, pet=true | 반려동물 비용 반영 승인 가능 | APPROVED |
| E_LongDistance_Seattle_Poor_WithOrigin | Seattle, long-distance, poor credit, origin 제공 | 계산 성공 | APPROVED |
| F_LongDistance_Seattle_Poor_NoOrigin | Seattle, long-distance, poor credit, origin 없음 | 입력 오류 | 400 Bad Request (정상) |
| G_LowRent_HighCash_Wichita_Good | Wichita, 저임대/고현금 | 승인 | APPROVED |
| H_HighRent_LowCash_Miami_Fair | Miami, 고임대/저현금, fair credit | 거절 | DENIED |
