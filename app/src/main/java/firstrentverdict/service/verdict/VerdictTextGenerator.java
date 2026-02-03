package firstrentverdict.service.verdict;

import org.springframework.stereotype.Component;
import firstrentverdict.model.verdict.Verdict;

/**
 * Generates contextual, narrative-driven "Why This Verdict" text.
 * 
 * DESIGN PRINCIPLES:
 * - Maximum 250 characters per explanation
 * - Judge-like authoritative tone (explains decision, not advice)
 * - City-specific context included
 * - Relatable scenarios (car repair, medical bills) for emotional resonance
 * - NO raw numbers - narrative only
 * 
 * ENHANCED: Uses city name, state, market tier, and deposit context
 * for personalized, persuasive explanations users can trust.
 */
@Component
public class VerdictTextGenerator {

    private static final int MAX_LENGTH = 250;

    /**
     * Generates verdict explanation text.
     * 
     * @throws IllegalStateException if generated text exceeds 250 chars
     */
    public String generate(VerdictContext ctx) {
        String text = switch (ctx.verdict()) {
            case APPROVED -> generateApprovedText(ctx);
            case BORDERLINE -> generateBorderlineText(ctx);
            case DENIED -> generateDeniedText(ctx);
        };

        if (text.length() > MAX_LENGTH) {
            throw new IllegalStateException(
                    String.format("Generated text exceeds %d chars: %d", MAX_LENGTH, text.length()));
        }

        return text;
    }

    private String generateApprovedText(VerdictContext ctx) {
        String marketDesc = getMarketDescription(ctx);

        if (ctx.hasPet()) {
            return String.format(
                    "You can cover all move-in costs in %s and still maintain a healthy emergency cushion—even with pet fees factored in. %s",
                    ctx.cityName(), marketDesc);
        }

        return String.format(
                "You have a solid financial foundation for this move. After paying upfront costs in %s, you'll retain a safety net for life's surprises. %s",
                ctx.cityName(), marketDesc);
    }

    private String generateBorderlineText(VerdictContext ctx) {
        String tierWarning = switch (ctx.marketTier()) {
            case HIGH_COST -> "In this competitive market, razor-thin margins leave you exposed.";
            case MODERATE -> "A single unexpected expense could tip you into financial stress.";
            case AFFORDABLE -> "Even in affordable markets, starting with minimal reserves is risky.";
        };

        return String.format(
                "You can cover the upfront costs, but you'd be walking a financial tightrope. %s",
                tierWarning);
    }

    private String generateDeniedText(VerdictContext ctx) {
        return switch (ctx.bottleneck()) {
            case IMMEDIATE_INSOLVENCY -> generateInsolvencyText(ctx);
            case CRITICAL_LIQUIDITY_RISK -> generateCriticalRiskText(ctx);
            case THIN_BUFFER_WARNING -> generateThinBufferText(ctx);
            default -> "Financial assessment indicates this move carries unacceptable risk at current funding levels.";
        };
    }

    private String generateInsolvencyText(VerdictContext ctx) {
        // Emphasize the concrete problem without showing exact numbers
        String depositContext = "";
        if (ctx.depositLegalNote() != null && !ctx.depositLegalNote().isEmpty()) {
            depositContext = " Note: " + truncateNote(ctx.depositLegalNote());
        }

        return String.format(
                "The combined weight of first month rent, security deposit, and moving costs in %s exceeds your available funds. This move cannot proceed without additional cash.%s",
                ctx.cityName(), depositContext.length() < 50 ? depositContext : "");
    }

    private String generateCriticalRiskText(VerdictContext ctx) {
        String scenario = ctx.hasPet()
                ? "One vet bill or car repair"
                : "One car repair or medical bill";

        return String.format(
                "You could hand over the move-in money, but you'd be left dangerously exposed. %s could trigger a financial spiral in %s's rental market.",
                scenario, ctx.cityName());
    }

    private String generateThinBufferText(VerdictContext ctx) {
        return String.format(
                "Your post-move reserves fall below the recommended safety threshold for %s. This creates vulnerability to any unexpected expense.",
                ctx.cityName());
    }

    /**
     * Returns a brief market description for approved verdicts
     */
    private String getMarketDescription(VerdictContext ctx) {
        if (ctx.isHighRiskDeposit()) {
            return "Landlords may still require credit verification.";
        }

        return switch (ctx.marketTier()) {
            case HIGH_COST -> "Well-prepared applicants succeed here.";
            case MODERATE -> "You're positioned competitively.";
            case AFFORDABLE -> "Strong financial footing for this market.";
        };
    }

    /**
     * Truncates long legal notes for inline use
     */
    private String truncateNote(String note) {
        if (note == null)
            return "";
        if (note.length() <= 60)
            return note;
        return note.substring(0, 57) + "...";
    }
}
