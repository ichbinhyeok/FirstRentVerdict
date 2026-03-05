package firstrentverdict.controller;

import firstrentverdict.model.dtos.CitiesData;
import firstrentverdict.repository.VerdictDataRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/RentVerdict/research")
public class ResearchController {

    private final VerdictDataRepository repository;

    public ResearchController(VerdictDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping({ "/move-in-cost-index", "/move-in-cost-index/" })
    public String moveInCostIndex(Model model) {
        List<CostIndexRow> rows = repository.getAllCities().stream()
                .map(this::toRow)
                .filter(Objects::nonNull)
                .toList();

        List<CostIndexRow> expensiveRows = rows.stream()
                .sorted(Comparator.comparingInt(CostIndexRow::upfront).reversed())
                .limit(20)
                .toList();

        List<CostIndexRow> affordableRows = rows.stream()
                .sorted(Comparator.comparingInt(CostIndexRow::upfront))
                .limit(20)
                .toList();

        List<CostIndexRow> pressureRows = rows.stream()
                .filter(r -> r.rentToIncomePct() != null)
                .sorted(Comparator.comparingDouble((CostIndexRow r) -> r.rentToIncomePct()).reversed())
                .limit(15)
                .toList();

        model.addAttribute("reportMonth", LocalDate.now().withDayOfMonth(1));
        model.addAttribute("sampleSize", rows.size());
        model.addAttribute("expensiveRows", expensiveRows);
        model.addAttribute("affordableRows", affordableRows);
        model.addAttribute("pressureRows", pressureRows);
        return "pages/research_move_in_index";
    }

    private CostIndexRow toRow(CitiesData.CityEntry cityEntry) {
        var rentOpt = repository.getRent(cityEntry.city(), cityEntry.state());
        var depositOpt = repository.getSecurityDeposit(cityEntry.city(), cityEntry.state());
        var movingOpt = repository.getMoving(cityEntry.city(), cityEntry.state());
        if (rentOpt.isEmpty() || depositOpt.isEmpty() || movingOpt.isEmpty()) {
            return null;
        }

        int rent = rentOpt.get().median();
        double multiplier = 1.0;
        if (depositOpt.get().city_practice() != null
                && depositOpt.get().city_practice().typicalMultipliers() != null
                && !depositOpt.get().city_practice().typicalMultipliers().isEmpty()) {
            multiplier = depositOpt.get().city_practice().typicalMultipliers().get(0);
        }
        int deposit = (int) Math.round(rent * multiplier);
        int moving = movingOpt.get().typical();
        int upfront = rent + deposit + moving;

        Double rentToIncomePct = repository.getCityEconomicFact(cityEntry.city(), cityEntry.state())
                .map(f -> f.annualRentToIncomePct())
                .orElse(null);

        return new CostIndexRow(
                cityEntry.city(),
                cityEntry.state(),
                cityEntry.city().toLowerCase().replace(" ", "-").replace(".", "") + "-" + cityEntry.state().toLowerCase(),
                rent,
                deposit,
                moving,
                upfront,
                rentToIncomePct,
                pressureLabel(rentToIncomePct));
    }

    private String pressureLabel(Double rentToIncomePct) {
        if (rentToIncomePct == null) {
            return "Unknown";
        }
        if (rentToIncomePct >= 45.0) {
            return "High";
        }
        if (rentToIncomePct >= 35.0) {
            return "Elevated";
        }
        return "Moderate";
    }

    public record CostIndexRow(
            String city,
            String state,
            String slug,
            int rent,
            int deposit,
            int moving,
            int upfront,
            Double rentToIncomePct,
            String pressureLabel) {
    }
}
