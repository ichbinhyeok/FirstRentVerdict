package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovingData(
        String version,
        String unit,
        @JsonProperty("move_type") String moveType,
        String notes,
        List<CityMoving> cities) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityMoving(
            String city,
            String state,
            int year,
            int typical,
            int low,
            int high,
            String assumptions,
            List<String> sources,
            @JsonProperty("missing_reason") String missingReason) {
    }
}
