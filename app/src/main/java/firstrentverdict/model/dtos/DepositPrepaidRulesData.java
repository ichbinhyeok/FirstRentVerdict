package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DepositPrepaidRulesData(
        String version,
        String dataset,
        @JsonProperty("last_checked") String lastChecked,
        String scope,
        @JsonProperty("usage_note") String usageNote,
        List<DepositPrepaidRule> rules) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DepositPrepaidRule(
            String state,
            @JsonProperty("deposit_cap_multiplier") Double depositCapMultiplier,
            @JsonProperty("cap_type") String capType,
            @JsonProperty("cap_formula") String capFormula,
            @JsonProperty("prepaid_rent_limit") String prepaidRentLimit,
            @JsonProperty("pet_deposit_included_in_cap") Boolean petDepositIncludedInCap,
            @JsonProperty("small_landlord_exception") String smallLandlordException,
            @JsonProperty("furnished_exception") String furnishedException,
            @JsonProperty("return_deadline_days") Integer returnDeadlineDays,
            @JsonProperty("interest_required") Object interestRequired,
            @JsonProperty("source_url") String sourceUrl,
            @JsonProperty("source_type") String sourceType,
            @JsonProperty("source_citation") String sourceCitation,
            @JsonProperty("effective_date") String effectiveDate,
            @JsonProperty("last_checked") String lastChecked,
            String confidence,
            @JsonProperty("product_risk_note") String productRiskNote) {
    }
}
