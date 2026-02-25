$tests = @(
    @{ name = "Valid City Page"; url = "http://localhost:8081/RentVerdict/verdict/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Invalid City Name"; url = "http://localhost:8081/RentVerdict/verdict/fake-city-ny"; expected = 404; method = "GET" }
    
    @{ name = "Valid Savings Page"; url = "http://localhost:8081/RentVerdict/verdict/can-i-move-with/5000/to/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Invalid Savings Amount (Crawl Trap 5001)"; url = "http://localhost:8081/RentVerdict/verdict/can-i-move-with/5001/to/new-york-ny"; expected = 404; method = "GET" }
    @{ name = "Invalid Savings Bounds"; url = "http://localhost:8081/RentVerdict/verdict/can-i-move-with/900000/to/new-york-ny"; expected = 404; method = "GET" }

    @{ name = "Valid Relocation"; url = "http://localhost:8081/RentVerdict/verdict/moving-from/chicago-il/to/new-york-ny"; expected = 200; method = "GET" }
    @{ name = "Invalid Origin City Relocation"; url = "http://localhost:8081/RentVerdict/verdict/moving-from/fake-city-il/to/new-york-ny"; expected = 404; method = "GET" }

    @{ name = "Valid Rent State Page"; url = "http://localhost:8081/RentVerdict/first-month-cost/3000/ny"; expected = 200; method = "GET" }
    @{ name = "Invalid Rent Amount (Crawl Trap 3001)"; url = "http://localhost:8081/RentVerdict/first-month-cost/3001/ny"; expected = 404; method = "GET" }
    @{ name = "Invalid Rent Bounds"; url = "http://localhost:8081/RentVerdict/first-month-cost/20000/ny"; expected = 404; method = "GET" }
)

Write-Host "========================== SMOKE TEST STARTING =========================="
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

$simulateUrl = "http://localhost:8081/RentVerdict/api/simulate"

# Valid Simulate
$validBody = '{"city":"New York","state":"NY","monthlyRent":3000,"availableCash":15000,"hasPet":false,"isLocalMove":true,"creditTier":"GOOD"}'
try {
    $res = Invoke-RestMethod -Uri $simulateUrl -Method Post -Body $validBody -ContentType "application/json" -ErrorAction Stop
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

Write-Host "========================== SMOKE TEST SUMMARY ==========================="
Write-Host "Passed: $passCount" -ForegroundColor Green
if ($failCount -gt 0) {
    Write-Host "Failed: $failCount" -ForegroundColor Red
}
else {
    Write-Host "Failed: 0" -ForegroundColor Green
}
Write-Host "========================================================================="
