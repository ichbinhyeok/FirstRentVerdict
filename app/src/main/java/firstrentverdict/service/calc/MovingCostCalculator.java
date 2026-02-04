package firstrentverdict.service.calc;

import firstrentverdict.model.dtos.MovingData;
import org.springframework.stereotype.Component;

@Component
public class MovingCostCalculator {

    // Industry standard estimates for 1-BR long distance move
    private static final int BASE_COST = 850;
    private static final double RATE_PER_MILE = 0.75;
    private static final int ONE_BR_WEIGHT_FACTOR = 600;

    /**
     * Calculates moving cost.
     * If isLocalMove is true, returns the city's typical local moving cost.
     * If false (long distance), calculates based on distance.
     */
    public int calculateCost(boolean isLocalMove, double distanceMiles, MovingData.CityMoving localData) {
        if (isLocalMove || distanceMiles < 50) {
            return localData.typical();
        }

        // Long distance formula
        return (int) (BASE_COST + (distanceMiles * RATE_PER_MILE) + ONE_BR_WEIGHT_FACTOR);
    }
}
