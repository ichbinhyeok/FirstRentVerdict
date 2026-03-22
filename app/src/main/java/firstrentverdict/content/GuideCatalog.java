package firstrentverdict.content;

import java.util.List;
import java.util.Optional;

public final class GuideCatalog {

    private GuideCatalog() {
    }

    public static List<GuideEntry> all() {
        return GUIDES;
    }

    public static Optional<GuideEntry> findBySlug(String slug) {
        if (slug == null) {
            return Optional.empty();
        }
        return GUIDES.stream().filter(g -> g.slug().equals(slug)).findFirst();
    }

    public static List<GuideEntry> recommendationsFor(String intentCategory) {
        if (intentCategory == null || intentCategory.isBlank()) {
            return GUIDES.stream().limit(3).toList();
        }
        String normalized = intentCategory.trim().toLowerCase();
        List<GuideEntry> matches = GUIDES.stream()
                .filter(g -> g.intentCategory().equals(normalized))
                .limit(3)
                .toList();
        if (!matches.isEmpty()) {
            return matches;
        }
        return GUIDES.stream().limit(3).toList();
    }

    public record GuideFaq(String question, String answer) {
    }

    public record GuideEntry(
            String slug,
            String title,
            String excerpt,
            String persona,
            String searchIntent,
            String intentCategory,
            String primaryKeyword,
            String ctaLabel,
            String ctaPath,
            List<GuideFaq> faqs,
            List<String> checklist,
            List<String> pitfalls) {
    }

