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
    <script>
        (function() {
            const measurementId = 'G-MK941K4W3S';
            const storageKey = 'frv_analytics_disabled';
            const params = new URLSearchParams(window.location.search);

            try {
                if (params.get('frv_analytics') === 'off') {
                    window.localStorage.setItem(storageKey, '1');
                } else if (params.get('frv_analytics') === 'on') {
                    window.localStorage.removeItem(storageKey);
                }
            } catch (err) {
                // Ignore storage access errors in restricted browser contexts.
            }

            const hostname = (window.location.hostname || '').toLowerCase();
            const isLocalHost = hostname === 'localhost' || hostname === '127.0.0.1' || hostname === '0.0.0.0';
            const isAutomation = navigator.webdriver === true;
            const hasTestFlag = params.get('frv_test') === '1';
            let userOptOut = false;

            try {
                userOptOut = window.localStorage.getItem(storageKey) === '1';
            } catch (err) {
                userOptOut = false;
            }

            const disableAnalytics = isLocalHost || isAutomation || hasTestFlag || userOptOut;
            window.__FRV_ANALYTICS_DISABLED__ = disableAnalytics;

            window.frvDisableAnalytics = function() {
                try {
                    window.localStorage.setItem(storageKey, '1');
                } catch (err) {
                    // no-op
                }
                window['ga-disable-' + measurementId] = true;
                window.__FRV_ANALYTICS_DISABLED__ = true;
            };

            window.frvEnableAnalytics = function() {
                try {
                    window.localStorage.removeItem(storageKey);
                } catch (err) {
                    // no-op
                }
                window['ga-disable-' + measurementId] = false;
                window.__FRV_ANALYTICS_DISABLED__ = false;
            };

            if (disableAnalytics) {
                window['ga-disable-' + measurementId] = true;
                window.frvTrack = function() {};
                return;
            }

            window.dataLayer = window.dataLayer || [];
            window.gtag = function(){ window.dataLayer.push(arguments); };
            window.gtag('js', new Date());
            window.gtag('config', measurementId);

            const gaScript = document.createElement('script');
            gaScript.async = true;
            gaScript.src = 'https://www.googletagmanager.com/gtag/js?id=' + encodeURIComponent(measurementId);
            document.head.appendChild(gaScript);

            // Centralized analytics helper used across templates.
            window.frvTrack = function(eventName, params) {
                if (typeof window.gtag !== 'function') return;
                window.gtag('event', eventName, params || {});
            };
        })();

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

            const lowerHref = href.toLowerCase();
            const isExternalHttp = lowerHref.startsWith('http://') || lowerHref.startsWith('https://');
            const isMailOrTel = lowerHref.startsWith('mailto:') || lowerHref.startsWith('tel:');
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

