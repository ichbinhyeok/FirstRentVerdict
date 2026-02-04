package firstrentverdict.repository;

import firstrentverdict.model.dtos.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class VerdictDataRepository {

    // Maps for O(1) lookup: Key format "City|State" (e.g. "Austin|TX")
    // Using simple concatenation for key as it is strictly controlled internal
    // data.
    private final Map<String, RentData.CityRent> rentMap = new ConcurrentHashMap<>();
    private final Map<String, SecurityDepositData> securityDepositMap = new ConcurrentHashMap<>();
    private final Map<String, CashBufferData.CityBuffer> cashBufferMap = new ConcurrentHashMap<>();
    private final Map<String, MovingData.CityMoving> movingMap = new ConcurrentHashMap<>();
    private final Map<String, PetData.CityPet> petMap = new ConcurrentHashMap<>();
    private final Map<String, StateLawData.StateLaw> stateLaws = new ConcurrentHashMap<>();
    private final Map<String, CityCoordinates.CityCoordinate> cityCoordinates = new ConcurrentHashMap<>();
    private final Map<String, CityInsightData.CityInsight> cityInsights = new ConcurrentHashMap<>();

    // Valid cities set
    private final Map<String, CitiesData.CityEntry> validCities = new ConcurrentHashMap<>();

    private String generateKey(String city, String state) {
        if (city == null || state == null)
            return "";
        return city.trim().toLowerCase() + "|" + state.trim().toLowerCase();
    }

    public void addRent(RentData.CityRent data) {
        rentMap.put(generateKey(data.city(), data.state()), data);
    }

    public void addSecurityDeposit(SecurityDepositData data) {
        securityDepositMap.put(generateKey(data.city(), data.state()), data);
    }

    public void addCashBuffer(CashBufferData.CityBuffer data) {
        cashBufferMap.put(generateKey(data.city(), data.state()), data);
    }

    public void addMoving(MovingData.CityMoving data) {
        movingMap.put(generateKey(data.city(), data.state()), data);
    }

    public void addPet(PetData.CityPet data) {
        petMap.put(generateKey(data.city(), data.state()), data);
    }

    public void addCity(CitiesData.CityEntry city) {
        validCities.put(generateKey(city.city(), city.state()), city);
    }

    public void addStateLaw(StateLawData.StateLaw law) {
        stateLaws.put(law.state().toUpperCase(), law);
    }

    public void addCityCoordinate(CityCoordinates.CityCoordinate coordinate) {
        cityCoordinates.put(generateKey(coordinate.city(), coordinate.state()), coordinate);
    }

    public void addCityInsight(CityInsightData.CityInsight insight) {
        cityInsights.put(generateKey(insight.city(), insight.state()), insight);
    }

    // Accessors
    public Optional<RentData.CityRent> getRent(String city, String state) {
        return Optional.ofNullable(rentMap.get(generateKey(city, state)));
    }

    public Optional<SecurityDepositData> getSecurityDeposit(String city, String state) {
        return Optional.ofNullable(securityDepositMap.get(generateKey(city, state)));
    }

    public Optional<CashBufferData.CityBuffer> getCashBuffer(String city, String state) {
        return Optional.ofNullable(cashBufferMap.get(generateKey(city, state)));
    }

    public Optional<MovingData.CityMoving> getMoving(String city, String state) {
        return Optional.ofNullable(movingMap.get(generateKey(city, state)));
    }

    public Optional<PetData.CityPet> getPet(String city, String state) {
        return Optional.ofNullable(petMap.get(generateKey(city, state)));
    }

    public boolean isValidCity(String city, String state) {
        return validCities.containsKey(generateKey(city, state));
    }

    public java.util.List<CitiesData.CityEntry> getAllCities() {
        return new java.util.ArrayList<>(validCities.values());
    }

    public Optional<StateLawData.StateLaw> getStateLaw(String state) {
        return Optional.ofNullable(stateLaws.get(state.toUpperCase()));
    }

    public Optional<CityCoordinates.CityCoordinate> getCityCoordinate(String city, String state) {
        return Optional.ofNullable(cityCoordinates.get(generateKey(city, state)));
    }

    public Optional<CityInsightData.CityInsight> getCityInsight(String city, String state) {
        return Optional.ofNullable(cityInsights.get(generateKey(city, state)));
    }
}
