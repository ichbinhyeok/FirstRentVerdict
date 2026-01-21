package firstrentverdict.service.verdict;

import org.springframework.stereotype.Component;
import firstrentverdict.model.verdict.Verdict;

/**
 * Generates contextual "Why This Verdict" text based on verdict context.
 * 
 * STRICT RULES:
 * - Maximum 250 characters
 * - Judge-like tone (explanatory, not advisory)
 * - No recommendations ("You should...")
 * - Template-based for consistency
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
        return "You have sufficient buffer to cover upfront costs and maintain financial safety. " +
                "This move is financially viable based on current data.";
    }

    private String generateBorderlineText(VerdictContext ctx) {
        return "Your upfront costs will consume most available cash, leaving minimal safety margin. " +
                "This creates vulnerability to unexpected expenses.";
    }

    private String generateDeniedText(VerdictContext ctx) {
        return switch (ctx.bottleneck()) {
            case IMMEDIATE_INSOLVENCY -> String.format(
                    "Your upfront requirement exceeds available funds by $%,d. " +
                            "This creates immediate insolvency with no recovery path.",
                    Math.abs(ctx.remainingCash()));
            case CRITICAL_LIQUIDITY_RISK -> String.format(
                    "While you can cover upfront costs, only $%,d remains—far below the $%,d safety threshold. " +
                            "One emergency could trigger financial crisis.",
                    ctx.remainingCash(), ctx.recommendedBuffer());
            case THIN_BUFFER_WARNING -> String.format(
                    "Your upfront costs will consume most available cash, leaving minimal safety margin. " +
                            "This creates vulnerability to unexpected expenses.");
            default -> "Financial assessment indicates unacceptable risk level for this move.";
        };
    }
}
