@import firstrentverdict.model.apply.ApplicationRiskFactor

@param ApplicationRiskFactor factor

<div class="apply-risk-row apply-risk-${factor.severity()}">
    <span>${factor.severity()}</span>
    <div>
        <strong>${factor.label()}</strong>
        <p>${factor.detail()}</p>
    </div>
</div>
