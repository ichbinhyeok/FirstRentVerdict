package gg.jte.generated.ondemand.pages;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.model.verdict.Verdict;
public final class JteresultGenerated {
	public static final String JTE_NAME = "pages/result.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,5,5,5,5,16,16,18,18,20,20,22,22,25,25,26,26,28,28,28,29,29,29,31,31,33,33,33,34,34,34,36,36,37,37,39,39,43,43,43,45,45,48,48,48,50,50,52,52,56,56,56,60,60,63,63,63,65,65,73,73,73,77,77,79,79,79,81,81,83,83,83,85,85,99,99,99,100,100,100,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, VerdictResult result) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div style=\"max-width: 720px; margin: 4rem auto; padding: 0 2rem;\">\r\n        \r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <a href=\"/RentVerdict/\" style=\"text-decoration: none; color: var(--text-tertiary); font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em;\">\r\n                &larr; Return to Engine\r\n            </a>\r\n        </div>\r\n\r\n        <div style=\"text-align: center; margin-bottom: 2rem;\">\r\n            <h3 style=\"font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 0.5rem;\">Final Decision</h3>\r\n            ");
				if (result.verdict() == Verdict.APPROVED) {
					jteOutput.writeContent("\r\n                <div style=\"font-size: 4.5rem; font-weight: 900; color: #2d5016; letter-spacing: -0.05em; line-height: 0.9;\">APPROVED</div>\r\n            ");
				} else if (result.verdict() == Verdict.BORDERLINE) {
					jteOutput.writeContent("\r\n                <div style=\"font-size: 4.5rem; font-weight: 900; color: #947600; letter-spacing: -0.05em; line-height: 0.9; text-decoration: underline;\">BORDERLINE</div>\r\n            ");
				} else {
					jteOutput.writeContent("\r\n                <div style=\"font-size: 4.5rem; font-weight: 900; color: var(--signal-error); letter-spacing: -0.05em; line-height: 0.9;\">DENIED</div>\r\n            ");
				}
				jteOutput.writeContent("\r\n        </div>\r\n\r\n        ");
				if (result.safetyGap() != null) {
					jteOutput.writeContent("\r\n            ");
					if (result.safetyGap().isApproved()) {
						jteOutput.writeContent("\r\n                <div style=\"position: sticky; top: 60px; background: var(--bg-body); border: 2px solid #2d5016; padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100;\">\r\n                    <div style=\"font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().toDisplayText());
						jteOutput.writeContent("</div>\r\n                    <div style=\"font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().actionPrompt());
						jteOutput.writeContent("</div>\r\n                </div>\r\n            ");
					} else {
						jteOutput.writeContent("\r\n                <div style=\"position: sticky; top: 60px; background: var(--bg-body); border: 2px solid var(--signal-error); padding: 1.5rem; margin-bottom: 3rem; text-align: center; z-index: 100; background-color: rgba(205, 44, 37, 0.03);\">\r\n                    <div style=\"font-size: 2rem; font-weight: 700; margin-bottom: 0.5rem;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().toDisplayText());
						jteOutput.writeContent("</div>\r\n                    <div style=\"font-size: 0.9rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.05em;\">");
						jteOutput.setContext("div", null);
						jteOutput.writeUserContent(result.safetyGap().actionPrompt());
						jteOutput.writeContent("</div>\r\n                </div>\r\n            ");
					}
					jteOutput.writeContent("\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				if (result.whyThisVerdict() != null) {
					jteOutput.writeContent("\r\n            <div style=\"margin-bottom: 4rem; padding: 2rem; background: var(--bg-panel); border-left: 4px solid var(--text-primary);\">\r\n                <h3 style=\"font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1rem;\">Why This Verdict</h3>\r\n                <p style=\"font-size: 1.25rem; line-height: 1.6; max-width: 65ch; color: var(--text-primary);\">\r\n                    ");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(result.whyThisVerdict());
					jteOutput.writeContent("\r\n                </p>\r\n                ");
					if (result.primaryBottleneck() != null) {
						jteOutput.writeContent("\r\n                    <div style=\"margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border-subtle);\">\r\n                        <span style=\"font-size: 0.85rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em;\">Primary Bottleneck:</span>\r\n                        <span style=\"font-weight: 600; margin-left: 0.5rem;\">");
						jteOutput.setContext("span", null);
						jteOutput.writeUserContent(result.primaryBottleneck());
						jteOutput.writeContent("</span>\r\n                    </div>\r\n                ");
					}
					jteOutput.writeContent("\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        <div style=\"margin-bottom: 3rem;\">\r\n            <p style=\"font-size: 1.25rem; line-height: 1.5; color: var(--text-primary);\">\r\n                ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(result.summary());
				jteOutput.writeContent("\r\n            </p>\r\n        </div>\r\n\r\n        ");
				if (result.primaryDistressFactor() != null) {
					jteOutput.writeContent("\r\n            <div style=\"border: 1px solid var(--signal-error); padding: 2rem; margin-bottom: 4rem; background-color: rgba(205, 44, 37, 0.02);\">\r\n                <h4 style=\"color: var(--signal-error); text-transform: uppercase; font-size: 0.75rem; letter-spacing: 0.05em; margin-bottom: 0.5rem;\">Critical Risk Factor</h4>\r\n                <p style=\"font-size: 1.25rem; font-weight: 600; color: var(--signal-error); margin: 0;\">");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(result.primaryDistressFactor());
					jteOutput.writeContent("</p>\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <h3 style=\"font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-secondary); margin-bottom: 1.5rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;\">Financial Data Points</h3>\r\n            \r\n            <div style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 4rem;\">\r\n                <div>\r\n                    <div style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;\">Total Upfront Cost</div>\r\n                    <div style=\"font-size: 2rem; font-weight: 600; letter-spacing: -0.02em;\">$");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().totalUpfrontCost()));
				jteOutput.writeContent("</div>\r\n                </div>\r\n                <div>\r\n                    <div style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 0.25rem;\">Remaining Buffer</div>\r\n                    ");
				if (result.financials().remainingBuffer() < result.financials().recommendedBuffer()) {
					jteOutput.writeContent("\r\n                        <div style=\"font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: var(--signal-error);\">\r\n                            $");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(String.format("%,d", result.financials().remainingBuffer()));
					jteOutput.writeContent("\r\n                        </div>\r\n                    ");
				} else {
					jteOutput.writeContent("\r\n                        <div style=\"font-size: 2rem; font-weight: 600; letter-spacing: -0.02em; color: var(--text-primary);\">\r\n                            $");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(String.format("%,d", result.financials().remainingBuffer()));
					jteOutput.writeContent("\r\n                        </div>\r\n                    ");
				}
				jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        <div style=\"text-align: center; border-top: 1px solid var(--border-subtle); padding-top: 3rem;\">\r\n           <p style=\"font-size: 0.8rem; color: var(--text-tertiary); margin-bottom: 2rem;\">\r\n               This verdict is final based on the provided parameters.<br>\r\n               To appeal or adjust parameters, start a new simulation.\r\n           </p>\r\n           <a href=\"/RentVerdict/\" class=\"btn-primary\" style=\"display: inline-block; text-decoration: none; width: auto; min-width: 200px;\">Initialize New Assessment</a>\r\n        </div>\r\n\r\n    </div>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		VerdictResult result = (VerdictResult)params.get("result");
		render(jteOutput, jteHtmlInterceptor, result);
	}
}
