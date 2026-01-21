package gg.jte.generated.ondemand.pages;
import firstrentverdict.model.verdict.VerdictResult;
import firstrentverdict.model.verdict.Verdict;
public final class JteresultGenerated {
	public static final String JTE_NAME = "pages/result.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,5,5,5,5,8,9,9,11,11,13,13,15,15,17,19,19,19,22,23,23,25,25,25,27,27,29,30,30,32,32,32,34,34,36,42,42,42,46,46,46,46,47,47,47,53,53,54,54,54,55,55,56,56,56,59,64,68,68,68,69,69,69,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, VerdictResult result) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"glass-card\">\r\n        \r\n        ");
				jteOutput.writeContent("\r\n        ");
				if (result.verdict() == Verdict.APPROVED) {
					jteOutput.writeContent("\r\n            <div class=\"verdict-badge verdict-approved\">APPROVED</div>\r\n        ");
				} else if (result.verdict() == Verdict.BORDERLINE) {
					jteOutput.writeContent("\r\n            <div class=\"verdict-badge verdict-borderline\">BORDERLINE</div>\r\n        ");
				} else {
					jteOutput.writeContent("\r\n            <div class=\"verdict-badge verdict-denied\">DENIED</div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"text-align: center; margin-bottom: 2rem;\">\r\n            <p style=\"font-size: 1.1rem; line-height: 1.6;\">");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(result.summary());
				jteOutput.writeContent("</p>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        ");
				if (result.primaryDistressFactor() != null) {
					jteOutput.writeContent("\r\n            <div style=\"background: rgba(239, 68, 68, 0.1); border: 1px solid var(--signal-danger); padding: 1rem; border-radius: 8px; margin-bottom: 2rem; color: #ffcccc;\">\r\n                <strong>🚨 Critical Risk Factor:</strong> ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(result.primaryDistressFactor());
					jteOutput.writeContent("\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				jteOutput.writeContent("\r\n        ");
				if (result.legalProtectionNote() != null) {
					jteOutput.writeContent("\r\n            <div style=\"background: rgba(16, 185, 129, 0.1); border: 1px solid var(--signal-safe); padding: 0.8rem; border-radius: 8px; margin-bottom: 2rem; font-size: 0.9rem;\">\r\n                <strong>⚖️ Legal Protection:</strong> ");
					jteOutput.setContext("div", null);
					jteOutput.writeUserContent(result.legalProtectionNote());
					jteOutput.writeContent("\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <h3 style=\"border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 0.5rem; margin-bottom: 1rem;\">The Cold Hard Numbers</h3>\r\n        \r\n        <div class=\"financial-grid\">\r\n            <div class=\"stat-card\">\r\n                <div class=\"stat-label\">Total Upfront Cost</div>\r\n                <div class=\"stat-value\" style=\"color: var(--signal-danger);\">$");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().totalUpfrontCost()));
				jteOutput.writeContent("</div>\r\n            </div>\r\n            <div class=\"stat-card\">\r\n                <div class=\"stat-label\">Remaining Safe Buffer</div>\r\n                <div class=\"stat-value\" style=\"color: ");
				jteOutput.setContext("div", "style");
				jteOutput.writeUserContent(result.financials().remainingBuffer() < result.financials().recommendedBuffer() ? "var(--signal-warning)" : "var(--signal-safe)");
				jteOutput.setContext("div", null);
				jteOutput.writeContent(";\">\r\n                    $");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().remainingBuffer()));
				jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        <ul class=\"breakdown-list\">\r\n            ");
				for (String line : result.breakdown()) {
					jteOutput.writeContent("\r\n                <li>");
					jteOutput.setContext("li", null);
					jteOutput.writeUserContent(line);
					jteOutput.writeContent("</li>\r\n            ");
				}
				jteOutput.writeContent("\r\n            <li><strong>Recommended Minimum Buffer:</strong> $");
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(String.format("%,d", result.financials().recommendedBuffer()));
				jteOutput.writeContent("</li>\r\n        </ul>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"margin-top: 2rem; display: flex; gap: 1rem; flex-wrap: wrap;\">\r\n            <a href=\"/RentVerdict/\" class=\"btn-primary\" style=\"text-align: center; text-decoration: none; background: var(--bg-surface); border: 1px solid var(--text-secondary); color: var(--text-primary);\">\r\n                Check Another City\r\n            </a>\r\n            ");
				jteOutput.writeContent("\r\n        </div>\r\n\r\n    </div>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		VerdictResult result = (VerdictResult)params.get("result");
		render(jteOutput, jteHtmlInterceptor, result);
	}
}
