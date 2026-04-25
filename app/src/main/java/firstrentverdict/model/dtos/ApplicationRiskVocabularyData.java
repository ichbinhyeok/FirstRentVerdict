package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApplicationRiskVocabularyData(
        String version,
        String dataset,
        @JsonProperty("last_checked") String lastChecked,
        @JsonProperty("usage_note") String usageNote,
        List<RiskTerm> terms) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RiskTerm(
            String term,
            String category,
            @JsonProperty("user_question") String userQuestion,
            @JsonProperty("tool_prompt") String toolPrompt) {
    }
}
