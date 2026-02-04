package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CityInsightData(
        String version,
        List<CityInsight> data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityInsight(
            String city,
            String state,
            String marketTrend,
            String seasonalTip,
            String localLaw,
            String renterAdvice) {
    }
}
