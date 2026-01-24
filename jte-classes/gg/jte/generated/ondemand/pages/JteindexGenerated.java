package gg.jte.generated.ondemand.pages;
import java.util.List;
import firstrentverdict.model.dtos.CitiesData.CityEntry;
public final class JteindexGenerated {
	public static final String JTE_NAME = "pages/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,5,5,5,5,7,24,33,33,34,34,34,34,34,34,34,34,34,34,34,34,34,34,35,35,70,78,96,96,96,103,110,110,111,111,111,111,111,111,111,111,113,113,113,113,113,113,115,115,124,124,124,125,125,125,3,3,3,3};
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
				jteOutput.writeContent("\r\n                        </select>\r\n                    </div>\r\n\r\n                    <div class=\"form-group\">\r\n                        <label for=\"monthlyRent\">Base Monthly Liability (Rent)</label>\r\n                        <input type=\"text\" id=\"monthlyRent\" name=\"monthlyRent\" placeholder=\"0\" inputmode=\"numeric\" required>\r\n                    </div>\r\n\r\n                    <div class=\"form-group\">\r\n                        <label for=\"availableCash\">Liquid Assets (Cash on Hand)</label>\r\n                        <input type=\"text\" id=\"availableCash\" name=\"availableCash\" placeholder=\"0\" inputmode=\"numeric\" required>\r\n                    </div>\r\n\r\n                    <div class=\"checkbox-group\">\r\n                        <label class=\"checkbox-label\">\r\n                            <input type=\"checkbox\" name=\"hasPet\" value=\"true\">\r\n                            <span>Pet</span>\r\n                        </label>\r\n                        <label class=\"checkbox-label\">\r\n                            <input type=\"checkbox\" name=\"isLocalMove\" value=\"true\" checked>\r\n                            <span>Local Move</span>\r\n                        </label>\r\n                    </div>\r\n\r\n                    <button type=\"submit\" class=\"btn-primary\">Execute Verdict Analysis</button>\r\n                    \r\n                    <script>\r\n                        document.addEventListener('DOMContentLoaded', () => {\r\n                            const currencyInputs = ['monthlyRent', 'availableCash'];\r\n                            \r\n                            currencyInputs.forEach(id => {\r\n                                const input = document.getElementById(id);\r\n                                if (!input) return;\r\n\r\n                                ");
				jteOutput.writeContent("\n                                input.addEventListener('blur', (e) => {\r\n                                    let value = e.target.value.replace(/,/g, '').replace(/[^0-9]/g, '');\r\n                                    if (value) {\r\n                                        e.target.value = parseInt(value).toLocaleString('en-US');\r\n                                    }\r\n                                });\r\n\r\n                                ");
				jteOutput.writeContent("\n                                input.addEventListener('focus', (e) => {\r\n                                    e.target.value = e.target.value.replace(/,/g, '');\r\n                                });\r\n                            });\r\n\r\n                            document.querySelector('form').addEventListener('submit', (e) => {\r\n                                currencyInputs.forEach(id => {\r\n                                    const input = document.getElementById(id);\r\n                                    if (input) {\r\n                                        input.value = input.value.replace(/,/g, '');\r\n                                    }\r\n                                });\r\n                            });\r\n                        });\r\n                    </script>\r\n                    \r\n                    <p style=\"text-align: center; margin-top: 2rem; font-size: 0.75rem; color: var(--text-tertiary);\">\r\n                        Simulation ID: ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase());
				jteOutput.writeContent("\r\n                    </p>\r\n                </form>\r\n            </div>\r\n        </div>\r\n    </div>\r\n\r\n    ");
				jteOutput.writeContent("\r\n    <div style=\"background: var(--bg-panel); border-top: 1px solid var(--border-subtle); padding: 4rem 2rem;\">\r\n        <div style=\"max-width: 1000px; margin: 0 auto;\">\r\n            <h2 style=\"font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.1em; color: var(--text-tertiary); margin-bottom: 2rem;\">\r\n                Popular Jurisdictions\r\n            </h2>\r\n            <div style=\"display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 1rem;\">\r\n                ");
				for (CityEntry city : cities.subList(0, Math.min(cities.size(), 12))) {
					jteOutput.writeContent("\r\n                    <a href=\"/RentVerdict/verdict/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(city.city().toLowerCase().replace(" ", "-"));
					jteOutput.setContext("a", null);
					jteOutput.writeContent("-");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(city.state().toLowerCase());
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\" \r\n                       style=\"color: var(--text-secondary); text-decoration: none; font-size: 0.9rem;\">\r\n                        ");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(city.city());
					jteOutput.writeContent(", ");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(city.state());
					jteOutput.writeContent(" &rarr;\r\n                    </a>\r\n                ");
				}
				jteOutput.writeContent("\r\n            </div>\r\n            <div style=\"margin-top: 2rem; text-align: center;\">\r\n                <a href=\"/RentVerdict/cities\" class=\"btn-primary\" style=\"display: inline-block; width: auto; padding: 0.8rem 2rem; font-size: 0.9rem; background: var(--bg-panel); color: var(--text-primary); border: 1px solid var(--border-subtle);\">\r\n                    View All 100+ Jurisdictions &rarr;\r\n                </a>\r\n            </div>\r\n        </div>\r\n    </div>\r\n");
			}
		}, "First Rent Verdict", "Standardize your rental financial risk interaction with the 50/30/20 rule. First Rent Verdict provides objective affordability analysis for new tenants.", null, false);
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<CityEntry> cities = (List<CityEntry>)params.get("cities");
		render(jteOutput, jteHtmlInterceptor, cities);
	}
}
