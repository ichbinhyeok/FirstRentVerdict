@param String title = "First Rent Verdict"
@param String description = "First Rent Verdict - Know your true move-in cost before signing the lease."
@param String canonical = "https://movecostinfo.com/"
@param boolean noindex = false
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <meta name="description" content="${description}">
    <link rel="canonical" href="${canonical}">
    
    @if(noindex)
        <meta name="robots" content="noindex, nofollow">
    @endif

    <!-- Open Graph -->
    <meta property="og:title" content="${title}">
    <meta property="og:description" content="${description}">
    <meta property="og:url" content="${canonical}">
    <meta property="og:type" content="website">
    <meta property="og:site_name" content="First Rent Verdict">

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Styles -->
    <link rel="stylesheet" href="/css/style.css">

    <!-- GA4 -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-5YQX4VQ8ZT"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'G-5YQX4VQ8ZT');
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
