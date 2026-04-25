package firstrentverdict.controller;

import firstrentverdict.model.apply.ShouldIApplyInput;
import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.repository.VerdictDataRepository;
import firstrentverdict.service.core.ShouldIApplyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/RentVerdict")
public class ShouldIApplyController {

    private final VerdictDataRepository repository;
    private final ShouldIApplyService shouldIApplyService;
    private final String baseUrl;

    public ShouldIApplyController(
            VerdictDataRepository repository,
            ShouldIApplyService shouldIApplyService,
            @Value("${app.base-url}") String baseUrl) {
        this.repository = repository;
        this.shouldIApplyService = shouldIApplyService;
        this.baseUrl = baseUrl;
    }

    @GetMapping("/should-i-apply")
    public String shouldIApply(Model model) {
        return renderChecker(model, PageMode.SHOULD_I_APPLY, null, ScenarioPrefill.empty());
    }

    @GetMapping("/application-fee-risk-checker")
    public String applicationFeeRiskChecker(Model model) {
        return renderChecker(model, PageMode.APPLICATION_FEE, null, ScenarioPrefill.empty());
    }

    @GetMapping("/move-in-cash-gap-calculator")
    public String moveInCashGapCalculator(Model model) {
        return renderChecker(model, PageMode.CASH_GAP, null, ScenarioPrefill.empty());
    }

    @GetMapping("/security-deposit-calculator")
    public String securityDepositCalculator(Model model) {
        return renderChecker(model, PageMode.SECURITY_DEPOSIT, null, ScenarioPrefill.empty());
    }

    @GetMapping("/pet-fee-move-in-cost-calculator")
    public String petFeeCalculator(Model model) {
        return renderChecker(model, PageMode.PET_FEE, null, ScenarioPrefill.empty());
    }

