@import firstrentverdict.model.apply.ApplicationCostLine

@param ApplicationCostLine line

<div class="apply-cost-row">
    <div>
        <strong>${line.label()}</strong>
        <p>${line.note()}</p>
    </div>
    <div class="apply-cost-value">
        <span>$${String.format("%,d", line.amount())}</span>
        @if(line.beforeApprovalRisk())
            <small>before approval</small>
        @endif
    </div>
</div>
