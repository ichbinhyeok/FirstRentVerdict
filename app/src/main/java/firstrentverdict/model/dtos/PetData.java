package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PetData(
        String version,
        String unit,
        String notes,
        List<CityPet> cities) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityPet(
            String city,
            String state,
            int year,
            @JsonProperty("one_time") CostRange oneTime,
            @JsonProperty("monthly_pet_rent") CostRange monthlyPetRent,
            List<String> sources,
            @JsonProperty("missing_reason") String missingReason) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CostRange(
            int avg,
            int low,
            int high,
            String notes) {
    }
}
