package gg.jte.generated.ondemand.pages;
import firstrentverdict.service.seo.CityContentGenerator.CityPageContent;
public final class Jtecity_landingGenerated {
	public static final String JTE_NAME = "pages/city_landing.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,4,4,8,8,11,18,18,18,18,18,18,25,28,28,28,31,31,31,35,38,44,44,44,47,47,47,50,50,50,54,60,60,60,63,63,63,64,64,64,65,65,65,70,76,76,77,77,80,80,80,83,83,83,86,86,87,87,90,94,94,94,101,112,122,122,122,123,123,123,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, CityPageContent pageData) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div style=\"max-width: 800px; margin: 4rem auto; padding: 0 1.5rem;\">\r\n        \r\n        ");
				jteOutput.writeContent("\r\n        <header style=\"text-align: center; margin-bottom: 3rem;\">\r\n            <div style=\"font-size: 0.85rem; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 0.5rem;\">\r\n                2026 Rental Market Data\r\n            </div>\r\n            <h1 style=\"font-size: 2.5rem; font-weight: 800; letter-spacing: -0.02em; line-height: 1.1; margin-bottom: 1rem;\">\r\n                Rental Financial Overview:<br>\r\n                <span style=\"color: var(--text-secondary);\">");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(pageData.city());
				jteOutput.writeContent(", ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(pageData.state());
				jteOutput.writeContent("</span>\r\n            </h1>\r\n            <p style=\"font-size: 1.1rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto; line-height: 1.6;\">\r\n                Objective financial requirements for prospective tenants, based on local economic indicators.\r\n            </p>\r\n        </header>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"background: var(--bg-surface); border-left: 4px solid var(--text-primary); padding: 2rem; margin-bottom: 3rem; border-radius: 4px; box-shadow: 0 4px 20px rgba(0,0,0,0.03);\">\r\n            <h2 style=\"font-size: 1.4rem; font-weight: 700; color: var(--text-primary); margin-bottom: 1rem; line-height: 1.3;\">\r\n                ");
				jteOutput.setContext("h2", null);
				jteOutput.writeUserContent(pageData.preVerdictHeadline());
				jteOutput.writeContent("\r\n            </h2>\r\n            <p style=\"font-size: 1.05rem; color: var(--text-secondary); line-height: 1.6;\">\r\n                ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(pageData.riskNarrative());
				jteOutput.writeContent("\r\n            </p>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 1.5rem; margin-bottom: 4rem;\">\r\n            \r\n            ");
				jteOutput.writeContent("\r\n            <div style=\"background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);\">\r\n                <div style=\"font-size: 0.9rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 1rem;\">\r\n                    Typical Income Requirement\r\n                </div>\r\n                <div style=\"font-size: 2.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;\">\r\n                    $");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", pageData.monthlyIncomeReq()));
				jteOutput.writeContent("<span style=\"font-size: 1rem; color: var(--text-tertiary); font-weight: 400;\">/mo</span>\r\n                </div>\r\n                <div style=\"font-size: 1rem; color: var(--text-secondary); margin-bottom: 1.5rem;\">\r\n                    or approx. <strong>$");
				jteOutput.setContext("strong", null);
				jteOutput.writeUserContent(String.format("%,d", pageData.yearlyIncomeReq()));
				jteOutput.writeContent("/yr</strong>\r\n                </div>\r\n                <p style=\"font-size: 0.9rem; color: var(--text-secondary); line-height: 1.5;\">\r\n                    ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(pageData.incomeLogicText());
				jteOutput.writeContent("\r\n                </p>\r\n            </div>\r\n\r\n            ");
				jteOutput.writeContent("\r\n            <div style=\"background: var(--bg-surface); padding: 2rem; border-radius: 12px; border: 1px solid var(--border-subtle);\">\r\n                <div style=\"font-size: 0.9rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 1rem;\">\r\n                    Est. Move-in Cash Needed\r\n                </div>\r\n                <div style=\"font-size: 2.5rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;\">\r\n                    $");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(String.format("%,d", pageData.totalUpfrontEstimate()));
				jteOutput.writeContent("\r\n                </div>\r\n                <div style=\"font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6;\">\r\n                    Includes <strong>First Month Rent</strong> ($");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(pageData.medianRent());
				jteOutput.writeContent("), \r\n                    <strong>Security Deposit</strong> (~$");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(pageData.typicalDeposit());
				jteOutput.writeContent("), and \r\n                    <strong>Moving Costs</strong> (~$");
				jteOutput.setContext("div", null);
				jteOutput.writeUserContent(pageData.typicalMoving());
				jteOutput.writeContent(").\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <h3 style=\"font-size: 0.9rem; font-weight: 600; color: var(--text-tertiary); text-transform: uppercase; margin-bottom: 2rem; letter-spacing: 0.05em;\">\r\n                Common Moving Questions\r\n            </h3>\r\n            \r\n            ");
				if (pageData.commonQuestions() != null) {
					jteOutput.writeContent("\r\n                ");
					for (firstrentverdict.service.seo.CityContentGenerator.QnA qna : pageData.commonQuestions()) {
						jteOutput.writeContent("\r\n                    <div style=\"margin-bottom: 2rem;\">\r\n                        <h4 style=\"font-size: 1.2rem; font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;\">\r\n                            ");
						jteOutput.setContext("h4", null);
						jteOutput.writeUserContent(qna.question());
						jteOutput.writeContent("\r\n                        </h4>\r\n                        <p style=\"font-size: 1rem; color: var(--text-secondary); line-height: 1.6;\">\r\n                            ");
						jteOutput.setContext("p", null);
						jteOutput.writeUserContent(qna.answer());
						jteOutput.writeContent("\r\n                        </p>\r\n                    </div>\r\n                ");
					}
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"margin-bottom: 4rem;\">\r\n            <h2 style=\"font-size: 1.5rem; font-weight: 700; margin-bottom: 1rem;\">Regional Market Context</h2>\r\n            <p style=\"font-size: 1.1rem; line-height: 1.7; color: var(--text-secondary); margin-bottom: 1.5rem;\">\r\n                ");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(pageData.marketContextText());
				jteOutput.writeContent("\r\n            </p>\r\n            <a href=\"/RentVerdict/guide/rent-affordability-rule\" style=\"font-size: 0.95rem; color: var(--text-primary); font-weight: 500; text-decoration: underline;\">\r\n                Learn more about our 50/30/20 Liquidity Framework &rarr;\r\n            </a>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"margin-bottom: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);\">\r\n            <h3 style=\"font-size: 1rem; font-weight: 600; color: var(--text-tertiary); margin-bottom: 1rem;\">POPULAR MARKETS</h3>\r\n            <div style=\"display: flex; gap: 1rem; flex-wrap: wrap;\">\r\n                <a href=\"/RentVerdict/verdict/austin-tx\" style=\"color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;\">Austin, TX</a>\r\n                <a href=\"/RentVerdict/verdict/dallas-tx\" style=\"color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;\">Dallas, TX</a>\r\n                <a href=\"/RentVerdict/verdict/miami-fl\" style=\"color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;\">Miami, FL</a>\r\n                <a href=\"/RentVerdict/verdict/new-york-ny\" style=\"color: var(--text-secondary); text-decoration: none; font-size: 0.9rem; border: 1px solid var(--border-subtle); padding: 0.5rem 1rem; border-radius: 20px;\">New York, NY</a>\r\n            </div>\r\n        </div>\r\n\r\n        ");
				jteOutput.writeContent("\r\n        <div style=\"background: var(--bg-surface); border: 1px solid var(--border-subtle); border-radius: 12px; padding: 3rem; text-align: center;\">\r\n            <h2 style=\"font-size: 1.8rem; font-weight: 700; margin-bottom: 1rem;\">Check Your Specific Fit</h2>\r\n            <p style=\"font-size: 1.1rem; color: var(--text-secondary); margin-bottom: 2rem; max-width: 500px; margin-left: auto; margin-right: auto;\">\r\n                Every financial situation is unique. Use current 2026 market data to check if this move is safe for you.\r\n            </p>\r\n            <a href=\"/RentVerdict/\" class=\"btn-primary\" style=\"text-decoration: none; display: inline-block; padding: 1rem 2rem; font-size: 1.1rem;\">Launch Financial Calculator</a>\r\n        </div>\r\n\r\n    </div>\r\n");
			}
		}, pageData.pageTitle(), pageData.metaDescription(), "https://lifeverdict.com/verdict/" + pageData.city().toLowerCase().replace(" ", "-") + "-" + pageData.state().toLowerCase(), false);
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		CityPageContent pageData = (CityPageContent)params.get("pageData");
		render(jteOutput, jteHtmlInterceptor, pageData);
	}
}
