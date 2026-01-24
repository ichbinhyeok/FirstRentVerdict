package gg.jte.generated.ondemand.pages;
import firstrentverdict.model.dtos.CitiesData.CityEntry;
import java.util.List;
import java.util.Map;
public final class JtelocationsGenerated {
	public static final String JTE_NAME = "pages/locations.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,6,6,9,9,18,18,20,20,20,22,22,24,24,24,24,24,24,24,24,26,26,26,29,29,32,32,53,53,53,54,54,54,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Map<String, List<CityEntry>> citiesByState) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div style=\"max-width: 1000px; margin: 4rem auto; padding: 0 2rem;\">\r\n        <div style=\"text-align: center; margin-bottom: 4rem;\">\r\n            <h1 class=\"hero-title\" style=\"font-size: 2.5rem;\">Supported Jurisdictions</h1>\r\n            <p class=\"hero-subtitle\" style=\"margin: 0 auto;\">Select your city to begin the affordability audit.</p>\r\n        </div>\r\n\r\n        <div class=\"split-layout\" style=\"display: block; border: none; min-height: auto;\">\r\n             <div style=\"column-count: 1; column-gap: 2rem;\">\r\n                ");
				for (String state : citiesByState.keySet()) {
					jteOutput.writeContent("\r\n                    <div style=\"break-inside: avoid; margin-bottom: 2rem; background: var(--bg-panel); padding: 1.5rem; border-radius: 8px;\">\r\n                        <h2 style=\"font-size: 1.2rem; margin-bottom: 1rem; border-bottom: 1px solid var(--border-subtle); padding-bottom: 0.5rem;\">");
					jteOutput.setContext("h2", null);
					jteOutput.writeUserContent(state);
					jteOutput.writeContent("</h2>\r\n                        <ul style=\"list-style: none; padding: 0;\">\r\n                            ");
					for (CityEntry city : citiesByState.get(state)) {
						jteOutput.writeContent("\r\n                                <li style=\"margin-bottom: 0.5rem;\">\r\n                                    <a href=\"/RentVerdict/verdict/");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(city.city().toLowerCase().replace(" ", "-"));
						jteOutput.setContext("a", null);
						jteOutput.writeContent("-");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(city.state().toLowerCase());
						jteOutput.setContext("a", null);
						jteOutput.writeContent("\" \r\n                                       style=\"text-decoration: none; color: var(--text-primary); font-size: 0.95rem; display: block; padding: 0.25rem 0;\">\r\n                                        ");
						jteOutput.setContext("a", null);
						jteOutput.writeUserContent(city.city());
						jteOutput.writeContent("\r\n                                    </a>\r\n                                </li>\r\n                            ");
					}
					jteOutput.writeContent("\r\n                        </ul>\r\n                    </div>\r\n                ");
				}
				jteOutput.writeContent("\r\n             </div>\r\n        </div>\r\n        \r\n        <style>\r\n             @media (min-width: 768px) {\r\n                 div[style*=\"column-count: 1\"] {\r\n                     column-count: 3 !important;\r\n                 }\r\n             }\r\n             @media (min-width: 1024px) {\r\n                 div[style*=\"column-count: 1\"] {\r\n                     column-count: 4 !important;\r\n                 }\r\n             }\r\n        </style>\r\n\r\n        <div class=\"back-link-container\" style=\"text-align: center; margin-top: 4rem;\">\r\n            <a href=\"/RentVerdict/\" class=\"back-link\">&larr; Return to Engine</a>\r\n        </div>\r\n    </div>\r\n");
			}
		}, "Available Jurisdictions - First Rent Verdict", "Complete index of supported cities for rent affordability analysis.", null, false);
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Map<String, List<CityEntry>> citiesByState = (Map<String, List<CityEntry>>)params.get("citiesByState");
		render(jteOutput, jteHtmlInterceptor, citiesByState);
	}
}
