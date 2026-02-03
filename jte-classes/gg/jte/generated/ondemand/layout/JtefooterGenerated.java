package gg.jte.generated.ondemand.layout;
public final class JtefooterGenerated {
	public static final String JTE_NAME = "layout/footer.jte";
	public static final int[] JTE_LINE_INFO = {14,14,14,14,14,14,14,14,14,14,14};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<footer class=\"site-footer\">\r\n    <div style=\"margin-bottom: 1rem; display: flex; justify-content: center; gap: 2rem; flex-wrap: wrap;\">\r\n        <a href=\"/RentVerdict/about\" style=\"color: var(--text-tertiary); text-decoration: none;\">About</a>\r\n        <a href=\"/RentVerdict/methodology\" style=\"color: var(--text-tertiary); text-decoration: none;\">Methodology</a>\r\n        <a href=\"/RentVerdict/guide/rent-affordability-rule\" style=\"color: var(--text-tertiary); text-decoration: none;\">Guide</a>\r\n        <span style=\"color: var(--border-subtle);\">|</span>\r\n        <a href=\"/RentVerdict/privacy\" style=\"color: var(--text-tertiary); text-decoration: none;\">Privacy</a>\r\n        <a href=\"/RentVerdict/terms\" style=\"color: var(--text-tertiary); text-decoration: none;\">Terms</a>\r\n        <a href=\"mailto:spainhyeok@gmail.com\" style=\"color: var(--text-tertiary); text-decoration: none;\">Contact</a>\r\n    </div>\r\n    <div style=\"font-size: 0.75rem; color: var(--text-tertiary);\">\r\n        &copy; 2026 First Rent Verdict. Confidential Risk Assessment System.\r\n    </div>\r\n</footer>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
