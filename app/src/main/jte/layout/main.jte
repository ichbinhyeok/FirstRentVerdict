@param String title = "First Rent Verdict"
@param String description = "First Rent Verdict - Know your true move-in cost before signing the lease."
@param String baseUrl = ""
@param String canonical = null
@param boolean noindex = false
@param gg.jte.Content content

!{
    String resolvedBaseUrl = baseUrl;
    if ((resolvedBaseUrl == null || resolvedBaseUrl.isBlank()) && canonical != null && canonical.startsWith("http")) {
        int schemeEnd = canonical.indexOf("://");
        int slashAfterHost = canonical.indexOf("/", schemeEnd + 3);
        resolvedBaseUrl = slashAfterHost > 0 ? canonical.substring(0, slashAfterHost) : canonical;
    }
    if (resolvedBaseUrl == null) {
        resolvedBaseUrl = "";
    }

    String resolvedCanonical = canonical != null && !canonical.isBlank()
            ? canonical
            : (resolvedBaseUrl.isBlank() ? "/" : resolvedBaseUrl + "/");
    String socialImageUrl = (resolvedBaseUrl.isBlank() ? "" : resolvedBaseUrl) + "/images/og-rent-verdict-v1.png";
}

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <meta name="description" content="${description}">
    <link rel="canonical" href="${resolvedCanonical}">
    
    @if(noindex)
        <meta name="robots" content="noindex, nofollow">
    @endif

    <!-- Open Graph -->
    <meta property="og:title" content="${title}">
    <meta property="og:description" content="${description}">
    <meta property="og:url" content="${resolvedCanonical}">
    <meta property="og:type" content="website">
    <meta property="og:site_name" content="First Rent Verdict">
    <meta property="og:image" content="${socialImageUrl}">
    <meta property="og:image:width" content="1200">
    <meta property="og:image:height" content="630">

    <!-- Twitter Card -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${title}">
    <meta name="twitter:description" content="${description}">
    <meta name="twitter:image" content="${socialImageUrl}">

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Favicon -->
    <link rel="icon" type="image/svg+xml" href="/images/favicon.svg">

    <!-- Styles -->
    <link rel="stylesheet" href="/css/style.css">

    <!-- GA4 -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-MK941K4W3S"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'G-MK941K4W3S');

        // Centralized analytics helper used across templates.
        window.frvTrack = function(eventName, params) {
            if (typeof gtag !== 'function') return;
            gtag('event', eventName, params || {});
        };

        document.addEventListener('click', function(e) {
            const link = e.target.closest('a');
            if (!link) return;

            const href = (link.getAttribute('href') || '').trim();
            const label = (link.dataset.trackLabel || link.textContent || href || '')
                .replace(/\s+/g, ' ')
                .trim()
                .slice(0, 120);
            const pagePath = window.location.pathname;
            const customEvent = link.dataset.trackEvent || '';
            const customCategory = link.dataset.trackCategory || 'engagement';

            if (customEvent) {
                window.frvTrack(customEvent, {
                    event_category: customCategory,
                    event_label: label,
                    link_url: href,
                    page_path: pagePath
                });
            }

            const isExternalHttp = /^https?:\/\//i.test(href);
            const isMailOrTel = /^(mailto:|tel:)/i.test(href);
            if (isExternalHttp || isMailOrTel) {
                if (customEvent !== 'outbound_click') {
                    window.frvTrack('outbound_click', {
                        event_category: 'navigation',
                        event_label: label,
                        link_url: href,
                        page_path: pagePath
                    });
                }
                return;
            }

            if (!customEvent && (link.classList.contains('btn') || (link.className || '').indexOf('btn') >= 0)) {
                window.frvTrack('cta_click', {
                    event_category: 'engagement',
                    event_label: label,
                    link_url: href,
                    page_path: pagePath
                });
            }
        }, { passive: true });

        document.addEventListener('submit', function(e) {
            const form = e.target;
            if (!form || !form.dataset) return;
            const formEvent = form.dataset.trackEvent || '';
            if (!formEvent) return;

            window.frvTrack(formEvent, {
                event_category: form.dataset.trackCategory || 'engagement',
                event_label: form.dataset.trackLabel || (form.getAttribute('action') || window.location.pathname),
                page_path: window.location.pathname
            });
        });
    </script>
</head>
<body>
    @template.layout.header()
    
    <main>
        ${content}
    </main>
    
    @template.layout.footer()
</body>
</html>

