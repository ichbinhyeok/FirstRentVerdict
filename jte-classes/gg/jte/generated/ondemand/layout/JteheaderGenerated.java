package gg.jte.generated.ondemand.layout;
public final class JteheaderGenerated {
	public static final String JTE_NAME = "layout/header.jte";
	public static final int[] JTE_LINE_INFO = {8,8,8,8,8,8,8,8,8,8,8};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<header class=\"site-header\">\r\n    <a href=\"/RentVerdict/\" class=\"header-brand\">First Rent Verdict</a>\r\n    <nav class=\"header-nav\">\r\n        <a href=\"/RentVerdict/\">Calculator</a>\r\n        <a href=\"/RentVerdict/cities\">Cities</a>\r\n        <a href=\"/RentVerdict/methodology\">How It Works</a>\r\n    </nav>\r\n</header>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