    @GetMapping("/should-i-apply-in/{slug}")
    public String cityShouldIApply(@PathVariable("slug") String slug, Model model) {
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.CITY_APPLY, city, ScenarioPrefill.withRent(medianRent));
    }

    @GetMapping("/city/{slug}/move-in-cost-calculator")
    public String cityMoveInCostCalculator(@PathVariable("slug") String slug, Model model) {
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.CITY_CASH_GAP, city, ScenarioPrefill.withRent(medianRent));
    }

    @GetMapping("/can-i-apply-with/{cash}/for/{rent}/in/{slug}")
    public String cashRentScenario(
            @PathVariable("cash") int cash,
            @PathVariable("rent") int rent,
            @PathVariable("slug") String slug,
            Model model) {
        if (cash < 0 || cash > 100000 || rent < 1 || rent > 30000) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario out of supported range");
        }
        CityResolution city = resolveCityOr404(slug);
        return renderChecker(model, PageMode.CASH_RENT_SCENARIO, city, ScenarioPrefill.builder().rent(rent).cash(cash).build());
    }

    @GetMapping("/can-i-move-with/{cash}/to/{slug}")
    public String cashCityScenario(
            @PathVariable("cash") int cash,
            @PathVariable("slug") String slug,
            Model model) {
        if (cash < 0 || cash > 100000) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario out of supported range");
        }
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.CASH_CITY_SCENARIO, city, ScenarioPrefill.builder().rent(medianRent).cash(cash).build());
    }

    @GetMapping("/application-fee/{fee}/in/{slug}")
    public String applicationFeeScenario(
            @PathVariable("fee") int fee,
            @PathVariable("slug") String slug,
            Model model) {
        if (fee < 1 || fee > 1000) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee out of supported range");
        }
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.APPLICATION_FEE_SCENARIO, city,
                ScenarioPrefill.builder().rent(medianRent).applicationFee(fee).build());
    }

    @GetMapping("/admin-fee/{fee}/in/{slug}")
    public String adminFeeScenario(
            @PathVariable("fee") int fee,
            @PathVariable("slug") String slug,
            Model model) {
        if (fee < 1 || fee > 2500) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fee out of supported range");
        }
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.ADMIN_FEE_SCENARIO, city,
                ScenarioPrefill.builder().rent(medianRent).adminFee(fee).build());
    }

    @GetMapping("/holding-deposit/{amount}/in/{slug}")
    public String holdingDepositScenario(
            @PathVariable("amount") int amount,
            @PathVariable("slug") String slug,
            Model model) {
        if (amount < 1 || amount > 10000) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Holding deposit out of supported range");
        }
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.HOLDING_DEPOSIT_SCENARIO, city,
                ScenarioPrefill.builder().rent(medianRent).holdingDeposit(amount).build());
    }

    @GetMapping("/can-i-apply-with/{income}/income-for/{rent}/rent-in/{slug}")
    public String incomeRentScenario(
            @PathVariable("income") int income,
            @PathVariable("rent") int rent,
            @PathVariable("slug") String slug,
            Model model) {
        if (income < 1 || income > 100000 || rent < 1 || rent > 30000) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario out of supported range");
        }
        CityResolution city = resolveCityOr404(slug);
        return renderChecker(model, PageMode.INCOME_RENT_SCENARIO, city,
                ScenarioPrefill.builder().rent(rent).income(income).build());
    }

    @GetMapping("/first-last-security-deposit-in/{slug}")
    public String firstLastSecurityScenario(@PathVariable("slug") String slug, Model model) {
        CityResolution city = resolveCityOr404(slug);
        int medianRent = repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(0);
        return renderChecker(model, PageMode.FIRST_LAST_SECURITY_SCENARIO, city,
                ScenarioPrefill.builder()
                        .rent(medianRent)
                        .securityDeposit(medianRent)
                        .prepaidRent(medianRent)
                        .build());
    }

    @PostMapping("/should-i-apply")
    public String submitShouldIApply(
            @RequestParam("cityState") String cityState,
            @RequestParam("monthlyRent") int monthlyRent,
            @RequestParam("availableCash") int availableCash,
            @RequestParam(value = "grossMonthlyIncome", required = false) Integer grossMonthlyIncome,
            @RequestParam(value = "applicantCount", defaultValue = "1") int applicantCount,
            @RequestParam(value = "applicationFee", defaultValue = "0") int applicationFee,
            @RequestParam(value = "adminFee", defaultValue = "0") int adminFee,
            @RequestParam(value = "holdingDeposit", defaultValue = "0") int holdingDeposit,
            @RequestParam(value = "moveInFee", defaultValue = "0") int moveInFee,
            @RequestParam(value = "brokerFee", defaultValue = "0") int brokerFee,
            @RequestParam(value = "securityDeposit", required = false) Integer securityDeposit,
            @RequestParam(value = "securityDepositConfirmed", defaultValue = "false") boolean securityDepositConfirmed,
            @RequestParam(value = "prepaidRent", defaultValue = "0") int prepaidRent,
            @RequestParam(value = "leaseTermMonths", required = false) Integer leaseTermMonths,
            @RequestParam(value = "hasPet", defaultValue = "false") boolean hasPet,
            @RequestParam(value = "petFee", defaultValue = "0") int petFee,
            @RequestParam(value = "petDeposit", defaultValue = "0") int petDeposit,
            @RequestParam(value = "monthlyPetRent", defaultValue = "0") int monthlyPetRent,
            @RequestParam(value = "utilityDeposit", defaultValue = "0") int utilityDeposit,
            @RequestParam(value = "applicationFeeRefundable", defaultValue = "false") boolean applicationFeeRefundable,
            @RequestParam(value = "adminFeeRefundable", defaultValue = "false") boolean adminFeeRefundable,
            @RequestParam(value = "holdingDepositRefundable", defaultValue = "false") boolean holdingDepositRefundable,
            @RequestParam(value = "moveInFeeRefundable", defaultValue = "false") boolean moveInFeeRefundable,
            @RequestParam(value = "brokerFeeRefundable", defaultValue = "false") boolean brokerFeeRefundable,
            @RequestParam(value = "moveInDate", defaultValue = "") String moveInDate,
            @RequestParam(value = "incomeRule", defaultValue = "3x rent") String incomeRule,
            @RequestParam Map<String, String> params,
            Model model) {

        String[] parts = parseCityState(cityState);
        ShouldIApplyInput input = new ShouldIApplyInput(
                parts[0],
                parts[1],
                monthlyRent,
                availableCash,
                grossMonthlyIncome,
                normalizeApplicantCount(applicantCount),
                applicationFee,
                adminFee,
                holdingDeposit,
                moveInFee,
                brokerFee,
                securityDeposit,
                securityDepositConfirmed && securityDeposit != null,
                prepaidRent,
                leaseTermMonths,
                hasPet,
                petFee,
                petDeposit,
                monthlyPetRent,
                utilityDeposit,
                applicationFeeRefundable,
                adminFeeRefundable,
                holdingDepositRefundable,
                moveInFeeRefundable,
                brokerFeeRefundable,
                moveInDate == null ? "" : moveInDate.trim(),
                incomeRule);

        validateInput(input);
        var result = shouldIApplyService.assess(input);

        model.addAttribute("input", input);
        model.addAttribute("result", result);
        model.addAttribute("changeSummary", buildChangeSummary(params, input, result.decision().label()));
        model.addAttribute("incomeAssumptions", incomeAssumptions());
        model.addAttribute("canonicalUrl", baseUrl + "/RentVerdict/should-i-apply");
        model.addAttribute("noindex", true);
        return "pages/should_i_apply_result";
    }

    private String renderChecker(
            Model model,
            PageMode mode,
            CityResolution city,
            ScenarioPrefill prefill) {
        model.addAttribute("cities", cities());
        model.addAttribute("incomeAssumptions", incomeAssumptions());
        model.addAttribute("mode", mode);
        model.addAttribute("selectedCityState", city == null ? null : city.city() + "|" + city.state());
        model.addAttribute("selectedCityLabel", city == null ? null : city.city() + ", " + city.state());
        model.addAttribute("presetRent", prefill.rent());
        model.addAttribute("presetCash", prefill.cash());
        model.addAttribute("presetIncome", prefill.income());
        model.addAttribute("presetApplicationFee", prefill.applicationFee());
        model.addAttribute("presetAdminFee", prefill.adminFee());
        model.addAttribute("presetHoldingDeposit", prefill.holdingDeposit());
        model.addAttribute("presetSecurityDeposit", prefill.securityDeposit());
        model.addAttribute("presetPrepaidRent", prefill.prepaidRent());
        model.addAttribute("scenarioPanelTitle", scenarioPanelTitle(mode, city, prefill));
        model.addAttribute("scenarioPanelBody", scenarioPanelBody(mode, city, prefill));
        model.addAttribute("scenarioMetricLabel", scenarioMetricLabel(mode));
        model.addAttribute("scenarioMetricValue", scenarioMetricValue(mode, prefill));
        model.addAttribute("scenarioRuleBadges", scenarioRuleBadges(city));
        model.addAttribute("relatedScenarioLinks", relatedScenarioLinks(mode, city, prefill));
        model.addAttribute("pageTitle", pageTitle(mode, city, prefill));
        model.addAttribute("pageDescription", pageDescription(mode, city, prefill));
        model.addAttribute("canonicalUrl", baseUrl + canonicalPath(mode, city, prefill));
        return "pages/should_i_apply";
    }

    private List<CitiesData.CityEntry> cities() {
        return repository.getAllCities().stream()
                .sorted(Comparator.comparing(CitiesData.CityEntry::state).thenComparing(CitiesData.CityEntry::city))
                .toList();
    }

    private List<firstrentverdict.model.dtos.ScreeningIncomeAssumptionsData.IncomeAssumption> incomeAssumptions() {
        return repository.getIncomeAssumptions().stream()
                .sorted(Comparator.comparingDouble(
                        firstrentverdict.model.dtos.ScreeningIncomeAssumptionsData.IncomeAssumption::rentToIncomeMultiplier))
                .toList();
    }

    private String[] parseCityState(String cityState) {
        if (cityState == null || !cityState.contains("|")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and state are required");
        }
        String[] parts = cityState.split("\\|");
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and state are required");
        }
        return new String[] { parts[0], parts[1] };
    }

    private void validateInput(ShouldIApplyInput input) {
        if (!repository.isValidCity(input.city(), input.state())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported city: " + input.city() + ", " + input.state());
        }
        if (input.monthlyRent() < 1 || input.monthlyRent() > 30000 || input.availableCash() < 0 || input.availableCash() > 100000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rent or cash value");
        }
        List<Integer> nonNegative = List.of(
                input.applicationFee(),
                input.adminFee(),
                input.holdingDeposit(),
                input.moveInFee(),
                input.brokerFee(),
                input.prepaidRent(),
                input.petFee(),
                input.petDeposit(),
                input.monthlyPetRent(),
                input.utilityDeposit());
        boolean hasNegative = nonNegative.stream().anyMatch(value -> value < 0);
        if (hasNegative || (input.securityDeposit() != null && input.securityDeposit() < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fee values must not be negative");
        }
        if (input.applicantCount() < 1 || input.applicantCount() > 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Applicant count is out of range");
        }
        if (input.leaseTermMonths() != null && (input.leaseTermMonths() < 1 || input.leaseTermMonths() > 36)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lease term is out of range");
        }
    }

    private List<String> buildChangeSummary(Map<String, String> params, ShouldIApplyInput input, String currentDecision) {
        if (!params.containsKey("prevDecision")) {
            return List.of();
        }
        List<String> changes = new ArrayList<>();
        addChange(changes, "Verdict", params.get("prevDecision"), currentDecision);
        addChange(changes, "Applicants", params.get("prevApplicantCount"), String.valueOf(input.applicantCount()));
        addMoneyChange(changes, "Application fee", params.get("prevApplicationFee"), input.applicationFee());
        addMoneyChange(changes, "Admin fee", params.get("prevAdminFee"), input.adminFee());
        addMoneyChange(changes, "Holding deposit", params.get("prevHoldingDeposit"), input.holdingDeposit());
        addMoneyChange(changes, "Move-in fee", params.get("prevMoveInFee"), input.moveInFee());
        addMoneyChange(changes, "Broker fee", params.get("prevBrokerFee"), input.brokerFee());
        addMoneyChange(changes, "Security deposit", params.get("prevSecurityDeposit"), input.securityDeposit());
        addMoneyChange(changes, "Prepaid rent", params.get("prevPrepaidRent"), input.prepaidRent());
        addMoneyChange(changes, "Pet deposit", params.get("prevPetDeposit"), input.petDeposit());
        addMoneyChange(changes, "Monthly pet rent", params.get("prevMonthlyPetRent"), input.monthlyPetRent());
        addChange(changes, "Deposit status", params.get("prevSecurityDepositConfirmed"), input.securityDepositConfirmed() ? "confirmed" : "unconfirmed");
        addChange(changes, "Refund protection", refundProtectionLabel(params), refundProtectionLabel(input));
        if (changes.isEmpty()) {
            changes.add("No material input changed. The result was rerun with the same listing details.");
        }
        return changes.stream().limit(6).toList();
    }

    private void addMoneyChange(List<String> changes, String label, String previous, Integer current) {
        addChange(changes, label, normalizeMoney(previous), "$" + format(current));
    }

    private void addChange(List<String> changes, String label, String previous, String current) {
        String oldValue = previous == null || previous.isBlank() ? "not set" : previous;
        String newValue = current == null || current.isBlank() ? "not set" : current;
        if (!Objects.equals(oldValue, newValue)) {
            changes.add(label + ": " + oldValue + " -> " + newValue);
        }
    }

    private String normalizeMoney(String value) {
        if (value == null || value.isBlank()) {
            return "$0";
        }
        try {
            return "$" + format(Integer.parseInt(value.replaceAll("[^0-9]", "")));
        } catch (NumberFormatException e) {
            return value;
        }
    }

    private String refundProtectionLabel(Map<String, String> params) {
        int protectedCount = 0;
        protectedCount += "true".equals(params.get("prevApplicationFeeRefundable")) ? 1 : 0;
        protectedCount += "true".equals(params.get("prevAdminFeeRefundable")) ? 1 : 0;
        protectedCount += "true".equals(params.get("prevHoldingDepositRefundable")) ? 1 : 0;
        protectedCount += "true".equals(params.get("prevMoveInFeeRefundable")) ? 1 : 0;
        protectedCount += "true".equals(params.get("prevBrokerFeeRefundable")) ? 1 : 0;
        return protectedCount + " protected charges";
    }

    private String refundProtectionLabel(ShouldIApplyInput input) {
        int protectedCount = 0;
        protectedCount += input.applicationFeeRefundable() ? 1 : 0;
        protectedCount += input.adminFeeRefundable() ? 1 : 0;
        protectedCount += input.holdingDepositRefundable() ? 1 : 0;
        protectedCount += input.moveInFeeRefundable() ? 1 : 0;
        protectedCount += input.brokerFeeRefundable() ? 1 : 0;
        return protectedCount + " protected charges";
    }

    private int normalizeApplicantCount(int applicantCount) {
        return Math.max(1, Math.min(6, applicantCount));
    }

    private CityResolution resolveCityOr404(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid city slug");
        }
        return repository.getCityBySlug(slug.toLowerCase(Locale.ROOT))
                .map(city -> new CityResolution(city.city(), city.state(), toCanonicalSlug(city.city(), city.state())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
    }

    private String toCanonicalSlug(String city, String state) {
        return city.toLowerCase(Locale.ROOT).replace(" ", "-").replace(".", "") + "-" + state.toLowerCase(Locale.ROOT);
    }

    private String pageTitle(PageMode mode, CityResolution city, ScenarioPrefill prefill) {
        String place = city == null ? "Apartment" : city.city() + " Apartment";
        return switch (mode) {
            case SHOULD_I_APPLY -> "Should I Apply? Apartment Risk Checker | First Rent Verdict";
            case APPLICATION_FEE -> "Application Fee Risk Checker | First Rent Verdict";
            case CASH_GAP -> "Move-In Cash Gap Calculator | First Rent Verdict";
            case SECURITY_DEPOSIT -> "Security Deposit Calculator Before You Apply | First Rent Verdict";
            case PET_FEE -> "Pet Fee Move-In Cost Calculator | First Rent Verdict";
            case CITY_APPLY -> "Should I Apply in " + city.city() + ", " + city.state() + "? | First Rent Verdict";
            case CITY_CASH_GAP -> city.city() + " Move-In Cost Calculator | First Rent Verdict";
            case CASH_RENT_SCENARIO -> "Can I Apply With $" + format(prefill.cash()) + " for $" + format(prefill.rent()) + " Rent in " + city.city() + "?";
            case CASH_CITY_SCENARIO -> "Can I Move With $" + format(prefill.cash()) + " to " + city.city() + "?";
            case APPLICATION_FEE_SCENARIO -> "Should I Pay a $" + format(prefill.applicationFee()) + " Application Fee in " + city.city() + "?";
            case ADMIN_FEE_SCENARIO -> "Should I Pay a $" + format(prefill.adminFee()) + " Apartment Admin Fee in " + city.city() + "?";
            case HOLDING_DEPOSIT_SCENARIO -> "Should I Pay a $" + format(prefill.holdingDeposit()) + " Holding Deposit in " + city.city() + "?";
            case INCOME_RENT_SCENARIO -> "Can I Apply for $" + format(prefill.rent()) + " Rent With $" + format(prefill.income()) + " Income in " + city.city() + "?";
            case FIRST_LAST_SECURITY_SCENARIO -> "First Month, Last Month, and Security Deposit in " + city.city() + " | Apply Risk Checker";
        };
    }

    private String pageDescription(PageMode mode, CityResolution city, ScenarioPrefill prefill) {
        String place = city == null ? "the apartment" : city.city() + ", " + city.state();
        return switch (mode) {
            case APPLICATION_FEE -> "Check application fee, admin fee, screening fee, and refund risk before paying.";
            case CASH_GAP, CITY_CASH_GAP, CASH_CITY_SCENARIO -> "Check whether your cash covers rent, deposit, fees, moving costs, and a post-move buffer before applying in " + place + ".";
            case SECURITY_DEPOSIT -> "Estimate deposit pressure and ask-before-paying risk before submitting a rental application.";
            case PET_FEE -> "Add pet deposits, pet fees, and monthly pet rent pressure before applying.";
            case APPLICATION_FEE_SCENARIO -> "Run a prefilled apartment application-fee risk check for " + place + " before paying $" + format(prefill.applicationFee()) + ".";
            case ADMIN_FEE_SCENARIO -> "Run a prefilled admin-fee and before-approval cash risk check for " + place + ".";
            case HOLDING_DEPOSIT_SCENARIO -> "Check holding deposit refund and cash-at-risk pressure before paying in " + place + ".";
            case INCOME_RENT_SCENARIO -> "Check whether $" + format(prefill.income()) + " monthly income clears a $" + format(prefill.rent()) + " rent screen in " + place + ".";
            case FIRST_LAST_SECURITY_SCENARIO -> "Check first month, last month, security deposit, moving cost, and state rule pressure before applying in " + place + ".";
            default -> "Check whether paying to apply would waste money, drain move-in cash, or trigger an avoidable rental screening risk.";
        };
    }

    private String canonicalPath(PageMode mode, CityResolution city, ScenarioPrefill prefill) {
        return switch (mode) {
            case SHOULD_I_APPLY -> "/RentVerdict/should-i-apply";
            case APPLICATION_FEE -> "/RentVerdict/application-fee-risk-checker";
            case CASH_GAP -> "/RentVerdict/move-in-cash-gap-calculator";
            case SECURITY_DEPOSIT -> "/RentVerdict/security-deposit-calculator";
            case PET_FEE -> "/RentVerdict/pet-fee-move-in-cost-calculator";
            case CITY_APPLY -> "/RentVerdict/should-i-apply-in/" + city.canonicalSlug();
            case CITY_CASH_GAP -> "/RentVerdict/city/" + city.canonicalSlug() + "/move-in-cost-calculator";
            case CASH_RENT_SCENARIO -> "/RentVerdict/can-i-apply-with/" + prefill.cash() + "/for/" + prefill.rent() + "/in/" + city.canonicalSlug();
            case CASH_CITY_SCENARIO -> "/RentVerdict/can-i-move-with/" + prefill.cash() + "/to/" + city.canonicalSlug();
            case APPLICATION_FEE_SCENARIO -> "/RentVerdict/application-fee/" + prefill.applicationFee() + "/in/" + city.canonicalSlug();
            case ADMIN_FEE_SCENARIO -> "/RentVerdict/admin-fee/" + prefill.adminFee() + "/in/" + city.canonicalSlug();
            case HOLDING_DEPOSIT_SCENARIO -> "/RentVerdict/holding-deposit/" + prefill.holdingDeposit() + "/in/" + city.canonicalSlug();
            case INCOME_RENT_SCENARIO -> "/RentVerdict/can-i-apply-with/" + prefill.income() + "/income-for/" + prefill.rent() + "/rent-in/" + city.canonicalSlug();
            case FIRST_LAST_SECURITY_SCENARIO -> "/RentVerdict/first-last-security-deposit-in/" + city.canonicalSlug();
        };
    }

    private String scenarioPanelTitle(PageMode mode, CityResolution city, ScenarioPrefill prefill) {
        String place = city == null ? "this market" : city.city() + ", " + city.state();
        return switch (mode) {
            case APPLICATION_FEE_SCENARIO -> "$" + format(prefill.applicationFee()) + " application fee before approval";
            case ADMIN_FEE_SCENARIO -> "$" + format(prefill.adminFee()) + " admin fee before approval";
            case HOLDING_DEPOSIT_SCENARIO -> "$" + format(prefill.holdingDeposit()) + " holding deposit before approval";
            case CASH_RENT_SCENARIO -> "$" + format(prefill.cash()) + " cash against $" + format(prefill.rent()) + " rent";
            case CASH_CITY_SCENARIO -> "$" + format(prefill.cash()) + " move-in cash in " + place;
            case INCOME_RENT_SCENARIO -> "$" + format(prefill.income()) + " income against $" + format(prefill.rent()) + " rent";
            case FIRST_LAST_SECURITY_SCENARIO -> "First month + last month + security deposit in " + place;
            case CITY_APPLY, CITY_CASH_GAP -> "Local apply screen for " + place;
            default -> "Before-you-pay apartment apply screen";
        };
    }

    private String scenarioPanelBody(PageMode mode, CityResolution city, ScenarioPrefill prefill) {
        String place = city == null ? "the selected market" : city.city() + ", " + city.state();
        return switch (mode) {
            case APPLICATION_FEE_SCENARIO -> "This page starts from the exact fee amount, then checks whether paying it makes sense after cash buffer, income screen, and loaded state fee rules.";
            case ADMIN_FEE_SCENARIO -> "Admin fees often feel small compared with rent, but they matter before approval because they can turn a weak application into cash at risk.";
            case HOLDING_DEPOSIT_SCENARIO -> "Holding deposits need a different read from normal deposits: the key question is what happens if the application is denied or the lease is not signed.";
            case CASH_RENT_SCENARIO, CASH_CITY_SCENARIO -> "This page tests whether the cash stack survives rent, deposits, fees, moving costs, and a reserve after the move.";
            case INCOME_RENT_SCENARIO -> "This page starts from the screening ratio, then checks whether the application still makes sense after the cash stack and before-approval fees.";
            case FIRST_LAST_SECURITY_SCENARIO -> "This page focuses on high-friction move-in terms where multiple rent-like charges can drain cash before keys.";
            case CITY_APPLY, CITY_CASH_GAP -> "This local screen uses available city and state assumptions for " + place + " instead of forcing a generic national answer.";
            default -> "Use the quick form to move from search intent to a concrete apply, pause, or do-not-apply decision.";
        };
    }

    private String scenarioMetricLabel(PageMode mode) {
        return switch (mode) {
            case APPLICATION_FEE_SCENARIO -> "Application fee";
            case ADMIN_FEE_SCENARIO -> "Admin fee";
            case HOLDING_DEPOSIT_SCENARIO -> "Holding deposit";
            case CASH_RENT_SCENARIO, CASH_CITY_SCENARIO -> "Cash position";
            case INCOME_RENT_SCENARIO -> "Income ratio";
            case FIRST_LAST_SECURITY_SCENARIO -> "Prepaid stack";
            case CITY_APPLY, CITY_CASH_GAP -> "Local rule load";
            default -> "Primary gate";
        };
    }

    private String scenarioMetricValue(PageMode mode, ScenarioPrefill prefill) {
        return switch (mode) {
            case APPLICATION_FEE_SCENARIO -> "$" + format(prefill.applicationFee());
            case ADMIN_FEE_SCENARIO -> "$" + format(prefill.adminFee());
            case HOLDING_DEPOSIT_SCENARIO -> "$" + format(prefill.holdingDeposit());
            case CASH_RENT_SCENARIO, CASH_CITY_SCENARIO -> "$" + format(prefill.cash());
            case INCOME_RENT_SCENARIO -> incomeRatio(prefill.income(), prefill.rent());
            case FIRST_LAST_SECURITY_SCENARIO -> "$" + format(sum(prefill.securityDeposit(), prefill.prepaidRent()));
            case CITY_APPLY, CITY_CASH_GAP -> "City + state";
            default -> "4 gates";
        };
    }

    private List<ScenarioBadge> scenarioRuleBadges(CityResolution city) {
        if (city == null) {
            return List.of(
                    new ScenarioBadge("Select city for local rules", "warn"),
                    new ScenarioBadge("Cash and income still run", "neutral"));
        }
        boolean feeRule = repository.getApplicationFeeRule(city.state()).isPresent();
        boolean depositRule = repository.getDepositPrepaidRule(city.state()).isPresent();
        return List.of(
                new ScenarioBadge(feeRule ? city.state() + " fee rule loaded" : city.state() + " fee rule not loaded", feeRule ? "ok" : "warn"),
                new ScenarioBadge(depositRule ? city.state() + " deposit rule loaded" : city.state() + " deposit rule not loaded", depositRule ? "ok" : "warn"),
                new ScenarioBadge("Guidance, not legal advice", "neutral"));
    }

    private List<ScenarioLink> relatedScenarioLinks(PageMode mode, CityResolution city, ScenarioPrefill prefill) {
        if (city == null) {
            return List.of(
                    new ScenarioLink("Application fee risk", "/RentVerdict/application-fee-risk-checker"),
                    new ScenarioLink("Move-in cash gap", "/RentVerdict/move-in-cash-gap-calculator"),
                    new ScenarioLink("Security deposit pressure", "/RentVerdict/security-deposit-calculator"),
                    new ScenarioLink("Pet fee move-in cost", "/RentVerdict/pet-fee-move-in-cost-calculator"));
        }

        int rent = prefill.rent() == null ? repository.getRent(city.city(), city.state()).map(r -> r.median()).orElse(1800) : prefill.rent();
        int income = prefill.income() == null ? rent * 3 : prefill.income();
        int cash = prefill.cash() == null ? 5000 : prefill.cash();
        String slug = city.canonicalSlug();
        String current = canonicalPath(mode, city, prefill);
        List<ScenarioLink> links = new ArrayList<>(List.of(
                new ScenarioLink("$75 application fee", "/RentVerdict/application-fee/75/in/" + slug),
                new ScenarioLink("$150 application fee", "/RentVerdict/application-fee/150/in/" + slug),
                new ScenarioLink("$300 admin fee", "/RentVerdict/admin-fee/300/in/" + slug),
                new ScenarioLink("$" + format(cash) + " cash check", "/RentVerdict/can-i-apply-with/" + cash + "/for/" + rent + "/in/" + slug),
                new ScenarioLink("$" + format(income) + " income screen", "/RentVerdict/can-i-apply-with/" + income + "/income-for/" + rent + "/rent-in/" + slug),
                new ScenarioLink("First/last/security stack", "/RentVerdict/first-last-security-deposit-in/" + slug)));
        return links.stream().filter(link -> !link.href().equals(current)).limit(5).toList();
    }

    private String incomeRatio(Integer income, Integer rent) {
        if (income == null || rent == null || rent <= 0) {
            return "Not set";
        }
        return String.format(Locale.US, "%.1fx", income.doubleValue() / rent.doubleValue());
    }

    private int sum(Integer first, Integer second) {
        return (first == null ? 0 : first) + (second == null ? 0 : second);
    }

    private String format(Integer amount) {
        if (amount == null) {
            return "0";
        }
        return String.format("%,d", amount);
    }

    public enum PageMode {
        SHOULD_I_APPLY,
        APPLICATION_FEE,
        CASH_GAP,
        SECURITY_DEPOSIT,
        PET_FEE,
        CITY_APPLY,
        CITY_CASH_GAP,
        CASH_RENT_SCENARIO,
        CASH_CITY_SCENARIO,
        APPLICATION_FEE_SCENARIO,
        ADMIN_FEE_SCENARIO,
        HOLDING_DEPOSIT_SCENARIO,
        INCOME_RENT_SCENARIO,
        FIRST_LAST_SECURITY_SCENARIO
    }

    public record ScenarioBadge(String label, String tone) {
    }

    public record ScenarioLink(String label, String href) {
    }

    private record CityResolution(String city, String state, String canonicalSlug) {
    }

    private record ScenarioPrefill(
            Integer rent,
            Integer cash,
            Integer income,
            Integer applicationFee,
            Integer adminFee,
            Integer holdingDeposit,
            Integer securityDeposit,
            Integer prepaidRent) {

        static ScenarioPrefill empty() {
            return builder().build();
        }

        static ScenarioPrefill withRent(Integer rent) {
            return builder().rent(rent).build();
        }

        static Builder builder() {
            return new Builder();
        }

        private static class Builder {
            private Integer rent;
            private Integer cash;
            private Integer income;
            private Integer applicationFee;
            private Integer adminFee;
            private Integer holdingDeposit;
            private Integer securityDeposit;
            private Integer prepaidRent;

            Builder rent(Integer rent) {
                this.rent = rent;
                return this;
            }

            Builder cash(Integer cash) {
                this.cash = cash;
                return this;
            }

            Builder income(Integer income) {
                this.income = income;
                return this;
            }

            Builder applicationFee(Integer applicationFee) {
                this.applicationFee = applicationFee;
                return this;
            }

            Builder adminFee(Integer adminFee) {
                this.adminFee = adminFee;
                return this;
            }

            Builder holdingDeposit(Integer holdingDeposit) {
                this.holdingDeposit = holdingDeposit;
                return this;
            }

            Builder securityDeposit(Integer securityDeposit) {
                this.securityDeposit = securityDeposit;
                return this;
            }

            Builder prepaidRent(Integer prepaidRent) {
                this.prepaidRent = prepaidRent;
                return this;
            }

            ScenarioPrefill build() {
                return new ScenarioPrefill(rent, cash, income, applicationFee, adminFee, holdingDeposit, securityDeposit, prepaidRent);
            }
        }
    }
}
