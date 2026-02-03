package gg.jte.generated.ondemand.layout;
public final class JtemainGenerated {
	public static final String JTE_NAME = "layout/main.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,11,11,11,11,12,12,12,12,12,12,12,12,12,13,13,13,13,13,13,13,13,13,15,15,17,17,19,20,20,20,20,20,20,20,20,20,21,21,21,21,21,21,21,21,21,22,22,22,22,22,22,22,22,22,29,31,31,31,31,31,31,31,31,31,32,32,32,32,32,32,32,32,32,35,40,43,46,56,56,59,59,59,62,62,65,65,65,0,1,2,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String title, String description, String canonical, boolean noindex, gg.jte.Content content) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(title);
		jteOutput.writeContent("</title>\r\n    <meta name=\"description\"");
		var __jte_html_attribute_0 = description;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    <link rel=\"canonical\"");
		var __jte_html_attribute_1 = canonical;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" href=\"");
			jteOutput.setContext("link", "href");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("link", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    \r\n    ");
		if (noindex) {
			jteOutput.writeContent("\r\n        <meta name=\"robots\" content=\"noindex, nofollow\">\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <meta property=\"og:title\"");
		var __jte_html_attribute_2 = title;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    <meta property=\"og:description\"");
		var __jte_html_attribute_3 = description;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_3);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    <meta property=\"og:url\"");
		var __jte_html_attribute_4 = canonical;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_4);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    <meta property=\"og:type\" content=\"website\">\r\n    <meta property=\"og:site_name\" content=\"First Rent Verdict\">\r\n    <meta property=\"og:image\" content=\"https://movecostinfo.com/images/og-rent-verdict-v1.png\">\r\n    <meta property=\"og:image:width\" content=\"1200\">\r\n    <meta property=\"og:image:height\" content=\"630\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <meta name=\"twitter:card\" content=\"summary_large_image\">\r\n    <meta name=\"twitter:title\"");
		var __jte_html_attribute_5 = title;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_5);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    <meta name=\"twitter:description\"");
		var __jte_html_attribute_6 = description;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
			jteOutput.writeContent(" content=\"");
			jteOutput.setContext("meta", "content");
			jteOutput.writeUserContent(__jte_html_attribute_6);
			jteOutput.setContext("meta", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    <meta name=\"twitter:image\" content=\"https://movecostinfo.com/images/og-rent-verdict-v1.png\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap\" rel=\"stylesheet\">\r\n    \r\n    ");
		jteOutput.writeContent("\r\n    <link rel=\"icon\" type=\"image/svg+xml\" href=\"/images/favicon.svg\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <link rel=\"stylesheet\" href=\"/css/style.css\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <script async src=\"https://www.googletagmanager.com/gtag/js?id=G-5YQX4VQ8ZT\"></script>\r\n    <script>\r\n        window.dataLayer = window.dataLayer || [];\r\n        function gtag(){dataLayer.push(arguments);}\r\n        gtag('js', new Date());\r\n        gtag('config', 'G-5YQX4VQ8ZT');\r\n    </script>\r\n</head>\r\n<body>\r\n    ");
		gg.jte.generated.ondemand.layout.JteheaderGenerated.render(jteOutput, jteHtmlInterceptor);
		jteOutput.writeContent("\r\n    \r\n    <main>\r\n        ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n    </main>\r\n    \r\n    ");
		gg.jte.generated.ondemand.layout.JtefooterGenerated.render(jteOutput, jteHtmlInterceptor);
		jteOutput.writeContent("\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String title = (String)params.getOrDefault("title", "First Rent Verdict");
		String description = (String)params.getOrDefault("description", "First Rent Verdict - Know your true move-in cost before signing the lease.");
		String canonical = (String)params.getOrDefault("canonical", "https://movecostinfo.com/");
		boolean noindex = (boolean)params.getOrDefault("noindex", false);
		gg.jte.Content content = (gg.jte.Content)params.get("content");
		render(jteOutput, jteHtmlInterceptor, title, description, canonical, noindex, content);
	}
}
