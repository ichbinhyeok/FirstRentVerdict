# Project Context: FirstRentVerdict

## 1. Project Overview
**FirstRentVerdict** is a web application designed to assess the financial feasibility of renting a property based on user inputs (rent, available cash, move specifics). It provides a "Verdict" (Approved, Borderline, Denied) and detailed financial breakdowns including upfront costs and recommended safety buffers.

## 2. Technology Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.4.1
- **Template Engine**: JTE (Java Template Engine) 3.1.12
- **Build Tool**: Gradle
- **Deployment**: Docker (Eclipse Temurin 21-JDK Alpine)
- **Testing**: JUnit 5

## 3. Core Logic: Verdict Assessment
Located in `firstrentverdict.service.core.VerdictService`, the application evaluates financial health using the following logic:

### Inputs
- Monthly Rent
- Available Cash
- City/State (used to fetch regional data like Security Deposit caps, Moving costs)
- Pet ownership (adds fees)

### Cost Calculation
1. **Security Deposit**: Calculated based on typical multipliers for the region, respecting state legal caps (e.g., if a state caps deposit at 2x rent, the system enforces it).
2. **Moving Costs**: Derived from regional data.
3. **Pet Fees**: Added if applicable.
4. **Total Upfront Cost**: `First Month Rent + Security Deposit + Moving Costs + Pet Fees`
5. **Remaining Cash**: `User Available Cash - Total Upfront Cost`

### Decision Engine (The Verdict)
- **DENIED**:
  - If `Remaining Cash < 0` (Immediate Insolvency)
  - If `Remaining Cash < (Recommended Buffer * 0.5)` (Critical Liquidity Risk)
- **BORDERLINE**:
  - If `Remaining Cash < Recommended Buffer` (Thin Buffer Warning)
- **APPROVED**:
  - If `Remaining Cash >= Recommended Buffer`

### Smart Features
- **Bottleneck Analysis**: Identifies the primary reason for a negative verdict (e.g., "Immediate Insolvency").
- **Market Zone**: Classifies the rent as "Below Market", "Market Standard", or "Premium Range" based on P25/P75 regional data.

## 4. Key Directory Structure
- `app/src/main/java/firstrentverdict/`
  - `controller/`: Web endpoints (`VerdictController`, `HomeController`)
  - `model/verdict/`: Core domain models (`Verdict`, `VerdictResult`)
  - `service/core/`: Main business logic (`VerdictService`)
  - `service/seo/`: SEO content generation (`CityContentGenerator`)
- `app/src/main/jte/`: Frontend templates (HTML structure)

## 5. SEO & Growth
The project includes an SEO strategy (`seo/` folder) and dynamic content generation for city-specific landing pages (`CityContentGenerator`), aiming to capture organic traffic for "rent affordability" queries.
