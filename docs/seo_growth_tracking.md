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

## 5) 개선 백로그 (우선순위)

| Priority | Task | Expected Impact | Status | Target Date |
|---|---|---|---|---|
| P0 | Sitemap 축소/재우선순위 (핵심 URL 우선) | 크롤/인덱싱 집중 | DONE (2026-03-05) | 2026-03-10 |
| P0 | URL 정규화 (slash 정책 통일 + 301) | 중복/404 변형 감소 | DONE (2026-03-05) | 2026-03-10 |
| P0 | 저효율 조합형 페이지 단계적 noindex 검토 | 인덱싱 신호 집중 | DONE (1차, 2026-03-05) | 2026-03-12 |
| P1 | 상위 노출 무클릭 페이지 title/meta 개편 | CTR 개선 | DONE (1차, 2026-03-05) | 2026-03-12 |
| P1 | 내부링크 강화 (핵심 허브 -> 핵심 랜딩) | 크롤 경로 개선 | DONE (1차, 2026-03-05) | 2026-03-14 |
| P1 | 링크 가능한 리서치 자산 발행 (월간 Move-In Index) | E-E-A-T/외부 인용 자산 | DONE (2026-03-05) | 2026-03-14 |
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

## 7) 실험 추적 (Experiment Log)

| Start Date | Experiment | Hypothesis | Success Metric | Check Date | Result |
|---|---|---|---|---|---|
| 2026-03-05 | P0 실행 전 베이스라인 확보 | 개선 후 인덱싱/노출 상승 | Indexed 상승, Discovered-Not-Indexed 하락 | 2026-03-19 | Pending |

## 8) 업데이트 루틴 (권장)

매주 2회(예: 화/금) 아래 순서로 갱신:

1. GSC 성과(최근 28일) 갱신: Impr/Clicks/CTR/Pos
2. GSC 색인 리포트 갱신: Indexed/Not Indexed/Discovered
3. 라이브 체크: robots, sitemap, 샘플 URL 상태코드
4. 이번 주 배포 변경사항과 KPI 변화를 연결 기록
5. 백로그 상태(`TODO -> DOING -> DONE`) 갱신

## 9) 다음 체크 포인트

- 2026-03-10: P0 1차 적용 후 재측정
- 2026-03-19: 첫 번째 반영 지연 구간(약 2주) 결과 확인

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
