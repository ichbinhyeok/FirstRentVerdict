package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApplicationFeeRulesData(
        String version,
        String dataset,
        @JsonProperty("last_checked") String lastChecked,
        String scope,
        @JsonProperty("usage_note") String usageNote,
        List<ApplicationFeeRule> rules) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApplicationFeeRule(
            @JsonProperty("jurisdiction_type") String jurisdictionType,
            String state,
            String city,
            @JsonProperty("fee_type") String feeType,
            @JsonProperty("cap_type") String capType,
            @JsonProperty("cap_amount") Integer capAmount,
            @JsonProperty("cap_currency") String capCurrency,
            @JsonProperty("cap_formula") String capFormula,
            @JsonProperty("refund_required_when") List<String> refundRequiredWhen,
            @JsonProperty("disclosure_required") List<String> disclosureRequired,
            @JsonProperty("portable_screening_report_rule") String portableScreeningReportRule,
            List<String> exceptions,
            @JsonProperty("product_risk_note") String productRiskNote,
            @JsonProperty("source_url") String sourceUrl,
            @JsonProperty("source_type") String sourceType,
            @JsonProperty("source_citation") String sourceCitation,
            @JsonProperty("effective_date") String effectiveDate,
            @JsonProperty("last_checked") String lastChecked,
            String confidence) {
    }
}
