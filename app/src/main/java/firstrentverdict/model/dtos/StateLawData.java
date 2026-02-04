package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StateLawData {
    // Wrapper/Static class for inner record if needed, or just the record itself.
    // Following the repository usage StateLawData.StateLaw, I'll make StateLawData
    // a container or just a class with inner record.

    public record StateLaw(
            String state,
            @JsonProperty("legal_cap_multiplier") Double legalCapMultiplier,
            @JsonProperty("effective_date") String effectiveDate,
            String exceptions,
            List<String> sources) {
    }
}
