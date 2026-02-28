package firstrentverdict.model.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CityEconomicFactsData(
        String version,
        String dataset,
        String notes,
        Coverage coverage,
        List<CityEconomicFact> cities) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Coverage(
            @JsonProperty("total_cities") int totalCities,
            @JsonProperty("matched_cities") int matchedCities,
            @JsonProperty("unmatched_cities") int unmatchedCities) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CityEconomicFact(
            String city,
            String state,
            int year,
            @JsonProperty("census_place_name") String censusPlaceName,
            @JsonProperty("census_place_geoid") String censusPlaceGeoid,
            @JsonProperty("median_household_income") Integer medianHouseholdIncome,
            @JsonProperty("median_gross_rent") Integer medianGrossRent,
            @JsonProperty("occupied_households") Integer occupiedHouseholds,
            @JsonProperty("renter_households") Integer renterHouseholds,
            @JsonProperty("renter_share_pct") Double renterSharePct,
            @JsonProperty("rent_burdened_35_plus_households") Integer rentBurdened35PlusHouseholds,
            @JsonProperty("rent_burdened_35_plus_share_pct") Double rentBurdened35PlusSharePct,
            @JsonProperty("annual_rent_to_income_pct") Double annualRentToIncomePct,
            List<String> sources,
            @JsonProperty("missing_reason") String missingReason) {
    }
}
