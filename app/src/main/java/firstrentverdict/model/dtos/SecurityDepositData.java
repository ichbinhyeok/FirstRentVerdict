package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SecurityDepositData(
                String city,
                String state,
                @JsonProperty("city_practice") CityPractice city_practice,
                List<String> sources) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CityPractice(
                        @JsonProperty("typical_multipliers") List<Double> typicalMultipliers,
                        @JsonProperty("high_risk_multipliers") List<Double> highRiskMultipliers,
                        String notes) {
        }
}
