@import gg.jte.Content

@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>First Rent Verdict</title>
    <link rel="stylesheet" href="/RentVerdict/css/style.css">
    <link rel="icon" type="image/png" href="/RentVerdict/images/favicon.png">
    <meta name="theme-color" content="#ffffff">
</head>
<body>
    @template.layout.header()
    <main style="flex: 1;">
        ${content}
    </main>
    @template.layout.footer()
</body>
</html>
