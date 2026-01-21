@import java.util.List
@import firstrentverdict.model.dtos.CitiesData.CityEntry

@param List<CityEntry> cities

@template.layout.main(content = @`
    <div class="glass-card">
        <header style="margin-bottom: 2rem;">
            <h1>FIRST APARTMENT</h1>
            <h2 class="subtitle">TRUE MOVE-IN COST VERDICT</h2>
            <p style="text-align: center; color: var(--text-muted); font-size: 0.9rem;">
                Don't let hidden move-in costs blindside you.
                <br>Run the engine to see if you're financially ready.
            </p>
        </header>

        <form action="/RentVerdict/verdict" method="post">
            <div class="form-group">
                <label for="cityState">Target City</label>
                <select id="cityState" name="cityState" required>
                    <option value="" disabled selected>Select a supported city...</option>
                    @for(CityEntry city : cities)
                        <option value="${city.city()}|${city.state()}">${city.city()}, ${city.state()}</option>
                    @endfor
                </select>
            </div>

            <div class="form-group">
                <label for="monthlyRent">Monthly Rent ($)</label>
                <input type="number" id="monthlyRent" name="monthlyRent" placeholder="e.g. 2500" min="100" required>
            </div>

            <div class="form-group">
                <label for="availableCash">Total Cash Available ($)</label>
                <input type="number" id="availableCash" name="availableCash" placeholder="Cash on hand before spending anything" min="0" required>
            </div>

            <div class="toggle-group">
                <label class="toggle-label">
                    <input type="checkbox" name="hasPet" value="true">
                    Bringing a Pet?
                </label>
                <label class="toggle-label">
                    <input type="checkbox" name="isLocalMove" value="true" checked>
                    Local Move?
                </label>
            </div>

            <button type="submit" class="btn-primary">GET VERDICT</button>
        </form>
    </div>
`)
