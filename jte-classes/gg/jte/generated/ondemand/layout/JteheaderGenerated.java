package gg.jte.generated.ondemand.layout;
public final class JteheaderGenerated {
	public static final String JTE_NAME = "layout/header.jte";
	public static final int[] JTE_LINE_INFO = {7,7,7,7,7,7,7,7,7,7,7};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<header class=\"site-header\" style=\"display: flex; justify-content: space-between; align-items: center; padding: 1rem 0;\">\r\n    <a href=\"/\" class=\"header-brand\" style=\"font-weight: 800; font-size: 1.2rem; text-decoration: none; color: var(--text-primary);\">LifeVerdict</a>\r\n    <nav style=\"display: flex; gap: 1.5rem; align-items: center;\">\r\n        <a href=\"/RentVerdict/\" style=\"color: var(--text-primary); text-decoration: none; font-weight: 600;\">Calculator</a>\r\n        <a href=\"/RentVerdict/cities\" style=\"color: var(--text-secondary); text-decoration: none; font-weight: 500;\">Cities</a>\r\n    </nav>\r\n</header>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
