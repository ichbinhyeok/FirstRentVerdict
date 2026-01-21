@import java.util.List
@import firstrentverdict.model.dtos.CitiesData.CityEntry

@param List<CityEntry> cities

@template.layout.main(content = @`
    <div class="split-layout">
        <!-- LEFT: Identity & Authority -->
        <div class="split-left animate-entry">
            <div style="max-width: 440px;">
                <h1 class="hero-title">First Apartment<br>True Cost Verdict</h1>
                <p class="hero-subtitle">
                    This is not a marketing suggestion.<br>
                    It is a mathematical certainty based on 2026 economic data.
                </p>
                <div style="margin-top: 4rem; padding-top: 2rem; border-top: 1px solid var(--border-subtle);">
                    <p style="font-size: 0.8rem; color: var(--text-tertiary);">
                        CONFIDENTIAL RISK ASSESSMENT<br>
                        DO NOT DISTRIBUTE OUTSIDE DECISION CIRCLE
                    </p>
                </div>
            </div>
        </div>

        <!-- RIGHT: Evaluation Form -->
        <div class="split-right">
            <div class="form-container animate-entry" style="animation-delay: 0.1s;">
                <form action="/RentVerdict/verdict" method="post">
                    
                    <div class="form-group">
                        <label for="cityState">Jurisdiction (Target City)</label>
                        <select id="cityState" name="cityState" required>
                            <option value="" disabled selected>Select Jurisdiction...</option>
                            @for(CityEntry city : cities)
                                <option value="${city.city()}|${city.state()}">${city.city()}, ${city.state()}</option>
                            @endfor
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="monthlyRent">Base Monthly Liability (Rent)</label>
                        <input type="number" id="monthlyRent" name="monthlyRent" placeholder="0.00" min="100" required>
                    </div>

                    <div class="form-group">
                        <label for="availableCash">Liquid Assets (Cash on Hand)</label>
                        <input type="number" id="availableCash" name="availableCash" placeholder="0.00" min="0" required>
                    </div>

                    <div class="checkbox-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="hasPet" value="true">
                            <span>Pet</span>
                        </label>
                        <label class="checkbox-label">
                            <input type="checkbox" name="isLocalMove" value="true" checked>
                            <span>Local Move</span>
                        </label>
                    </div>

                    <button type="submit" class="btn-primary">Execute Verdict Analysis</button>
                    
                    <p style="text-align: center; margin-top: 2rem; font-size: 0.75rem; color: var(--text-tertiary);">
                        Simulation ID: ${java.util.UUID.randomUUID().toString().substring(0,8).toUpperCase()}
                    </p>
                </form>
            </div>
        </div>
    </div>
`)
