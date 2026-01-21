package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CitiesData(
        String version,
        List<CityEntry> cities) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityEntry(
            String city,
            String state) {
    }
}
