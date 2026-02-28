param(
    [string]$BaseUrl = "http://localhost:8080"
)

$tests = @(
    @{ name = "Canonical Root Redirect (/RentVerdict -> /RentVerdict/)"; url = "$BaseUrl/RentVerdict"; expected = 301; method = "GET" }
    @{ name = "Valid City Page"; url = "$BaseUrl/RentVerdict/verdict/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Canonical Dotted Slug Redirect"; url = "$BaseUrl/RentVerdict/verdict/st.-louis-mo"; expected = 301; method = "GET" }
    @{ name = "Invalid City Name"; url = "$BaseUrl/RentVerdict/verdict/fake-city-ny"; expected = 404; method = "GET" }

    @{ name = "Valid Savings Page (5k)"; url = "$BaseUrl/RentVerdict/verdict/can-i-move-with/5000/to/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Invalid Savings Amount (Whitelist 4000)"; url = "$BaseUrl/RentVerdict/verdict/can-i-move-with/4000/to/new-york-ny"; expected = 404; method = "GET" }
    @{ name = "Invalid Savings Bounds"; url = "$BaseUrl/RentVerdict/verdict/can-i-move-with/900000/to/new-york-ny"; expected = 404; method = "GET" }

    @{ name = "Valid Relocation"; url = "$BaseUrl/RentVerdict/verdict/moving-from/chicago-il/to/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Invalid Origin City Relocation"; url = "$BaseUrl/RentVerdict/verdict/moving-from/fake-city-il/to/new-york-ny"; expected = 404; method = "GET" }

    @{ name = "Valid Credit Tier (poor)"; url = "$BaseUrl/RentVerdict/verdict/credit/poor/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Invalid Credit Tier"; url = "$BaseUrl/RentVerdict/verdict/credit/aaa/new-york-ny"; expected = 404; method = "GET" }
    @{ name = "Removed Placeholder - Salary Needed (410 GONE)"; url = "$BaseUrl/RentVerdict/verdict/salary-needed/new-york-ny"; expected = 410; method = "GET" }
    @{ name = "Removed Placeholder - No Cosigner (410 GONE)"; url = "$BaseUrl/RentVerdict/verdict/no-cosigner/new-york-ny"; expected = 410; method = "GET" }

    @{ name = "Guide Hub"; url = "$BaseUrl/RentVerdict/guides"; expected = 200; method = "GET" }
    @{ name = "Guide Article (Bad Credit No Cosigner)"; url = "$BaseUrl/RentVerdict/guides/rent-with-bad-credit-no-cosigner"; expected = 200; method = "GET" }
    @{ name = "Guide Article (Invalid Slug)"; url = "$BaseUrl/RentVerdict/guides/not-found-guide"; expected = 404; method = "GET" }

    @{ name = "Deleted Rent State Page (410 GONE)"; url = "$BaseUrl/RentVerdict/first-month-cost/3000/ny"; expected = 410; method = "GET" }
    @{ name = "Compare Page Placeholder Removed (410 GONE)"; url = "$BaseUrl/RentVerdict/verdict/compare/austin-tx-vs-new-york-ny"; expected = 410; method = "GET" }

    @{ name = "Robots TXT"; url = "$BaseUrl/robots.txt"; expected = 200; method = "GET" }
    @{ name = "Sitemap XML"; url = "$BaseUrl/sitemap.xml"; expected = 200; method = "GET" }
)

Write-Host "========================== SMOKE TEST STARTING =========================="
Write-Host "Base URL: $BaseUrl"
$passCount = 0
$failCount = 0

foreach ($test in $tests) {
    try {
        $curlOutput = curl.exe -s -o /dev/null -w "%{http_code}" $test.url
        $statusCode = [int]$curlOutput
    }
    catch {
        $statusCode = 0
    }

    if ($statusCode -eq $test.expected) {
        Write-Host "[PASS] $($test.name) - Got $statusCode" -ForegroundColor Green
        $passCount++
    }
    else {
        Write-Host "[FAIL] $($test.name) - Expected $($test.expected) but got $statusCode (URL: $($test.url))" -ForegroundColor Red
        $failCount++
    }
}

