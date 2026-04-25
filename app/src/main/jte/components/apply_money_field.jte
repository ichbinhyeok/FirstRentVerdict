@param String id
@param String name
@param String label
@param String value = ""
@param String placeholder = ""
@param boolean required = false

!{
    String displayPlaceholder = placeholder != null && placeholder.startsWith("$") ? placeholder.substring(1) : placeholder;
}

<div class="apply-field apply-money-field">
    <label for="${id}">${label}</label>
    <div class="apply-input-shell">
        <span>$</span>
        <input id="${id}" name="${name}" class="apply-input js-money" inputmode="numeric" value="${value}" placeholder="${displayPlaceholder}" required="${required}">
    </div>
</div>
