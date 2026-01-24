package gg.jte.generated.ondemand.layout;
public final class JtemainGenerated {
	public static final String JTE_NAME = "layout/main.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,11,11,11,11,12,12,13,13,13,13,13,13,13,13,13,14,14,15,15,16,16,16,16,16,16,16,16,16,17,17,17,17,17,17,17,17,17,18,18,19,19,21,21,23,23,25,27,27,27,27,27,27,27,27,27,28,28,29,29,29,29,29,29,29,29,29,30,30,33,39,40,43,43,45,45,45,47,47,50,50,50,0,1,2,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content, String title, String description, String canonical, boolean noindex) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(title);
		jteOutput.writeContent("</title>\r\n    ");
		if (description != null) {
			jteOutput.writeContent("\r\n        <meta name=\"description\"");
			var __jte_html_attribute_0 = description;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeContent(" content=\"");
				jteOutput.setContext("meta", "content");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("meta", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		if (canonical != null) {
			jteOutput.writeContent("\r\n        <link rel=\"canonical\"");
			var __jte_html_attribute_1 = canonical;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
				jteOutput.writeContent(" href=\"");
				jteOutput.setContext("link", "href");
				jteOutput.writeUserContent(__jte_html_attribute_1);
				jteOutput.setContext("link", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\r\n        <meta property=\"og:url\"");
			var __jte_html_attribute_2 = canonical;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
				jteOutput.writeContent(" content=\"");
				jteOutput.setContext("meta", "content");
				jteOutput.writeUserContent(__jte_html_attribute_2);
				jteOutput.setContext("meta", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		if (noindex) {
			jteOutput.writeContent("\r\n        <meta name=\"robots\" content=\"noindex, nofollow\">\r\n    ");
		} else {
			jteOutput.writeContent("\r\n        <meta name=\"robots\" content=\"index, follow\">\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <meta property=\"og:type\" content=\"website\">\r\n    <meta property=\"og:title\"");
		var __jte_html_attribute_3 = title;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_3);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    ");
		if (description != null) {
			jteOutput.writeContent("\r\n        <meta property=\"og:description\"");
			var __jte_html_attribute_4 = description;
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
				jteOutput.writeContent(" content=\"");
				jteOutput.setContext("meta", "content");
				jteOutput.writeUserContent(__jte_html_attribute_4);
				jteOutput.setContext("meta", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\r\n    ");
		}
		jteOutput.writeContent("\r\n    <meta property=\"og:site_name\" content=\"First Rent Verdict\">\r\n    <meta property=\"og:locale\" content=\"en_US\">\r\n    <meta property=\"og:image\" content=\"https://lifeverdict.com/images/og-card.png\"> ");
		jteOutput.writeContent("\r\n\r\n    <link rel=\"stylesheet\" href=\"/RentVerdict/css/style.css\">\r\n    <link rel=\"icon\" type=\"image/png\" href=\"/RentVerdict/images/favicon.png\">\r\n    <meta name=\"theme-color\" content=\"#ffffff\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    ");
		jteOutput.writeContent("\r\n</head>\r\n<body>\r\n    ");
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
		String title = (String)params.getOrDefault("title", "First Rent Verdict");
		String description = (String)params.getOrDefault("description", "Standardize your rental financial risk interaction with the 50/30/20 rule. First Rent Verdict provides objective affordability analysis for new tenants.");
		String canonical = (String)params.getOrDefault("canonical", null);
		boolean noindex = (boolean)params.getOrDefault("noindex", false);
		render(jteOutput, jteHtmlInterceptor, content, title, description, canonical, noindex);
	}
}
