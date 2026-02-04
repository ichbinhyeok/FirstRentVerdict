package firstrentverdict.model.dtos;

import java.util.List;

public record CityCoordinates(
        String version,
        String notes,
        List<CityCoordinate> cities) {
    public record CityCoordinate(
            String city,
            String state,
            double lat,
            double lng) {
    }
}
