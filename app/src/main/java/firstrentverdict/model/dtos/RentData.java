package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RentData(
        String version,
        String unit,
        String notes,
        List<CityRent> cities) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityRent(
            String city,
            String state,
            int year,
            int median,
            int p25,
            int p75,
            List<String> sources,
            @JsonProperty("missing_reason") String missingReason,
            @JsonProperty("is_outdated") boolean isOutdated) {
    }
}