    private static final List<GuideEntry> GUIDES = List.of(
            new GuideEntry(
                    "how-much-cash-do-i-need-to-move-into-an-apartment",
                    "How Much Cash Do I Need to Move Into an Apartment?",
                    "A renter-first breakdown of first month, deposit, utility setup, moving, and the buffer you need before you sign.",
                    "Cash-tight renter trying to avoid a failed move-in",
                    "Estimate the real cash threshold before applying",
                    "cost",
                    "how much cash do i need to move into an apartment",
                    "Run My Move-In Audit",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "How much cash do I need before moving into an apartment?",
                                    "Most renters need more than first month and deposit. Plan for utility setup, moving logistics, and a first-60-day cash buffer."),
                            new GuideFaq(
                                    "What costs are easiest to underestimate?",
                                    "Utility deposits, moving truck help, pet fees, broker fees, and temporary housing are the most common misses."),
                            new GuideFaq(
                                    "Should I apply if I only have deposit money?",
                                    "Usually no. If move-in wipes out your remaining cash, one small surprise can push you into missed-rent risk.")),
                    List.of(
                            "Add first month, deposit, and moving cost before you apply anywhere.",
                            "Keep a post-move buffer for at least 30 to 60 days of volatility.",
                            "Use city-level median rent to pressure-test your target unit."),
                    List.of(
                            "Treating rent and deposit as the full move-in budget.",
                            "Ignoring utility setup, pet fees, and broker fees.",
                            "Spending emergency cash on move-in day and starting at zero.")),
            new GuideEntry(
                    "apartment-guarantor-services-vs-larger-security-deposit",
                    "Apartment Guarantor Service vs Larger Security Deposit",
                    "Decide when a guarantor fee improves approval odds and when a higher deposit or prepaid rent is the cheaper move.",
                    "Applicant comparing fee-based guarantor help with cash alternatives",
                    "Choose the cheapest approval path that actually changes the outcome",
                    "approval",
                    "apartment guarantor service",
                    "Compare My Approval Options",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "Is a guarantor service better than paying a bigger deposit?",
                                    "Only if the building accepts both paths and the guarantor meaningfully raises approval probability."),
                            new GuideFaq(
                                    "Can prepaid rent replace a guarantor?",
                                    "Sometimes, but not every landlord accepts prepaid rent as a substitute for weak screening factors."),
                            new GuideFaq(
                                    "What should I verify before paying a guarantor fee?",
                                    "Confirm the property accepts that provider, the fee structure, and whether the coverage is valid for renewals.")),
                    List.of(
                            "Ask the property which approval alternatives they explicitly accept.",
                            "Compare guarantor cost with higher deposit or prepaid rent scenarios.",
                            "Use the option that changes approval odds at the lowest total cost."),
                    List.of(
                            "Paying a guarantor provider before confirming property acceptance.",
                            "Comparing monthly rent only and ignoring one-time fees.",
                            "Assuming guarantor approval guarantees lease approval.")),
            new GuideEntry(
                    "pet-deposit-and-pet-rent-negotiation-guide",
                    "Pet Deposit and Pet Rent: What Renters Can Actually Negotiate",
                    "A practical guide to pet deposits, monthly pet rent, and the documents that help reduce pet-related move-in charges.",
                    "Pet owner trying to reduce move-in friction",
                    "Lower pet-related move-in charges without getting blindsided",
                    "cost",
                    "pet deposit and pet rent",
                    "Check My Pet Move-In Cost",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "Can renters negotiate a pet deposit?",
                                    "Sometimes. Documentation like vaccination records, renter references, and pet insurance can improve your position."),
                            new GuideFaq(
                                    "What is the difference between pet deposit and pet rent?",
                                    "Pet deposit is usually a one-time upfront charge, while pet rent is a recurring monthly fee."),
                            new GuideFaq(
                                    "Should I prioritize lower pet rent or lower pet deposit?",
                                    "If cash is tight, a lower upfront deposit usually matters more than a small monthly pet-rent difference.")),
                    List.of(
                            "Ask for both the upfront pet charge and the recurring pet-rent amount in writing.",
                            "Bring vet records and prior landlord references before negotiating.",
                            "Optimize for total move-in cash if liquidity is your main bottleneck."),
                    List.of(
                            "Only comparing monthly pet rent and ignoring the upfront fee.",
                            "Assuming every building treats pet fees the same way.",
                            "Applying before understanding breed, weight, or quantity restrictions.")),
            new GuideEntry(
                    "rent-with-bad-credit-no-cosigner",
                    "Renting with Bad Credit and No Cosigner (2026 Playbook)",
                    "A practical approval path for renters with low credit and no family backup. Focus on documents, target inventory, and realistic cash thresholds.",
                    "Credit-challenged renter without support network",
                    "Get approved this month despite low score",
                    "approval",
                    "rent with bad credit no cosigner",
                    "Run My Approval Audit",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "Can I rent with bad credit and no cosigner?",
                                    "Yes. Approval is harder, but targeting flexible landlords and showing stronger liquidity can improve acceptance odds."),
                            new GuideFaq(
                                    "What documents matter most when credit is weak?",
                                    "Landlords prioritize stable income proof, recent bank statements, and references that show consistent payment behavior."),
                            new GuideFaq(
                                    "Should I apply everywhere to increase odds?",
                                    "No. Broad low-fit applications burn fees and create churn. Start with inventory that explicitly reviews non-standard profiles.")),
                    List.of(
                            "Prepare proof of income plus 3 months bank statements.",
                            "Target private landlords and older managed stock first.",
                            "Offer larger upfront liquidity only when lease terms are clear."),
                    List.of(
                            "Applying to premium buildings first and burning application fees.",
                            "Paying non-refundable screening fees without clear criteria.",
                            "Signing without confirming deposit refund terms.")),
            new GuideEntry(
                    "apartment-guarantor-cost-breakdown",
                    "Apartment Guarantor Cost Breakdown: When It Is Worth Paying",
                    "Understand guarantor fee ranges, when they reduce rejection risk, and when a larger cash buffer is cheaper than guarantor fees.",
                    "Renter comparing guarantor fee vs cash strategy",
                    "Compare guarantor option against rejection risk",
                    "cost",
                    "lease guarantor cost",
                    "Estimate My Cash Gap",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "How much does a lease guarantor usually cost?",
                                    "Cost varies by market and provider, often calculated as a percentage of annual rent or a fixed coverage fee."),
                            new GuideFaq(
                                    "When is a guarantor worth paying for?",
                                    "It is worth it when it materially changes approval probability and costs less than repeated denial cycles."),
                            new GuideFaq(
                                    "Can extra cash replace a guarantor?",
                                    "Sometimes. If a property accepts higher upfront liquidity, a cash strategy can be cheaper than guarantor fees.")),
                    List.of(
                            "Calculate total guarantor cost against one-year lease value.",
                            "Compare guarantor fee to expected move-in shortfall.",
                            "Use guarantor only if approval probability shifts materially."),
                    List.of(
                            "Paying guarantor fees before verifying building acceptance.",
                            "Ignoring renewals and fee recurrence conditions.",
                            "Assuming guarantor approval equals lease approval.")),
            new GuideEntry(
                    "second-chance-apartments-qualification",
                    "Second-Chance Apartments: Qualification Checklist by City",
                    "How to qualify for second-chance inventory using a clean narrative, documentation sequence, and realistic rent-to-income targets.",
                    "Applicant with eviction or disrupted credit history",
                    "Find inventory that actually reviews exceptions",
                    "approval",
                    "second chance apartments",
                    "Find Cities With Better Approval Odds",
                    "/RentVerdict/cities",
                    List.of(
                            new GuideFaq(
                                    "What are second-chance apartments?",
                                    "They are rentals that may consider applicants with prior credit or housing disruptions when supporting evidence is strong."),
                            new GuideFaq(
                                    "How do I improve approval odds on second-chance listings?",
                                    "Lead with recovery evidence, references, and a clear timeline that shows stability after prior issues."),
                            new GuideFaq(
                                    "Do second-chance apartments cost more?",
                                    "Some require higher deposits or stricter terms, so compare total move-in cost before applying.")),
                    List.of(
                            "Prepare a one-page explanation with dates and resolution status.",
                            "Collect landlord references and current payment history.",
                            "Filter listings by screening flexibility before applying."),
                    List.of(
                            "Applying broadly without screening policy checks.",
                            "Leading with negative history before showing recovery evidence.",
                            "Ignoring location mismatch between income and rent.")),
            new GuideEntry(
                    "no-credit-check-apartments",
                    "How to Find No Credit Check Apartments in 2026",
                    "A step-by-step strategy for bypassing traditional credit checks, including private landlords, guarantor options, and proof of liquidity.",
                    "Thin-file renter or newcomer with no credit history",
                    "Bypass hard-credit gating and reduce rejection loops",
                    "approval",
                    "no credit check apartments",
                    "Audit My Move-In Budget",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "Are no-credit-check apartments legitimate?",
                                    "Some are legitimate, but scams are common. Verify ownership, lease terms, and payment channels before sending money."),
                            new GuideFaq(
                                    "What replaces a credit check in these rentals?",
                                    "Owners usually review income proof, bank balances, references, and overall application completeness."),
                            new GuideFaq(
                                    "Can no-credit-check units still deny applications?",
                                    "Yes. No credit check does not mean guaranteed approval if income, documents, or rental history are weak.")),
                    List.of(
                            "Start with owner-managed units and direct outreach.",
                            "Prepare liquidity proof and employment verification packet.",
                            "Use smaller target radius to speed response cycles."),
                    List.of(
                            "Confusing no-credit-check ads with scam listings.",
                            "Submitting incomplete documentation.",
                            "Overpaying for rushed, low-quality units.")),
            new GuideEntry(
                    "how-to-rent-with-eviction",
                    "Renting with an Eviction Record: Survival Guide",
                    "Evictions can remain visible for years. Learn how to present context, raise credibility, and choose inventory where exceptions are reviewed.",
                    "Renter rebuilding after eviction",
                    "Re-enter rental market without repeated denials",
                    "recovery",
                    "rent with eviction record",
                    "Check My City-Level Risk",
                    "/RentVerdict/cities",
                    List.of(
                            new GuideFaq(
                                    "Can I rent after an eviction record?",
                                    "Yes. It is difficult but possible with transparent disclosure, stable income, and targeted inventory."),
                            new GuideFaq(
                                    "What should I say about a past eviction?",
                                    "Use a short factual timeline, include resolution status, and show what has changed in your finances."),
                            new GuideFaq(
                                    "Which landlords are more flexible after eviction?",
                                    "Smaller operators and exception-friendly properties are often more flexible than rigid algorithm-first screening flows.")),
                    List.of(
                            "Document income stability and payment discipline.",
                            "Prioritize local markets with broader mid-tier inventory.",
                            "Lead with corrected facts and resolution proof."),
                    List.of(
                            "Applying to strict algorithm-first inventory first.",
                            "Hiding record details until late-stage checks.",
                            "Accepting unfavorable lease terms under pressure.")),
            new GuideEntry(
                    "first-time-renter-budget",
                    "First-Time Renter Budget Checklist (Hidden Costs)",
                    "Moving costs are not just first month and deposit. Use this checklist for utility setup, applications, and first-60-day cash safety.",
                    "First-time renter with limited cash planning experience",
                    "Avoid underestimating move-in liquidity",
                    "cost",
                    "first apartment move in cost checklist",
                    "Test My First-Move Budget",
                    "/RentVerdict/",
                    List.of(
                            new GuideFaq(
                                    "How much cash should a first-time renter have before moving?",
                                    "Plan for first month, deposit, moving logistics, setup fees, and an emergency buffer for early-month volatility."),
                            new GuideFaq(
                                    "What hidden costs do first-time renters miss most?",
                                    "Application fees, utility setup, transport, temporary housing, and basic furnishing costs are commonly missed."),
                            new GuideFaq(
                                    "Is rent alone enough to measure affordability?",
                                    "No. True affordability includes full move-in liquidity and ongoing monthly resilience, not just rent amount.")),
                    List.of(
                            "Include utility deposits and setup fees in day-one budget.",
                            "Reserve buffer for first 60 days after move-in.",
                            "Benchmark rent target against verified city median."),
                    List.of(
                            "Using only monthly rent to define affordability.",
                            "Ignoring moving logistics and temporary housing.",
                            "Using emergency savings as deposit-only fund.")),
            new GuideEntry(
                    "nyc-broker-fee-change-renter-checklist",
                    "NYC Broker Fee Rule Change: Renter Checklist",
                    "Action checklist for renters navigating recent broker-fee policy changes in NYC and avoiding fee confusion during lease negotiations.",
                    "NYC renter comparing listings under changing fee rules",
                    "Protect budget under rule-change uncertainty",
                    "policy",
                    "nyc broker fee law",
                    "Run NYC Move-In Cost Audit",
                    "/RentVerdict/verdict/new-york-ny",
                    List.of(
                            new GuideFaq(
                                    "Who should pay a broker fee in NYC listings?",
                                    "Responsibility depends on who engaged the broker and listing terms, so require written disclosure before applying."),
                            new GuideFaq(
                                    "How can renters avoid broker-fee confusion?",
                                    "Ask for an itemized fee schedule early and keep screenshots of listing terms and communication."),
                            new GuideFaq(
                                    "Should I pay broker fees before final lease review?",
                                    "Avoid upfront payment until legal responsibility and signed lease terms are fully clear.")),
                    List.of(
                            "Ask who hired the broker and who is fee-responsible in writing.",
                            "Request a full fee schedule before applying.",
                            "Track listing screenshots and communication logs."),
                    List.of(
                            "Paying fees before legal responsibility is clear.",
                            "Treating all listings as identical under policy transition.",
                            "Ignoring signed disclosure language.")));
}
