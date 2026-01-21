package gg.jte.generated.ondemand.pages;
import java.util.List;
import firstrentverdict.model.dtos.CitiesData.CityEntry;
public final class JteindexGenerated {
	public static final String JTE_NAME = "pages/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,5,5,5,5,7,24,33,33,34,34,34,34,34,34,34,34,34,34,34,34,34,34,35,35,63,63,63,69,69,69,70,70,70,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<CityEntry> cities) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"split-layout\">\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"split-left animate-entry\">\r\n            <div style=\"max-width: 440px;\">\r\n                <h1 class=\"hero-title\">First Apartment<br>True Cost Verdict</h1>\r\n                <p class=\"hero-subtitle\">\r\n                    This is not a marketing suggestion.<br>\r\n                    It is a mathematical certainty based on 2026 economic data.\r\n                </p>\r\n                <div style=\"margin-top: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);\">\r\n                    <p style=\"font-size: 0.8rem; color: var(--text-tertiary);\">\r\n                        CONFIDENTIAL RISK ASSESSMENT<br>\r\n                        DO NOT DISTRIBUTE OUTSIDE DECISION CIRCLE\r\n                    </p>\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div class=\"split-right\">\r\n            <div class=\"form-container animate-entry\" style=\"animation-delay: 0.1s;\">\r\n                <form action=\"/RentVerdict/verdict\" method=\"post\">\r\n                    \r\n                    <div class=\"form-group\">\r\n                        <label for=\"cityState\">Jurisdiction (Target City)</label>\r\n                        <select id=\"cityState\" name=\"cityState\" required>\r\n                            <option value=\"\" disabled selected>Select Jurisdiction...</option>\r\n                            ");
				for (CityEntry city : cities) {
					jteOutput.writeContent("\r\n                                <option value=\"");
					jteOutput.setContext("option", "value");
					jteOutput.writeUserContent(city.city());
					jteOutput.setContext("option", null);
					jteOutput.writeContent("|");
					jteOutput.setContext("option", "value");
					jteOutput.writeUserContent(city.state());
					jteOutput.setContext("option", null);
					jteOutput.writeContent("\">");
					jteOutput.setContext("option", null);
					jteOutput.writeUserContent(city.city());
					jteOutput.writeContent(", ");
					jteOutput.setContext("option", null);
					jteOutput.writeUserContent(city.state());
					jteOutput.writeContent("</option>\r\n                            ");
				}
				jteOutput.writeContent("\r\n                        </select>\r\n                    </div>\r\n\r\n                    <div class=\"form-group\">\r\n                        <label for=\"monthlyRent\">Base Monthly Liability (Rent)</label>\r\n                        <input type=\"number\" id=\"monthlyRent\" name=\"monthlyRent\" placeholder=\"0.00\" min=\"100\" required>\r\n                    </div>\r\n\r\n                    <div class=\"form-group\">\r\n                        <label for=\"availableCash\">Liquid Assets (Cash on Hand)</label>\r\n                        <input type=\"number\" id=\"availableCash\" name=\"availableCash\" placeholder=\"0.00\" min=\"0\" required>\r\n                    </div>\r\n\r\n                    <div class=\"checkbox-group\">\r\n                        <label class=\"checkbox-label\">\r\n                            <input type=\"checkbox\" name=\"hasPet\" value=\"true\">\r\n                            <span>Pet</span>\r\n                        </label>\r\n                        <label class=\"checkbox-label\">\r\n                            <input type=\"checkbox\" name=\"isLocalMove\" value=\"true\" checked>\r\n                            <span>Local Move</span>\r\n                        </label>\r\n                    </div>\r\n\r\n                    <button type=\"submit\" class=\"btn-primary\">Execute Verdict Analysis</button>\r\n                    \r\n                    <p style=\"text-align: center; margin-top: 2rem; font-size: 0.75rem; color: var(--text-tertiary);\">\r\n                        Simulation ID: ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase());
				jteOutput.writeContent("\r\n                    </p>\r\n                </form>\r\n            </div>\r\n        </div>\r\n    </div>\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<CityEntry> cities = (List<CityEntry>)params.get("cities");
		render(jteOutput, jteHtmlInterceptor, cities);
	}
}
