@import firstrentverdict.model.apply.ShouldIApplyInput
@import firstrentverdict.model.apply.ShouldIApplyResult
@import firstrentverdict.model.apply.ApplicationCostLine
@import firstrentverdict.model.apply.ApplicationDecision
@import firstrentverdict.model.apply.ApplicationRiskFactor
@import firstrentverdict.model.dtos.ScreeningIncomeAssumptionsData.IncomeAssumption
@import java.lang.Math
@import java.util.List
@import java.net.URLEncoder
@import java.nio.charset.StandardCharsets

@param ShouldIApplyInput input
@param ShouldIApplyResult result
@param List<IncomeAssumption> incomeAssumptions
@param List<String> changeSummary
@param String canonicalUrl
@param boolean noindex = true
@param String baseUrl = ""

!{
    String cityState = input.city() + ", " + input.state();
    String title = result.decision().label() + " - Should I Apply in " + cityState + "?";
    String signedPostMove = (result.postMoveCash() < 0 ? "-$" : "$") + String.format("%,d", Math.abs(result.postMoveCash()));
    String verdictClass = "apply-verdict-" + result.decision().cssClass();
    String blockerText = String.valueOf(result.blockerCount());
    String pauseText = String.valueOf(result.pauseGateCount());
    String cashNeededText = "$" + String.format("%,d", result.cashNeededToClear());
    boolean rentTargetRelevant = result.saferMonthlyRentTarget() > 0 && result.saferMonthlyRentTarget() < input.monthlyRent();
    String saferRentText = rentTargetRelevant
            ? "$" + String.format("%,d", result.saferMonthlyRentTarget()) + "/mo"
            : result.constraintLabel().startsWith("Policy")
                    ? "Fix terms first"
                    : result.constraintLabel().startsWith("Written")
                            ? "Confirm first"
                            : result.decision() == ApplicationDecision.APPLY
                                    ? "Clears current rent"
                                    : "No safe target";
    String saferRentNote = rentTargetRelevant
            ? "same fee pattern"
            : result.constraintLabel().startsWith("Policy")
                    ? "resolve blocker"
                    : result.constraintLabel().startsWith("Written")
                            ? "then re-run"
                            : result.decision() == ApplicationDecision.APPLY
                                    ? "current stack clears"
                                    : "cash + income";
    String selectedCityState = input.city() + "|" + input.state();
    String grossIncomeValue = input.grossMonthlyIncome() == null ? "" : input.grossMonthlyIncome().toString();
    String securityDepositValue = input.securityDeposit() == null ? "" : input.securityDeposit().toString();
    String leaseTermValue = input.leaseTermMonths() == null ? "" : input.leaseTermMonths().toString();
    String moveInDateValue = input.moveInDate() == null ? "" : input.moveInDate();
    String plainVerdict = switch (result.decision()) {
        case APPLY -> "This listing can move forward if the property confirms the same numbers in writing.";
        case PAUSE -> "This listing is not a no, but paying now leaves an avoidable open risk.";
        case DO_NOT_APPLY -> "Do not pay yet. One hard gate needs to change before this is a safe application.";
    };
    String plainRisk = result.preApprovalCashRisk() > 0
            ? "$" + String.format("%,d", result.preApprovalCashRisk()) + " can be at risk before approval unless the property confirms refund or credit terms."
            : "No entered pre-approval charge is currently marked as unrecoverable.";
    String plainFix = result.cashNeededToClear() > 0
            ? "The fastest fix is lowering upfront cash or adding about $" + String.format("%,d", result.cashNeededToClear()) + "."
            : result.decision() == ApplicationDecision.APPLY
                    ? "The next fix is procedural: save the written fee schedule before paying."
                    : "The next fix is closing the written confirmation gate.";
    String leadBody = "Verdict: " + result.decision().label()
            + "\nCity: " + cityState
            + "\nMonthly rent: $" + String.format("%,d", input.monthlyRent())
            + "\nCash available: $" + String.format("%,d", input.availableCash())
            + "\nCash to clear: $" + String.format("%,d", result.cashNeededToClear())
            + "\nFirst constraint: " + result.constraintLabel()
            + "\n\nListing link or address:\n\nWhat the property told me:\n";
    String leadMailto = "mailto:shinhyeok22@gmail.com?subject="
            + URLEncoder.encode("Apply-safe listing review request", StandardCharsets.UTF_8).replace("+", "%20")
            + "&body="
            + URLEncoder.encode(leadBody, StandardCharsets.UTF_8).replace("+", "%20");
}

