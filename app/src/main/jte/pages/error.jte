@param int status = 500
@param String message = "Something went wrong on our end."

@template.layout.main(
    title = "Error " + status + " - First Rent Verdict",
    noindex = true,
    content = @`
    <div style="max-width: 600px; margin: 100px auto; padding: var(--space-8); text-align: center;" class="animate-in">
        <div style="font-size: 5rem; font-weight: 800; color: var(--accent-primary); line-height: 1; margin-bottom: var(--space-4);">
            ${status}
        </div>
        <h1 class="hero-title" style="font-size: 2rem; margin-bottom: var(--space-4);">
            @if(status == 404)
                Page Not Found
            @else
                System Error
            @endif
        </h1>
        <p class="hero-subtitle" style="margin-bottom: var(--space-8);">
            ${message}
        </p>
        
        <div style="display: flex; gap: var(--space-4); justify-content: center;">
            <a href="/RentVerdict/" class="btn btn-primary">
                Return Home
            </a>
            <a href="mailto:support@movecostinfo.com" class="btn btn-secondary">
                Report Issue
            </a>
        </div>

        <div style="margin-top: var(--space-12); padding-top: var(--space-8); border-top: 1px solid var(--border-default); color: var(--text-tertiary); font-size: 0.875rem;">
            Error Reference ID: ${java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase()}
        </div>
    </div>
`)
