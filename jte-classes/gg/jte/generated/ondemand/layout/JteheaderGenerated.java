package gg.jte.generated.ondemand.layout;
public final class JteheaderGenerated {
	public static final String JTE_NAME = "layout/header.jte";
	public static final int[] JTE_LINE_INFO = {3,3,3,3,3,3,3,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<header class=\"site-header\">\r\n    <a href=\"/\" class=\"header-brand\">LifeVerdict</a>\r\n</header>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
