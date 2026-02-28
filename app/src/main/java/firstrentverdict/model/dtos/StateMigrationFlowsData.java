package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StateMigrationFlowsData(
        String version,
        String dataset,
        @JsonProperty("source_url") String sourceUrl,
        String notes,
        List<StateFlow> states) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StateFlow(
            @JsonProperty("to_state") String toState,
            @JsonProperty("total_inbound_returns") Integer totalInboundReturns,
            @JsonProperty("top_origins") List<OriginFlow> topOrigins) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OriginFlow(
            @JsonProperty("from_state") String fromState,
            int returns,
            int exemptions,
            @JsonProperty("agi_thousands") int agiThousands,
            @JsonProperty("share_pct") double sharePct) {
    }
}
