package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SecurityDepositRaw(
        String version,
        String unit,
        String notes,
        @JsonProperty("state_laws") List<StateLaw> stateLaws,
        List<CityDeposit> cities) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StateLaw(
            String state,
            @JsonProperty("legal_cap_multiplier") Double legalCapMultiplier,
            @JsonProperty("effective_date") String effectiveDate,
            String exceptions,
            List<String> sources) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityDeposit(
            String city,
            String state,
            @JsonProperty("city_practice") CityPractice cityPractice,
            List<String> sources,
            @JsonProperty("missing_reason") String missingReason) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityPractice(
            @JsonProperty("typical_multipliers") List<Double> typicalMultipliers,
            @JsonProperty("high_risk_multipliers") List<Double> highRiskMultipliers,
            String notes) {
    }
}