@template.layout.main(
    title = title + " | First Rent Verdict",
    description = "Application risk result for " + cityState + ": application fee risk, deposit pressure, move-in cash gap, income screen, and questions to ask before paying.",
    canonical = canonicalUrl,
    noindex = noindex,
    content = @`
    <div class="apply-result">
        <section class="apply-result-hero">
            <div class="apply-result-copy">
                <div class="apply-result-path">
                    <a href="/RentVerdict/should-i-apply" class="apply-back">Should I Apply</a>
                    <span>/${cityState}</span>
                </div>
                <span class="apply-eyebrow">Decision output</span>
                <h1 class="${verdictClass}">${result.decision().label()}</h1>
                <p>${result.summary()}</p>
                <div class="apply-basis">${result.decisionBasis()}</div>
            </div>
            <div class="apply-result-metrics">
                @template.components.apply_metric(label = "Blockers", value = blockerText, note = "hard gates", tone = result.blockerCount() > 0 ? "danger" : "neutral")
                @template.components.apply_metric(label = "Pause Gates", value = pauseText, note = "needs confirmation", tone = result.pauseGateCount() > 0 ? "warning" : "neutral")
                @template.components.apply_metric(label = "Post-Move Cash", value = signedPostMove, note = "after estimated stack", tone = result.postMoveCash() < 0 ? "danger" : "neutral")
                @template.components.apply_metric(label = "Income Needed", value = "$" + String.format("%,d", result.requiredMonthlyIncome()) + "/mo", note = result.incomeStatus(), tone = result.incomeStatus().startsWith("Below") ? "danger" : "neutral")
            </div>
        </section>

        <section class="apply-decision-plan">
            <div class="apply-plan-copy">
                <span>Action plan</span>
                <h2>${result.primaryAction()}</h2>
                <p>${result.nextBestMove()}</p>
                <div class="apply-plan-diagnosis">
                    <span>First constraint</span>
                    <strong>${result.constraintLabel()}</strong>
                    <p>${result.approvalPath()}</p>
                </div>
            </div>
            <div class="apply-plan-metrics">
                <div>
                    <span>Cash to clear</span>
                    <strong>${cashNeededText}</strong>
                    <small>stack + buffer</small>
                </div>
                <div>
                    <span>Safer rent target</span>
                    <strong>${saferRentText}</strong>
                    <small>${saferRentNote}</small>
                </div>
            </div>
            <div class="apply-plan-levers">
                <span>Levers to test</span>
                <ul>
                    @for(String move : result.negotiationMoves())
                        <li>${move}</li>
                    @endfor
                </ul>
            </div>
            <div class="apply-plan-steps">
                <span>Next steps</span>
                <ol>
                    @for(String step : result.decisionSteps())
                        <li>${step}</li>
                    @endfor
                </ol>
            </div>
            <div class="apply-plan-message">
                <div class="apply-message-head">
                    <span>Send this before paying</span>
                    <button type="button" class="apply-copy-button" data-copy-message>Copy</button>
                </div>
                <p data-property-message>${result.propertyMessage()}</p>
            </div>
            <div class="apply-plan-safe">
                <span>What would make this apply-safe?</span>
                <ul>
                    @for(String change : result.applySafeChanges())
                        <li>${change}</li>
                    @endfor
                </ul>
            </div>
        </section>

        <section class="apply-plain">
            <div class="apply-plain-card">
                <span>In plain English</span>
                <strong>${plainVerdict}</strong>
                <p>${plainRisk}</p>
            </div>
            <div class="apply-plain-card">
                <span>Why</span>
                <strong>${result.constraintLabel()}</strong>
                <p>${plainFix}</p>
            </div>
            <div class="apply-plain-card">
                <span>Before you pay</span>
                <strong>Get written proof</strong>
                <p>Confirm the screening criteria, refund or credit terms, and the exact due dates for every charge.</p>
            </div>
        </section>

        <section class="apply-term-strip">
            <span>Term decoder</span>
            <dl>
                <div>
                    <dt>Before approval</dt>
                    <dd>Money you may pay before knowing whether you are accepted.</dd>
                </div>
                <div>
                    <dt>Refundable / credited</dt>
                    <dd>Money the property says you get back or applies to move-in. Keep written proof.</dd>
                </div>
                <div>
                    <dt>Cash to clear</dt>
                    <dd>Extra cash needed to cover the stack and still keep the local buffer.</dd>
                </div>
            </dl>
        </section>

        <section class="apply-trust-disclaimer">
            <div>
                <span>Trust boundary</span>
                <strong>This is a risk screen, not legal, financial, or approval advice.</strong>
                <p>Use the output to decide what to verify before paying. Property-specific lease terms, local enforcement, and current rules can differ from the reference data.</p>
            </div>
            <div class="apply-trust-actions">
                <a href="/RentVerdict/methodology">Methodology</a>
                <a href="/RentVerdict/data-sources">Data sources</a>
                <a href="/RentVerdict/corrections">Report an issue</a>
            </div>
        </section>

        @if(!changeSummary.isEmpty())
            <section class="apply-change-strip">
                <div>
                    <span>What changed</span>
                    <strong>Result updated from your refined inputs</strong>
                </div>
                <ul>
                    @for(String change : changeSummary)
                        <li>${change}</li>
                    @endfor
                </ul>
            </section>
        @endif

        <section class="apply-refine">
            <div class="apply-refine-head">
                <div>
                    <span>Refine result</span>
                    <h2>Update the listing details and rerun</h2>
                </div>
                <p>The quick check got you here. Add the details that change approval risk and cash loss.</p>
            </div>
            <form class="apply-refine-form" action="/RentVerdict/should-i-apply" method="post">
                <input type="hidden" name="cityState" value="${selectedCityState}">
                <input type="hidden" name="prevDecision" value="${result.decision().label()}">
                <input type="hidden" name="prevApplicantCount" value="${String.valueOf(input.applicantCount())}">
                <input type="hidden" name="prevApplicationFee" value="${String.valueOf(input.applicationFee())}">
                <input type="hidden" name="prevAdminFee" value="${String.valueOf(input.adminFee())}">
                <input type="hidden" name="prevHoldingDeposit" value="${String.valueOf(input.holdingDeposit())}">
                <input type="hidden" name="prevMoveInFee" value="${String.valueOf(input.moveInFee())}">
                <input type="hidden" name="prevBrokerFee" value="${String.valueOf(input.brokerFee())}">
                <input type="hidden" name="prevSecurityDeposit" value="${securityDepositValue}">
                <input type="hidden" name="prevPrepaidRent" value="${String.valueOf(input.prepaidRent())}">
                <input type="hidden" name="prevPetDeposit" value="${String.valueOf(input.petDeposit())}">
                <input type="hidden" name="prevMonthlyPetRent" value="${String.valueOf(input.monthlyPetRent())}">
                <input type="hidden" name="prevSecurityDepositConfirmed" value="${input.securityDepositConfirmed() ? "confirmed" : "unconfirmed"}">
                <input type="hidden" name="prevApplicationFeeRefundable" value="${input.applicationFeeRefundable() ? "true" : "false"}">
                <input type="hidden" name="prevAdminFeeRefundable" value="${input.adminFeeRefundable() ? "true" : "false"}">
                <input type="hidden" name="prevHoldingDepositRefundable" value="${input.holdingDepositRefundable() ? "true" : "false"}">
                <input type="hidden" name="prevMoveInFeeRefundable" value="${input.moveInFeeRefundable() ? "true" : "false"}">
                <input type="hidden" name="prevBrokerFeeRefundable" value="${input.brokerFeeRefundable() ? "true" : "false"}">
                <div class="apply-grid apply-grid-4">
                    @template.components.apply_money_field(id = "refineMonthlyRent", name = "monthlyRent", label = "Monthly rent", value = String.valueOf(input.monthlyRent()), required = true)
                    @template.components.apply_money_field(id = "refineAvailableCash", name = "availableCash", label = "Cash available", value = String.valueOf(input.availableCash()), required = true)
                    @template.components.apply_money_field(id = "refineGrossMonthlyIncome", name = "grossMonthlyIncome", label = "Gross income", value = grossIncomeValue, placeholder = "Optional")
                    <div class="apply-field">
                        <label for="refineApplicantCount">Applicants</label>
                        <input id="refineApplicantCount" name="applicantCount" class="apply-input js-count" type="number" min="1" max="6" value="${String.valueOf(input.applicantCount())}">
                    </div>
                </div>

                <div class="apply-grid apply-grid-4">
                    @template.components.apply_money_field(id = "refineApplicationFee", name = "applicationFee", label = "Application fee", value = String.valueOf(input.applicationFee()))
                    @template.components.apply_money_field(id = "refineAdminFee", name = "adminFee", label = "Admin fee", value = String.valueOf(input.adminFee()))
                    @template.components.apply_money_field(id = "refineHoldingDeposit", name = "holdingDeposit", label = "Holding deposit", value = String.valueOf(input.holdingDeposit()))
                    @template.components.apply_money_field(id = "refineMoveInFee", name = "moveInFee", label = "Move-in fee", value = String.valueOf(input.moveInFee()))
                </div>

                <details class="apply-details apply-refine-details" open>
                    <summary>
                        <span>Deeper inputs</span>
                        <strong>Opened after the first result because intent is higher</strong>
                    </summary>
                    <div class="apply-details-body">
                        <div class="apply-grid apply-grid-4">
                            @template.components.apply_money_field(id = "refineSecurityDeposit", name = "securityDeposit", label = "Security deposit", value = securityDepositValue, placeholder = "Estimate if blank")
                            @template.components.apply_money_field(id = "refinePrepaidRent", name = "prepaidRent", label = "Prepaid rent", value = String.valueOf(input.prepaidRent()))
                            @template.components.apply_money_field(id = "refineBrokerFee", name = "brokerFee", label = "Broker fee", value = String.valueOf(input.brokerFee()))
                            @template.components.apply_money_field(id = "refineUtilityDeposit", name = "utilityDeposit", label = "Utility deposit", value = String.valueOf(input.utilityDeposit()))
                            <div class="apply-field">
                                <label for="refineMoveInDate">Move-in date</label>
                                <input id="refineMoveInDate" name="moveInDate" class="apply-input" type="date" value="${moveInDateValue}">
                            </div>
                            <div class="apply-field">
                                <label for="refineLeaseTermMonths">Lease term</label>
                                <select id="refineLeaseTermMonths" name="leaseTermMonths" class="apply-input">
                                    <option value="" selected="${leaseTermValue.isBlank()}">Unknown</option>
                                    <option value="6" selected="${leaseTermValue.equals("6")}">6 months</option>
                                    <option value="12" selected="${leaseTermValue.equals("12")}">12 months</option>
                                    <option value="18" selected="${leaseTermValue.equals("18")}">18 months</option>
                                    <option value="24" selected="${leaseTermValue.equals("24")}">24 months</option>
                                </select>
                            </div>
                            <div class="apply-field">
                                <label for="refineIncomeRule">Income rule</label>
                                <select id="refineIncomeRule" name="incomeRule" class="apply-input">
                                    @for(IncomeAssumption assumption : incomeAssumptions)
                                        <option value="${assumption.thresholdLabel()}" selected="${assumption.thresholdLabel().equals(input.incomeRule())}">${assumption.thresholdLabel()}</option>
                                    @endfor
                                </select>
                            </div>
                            @template.components.apply_money_field(id = "refinePetFee", name = "petFee", label = "Pet fee", value = String.valueOf(input.petFee()))
                            @template.components.apply_money_field(id = "refinePetDeposit", name = "petDeposit", label = "Pet deposit", value = String.valueOf(input.petDeposit()))
                            @template.components.apply_money_field(id = "refineMonthlyPetRent", name = "monthlyPetRent", label = "Monthly pet rent", value = String.valueOf(input.monthlyPetRent()))
                        </div>
                        <div class="apply-check-row">
                            <label class="apply-check">
                                <input type="checkbox" name="securityDepositConfirmed" value="true" checked="${input.securityDepositConfirmed()}">
                                <span>Deposit is listing-confirmed</span>
                            </label>
                            <label class="apply-check">
                                <input type="checkbox" name="hasPet" value="true" checked="${input.hasPet()}">
                                <span>Pet is part of this application</span>
                            </label>
                        </div>
                        <div class="apply-refund-grid">
                            <span>Refund or credit protection</span>
                            <label class="apply-check">
                                <input type="checkbox" name="applicationFeeRefundable" value="true" checked="${input.applicationFeeRefundable()}">
                                <span>Application fee refundable/credited</span>
                            </label>
                            <label class="apply-check">
                                <input type="checkbox" name="adminFeeRefundable" value="true" checked="${input.adminFeeRefundable()}">
                                <span>Admin fee refundable/credited</span>
                            </label>
                            <label class="apply-check">
                                <input type="checkbox" name="holdingDepositRefundable" value="true" checked="${input.holdingDepositRefundable()}">
                                <span>Holding deposit refundable/credited</span>
                            </label>
                            <label class="apply-check">
                                <input type="checkbox" name="moveInFeeRefundable" value="true" checked="${input.moveInFeeRefundable()}">
                                <span>Move-in fee refundable/credited</span>
                            </label>
                            <label class="apply-check">
                                <input type="checkbox" name="brokerFeeRefundable" value="true" checked="${input.brokerFeeRefundable()}">
                                <span>Broker fee refundable/credited</span>
                            </label>
                        </div>
                    </div>
                </details>

                <div class="apply-submit-row">
                    <span>Reruns the same gate engine with the refined inputs.</span>
                    <button type="submit" class="apply-submit">Rerun result</button>
                </div>
            </form>
        </section>

        <section class="apply-result-workbench">
            @template.components.apply_panel(title = "Cost Stack", meta = "$" + String.format("%,d", result.totalMoveInCash()), content = @`
                <div class="apply-cost-list">
                    @for(ApplicationCostLine line : result.costLines())
                        @template.components.apply_cost_row(line = line)
                    @endfor
                </div>
            `)

            @template.components.apply_panel(title = "Gate Output", meta = result.incomeStatus(), content = @`
                @if(result.riskFactors().isEmpty())
                    <div class="apply-empty">No blocking risk flags from the entered numbers. Confirm refundability before paying.</div>
                @else
                    <div class="apply-risk-list">
                        @for(ApplicationRiskFactor factor : result.riskFactors())
                            @template.components.apply_risk_row(factor = factor)
                        @endfor
                    </div>
                @endif
            `)
        </section>

        @template.components.apply_panel(title = "Questions Before Payment", meta = "send to property", content = @`
            <ol class="apply-question-list">
                @for(String question : result.questionsToAsk())
                    <li>${question}</li>
                @endfor
            </ol>
        `)

        <section class="apply-rule-ledger">
            <div>
                <span>Listing detail</span>
                <p>${input.applicantCount()} applicant${input.applicantCount() == 1 ? "" : "s"}; ${input.leaseTermMonths() == null ? "lease term unknown" : input.leaseTermMonths() + "-month lease"}; ${moveInDateValue.isBlank() ? "move-in date unknown" : "move-in " + moveInDateValue}; deposit ${input.securityDepositConfirmed() ? "listing-confirmed" : "estimated or unconfirmed"}.</p>
            </div>
            <div>
                <span>Fee rule</span>
                <p>${result.applicationRuleSummary()}</p>
            </div>
            <div>
                <span>Deposit rule</span>
                <p>${result.depositRuleSummary()}</p>
            </div>
            <div>
                <span>Coverage</span>
                <p>${result.dataCoverageNote()}</p>
            </div>
        </section>

        <section class="apply-lead-cta">
            <div>
                <span>Next step</span>
                <h2>Get an apply-safe listing review</h2>
                <p>Send this result with the listing link. I will check the fee stack, refund terms, and first blocker before you pay.</p>
            </div>
            <div class="apply-lead-actions">
                <a href="${leadMailto}" class="apply-action-primary" data-track-event="lead_submit" data-track-category="v3_checker" data-track-label="apply_safe_listing_review_mailto">Review this listing</a>
                <a href="/RentVerdict/should-i-apply" class="apply-action-secondary">Check another listing</a>
            </div>
            <small>No sponsor required. This captures demand first; partner matching can be added later if supply exists.</small>
        </section>
    </div>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const button = document.querySelector('[data-copy-message]');
            const message = document.querySelector('[data-property-message]');

            function setCopied() {
                const original = button.textContent;
                button.textContent = 'Copied';
                button.classList.add('is-copied');
                window.setTimeout(function () {
                    button.textContent = original;
                    button.classList.remove('is-copied');
                }, 1600);
            }

            if (button && message) {
                button.addEventListener('click', function () {
                    const text = message.textContent.trim();
                    function fallbackCopy() {
                        const textarea = document.createElement('textarea');
                        textarea.value = text;
                        textarea.setAttribute('readonly', '');
                        textarea.style.position = 'fixed';
                        textarea.style.left = '-9999px';
                        document.body.appendChild(textarea);
                        textarea.select();
                        try {
                            document.execCommand('copy');
                        } finally {
                            document.body.removeChild(textarea);
                        }
                        setCopied();
                    }

                    if (navigator.clipboard && navigator.clipboard.writeText) {
                        navigator.clipboard.writeText(text).then(setCopied).catch(fallbackCopy);
                        return;
                    }
                    fallbackCopy();
                });
            }

            const refineForm = document.querySelector('.apply-refine-form');
            if (refineForm) {
                const moneyInputs = Array.from(refineForm.querySelectorAll('.js-money'));
                function clean(value) {
                    return (value || '').replace(/[$,]/g, '').replace(/[^0-9]/g, '');
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
                });
                refineForm.addEventListener('submit', function () {
                    moneyInputs.forEach(function (input) {
                        input.value = clean(input.value);
                    });
                });
            }
        });
    </script>
`)
