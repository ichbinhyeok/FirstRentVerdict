@param String label
@param String value
@param String note = ""
@param String tone = "neutral"

<div class="apply-metric apply-metric-${tone}">
    <span>${label}</span>
    <strong>${value}</strong>
    @if(note != null && !note.isBlank())
        <small>${note}</small>
    @endif
</div>
