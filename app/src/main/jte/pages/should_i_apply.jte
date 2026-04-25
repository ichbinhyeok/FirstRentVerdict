@import java.util.List
@import firstrentverdict.model.dtos.CitiesData.CityEntry
@import firstrentverdict.model.dtos.ScreeningIncomeAssumptionsData.IncomeAssumption
@import firstrentverdict.controller.ShouldIApplyController.PageMode
@import firstrentverdict.controller.ShouldIApplyController.ScenarioBadge
@import firstrentverdict.controller.ShouldIApplyController.ScenarioLink

@param List<CityEntry> cities
@param List<IncomeAssumption> incomeAssumptions
@param PageMode mode
@param String selectedCityState = null
@param String selectedCityLabel = null
@param Integer presetRent = null
@param Integer presetCash = null
@param Integer presetIncome = null
@param Integer presetApplicationFee = null
@param Integer presetAdminFee = null
@param Integer presetHoldingDeposit = null
@param Integer presetSecurityDeposit = null
@param Integer presetPrepaidRent = null
@param String pageTitle
@param String pageDescription
@param String canonicalUrl
@param String baseUrl = ""
@param String scenarioPanelTitle
@param String scenarioPanelBody
@param String scenarioMetricLabel
@param String scenarioMetricValue
@param List<ScenarioBadge> scenarioRuleBadges
@param List<ScenarioLink> relatedScenarioLinks

!{
    String presetRentValue = presetRent == null ? "" : presetRent.toString();
    String presetCashValue = presetCash == null ? "" : presetCash.toString();
    String presetIncomeValue = presetIncome == null ? "" : presetIncome.toString();
    String presetApplicationFeeValue = presetApplicationFee == null ? "0" : presetApplicationFee.toString();
    String presetAdminFeeValue = presetAdminFee == null ? "0" : presetAdminFee.toString();
    String presetHoldingDepositValue = presetHoldingDeposit == null ? "0" : presetHoldingDeposit.toString();
    String presetSecurityDepositValue = presetSecurityDeposit == null ? "" : presetSecurityDeposit.toString();
    String presetPrepaidRentValue = presetPrepaidRent == null ? "0" : presetPrepaidRent.toString();
    String toolLabel = switch (mode) {
        case APPLICATION_FEE -> "Fee gate";
        case APPLICATION_FEE_SCENARIO -> "Application fee scenario";
        case ADMIN_FEE_SCENARIO -> "Admin fee scenario";
        case HOLDING_DEPOSIT_SCENARIO -> "Holding deposit scenario";
        case CASH_GAP, CITY_CASH_GAP, CASH_CITY_SCENARIO -> "Cash gate";
        case SECURITY_DEPOSIT -> "Deposit gate";
        case FIRST_LAST_SECURITY_SCENARIO -> "Deposit stack scenario";
        case INCOME_RENT_SCENARIO -> "Income screen scenario";
        case PET_FEE -> "Pet cost gate";
        default -> "Apply gate";
    };
    String scopeTitle = switch (mode) {
        case APPLICATION_FEE_SCENARIO -> "Should I pay a $" + presetApplicationFeeValue + " application fee in " + selectedCityLabel + "?";
        case ADMIN_FEE_SCENARIO -> "Should I pay a $" + presetAdminFeeValue + " admin fee in " + selectedCityLabel + "?";
        case HOLDING_DEPOSIT_SCENARIO -> "Should I pay a $" + presetHoldingDepositValue + " holding deposit in " + selectedCityLabel + "?";
        case INCOME_RENT_SCENARIO -> "Can $" + presetIncomeValue + " income clear $" + presetRentValue + " rent in " + selectedCityLabel + "?";
        case FIRST_LAST_SECURITY_SCENARIO -> "First, last, and security deposit in " + selectedCityLabel;
        default -> selectedCityLabel == null ? "Should I apply before paying?" : "Should I apply in " + selectedCityLabel + "?";
    };
    String scopeLine = switch (mode) {
        case APPLICATION_FEE -> "Application, screening, admin, and holding charges before approval.";
        case APPLICATION_FEE_SCENARIO -> "Prefilled application fee risk check before you pay.";
        case ADMIN_FEE_SCENARIO -> "Prefilled admin fee and before-approval cash-at-risk check.";
        case HOLDING_DEPOSIT_SCENARIO -> "Prefilled holding deposit risk check before approval.";
        case CASH_GAP, CITY_CASH_GAP, CASH_CITY_SCENARIO -> "Rent, deposits, fees, moving cost, and post-move buffer.";
        case SECURITY_DEPOSIT -> "Security deposit and prepaid rent pressure before application.";
        case FIRST_LAST_SECURITY_SCENARIO -> "First month, last month, security deposit, moving cost, and state rule pressure.";
        case INCOME_RENT_SCENARIO -> "Rent-to-income screen, cash stack, and safer application path.";
        case PET_FEE -> "Pet charges included in the move-in cash stack.";
        default -> "One output from four gates: cash, income, fee, and deposit.";
    };
}

