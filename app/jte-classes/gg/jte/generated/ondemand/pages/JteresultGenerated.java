package gg.jte.generated.ondemand.pages;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.model.verdict.Verdict;
import firstrentverdict.model.verdict.MarketPosition;
public final class JteresultGenerated {
	public static final String JTE_NAME = "pages/result.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,6,6,6,6,18,18,20,20,22,22,24,24,29,29,30,30,32,32,32,33,33,33,35,35,37,37,37,38,38,38,40,40,41,41,44,44,48,48,48,50,50,53,53,53,55,55,57,57,59,59,63,63,66,66,66,68,68,71,71,73,73,75,75,75,77,77,80,80,80,82,82,85,85,88,89,89,89,89,96,96,96,101,101,101,115,115,115,117,117,117,117,117,117,117,117,117,118,118,118,118,118,118,118,118,118,119,119,119,119,119,119,119,119,119,141,141,141,143,148,151,151,154,154,154,155,155,155,157,157,157,159,159,164,166,166,166,167,169,172,173,174,175,175,175,175,176,176,176,176,179,179,179,185,186,186,188,188,188,188,189,189,189,192,192,195,195,195,195,196,196,196,233,245,260,264,270,274,278,302,358,358,358,359,359,359,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, VerdictResult result) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div style=\"max-width: 720px; margin: 4rem auto; padding: 0 2rem;\">\r\n        \r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <a href=\"/RentVerdict/\" style=\"text-decoration: none; color: var(--text-tertiary); font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em;\">\r\n                &larr; Return to Engine\r\n            </a>\r\n        </div>\r\n\r\n        <div style=\"text-align: center; margin-bottom: 2rem;\">\r\n            <h3 style=\"font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 0.5rem;\">Final Decision</h3>\r\n            <div id=\"verdict-badge\">\r\n                ");
				if (result.verdict() == Verdict.APPROVED) {
					jteOutput.writeContent("\r\n                    <div style=\"font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;\">APPROVED</div>\r\n                ");
				} else if (result.verdict() == Verdict.BORDERLINE) {
					jteOutput.writeContent("\r\n                    <div style=\"font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;\">BORDERLINE</div>\r\n                ");
				} else {
					jteOutput.writeContent("\r\n                    <div style=\"font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;\">DENIED</div>\r\n                ");
				}
				jteOutput.writeContent("\r\n            </div>\r\n        </div>\r\n\r\n        <div id=\"safety-gap-container\">\r\n            ");
				if (result.safetyGap() != null) {
					jteOutput.writeContent("\r\n                ");
					if (result.safetyGap().isApproved()) {
						jteOutput.writeContent("\r\n                    <div style=\"position: sticky; top: 60px; background: var(--bg-body); border: 2px solid #2d5016; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100;\">\r\n                        <div id=\"safety-gap-amount\" style=\"font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().toDisplayText());
						jteOutput.writeContent("</div>\r\n                        <div id=\"safety-gap-action\" style=\"font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().actionPrompt());
						jteOutput.writeContent("</div>\r\n                    </div>\r\n                ");
					} else {
						jteOutput.writeContent("\r\n                    <div style=\"position: sticky; top: 60px; background: var(--bg-body); border: 2px solid var(--signal-error); padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: rgba(205, 44, 37, 0.03);\">\r\n                        <div id=\"safety-gap-amount\" style=\"font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().toDisplayText());
						jteOutput.writeContent("</div>\r\n                        <div id=\"safety-gap-action\" style=\"font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().actionPrompt());
						jteOutput.writeContent("</div>\r\n                    </div>\r\n                ");
					}
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n        </div>\r\n\r\n        ");
				if (result.whyThisVerdict() != null) {
					jteOutput.writeContent("\r\n            <div style=\"margin-bottom: 4rem; padding: 2rem; background: var(--bg-panel); border-left: 4px solid var(--text-primary);\">\r\n                <h3 style=\"font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1rem;\">Why This Verdict</h3>\r\n                <p id=\"why-text\" style=\"font-size: 1.25rem; line-height: 1.6; max-width: 65ch; color: var(--text-primary);\">\r\n                    ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(result.whyThisVerdict());
					jteOutput.writeContent("\r\n                </p>\r\n                ");
					if (result.primaryBottleneck() != null) {
						jteOutput.writeContent("\r\n                    <div style=\"margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border-subtle);\">\r\n                        <span style=\"font-size: 0.85rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em;\">Primary Bottleneck:</span>\r\n                        <span id=\"bottleneck-text\" style=\"font-weight: 600; margin-left: 0.5rem;\">");
						jteOutput.setContext("span", null);
						jteOutput.writeUserContent(result.primaryBottleneck());
						jteOutput.writeContent("</span>\r\n                    </div>\r\n                ");
					}
					jteOutput.writeContent("\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				if (result.contributingFactors() != null && !result.contributingFactors().isEmpty()) {
					jteOutput.writeContent("\r\n            <div class=\"verdict-contributing\" style=\"margin-bottom: 3rem;\">\r\n                <h4 style=\"font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;\">Contributing Factors</h4>\r\n                <ul style=\"list-style: none; padding: 0;\">\r\n                    ");
					for (String factor : result.contributingFactors()) {
						jteOutput.writeContent("\r\n                        <li style=\"margin-bottom: 1rem; padding-left: 1.5rem; position: relative; color: var(--text-secondary);\">\r\n                            <span style=\"position: absolute; left: 0; color: var(--text-tertiary);\">→</span>\r\n                            ");
						jteOutput.setContext("li", null);
						jteOutput.writeUserContent(factor);
						jteOutput.writeContent("\r\n                        </li>\r\n                    ");
					}
					jteOutput.writeContent("\r\n                </ul>\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				if (result.regionalContext() != null) {
					jteOutput.writeContent("\r\n            <details class=\"verdict-regional\" style=\"margin-top: 3rem; margin-bottom: 3rem; padding: 1.5rem; background: var(--bg-body); border: 1px solid var(--border-subtle); border-radius: 4px;\">\r\n                <summary style=\"cursor: pointer; font-weight: 600; font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-tertiary); user-select: none;\">Regional Context: ");
					jteOutput.setContext("summary", null);
					jteOutput.writeUserContent(result.regionalContext().cityName());
					jteOutput.writeContent("</summary>\r\n                <ul style=\"list-style: none; padding: 0; margin-top: 1rem;\">\r\n                    ");
					for (String context : result.regionalContext().contextFactors()) {
						jteOutput.writeContent("\r\n                        <li style=\"margin-bottom: 0.75rem; padding-left: 1.5rem; position: relative; font-size: 0.95rem; color: var(--text-secondary);\">\r\n                            <span style=\"position: absolute; left: 0; color: var(--text-tertiary);\">•</span>\r\n                            ");
						jteOutput.setContext("li", null);
						jteOutput.writeUserContent(context);
						jteOutput.writeContent("\r\n                        </li>\r\n                    ");
					}
					jteOutput.writeContent("\r\n                </ul>\r\n            </details>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div id=\"recovery-hint\" style=\"margin-bottom: 2rem; padding: 1.5rem; background: var(--bg-body); border: 2px solid var(--text-tertiary); border-radius: 4px; display: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(result.verdict() != Verdict.APPROVED ? "block" : "none");
				jteOutput.setContext("div", null);
				jteOutput.writeContent("; text-align: left;\">\r\n            <h4 style=\"margin: 0 0 1rem 0; font-size: 0.9rem; text-transform: uppercase; color: var(--text-secondary); letter-spacing: 0.05em;\">To Reach Approved Status:</h4>\r\n            <div style=\"font-size: 1rem; color: var(--text-primary); line-height: 1.5;\">\r\n                You must meet <strong>ONE</strong> of the following conditions:\r\n                <ul style=\"margin: 0.5rem 0 0 0; padding-left: 1.5rem;\">\r\n                    <li style=\"margin-bottom: 0.5rem;\">\r\n                         Reduce rent to <strong>$<span id=\"target-rent\">\r\n                         ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(String.format("%,d", (int) Math.floor((result.financials().remainingBuffer() + result.financials().totalUpfrontCost() - result.financials().staticCosts() - result.financials().recommendedBuffer()) / result.financials().upfrontBaseMultiplier())));
				jteOutput.writeContent("\r\n                         </span></strong> or lower\r\n                    </li>\r\n                    <li>\r\n                        Add <strong>$<span id=\"target-cash\">\r\n                        ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(String.format("%,d", Math.max(0, result.financials().recommendedBuffer() - result.financials().remainingBuffer())));
				jteOutput.writeContent("\r\n                        </span></strong> in available cash\r\n                    </li>\r\n                </ul>\r\n            </div>\r\n             <div id=\"target-rent-error\" style=\"display:none; color: var(--signal-error); font-size: 0.85rem; margin-top: 0.5rem;\">\r\n                No viable rent at current cash level.\r\n            </div>\r\n        </div>\r\n\r\n        <div class=\"simulation-lab\">\r\n            <h4>Simulation Lab: Change the Variables</h4>\r\n            <div class=\"sim-controls\">\r\n                <div class=\"control-group\">\r\n                    <label>Monthly Rent: $<span id=\"rent-val\">");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(result.financials().monthlyRent());
				jteOutput.writeContent("</span></label>\r\n                    <input type=\"range\" id=\"rent-slider\" \r\n                          ");
				var __jte_html_attribute_0 = Math.max(500, result.financials().monthlyRent() - 1000);
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" min=\"");
					jteOutput.setContext("input", "min");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" \r\n                          ");
				var __jte_html_attribute_1 = result.financials().monthlyRent() + 1000;
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
					jteOutput.writeContent(" max=\"");
					jteOutput.setContext("input", "max");
					jteOutput.writeUserContent(__jte_html_attribute_1);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" \r\n                          ");
				var __jte_html_attribute_2 = result.financials().monthlyRent();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
					jteOutput.writeContent(" value=\"");
					jteOutput.setContext("input", "value");
					jteOutput.writeUserContent(__jte_html_attribute_2);
					jteOutput.setContext("input", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" \r\n                           step=\"50\">\r\n                    <div class=\"control-info\">Slide to see impact of different rent levels</div>\r\n                </div>\r\n                <div class=\"control-group\">\r\n                    <label>Additional Cash: $<span id=\"cash-val\">0</span></label>\r\n                    <input type=\"range\" id=\"cash-slider\" \r\n                           min=\"0\" max=\"20000\" value=\"0\" step=\"500\">\r\n                    <div class=\"control-info\">Simulate extra savings or external support</div>\r\n                </div>\r\n            </div>\r\n            <div id=\"sim-status\" style=\"display:none; text-align: center; margin-top: 1rem; font-size: 0.8rem; color: var(--text-tertiary);\">\r\n                Recalculating verdict...\r\n            </div>\r\n        </div>\r\n\r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <h3 style=\"font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;\">Financial Data Points</h3>\r\n            \r\n            <div style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 4rem;\">\r\n                <div>\r\n                    <div style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;\">Total Upfront Cost</div>\r\n                    <div id=\"total-upfront-cost\" style=\"font-size: 2rem; font-weight: 600; letter-spacing: -0.02em;\">$");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().totalUpfrontCost()));
				jteOutput.writeContent("</div>\r\n                    \r\n                    ");
				jteOutput.writeContent("\r\n                    <div style=\"font-size: 0.7rem; color: var(--text-tertiary); margin-top: 1rem; font-style: italic;\">\r\n                        * \"Applied Baseline/Standard\": Used when specific rules are absent.\r\n                    </div>\r\n\r\n                    ");
				jteOutput.writeContent("\r\n                    <div style=\"margin-top: 0.5rem; border-top: 1px dashed var(--border-subtle); padding-top: 1rem;\">\r\n                        <ul style=\"list-style: none; padding: 0; font-size: 0.9rem;\">\r\n                             ");
				for (firstrentverdict.model.verdict.FinancialLineItem item : result.financials().costBreakdown()) {
					jteOutput.writeContent("\r\n                                <li style=\"display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 0.75rem;\">\r\n                                    <div>\r\n                                        <div style=\"font-weight: 500; color: var(--text-secondary);\">");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(item.label());
					jteOutput.writeContent("</div>\r\n                                        <div style=\"font-size: 0.75rem; color: var(--text-tertiary); margin-top: 0.1rem;\">");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(item.annotation());
					jteOutput.writeContent("</div>\r\n                                    </div>\r\n                                    <div style=\"font-weight: 600; color: var(--text-primary);\">$");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(String.format("%,d", item.amount()));
					jteOutput.writeContent("</div>\r\n                                </li>\r\n                             ");
				}
				jteOutput.writeContent("\r\n                        </ul>\r\n                    </div>\r\n                </div>\r\n                <div>\r\n                     ");
				jteOutput.writeContent("\r\n                     <div style=\"margin-bottom: 2rem; padding: 1rem; background: var(--bg-body); border: 1px solid var(--border-subtle); border-radius: 4px;\">\r\n                        <div style=\"font-size: 0.8rem; text-transform: uppercase; color: var(--text-secondary); margin-bottom: 0.5rem; letter-spacing: 0.05em;\">Market Radar: ");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(result.marketPosition().marketZone());
				jteOutput.writeContent("</div>\r\n                        ");
				jteOutput.writeContent("\r\n                        <div style=\"position: relative; height: 8px; background: #e0e0e0; border-radius: 4px; margin-top: 1.5rem; margin-bottom: 1.5rem;\">\r\n                            ");
				jteOutput.writeContent("\r\n                             <div style=\"position: absolute; left: 0%; width: 100%; height: 100%; background: linear-gradient(90deg, #e0e0e0 0%, #d1e7dd 25%, #d1e7dd 75%, #e0e0e0 100%); border-radius: 4px;\"></div>\r\n                             \r\n                             ");
				jteOutput.writeContent("\r\n                             ");
				jteOutput.writeContent("\r\n                             ");
				jteOutput.writeContent("\r\n                             <div style=\"position: absolute; left: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 1.5) * 100.0)) + "%");
				jteOutput.setContext("div", null);
				jteOutput.writeContent("; top: -6px; width: 4px; height: 20px; background: var(--text-primary); border: 1px solid #fff;\"></div>\r\n                             <div style=\"position: absolute; left: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(Math.min(100.0, Math.max(0.0, (double)result.financials().monthlyRent() / (result.marketPosition().medianRent() * 1.5) * 100.0)) + "%");
				jteOutput.setContext("div", null);
				jteOutput.writeContent("; top: -20px; transform: translateX(-50%); font-size: 0.7rem; font-weight: 700;\">YOU</div>\r\n                        </div>\r\n                        <div style=\"display: flex; justify-content: space-between; font-size: 0.7rem; color: var(--text-tertiary);\">\r\n                            <span>Lower ($");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(result.marketPosition().p25Rent());
				jteOutput.writeContent(")</span>\r\n                            <span>Median</span>\r\n                            <span>Higher</span>\r\n                        </div>\r\n                     </div>\r\n                     \r\n                    ");
				jteOutput.writeContent("\r\n                     ");
				if (result.marketPosition().userRent() > result.marketPosition().p25Rent()) {
					jteOutput.writeContent("\r\n                        <div style=\"margin-bottom: 2rem; text-align: center;\">\r\n                             <button onclick=\"simulateMarketCorrection(");
					jteOutput.setContext("button", "onclick");
					jteOutput.writeUserContent(result.marketPosition().p25Rent());
					jteOutput.setContext("button", null);
					jteOutput.writeContent(")\" style=\"background-color: transparent; border: 1px solid var(--text-primary); border-radius: 4px; padding: 0.5rem 1rem; font-size: 0.8rem; cursor: pointer; color: var(--text-primary);\">\r\n                                 Simulate Market Correction (Rent $");
					jteOutput.setContext("button", null);
					jteOutput.writeUserContent(result.marketPosition().p25Rent());
					jteOutput.writeContent(")\r\n                             </button>\r\n                        </div>\r\n                     ");
				}
				jteOutput.writeContent("\r\n\r\n                    <div style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;\">Remaining Buffer</div>\r\n                    <div id=\"remaining-buffer\" style=\"font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-error)" : "var(--text-primary)");
				jteOutput.setContext("div", null);
				jteOutput.writeContent(";\">\r\n                        $");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().remainingBuffer()));
				jteOutput.writeContent("\r\n                    </div>\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        <div style=\"text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 3rem;\">\r\n           <p style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;\">\r\n               This verdict is an automated assessment based on provided parameters.<br>\r\n               Adjust variables in the Simulation Lab to see alternative outcomes.\r\n           </p>\r\n           <a href=\"/RentVerdict/\" class=\"btn-primary\" style=\"display: inline-block; text-decoration: none; width: auto; min-width: 200px;\">Initialize New Assessment</a>\r\n        </div>\r\n\r\n    </div>\r\n\r\n    <script>\r\n        const rentSlider = document.getElementById('rent-slider');\r\n        const cashSlider = document.getElementById('cash-slider');\r\n        const rentVal = document.getElementById('rent-val');\r\n        const cashVal = document.getElementById('cash-val');\r\n        const simStatus = document.getElementById('sim-status');\r\n        const recoveryHint = document.getElementById('recovery-hint');\r\n        const targetRentSpan = document.getElementById('target-rent');\r\n        const targetCashSpan = document.getElementById('target-cash');\r\n        const targetRentError = document.getElementById('target-rent-error');\r\n\r\n        let debounceTimer;\r\n\r\n        function formatCurrency(amount) {\r\n            return amount.toLocaleString();\r\n        }\r\n\r\n        function updateUI(data) {\r\n            const result = data.result;\r\n            const financials = result.financials;\r\n            \r\n            ");
				jteOutput.writeContent("\n            const badge = document.getElementById('verdict-badge');\r\n            let badgeHtml = '';\r\n            if (result.verdict === 'APPROVED') {\r\n                badgeHtml = '<div style=\"font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;\">APPROVED</div>';\r\n            } else if (result.verdict === 'BORDERLINE') {\r\n                badgeHtml = '<div style=\"font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;\">BORDERLINE</div>';\r\n            } else {\r\n                badgeHtml = '<div style=\"font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;\">DENIED</div>';\r\n            }\r\n            badge.innerHTML = badgeHtml;\r\n\r\n            ");
				jteOutput.writeContent("\n            const sgContainer = document.getElementById('safety-gap-container');\r\n            if (result.safetyGap) {\r\n                const isPositive = result.safetyGap.isApproved;\r\n                const color = isPositive ? \"var(--signal-success)\" : \"var(--signal-error)\";\r\n                const bgColor = isPositive ? \"var(--bg-body)\" : \"rgba(205, 44, 37, 0.03)\";\r\n                \r\n                sgContainer.innerHTML = \r\n                    '<div style=\"position: sticky; top: 60px; background: var(--bg-body); border: 2px solid ' + color + '; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: ' + bgColor + ';\">' +\r\n                        '<div id=\"safety-gap-amount\" style=\"font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;\">' + result.safetyGap.toDisplayText + '</div>' + \r\n                        '<div id=\"safety-gap-action\" style=\"font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;\">' + result.safetyGap.actionPrompt + '</div>' +\r\n                    '</div>';\r\n\r\n            }\r\n\r\n            ");
				jteOutput.writeContent("\n            document.getElementById('why-text').innerText = result.whyThisVerdict;\r\n            document.getElementById('bottleneck-text').innerText = result.primaryBottleneck;\r\n\r\n            ");
				jteOutput.writeContent("\n            document.getElementById('total-upfront-cost').innerText = '$' + financials.totalUpfrontCost.toLocaleString();\r\n            const remBuffer = document.getElementById('remaining-buffer');\r\n            remBuffer.innerText = '$' + financials.remainingBuffer.toLocaleString();\r\n            remBuffer.style.color = financials.remainingBuffer < financials.recommendedBuffer ? 'var(--signal-error)' : 'var(--text-primary)';\r\n            \r\n            ");
				jteOutput.writeContent("\n            if (result.verdict !== 'APPROVED') {\r\n                recoveryHint.style.display = 'block';\r\n                \r\n                ");
				jteOutput.writeContent("\n                const neededCash = Math.max(0, financials.recommendedBuffer - financials.remainingBuffer);\r\n                targetCashSpan.innerText = formatCurrency(neededCash);\r\n\r\n                ");
				jteOutput.writeContent("\n                const currentAvailable = financials.remainingBuffer + financials.totalUpfrontCost;\r\n                const maxRent = Math.floor((currentAvailable - financials.staticCosts - financials.recommendedBuffer) / financials.upfrontBaseMultiplier);\r\n                \r\n                if (maxRent < 0) {\r\n                     targetRentSpan.innerText = \"N/A\";\r\n                     targetRentError.style.display = 'block';\r\n                } else {\r\n                     targetRentSpan.innerText = formatCurrency(maxRent);\r\n                     targetRentError.style.display = 'none';\r\n                }\r\n\r\n            } else {\r\n                recoveryHint.style.display = 'none';\r\n            }\r\n\r\n            simStatus.style.display = 'none';\r\n            document.querySelectorAll('.simulation-lab, #verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.remove('updating'));\r\n        }\r\n\r\n        function simulateMarketCorrection(targetRent) {\r\n            simStatus.style.display = 'block';\r\n            document.querySelectorAll('#verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.add('updating'));\r\n            \r\n            ");
				jteOutput.writeContent("\n            rentSlider.value = targetRent;\r\n            rentVal.innerText = targetRent;\r\n\r\n            const payload = {\r\n                adjustedRent: targetRent,\r\n                cashInjection: 0\r\n            };\r\n\r\n            fetch('/RentVerdict/what-if', {\r\n                method: 'POST',\r\n                headers: { 'Content-Type': 'application/json' },\r\n                body: JSON.stringify(payload)\r\n            })\r\n            .then(response => response.json())\r\n            .then(data => updateUI(data))\r\n            .catch(err => {\r\n                console.error('Market Correction failed:', err);\r\n                simStatus.innerText = 'Error applying market correction.';\r\n            });\r\n        }\r\n\r\n        function runSimulation() {\r\n            simStatus.style.display = 'block';\r\n            document.querySelectorAll('#verdict-badge, #safety-gap-container, #why-text, #bottleneck-text').forEach(el => el.classList.add('updating'));\r\n            \r\n            const payload = {\r\n                adjustedRent: parseInt(rentSlider.value),\r\n                cashInjection: parseInt(cashSlider.value)\r\n            };\r\n\r\n            fetch('/RentVerdict/what-if', {\r\n                method: 'POST',\r\n                headers: { 'Content-Type': 'application/json' },\r\n                body: JSON.stringify(payload)\r\n            })\r\n            .then(response => response.json())\r\n            .then(data => updateUI(data))\r\n            .catch(err => {\r\n                console.error('Simulation failed:', err);\r\n                simStatus.innerText = 'Error updating simulation.';\r\n            });\r\n        }\r\n\r\n        rentSlider.addEventListener('input', () => {\r\n            rentVal.innerText = rentSlider.value;\r\n            clearTimeout(debounceTimer);\r\n            debounceTimer = setTimeout(runSimulation, 300);\r\n        });\r\n\r\n        cashSlider.addEventListener('input', () => {\r\n            cashVal.innerText = cashSlider.value;\r\n            clearTimeout(debounceTimer);\r\n            debounceTimer = setTimeout(runSimulation, 300);\r\n        });\r\n    </script>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		VerdictResult result = (VerdictResult)params.get("result");
		render(jteOutput, jteHtmlInterceptor, result);
	}
}
