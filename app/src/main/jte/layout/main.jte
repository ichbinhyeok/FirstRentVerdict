@import gg.jte.Content
@param Content content
@param String title = "First Rent Verdict"
@param String description = "Standardize your rental financial risk interaction with the 50/30/20 rule. First Rent Verdict provides objective affordability analysis for new tenants."
@param String canonical = null
@param boolean noindex = false

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    @if(description != null)
        <meta name="description" content="${description}">
    @endif
    @if(canonical != null)
        <link rel="canonical" href="${canonical}">
        <meta property="og:url" content="${canonical}">
    @endif
    @if(noindex)
        <meta name="robots" content="noindex, nofollow">
    @else
        <meta name="robots" content="index, follow">
    @endif

    <!-- Open Graph / Social -->
    <meta property="og:type" content="website">
    <meta property="og:title" content="${title}">
    @if(description != null)
        <meta property="og:description" content="${description}">
    @endif
    <meta property="og:site_name" content="First Rent Verdict">
    <meta property="og:locale" content="en_US">
    <meta property="og:image" content="https://movecostinfo.com/images/og-card.png"> <!-- Placeholder/Default -->

    <link rel="stylesheet" href="/css/style.css">
    <link rel="icon" type="image/png" href="/images/favicon.png">
    <meta name="theme-color" content="#ffffff">

    <!-- Google Search Console Verification -->
    <!-- <meta name="google-site-verification" content="" /> -->
</head>
<body>
    @template.layout.header()
    <main style="flex: 1;">
        ${content}
    </main>
    @template.layout.footer()
</body>
</html>