@template.layout.main(
    title = pageTitle,
    description = pageDescription,
    canonical = canonicalUrl,
    content = @`
    <script type="application/ld+json">
    {
      "@context": "https://schema.org",
      "@type": "WebApplication",
      "name": "Should I Apply? Apartment Risk Checker",
      "url": "${canonicalUrl}",
      "description": "${pageDescription}",
      "applicationCategory": "FinanceApplication",
      "operatingSystem": "Any",
      "offers": {"@type": "Offer", "price": "0", "priceCurrency": "USD"}
    }
    </script>

    <div class="apply-console">
        <section class="apply-command">
            <div class="apply-command-copy">
                <span class="apply-eyebrow">${toolLabel}</span>
                <h1>${scopeTitle}</h1>
                <p>${scopeLine}</p>
            </div>
            <div class="apply-command-output" aria-label="Decision preview">
                <span>Decision preview</span>
                <div class="apply-preview-head">
                    <strong>Pre-pay risk scan</strong>
                    <em>4 gates</em>
                </div>
                <div class="apply-preview-verdict">
                    <b>Run before you pay</b>
                    <small>Application fee, deposit, cash gap, income screen</small>
                </div>
                <div class="apply-preview-bars" aria-hidden="true">
                    <i></i>
                    <i></i>
                    <i></i>
                </div>
                <div class="apply-preview-scale">
                    <strong>Apply</strong>
                    <strong>Pause</strong>
                    <strong>Do Not Apply</strong>
                </div>
            </div>
        </section>

        <section class="apply-scenario-panel" aria-label="Scenario quality panel">
            <div class="apply-scenario-copy">
                <span>Search intent locked</span>
                <h2>${scenarioPanelTitle}</h2>
                <p>${scenarioPanelBody}</p>
            </div>
            <div class="apply-scenario-meter">
                <span>${scenarioMetricLabel}</span>
                <strong>${scenarioMetricValue}</strong>
                <small>Then run cash, income, fee, and deposit gates together.</small>
            </div>
            <div class="apply-scenario-badges" aria-label="Loaded rules">
                @for(ScenarioBadge badge : scenarioRuleBadges)
                    <span class="apply-rule-badge apply-rule-badge-${badge.tone()}">${badge.label()}</span>
                @endfor
                <div class="apply-trust-mini">
                    <a href="/RentVerdict/methodology">Methodology</a>
                    <a href="/RentVerdict/data-sources">Data sources</a>
                </div>
            </div>
        </section>

        <section class="apply-workbench">
            <aside class="apply-rail" aria-label="Input sequence">
                <span class="apply-rail-mark">FRV</span>
                <ol>
                    <li><span>01</span> Listing</li>
                    <li><span>02</span> Screening</li>
                    <li><span>03</span> Cash at risk</li>
                    <li><span>04</span> Move-in stack</li>
                </ol>
            </aside>

            <form class="apply-form" action="/RentVerdict/should-i-apply" method="post" data-track-event="should_i_apply_start" data-track-category="v3_checker" data-track-label="${toolLabel}">
                <div class="apply-form-title">
                    <div>
                        <span>Decision desk</span>
                        <h2>Enter the listing numbers</h2>
                    </div>
                    <p>Estimates are enough. The result separates hard blockers from questions worth confirming before payment.</p>
                </div>

                @template.components.apply_panel(title = "Listing", meta = "required", content = @`
                    <div class="apply-grid apply-grid-2">
                        <div class="apply-field apply-span-2">
                            <label for="cityState">Target city</label>
                            <select id="cityState" name="cityState" class="apply-input" required>
                                <option value="" disabled selected="${selectedCityState == null}">Select a city</option>
                                @for(CityEntry city : cities)
                                    !{ String value = city.city() + "|" + city.state(); }
                                    <option value="${value}" selected="${selectedCityState != null && selectedCityState.equals(value)}">${city.city()}, ${city.state()}</option>
                                @endfor
                            </select>
                        </div>
                        @template.components.apply_money_field(id = "monthlyRent", name = "monthlyRent", label = "Monthly rent", value = presetRentValue, placeholder = "$2,000", required = true)
                        @template.components.apply_money_field(id = "availableCash", name = "availableCash", label = "Cash available", value = presetCashValue, placeholder = "$6,000", required = true)
                    </div>
                `)

                @template.components.apply_panel(title = "Screening", meta = "assumption", content = @`
                    <div class="apply-grid apply-grid-2">
                        @template.components.apply_money_field(id = "grossMonthlyIncome", name = "grossMonthlyIncome", label = "Gross monthly income", value = presetIncomeValue, placeholder = "Optional")
                        <div class="apply-field">
                            <label for="incomeRule">Income rule</label>
                            <select id="incomeRule" name="incomeRule" class="apply-input">
                                @for(IncomeAssumption assumption : incomeAssumptions)
                                    <option value="${assumption.thresholdLabel()}" selected="${assumption.thresholdLabel().equals("3x rent")}">${assumption.thresholdLabel()}</option>
                                @endfor
                            </select>
                        </div>
                        <div class="apply-field">
                            <label for="applicantCount">Applicants</label>
                            <input id="applicantCount" name="applicantCount" class="apply-input js-count" type="number" min="1" max="6" value="1">
                        </div>
                    </div>
                `)

                @template.components.apply_panel(title = "Before Approval", meta = "cash at risk", content = @`
                    <div class="apply-grid apply-grid-4">
                        @template.components.apply_money_field(id = "applicationFee", name = "applicationFee", label = "Application fee", value = presetApplicationFeeValue)
                        @template.components.apply_money_field(id = "adminFee", name = "adminFee", label = "Admin fee", value = presetAdminFeeValue)
                        @template.components.apply_money_field(id = "holdingDeposit", name = "holdingDeposit", label = "Holding deposit", value = presetHoldingDepositValue)
                        @template.components.apply_money_field(id = "moveInFee", name = "moveInFee", label = "Move-in fee", value = "0")
                    </div>
                `)

                @template.components.apply_panel(title = "Move-In Stack", meta = "due before keys", content = @`
                    <div class="apply-grid apply-grid-4">
                        @template.components.apply_money_field(id = "securityDeposit", name = "securityDeposit", label = "Security deposit", value = presetSecurityDepositValue, placeholder = "Estimate if blank")
                        @template.components.apply_money_field(id = "prepaidRent", name = "prepaidRent", label = "Prepaid rent", value = presetPrepaidRentValue)
                        @template.components.apply_money_field(id = "utilityDeposit", name = "utilityDeposit", label = "Utility deposit", value = "0")
                        @template.components.apply_money_field(id = "petFee", name = "petFee", label = "Pet fee", value = "0")
                    </div>
                    <div class="apply-check-row">
                        <label class="apply-check">
                            <input type="checkbox" name="securityDepositConfirmed" value="true">
                            <span>Deposit is listing-confirmed</span>
                        </label>
                        <label class="apply-check">
                            <input type="checkbox" name="hasPet" value="true">
                            <span>Pet is part of this application</span>
                        </label>
                    </div>
                `)

                <details class="apply-details">
                    <summary>
                        <span>Optional details</span>
                        <strong>Improve accuracy without blocking the quick check</strong>
                    </summary>
                    <div class="apply-details-body">
                        <div class="apply-grid apply-grid-4">
                            <div class="apply-field">
                                <label for="leaseTermMonths">Lease term</label>
                                <select id="leaseTermMonths" name="leaseTermMonths" class="apply-input">
                                    <option value="">Unknown</option>
                                    <option value="6">6 months</option>
                                    <option value="12">12 months</option>
                                    <option value="18">18 months</option>
                                    <option value="24">24 months</option>
                                </select>
                            </div>
                            @template.components.apply_money_field(id = "brokerFee", name = "brokerFee", label = "Broker fee", value = "0")
                            @template.components.apply_money_field(id = "petDeposit", name = "petDeposit", label = "Pet deposit", value = "0")
                            @template.components.apply_money_field(id = "monthlyPetRent", name = "monthlyPetRent", label = "Monthly pet rent", value = "0")
                        </div>
                    </div>
                </details>

                <div class="apply-submit-row">
                    <span>Uses city/state fee and deposit rules when coverage exists.</span>
                    <button type="submit" class="apply-submit">Run gates</button>
                </div>
            </form>

            <aside class="apply-inspector" aria-label="Decision gates">
                <div class="apply-inspector-head">
                    <span>Decision model</span>
                    <strong>Gate based</strong>
                </div>
                <div class="apply-live-card" aria-live="polite">
                    <div class="apply-live-head">
                        <span>Live cash stack</span>
                        <strong data-live-decision>Enter rent and cash</strong>
                    </div>
                    <div class="apply-live-grid">
                        <div>
                            <span>Cash after stack</span>
                            <strong data-live-post-cash>$0</strong>
                        </div>
                        <div>
                            <span>Listing stack</span>
                            <strong data-live-stack>$0</strong>
                        </div>
                        <div>
                            <span>Income needed</span>
                            <strong data-live-income>$0/mo</strong>
                        </div>
                        <div>
                            <span>Before approval</span>
                            <strong data-live-preapproval>$0</strong>
                        </div>
                    </div>
                    <p data-live-note>Uses one month rent as the deposit placeholder until entered.</p>
                </div>
                <ol class="apply-gates">
                    <li>
                        <strong>Cash gate</strong>
                        <span>Move-in stack must fit available cash.</span>
                    </li>
                    <li>
                        <strong>Income gate</strong>
                        <span>Entered income is checked against the selected assumption.</span>
                    </li>
                    <li>
                        <strong>Fee gate</strong>
                        <span>Loaded state rules flag fee caps, refunds, and disclosures.</span>
                    </li>
                    <li>
                        <strong>Deposit gate</strong>
                        <span>Loaded state rules flag deposit and prepaid-rent pressure.</span>
                    </li>
                </ol>
                <div class="apply-output-scale">
                    <span>Apply</span>
                    <span>Pause</span>
                    <span>Do Not Apply</span>
                </div>
                @if(!relatedScenarioLinks.isEmpty())
                    <div class="apply-related-scenarios">
                        <span>Related scenarios</span>
                        @for(ScenarioLink link : relatedScenarioLinks)
                            <a href="${link.href()}">${link.label()}</a>
                        @endfor
                    </div>
                @endif
                <div class="apply-tool-links">
                    <a href="/RentVerdict/application-fee-risk-checker">Fee risk</a>
                    <a href="/RentVerdict/move-in-cash-gap-calculator">Cash gap</a>
                    <a href="/RentVerdict/security-deposit-calculator">Deposit</a>
                    <a href="/RentVerdict/pet-fee-move-in-cost-calculator">Pet cost</a>
                </div>
            </aside>
        </section>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const form = document.querySelector('.apply-form');
            const moneyInputs = Array.from(document.querySelectorAll('.js-money'));
            const countInputs = Array.from(document.querySelectorAll('.js-count'));
            const liveDecision = document.querySelector('[data-live-decision]');
            const livePostCash = document.querySelector('[data-live-post-cash]');
            const liveStack = document.querySelector('[data-live-stack]');
            const liveIncome = document.querySelector('[data-live-income]');
            const livePreapproval = document.querySelector('[data-live-preapproval]');
            const liveNote = document.querySelector('[data-live-note]');

            function clean(value) {
                return (value || '').replace(/[$,]/g, '').replace(/[^0-9]/g, '');
            }

            function money(id) {
                const input = document.getElementById(id);
                return Number(clean(input ? input.value : '')) || 0;
            }

            function formatDollars(value) {
                const abs = Math.abs(Math.round(value));
                return (value < 0 ? '-$' : '$') + abs.toLocaleString('en-US');
            }

            function selectedMultiplier() {
                const select = document.getElementById('incomeRule');
                const match = ((select && select.value) || '3x rent').match(/[0-9.]+/);
                return match ? Number(match[0]) : 3;
            }

            function updateLivePreview() {
                if (!form || !liveDecision) {
                    return;
                }

                const rent = money('monthlyRent');
                const cash = money('availableCash');
                const enteredDeposit = money('securityDeposit');
                const deposit = enteredDeposit > 0 ? enteredDeposit : rent;
                const applicationFee = money('applicationFee');
                const adminFee = money('adminFee');
                const holdingDeposit = money('holdingDeposit');
                const moveInFee = money('moveInFee');
                const prepaidRent = money('prepaidRent');
                const utilityDeposit = money('utilityDeposit');
                const hasPet = !!document.querySelector('input[name="hasPet"]:checked');
                const petFee = hasPet ? money('petFee') : 0;
                const grossIncome = money('grossMonthlyIncome');
                const applicantCount = Math.max(1, Math.min(6, Number(clean((document.getElementById('applicantCount') || {}).value || '1')) || 1));
                const applicationFeeTotal = applicationFee * applicantCount;
                const brokerFee = money('brokerFee');
                const petDeposit = hasPet ? money('petDeposit') : 0;
                const monthlyPetRent = hasPet ? money('monthlyPetRent') : 0;
                const beforeApproval = applicationFeeTotal + adminFee + holdingDeposit + moveInFee;
                const listingStack = rent + deposit + prepaidRent + moveInFee + brokerFee + applicationFeeTotal + adminFee + holdingDeposit + utilityDeposit + petFee + petDeposit + monthlyPetRent;
                const postCash = cash - listingStack;
                const incomeNeeded = Math.ceil((rent + monthlyPetRent) * selectedMultiplier());

                livePostCash.textContent = formatDollars(postCash);
                liveStack.textContent = formatDollars(listingStack);
                liveIncome.textContent = formatDollars(incomeNeeded) + '/mo';
                livePreapproval.textContent = formatDollars(beforeApproval);

                liveDecision.classList.remove('is-apply', 'is-pause', 'is-blocked');
                if (rent <= 0 || cash <= 0) {
                    liveDecision.textContent = 'Enter rent and cash';
                    liveNote.textContent = 'This preview updates before the server-side gate result.';
                    return;
                }
                if (postCash < 0) {
                    liveDecision.textContent = 'Cash short';
                    liveDecision.classList.add('is-blocked');
                    liveNote.textContent = 'The listing stack is above available cash before local moving cost and buffer.';
                    return;
                }
                if (grossIncome > 0 && grossIncome < incomeNeeded) {
                    liveDecision.textContent = 'Income short';
                    liveDecision.classList.add('is-blocked');
                    liveNote.textContent = 'Cash may work, but entered income is below the selected rent rule.';
                    return;
                }
                if (beforeApproval >= 350 || enteredDeposit <= 0) {
                    liveDecision.textContent = 'Pause before paying';
                    liveDecision.classList.add('is-pause');
                    liveNote.textContent = enteredDeposit <= 0
                            ? 'Deposit is estimated as one month rent until you enter the listing amount.'
                            : 'Confirm refundability before paying high pre-approval charges.';
                    return;
                }
                liveDecision.textContent = 'Ready to run gates';
                liveDecision.classList.add('is-apply');
                liveNote.textContent = 'Submit to run city/state rules, fee flags, and the full decision engine.';
            }

            moneyInputs.forEach(function (input) {
                input.addEventListener('blur', function () {
                    const value = clean(input.value);
                    if (value) {
                        input.value = Number(value).toLocaleString('en-US');
                    }
                });
                input.addEventListener('focus', function () {
                    input.value = clean(input.value);
                });
                input.addEventListener('input', updateLivePreview);
            });
            countInputs.forEach(function (input) {
                input.addEventListener('input', updateLivePreview);
            });

            if (form) {
                form.addEventListener('change', updateLivePreview);
                form.addEventListener('submit', function () {
                    moneyInputs.forEach(function (input) {
                        input.value = clean(input.value);
                    });
                });
            }

            updateLivePreview();
        });
    </script>
`)