Write-Host "--- Content Assertion ---"
try {
    $cityHtml = (Invoke-WebRequest -Uri "$BaseUrl/RentVerdict/verdict/new-york-ny" -UseBasicParsing -ErrorAction Stop).Content
    if ($cityHtml -match "Security Deposit" -and $cityHtml -match "TOTAL UPFRONT") {
        Write-Host "[PASS] Core UI components exist (Security Deposit & TOTAL UPFRONT)" -ForegroundColor Green
        $passCount++
    }
    else {
        Write-Host "[FAIL] Core UI components missing in City Page" -ForegroundColor Red
        $failCount++
    }
}
catch {
    Write-Host "[FAIL] Content Assertion failed. $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

Write-Host "--- SEO Assertion ---"
try {
    $sitemap = (Invoke-WebRequest -Uri "$BaseUrl/sitemap.xml" -UseBasicParsing -ErrorAction Stop).Content
    if ($sitemap -match "/RentVerdict/verdict/st-louis-mo" -and $sitemap -notmatch "/verdict/compare/" -and $sitemap -notmatch "/RentVerdict/verdict/st.-louis-mo") {
        Write-Host "[PASS] Sitemap canonical/placeholder constraints" -ForegroundColor Green
        $passCount++
    }
    else {
        Write-Host "[FAIL] Sitemap canonical/placeholder constraints failed" -ForegroundColor Red
        $failCount++
    }
}
catch {
    Write-Host "[FAIL] Sitemap Assertion failed. $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

$simulateUrl = "$BaseUrl/RentVerdict/api/simulate"

# Valid Simulate
$validBody = '{"city":"New York","state":"NY","monthlyRent":3000,"availableCash":15000,"hasPet":false,"isLocalMove":true,"creditTier":"GOOD"}'
try {
    $null = Invoke-RestMethod -Uri $simulateUrl -Method Post -Body $validBody -ContentType "application/json" -ErrorAction Stop
    Write-Host "[PASS] Valid Simulate API - Got 200" -ForegroundColor Green
    $passCount++
}
catch {
    Write-Host "[FAIL] Valid Simulate API - Failed. $($_.Exception.Message)" -ForegroundColor Red
    $failCount++
}

# Invalid Simulate (Soft limits)
$invalidBody = '{"monthlyRent":50000,"availableCash":15000,"hasPet":false,"isLocalMove":true,"creditTier":"GOOD"}'
try {
    $res = Invoke-WebRequest -Uri $simulateUrl -Method Post -Body $invalidBody -ContentType "application/json" -ErrorAction Stop
    $statusCode = [int]$res.StatusCode
}
catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
}

if ($statusCode -eq 400) {
    Write-Host "[PASS] Invalid Simulate API - Got 400" -ForegroundColor Green
    $passCount++
}
else {
    Write-Host "[FAIL] Invalid Simulate API - Expected 400 but got $statusCode" -ForegroundColor Red
    $failCount++
}

# Invalid Simulate (Unsupported city)
$badCityBody = '{"city":"Fake City","state":"NY","monthlyRent":3000,"availableCash":15000,"hasPet":false,"isLocalMove":true,"creditTier":"GOOD"}'
try {
    $res = Invoke-WebRequest -Uri $simulateUrl -Method Post -Body $badCityBody -ContentType "application/json" -ErrorAction Stop
    $statusCode = [int]$res.StatusCode
}
catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
}

if ($statusCode -eq 400) {
    Write-Host "[PASS] Bad City Simulate API - Got 400" -ForegroundColor Green
    $passCount++
}
else {
    Write-Host "[FAIL] Bad City Simulate API - Expected 400 but got $statusCode" -ForegroundColor Red
    $failCount++
}

# Invalid Simulate (Long-distance without origin)
$missingOriginBody = '{"city":"New York","state":"NY","monthlyRent":3000,"availableCash":15000,"hasPet":false,"isLocalMove":false,"creditTier":"GOOD"}'
try {
    $res = Invoke-WebRequest -Uri $simulateUrl -Method Post -Body $missingOriginBody -ContentType "application/json" -ErrorAction Stop
    $statusCode = [int]$res.StatusCode
}
catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
}

if ($statusCode -eq 400) {
    Write-Host "[PASS] Missing Origin Simulate API - Got 400" -ForegroundColor Green
    $passCount++
}
else {
    Write-Host "[FAIL] Missing Origin Simulate API - Expected 400 but got $statusCode" -ForegroundColor Red
    $failCount++
}

Write-Host "========================== SMOKE TEST SUMMARY ==========================="
Write-Host "Passed: $passCount" -ForegroundColor Green
if ($failCount -gt 0) {
    Write-Host "Failed: $failCount" -ForegroundColor Red
}
else {
    Write-Host "Failed: 0" -ForegroundColor Green
}
Write-Host "========================================================================="

if ($failCount -gt 0) {
    exit 1
}
