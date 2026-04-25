@param String title
@param String meta = ""
@param gg.jte.Content content

<section class="apply-panel">
    <div class="apply-panel-head">
        <h2>${title}</h2>
        @if(meta != null && !meta.isBlank())
            <span>${meta}</span>
        @endif
    </div>
    ${content}
</section>
