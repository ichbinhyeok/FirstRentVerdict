package gg.jte.generated.ondemand.pages;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.model.verdict.Verdict;
import firstrentverdict.model.verdict.MarketPosition;
public final class JteresultGenerated {
	public static final String JTE_NAME = "pages/result.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,6,6,6,6,18,18,20,20,22,22,24,24,27,28,28,29,30,30,34,34,35,35,37,37,37,38,38,38,40,40,42,42,42,43,45,45,45,45,45,45,48,48,49,49,52,52,56,56,56,58,58,61,61,61,63,63,65,65,67,67,71,71,74,74,74,76,76,79,79,81,81,83,83,83,84,84,84,84,88,88,91,91,91,93,93,96,96,99,100,100,100,100,106,106,111,111,113,113,113,115,115,119,119,119,136,136,136,139,139,139,139,139,139,139,139,139,140,140,140,140,140,140,140,140,140,162,162,162,164,169,172,172,175,175,175,176,176,176,178,178,178,180,180,188,190,190,190,191,193,196,197,198,199,199,199,199,200,200,200,200,203,203,203,209,210,210,212,212,212,212,213,213,213,216,216,219,219,219,219,220,220,220,226,230,230,230,266,269,281,288,300,301,309,315,319,323,344,349,350,368,424,424,424,425,425,425,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, VerdictResult result) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div style=\"max-width: 720px; margin: 4rem auto; padding: 0 2rem;\">\r\n        \r\n        <div class=\"back-link-container\">\r\n            <a href=\"/RentVerdict/\" class=\"back-link\">\r\n                &larr; Return to Engine\r\n            </a>\r\n        </div>\r\n\r\n        <div class=\"verdict-section\">\r\n            <h3 class=\"verdict-label\">Final Decision</h3>\r\n            <div id=\"verdict-badge\">\r\n                ");
				if (result.verdict() == Verdict.APPROVED) {
					jteOutput.writeContent("\r\n                    <div class=\"verdict-text verdict-approved\">APPROVED</div>\r\n                ");
				} else if (result.verdict() == Verdict.BORDERLINE) {
					jteOutput.writeContent("\r\n                    <div class=\"verdict-text verdict-borderline\">BORDERLINE</div>\r\n                ");
				} else {
					jteOutput.writeContent("\r\n                    <div class=\"verdict-text verdict-denied\">DENIED</div>\r\n                ");
				}
				jteOutput.writeContent("\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            ");
				if (result.verdict() != Verdict.APPROVED) {
					jteOutput.writeContent("\r\n               ");
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n        </div>\r\n\r\n        <div id=\"safety-gap-container\">\r\n            ");
				if (result.safetyGap() != null) {
					jteOutput.writeContent("\r\n                ");
					if (result.safetyGap().isApproved()) {
						jteOutput.writeContent("\r\n                    <div class=\"safety-gap-card safety-gap-approved\">\r\n                        <div id=\"safety-gap-amount\" class=\"safety-gap-amount\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().displayText());
						jteOutput.writeContent("</div>\r\n                        <div id=\"safety-gap-action\" class=\"safety-gap-action\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().actionPrompt());
						jteOutput.writeContent("</div>\r\n                    </div>\r\n                ");
					} else {
						jteOutput.writeContent("\r\n                    <div class=\"safety-gap-card safety-gap-denied\">\r\n                        <div id=\"safety-gap-amount\" class=\"safety-gap-amount\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().displayText());
						jteOutput.writeContent("</div>\r\n                        ");
						jteOutput.writeContent("\r\n                         <div class=\"math-proof\" style=\"background: transparent; padding: 0; color: var(--signal-error); font-weight: 500; font-size: 0.85rem;\">\r\n                            Total Upfront $");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(String.format("%,d", result.financials().totalUpfrontCost()));
						jteOutput.writeContent(" — Available Cash $");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(String.format("%,d", result.financials().availableCash()));
						jteOutput.writeContent("\r\n                        </div>\r\n                    </div>\r\n                ");
					}
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n        </div>\r\n\r\n        ");
				if (result.whyThisVerdict() != null) {
					jteOutput.writeContent("\r\n            <div class=\"why-verdict-section\">\r\n                <h3 class=\"section-title\" style=\"font-weight: 700; letter-spacing: 0.02em;\">WHY THIS VERDICT</h3>\r\n                <p id=\"why-text\" class=\"why-text\" style=\"font-weight: 400; color: var(--text-primary);\">\r\n                    ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(result.whyThisVerdict());
					jteOutput.writeContent("\r\n                </p>\r\n                ");
					if (result.primaryBottleneck() != null) {
						jteOutput.writeContent("\r\n                    <div class=\"bottleneck-container\">\r\n                        <span class=\"bottleneck-label\">Primary Bottleneck:</span>\r\n                        <span id=\"bottleneck-text\" class=\"bottleneck-value\">");
						jteOutput.setContext("span", null);
						jteOutput.writeUserContent(result.primaryBottleneck());
						jteOutput.writeContent("</span>\r\n                    </div>\r\n                ");
					}
					jteOutput.writeContent("\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				if (result.contributingFactors() != null && !result.contributingFactors().isEmpty()) {
					jteOutput.writeContent("\r\n            <div class=\"contributing-section\">\r\n                <h4 class=\"contributing-title\" style=\"font-weight: 700; letter-spacing: 0.02em;\">CONTRIBUTING FACTORS</h4>\r\n                <ul class=\"contributing-list\">\r\n                    ");
					for (String factor : result.contributingFactors()) {
						jteOutput.writeContent("\r\n                        <li class=\"contributing-item\">\r\n                            <span class=\"contributing-bullet\">→</span>\r\n                            ");
						jteOutput.setContext("li", null);
						jteOutput.writeUserContent(factor);
						jteOutput.writeContent("\r\n                        </li>\r\n                    ");
					}
					jteOutput.writeContent("\r\n                </ul>\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				if (result.regionalContext() != null) {
					jteOutput.writeContent("\r\n            <details class=\"regional-details\">\r\n                <summary class=\"regional-summary\">REGION: ");
					jteOutput.setContext("summary", null);
					jteOutput.writeUserContent(result.regionalContext().cityName().toUpperCase());
					jteOutput.writeContent("</summary>\r\n                <div class=\"market-warning\" style=\"display: ");
					jteOutput.setContext("div", "style");
					jteOutput.writeUserContent(result.verdict() == Verdict.DENIED && result.marketPosition().marketZone().equals("Below Market") ? "block" : "none");
					jteOutput.setContext("div", null);
					jteOutput.writeContent("; margin-top: 0.5rem; font-size: 0.9rem; color: var(--text-secondary);\">\r\n                    <strong>Market Position: Below Market.</strong> However, affordability fails due to upfront cash constraints.\r\n                </div>\r\n                <ul class=\"contributing-list\" style=\"margin-top: 1rem;\">\r\n                    ");
					for (String context : result.regionalContext().contextFactors()) {
						jteOutput.writeContent("\r\n                        <li class=\"contributing-item\" style=\"font-size: 0.95rem;\">\r\n                            <span class=\"contributing-bullet\">•</span>\r\n                            ");
						jteOutput.setContext("li", null);
						jteOutput.writeUserContent(context);
						jteOutput.writeContent("\r\n                        </li>\r\n                    ");
					}
					jteOutput.writeContent("\r\n                </ul>\r\n            </details>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div id=\"recovery-hint\" class=\"recovery-hint-box\" style=\"display: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(result.verdict() != Verdict.APPROVED ? "block" : "none");
				jteOutput.setContext("div", null);
				jteOutput.writeContent(";\">\r\n            <h4 class=\"verdict-label\" style=\"margin-bottom: 1rem;\">To Reach Approved Status:</h4>\r\n            <div style=\"font-size: 1rem; color: var(--text-primary); line-height: 1.5;\">\r\n                You must meet <strong>ONE</strong> of the following conditions:\r\n                <ul style=\"margin: 0.5rem 0 0 0; padding-left: 1.5rem;\">\r\n                    <li style=\"margin-bottom: 0.5rem;\">\r\n                        ");
				if (((result.financials().availableCash() - result.financials().staticCosts() - result.financials().recommendedBuffer()) / result.financials().upfrontBaseMultiplier()) < 0) {
					jteOutput.writeContent("\r\n                             Reduce rent to <strong>Option Unavailable</strong>\r\n                             <div style=\"color: var(--signal-error); font-size: 0.85rem; margin-top: 0.5rem;\">\r\n                                Current cash level cannot support any rental scenario.\r\n                            </div>\r\n                        ");
				} else {
					jteOutput.writeContent("\r\n                             Reduce rent to <strong>$<span id=\"target-rent\">\r\n                             ");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(String.format("%,d", (int) Math.floor((result.financials().availableCash() - result.financials().staticCosts() - result.financials().recommendedBuffer()) / result.financials().upfrontBaseMultiplier())));
					jteOutput.writeContent("\r\n                             </span></strong> or lower\r\n                        ");
				}
				jteOutput.writeContent("\r\n                    </li>\r\n                    <li>\r\n                        Add <strong>$<span id=\"target-cash\">\r\n                        ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(String.format("%,d", Math.max(0, result.financials().recommendedBuffer() - result.financials().remainingBuffer())));
				jteOutput.writeContent("\r\n                        </span></strong> in available cash\r\n                        <div style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-top: 0.2rem;\">\r\n                            (Includes upfront shortfall + required post-move safety buffer)\r\n                        </div>\r\n                    </li>\r\n                </ul>\r\n            </div>\r\n             <div id=\"target-rent-error\" style=\"display:none; color: var(--signal-error); font-size: 0.85rem; margin-top: 0.5rem;\">\r\n                Current cash level cannot support any rental scenario.\r\n            </div>\r\n        </div>\r\n\r\n        <div class=\"simulation-lab\">\r\n            <h4>Simulation Lab: Change the Variables</h4>\r\n            <div class=\"sim-controls\">\r\n                <div class=\"control-group\">\r\n                    <label>Monthly Rent: $<span id=\"rent-val\">");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(result.financials().monthlyRent());
				jteOutput.writeContent("</span></label>\r\n                    <input type=\"range\" id=\"rent-slider\" \r\n                           min=\"500\" \r\n                          ");
				var __jte_html_attribute_0 = (int)Math.max(3000, Math.max(result.financials().monthlyRent() * 1.5, result.marketPosition().p75Rent() * 1.5));
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" max=\"");
					jteOutput.setContext("input", "max");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" \r\n                          ");
				var __jte_html_attribute_1 = result.financials().monthlyRent();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" \r\n                           step=\"50\">\r\n                    <div class=\"control-info\">Slide to see impact of different rent levels</div>\r\n                </div>\r\n                <div class=\"control-group\">\r\n                    <label>Additional Cash: $<span id=\"cash-val\">0</span></label>\r\n                    <input type=\"range\" id=\"cash-slider\" \r\n                           min=\"0\" max=\"20000\" value=\"0\" step=\"500\">\r\n                    <div class=\"control-info\">Simulate extra savings or external support</div>\r\n                </div>\r\n            </div>\r\n            <div id=\"sim-status\" style=\"display:none; text-align: center; margin-top: 1rem; font-size: 0.8rem; color: var(--text-tertiary);\">\r\n                Recalculating verdict...\r\n            </div>\r\n        </div>\r\n\r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <h3 class=\"contributing-title\" style=\"margin-bottom: 1.5rem; font-weight: 700; letter-spacing: 0.02em;\">FINANCIAL DATA POINTS</h3>\r\n            \r\n            <div class=\"financial-grid\">\r\n                <div>\r\n                    <div class=\"financial-label-sm\">Total Upfront Cost</div>\r\n                    <div id=\"total-upfront-cost\" class=\"financial-value-lg\">$");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().totalUpfrontCost()));
				jteOutput.writeContent("</div>\r\n                    \r\n                    ");
				jteOutput.writeContent("\r\n                    <div style=\"font-size: 0.7rem; color: var(--text-tertiary); margin-top: 1rem; font-style: italic;\">\r\n                        * \"Applied Baseline/Standard\": Used when specific rules are absent.\r\n                    </div>\r\n\r\n                    ");
				jteOutput.writeContent("\r\n                    <div style=\"margin-top: 0.5rem; border-top: 1px dashed var(--border-subtle); padding-top: 1rem;\">\r\n                        <ul style=\"list-style: none; padding: 0; font-size: 0.9rem;\">\r\n                             ");
				for (firstrentverdict.model.verdict.FinancialLineItem item : result.financials().costBreakdown()) {
					jteOutput.writeContent("\r\n                                <li style=\"display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 0.75rem;\">\r\n                                    <div>\r\n                                        <div style=\"font-weight: 500; color: var(--text-secondary);\">");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(item.label());
					jteOutput.writeContent("</div>\r\n                                        <div class=\"receipt-annotation-locked\" style=\"font-size: 0.75rem; color: var(--text-tertiary); margin-top: 0.1rem;\">");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(item.annotation());
					jteOutput.writeContent("</div>\r\n                                    </div>\r\n                                    <div style=\"font-weight: 600; color: var(--text-primary);\">$");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(String.format("%,d", item.amount()));
					jteOutput.writeContent("</div>\r\n                                </li>\r\n                             ");
				}
				jteOutput.writeContent("\r\n                        </ul>\r\n                        <div class=\"receipt-guardrail\">\r\n                            Locked items represent requirements under the selected scenario.\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n                <div>\r\n                     ");
				jteOutput.writeContent("\r\n                     <div class=\"market-radar-container\">\r\n                        <div class=\"radar-title\">Market Radar: <span id=\"radar-title-text\">");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(result.marketPosition().marketZone());
				jteOutput.writeContent("</span></div>\r\n                        ");
				jteOutput.writeContent("\r\n                        <div class=\"radar-track\">\r\n                            ");
				jteOutput.writeContent("\r\n                             <div class=\"radar-range\"></div>\r\n                             \r\n                             ");
				jteOutput.writeContent("\r\n                             ");
				jteOutput.writeContent("\r\n                             ");
				jteOutput.writeContent("\r\n                             <div id=\"radar-marker\" class=\"radar-marker\" style=\"left: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 1.5) * 100.0)) + "%");
				jteOutput.setContext("div", null);
				jteOutput.writeContent(";\"></div>\r\n                             <div id=\"radar-label\" class=\"radar-label\" style=\"left: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 1.5) * 100.0)) + "%");
				jteOutput.setContext("div", null);
				jteOutput.writeContent(";\">YOU</div>\r\n                        </div>\r\n                        <div class=\"radar-legend\">\r\n                            <span>Lower ($");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(result.marketPosition().p25Rent());
				jteOutput.writeContent(")</span>\r\n                            <span>Median</span>\r\n                            <span>Higher</span>\r\n                        </div>\r\n                     </div>\r\n                     \r\n                    ");
				jteOutput.writeContent("\r\n                     ");
				if (result.marketPosition().userRent() > result.marketPosition().p25Rent()) {
					jteOutput.writeContent("\r\n                        <div style=\"margin-bottom: 2rem; text-align: center;\">\r\n                             <button onclick=\"simulateMarketCorrection(");
					jteOutput.setContext("button", "onclick");
					jteOutput.writeUserContent(result.marketPosition().p25Rent());
					jteOutput.setContext("button", null);
					jteOutput.writeContent(")\" class=\"market-correction-btn\">\r\n                                 Simulate Market Correction (Rent $");
					jteOutput.setContext("button", null);
					jteOutput.writeUserContent(result.marketPosition().p25Rent());
					jteOutput.writeContent(")\r\n                             </button>\r\n                        </div>\r\n                     ");
				}
				jteOutput.writeContent("\r\n\r\n                    <div class=\"financial-label-sm\">Remaining Buffer</div>\r\n                    <div id=\"remaining-buffer\" class=\"financial-value-lg\" style=\"color: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-error)" : "var(--text-primary)");
				jteOutput.setContext("div", null);
				jteOutput.writeContent(";\">\r\n                        $");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().remainingBuffer()));
				jteOutput.writeContent("\r\n                    </div>\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <details class=\"methodology-footnote\">\r\n             <summary>Methodology & Data Sources</summary>\r\n             <ul class=\"methodology-list\">\r\n                 <li><strong>Data Source:</strong> 2026 Q1 Regional Market Index (");
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(result.regionalContext().cityName());
				jteOutput.writeContent(")</li>\r\n                 <li><strong>Compliance:</strong> State Security Deposit Regulations (Local Compliance)</li>\r\n                 <li><strong>Benchmarks:</strong> Standardized Moving Cost Baselines</li>\r\n             </ul>\r\n        </details>\r\n\r\n        <div class=\"new-assessment-section\">\r\n           <p style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;\">\r\n               This scenario fails due to cash constraints, not rent level.\r\n           </p>\r\n           <a href=\"/RentVerdict/\" class=\"btn-primary\" style=\"display: inline-block; text-decoration: none; width: auto; min-width: 200px;\">Run a New Scenario</a>\r\n        </div>\r\n\r\n    </div>\r\n\r\n    <script>\r\n        const rentSlider = document.getElementById('rent-slider');\r\n        const cashSlider = document.getElementById('cash-slider');\r\n        const rentVal = document.getElementById('rent-val');\r\n        const cashVal = document.getElementById('cash-val');\r\n        const simStatus = document.getElementById('sim-status');\r\n        const recoveryHint = document.getElementById('recovery-hint');\r\n        const targetRentSpan = document.getElementById('target-rent');\r\n        const targetCashSpan = document.getElementById('target-cash');\r\n        const targetRentError = document.getElementById('target-rent-error');\r\n\r\n        let debounceTimer;\r\n\r\n        function formatCurrency(amount) {\r\n            return amount.toLocaleString();\r\n        }\r\n\r\n        function updateUI(data) {\r\n            const result = data.result;\r\n            const financials = result.financials;\r\n            \r\n            ");
				jteOutput.writeContent("\n            const badge = document.getElementById('verdict-badge');\r\n            let badgeHtml = '';\r\n            ");
				jteOutput.writeContent("\n            const verdict = result.verdict || 'DENIED'; \r\n            \r\n            if (verdict === 'APPROVED') {\r\n                badgeHtml = '<div style=\"font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;\">APPROVED</div>';\r\n            } else if (verdict === 'BORDERLINE') {\r\n                badgeHtml = '<div style=\"font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;\">BORDERLINE</div>';\r\n            } else {\r\n                badgeHtml = '<div style=\"font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;\">DENIED</div>';\r\n            }\r\n            badge.innerHTML = badgeHtml;\r\n\r\n            ");
				jteOutput.writeContent("\n            const sgContainer = document.getElementById('safety-gap-container');\r\n            if (result.safetyGap) {\r\n                const isPositive = result.safetyGap.isApproved;\r\n                const color = isPositive ? \"var(--signal-success)\" : \"var(--signal-error)\";\r\n                const bgColor = isPositive ? \"var(--bg-body)\" : \"rgba(205, 44, 37, 0.03)\";\r\n                \r\n                ");
				jteOutput.writeContent("\n                const displayTxt = result.safetyGap.displayText || \"Risk Detected\";\r\n                const actionPrmpt = result.safetyGap.actionPrompt || \"Action Required\";\r\n\r\n                sgContainer.innerHTML = \r\n                    '<div style=\"position: sticky; top: 60px; background: var(--bg-body); border: 2px solid ' + color + '; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: ' + bgColor + ';\">' +\r\n                        '<div id=\"safety-gap-amount\" style=\"font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;\">' + displayTxt + '</div>' + \r\n                        '<div id=\"safety-gap-action\" style=\"font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;\">' + actionPrmpt + '</div>' +\r\n                    '</div>';\r\n\r\n            }\r\n\r\n            ");
				jteOutput.writeContent("\n            ");
				jteOutput.writeContent("\n            const whyText = result.whyThisVerdict || \"Financial assessment indicates unacceptable risk level for this move.\";\r\n            document.getElementById('why-text').innerText = whyText;\r\n            \r\n            if (result.primaryBottleneck) {\r\n                 document.getElementById('bottleneck-text').innerText = result.primaryBottleneck;\r\n            }\r\n\r\n            ");
				jteOutput.writeContent("\n            document.getElementById('total-upfront-cost').innerText = '$' + financials.totalUpfrontCost.toLocaleString();\r\n            const remBuffer = document.getElementById('remaining-buffer');\r\n            remBuffer.innerText = '$' + financials.remainingBuffer.toLocaleString();\r\n            remBuffer.style.color = financials.remainingBuffer < financials.recommendedBuffer ? 'var(--signal-error)' : 'var(--text-primary)';\r\n            \r\n            ");
				jteOutput.writeContent("\n            if (verdict !== 'APPROVED') {\r\n                recoveryHint.style.display = 'block';\r\n                \r\n                ");
				jteOutput.writeContent("\n                const neededCash = Math.max(0, financials.recommendedBuffer - financials.remainingBuffer);\r\n                targetCashSpan.innerText = formatCurrency(neededCash);\r\n\r\n                ");
				jteOutput.writeContent("\n                const currentAvailable = financials.remainingBuffer + financials.totalUpfrontCost;\r\n                const maxRent = Math.floor((currentAvailable - financials.staticCosts - financials.recommendedBuffer) / financials.upfrontBaseMultiplier);\r\n                \r\n                if (maxRent < 0) {\r\n                     targetRentSpan.innerText = \"Option Unavailable\";\r\n                     targetRentSpan.style.fontSize = \"0.9em\"; \r\n                     targetRentSpan.style.color = \"var(--text-tertiary)\";\r\n                     targetRentError.innerText = \"Current cash level cannot support any rental scenario.\";\r\n                     targetRentError.style.display = 'block';\r\n                } else {\r\n                     targetRentSpan.innerText = formatCurrency(maxRent);\r\n                     targetRentSpan.style.fontSize = \"inherit\";\r\n                     targetRentSpan.style.color = \"inherit\";\r\n                     targetRentError.style.display = 'none';\r\n                }\r\n\r\n            } else {\r\n                recoveryHint.style.display = 'none';\r\n            }\r\n\r\n            ");
				jteOutput.writeContent("\n            const marketPos = result.marketPosition;\r\n            if (marketPos) {\r\n                document.getElementById('radar-title-text').innerText = marketPos.marketZone;\r\n                \r\n                ");
				jteOutput.writeContent("\n                ");
				jteOutput.writeContent("\n                const percentage = Math.min(100.0, Math.max(0.0, (financials.monthlyRent / (marketPos.medianRent * 1.5)) * 100.0));\r\n                \r\n                const marker = document.getElementById('radar-marker');\r\n                const label = document.getElementById('radar-label');\r\n                \r\n                marker.style.left = percentage + '%';\r\n                label.style.left = percentage + '%';\r\n            }\r\n\r\n            simStatus.style.display = 'none';\r\n            document.querySelectorAll('.simulation-lab, .verdict-text, .safety-gap-card, .why-text, .bottleneck-value').forEach(el => el.classList.remove('updating'));\r\n        }\r\n\r\n        function simulateMarketCorrection(targetRent) {\r\n            simStatus.style.display = 'block';\r\n            document.querySelectorAll('.verdict-text, .safety-gap-card, .why-text, .bottleneck-value').forEach(el => el.classList.add('updating'));\r\n            \r\n            ");
				jteOutput.writeContent("\n            rentSlider.value = targetRent;\r\n            rentVal.innerText = targetRent;\r\n\r\n            const payload = {\r\n                adjustedRent: targetRent,\r\n                cashInjection: 0\r\n            };\r\n\r\n            fetch('/RentVerdict/what-if', {\r\n                method: 'POST',\r\n                headers: { 'Content-Type': 'application/json' },\r\n                body: JSON.stringify(payload)\r\n            })\r\n            .then(response => response.json())\r\n            .then(data => updateUI(data))\r\n            .catch(err => {\r\n                console.error('Market Correction failed:', err);\r\n                simStatus.innerText = 'Error applying market correction.';\r\n            });\r\n        }\r\n\r\n        function runSimulation() {\r\n            simStatus.style.display = 'block';\r\n            document.querySelectorAll('.verdict-text, .safety-gap-card, .why-text, .bottleneck-value').forEach(el => el.classList.add('updating'));\r\n            \r\n            const payload = {\r\n                adjustedRent: parseInt(rentSlider.value),\r\n                cashInjection: parseInt(cashSlider.value)\r\n            };\r\n\r\n            fetch('/RentVerdict/what-if', {\r\n                method: 'POST',\r\n                headers: { 'Content-Type': 'application/json' },\r\n                body: JSON.stringify(payload)\r\n            })\r\n            .then(response => response.json())\r\n            .then(data => updateUI(data))\r\n            .catch(err => {\r\n                console.error('Simulation failed:', err);\r\n                simStatus.innerText = 'Error updating simulation.';\r\n            });\r\n        }\r\n\r\n        rentSlider.addEventListener('input', () => {\r\n            rentVal.innerText = rentSlider.value;\r\n            clearTimeout(debounceTimer);\r\n            debounceTimer = setTimeout(runSimulation, 300);\r\n        });\r\n\r\n        cashSlider.addEventListener('input', () => {\r\n            cashVal.innerText = cashSlider.value;\r\n            clearTimeout(debounceTimer);\r\n            debounceTimer = setTimeout(runSimulation, 300);\r\n        });\r\n    </script>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		VerdictResult result = (VerdictResult)params.get("result");
		render(jteOutput, jteHtmlInterceptor, result);
	}
}
