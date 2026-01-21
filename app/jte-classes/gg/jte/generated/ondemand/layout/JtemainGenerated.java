package gg.jte.generated.ondemand.layout;
import gg.jte.Content;
public final class JtemainGenerated {
	public static final String JTE_NAME = "layout/main.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,15,15,15,17,17,17,19,19,22,22,22,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>First Rent Verdict</title>\r\n    <link rel=\"stylesheet\" href=\"/RentVerdict/css/style.css\">\r\n    <link rel=\"icon\" type=\"image/png\" href=\"/RentVerdict/images/favicon.png\">\r\n    <meta name=\"theme-color\" content=\"#ffffff\">\r\n</head>\r\n<body>\r\n    ");
		gg.jte.generated.ondemand.layout.JteheaderGenerated.render(jteOutput, jteHtmlInterceptor);
		jteOutput.writeContent("\r\n    <main style=\"flex: 1;\">\r\n        ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n    </main>\r\n    ");
		gg.jte.generated.ondemand.layout.JtefooterGenerated.render(jteOutput, jteHtmlInterceptor);
		jteOutput.writeContent("\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content content = (Content)params.get("content");
		render(jteOutput, jteHtmlInterceptor, content);
	}
}
