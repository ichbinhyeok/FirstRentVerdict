package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ScreeningIncomeAssumptionsData(
        String version,
        String dataset,
        @JsonProperty("last_checked") String lastChecked,
        @JsonProperty("usage_note") String usageNote,
        List<IncomeAssumption> assumptions) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IncomeAssumption(
            @JsonProperty("threshold_label") String thresholdLabel,
            @JsonProperty("rent_to_income_multiplier") double rentToIncomeMultiplier,
            @JsonProperty("risk_band") String riskBand,
            String description,
            @JsonProperty("is_law") boolean isLaw,
            String confidence) {
    }
}
