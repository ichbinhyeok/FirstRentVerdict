package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CashBufferData(
        String version,
        String unit,
        String formula,
        String notes,
        List<CityBuffer> cities) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityBuffer(
            String city,
            String state,
            int year,
            @JsonProperty("monthly_non_rent_essentials") int monthlyNonRentEssentials,
            @JsonProperty("recommended_post_move_buffer") int recommendedPostMoveBuffer,
            String granularity,
            @JsonProperty("essentials_sources") List<String> essentialsSources,
            @JsonProperty("heuristic_sources") List<String> heuristicSources,
            @JsonProperty("missing_reason") String missingReason) {
    }
}
