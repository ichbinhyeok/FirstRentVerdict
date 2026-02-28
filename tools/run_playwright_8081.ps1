param(
    [string]$JavaHome = "C:\Users\Administrator\FirstRentVerdict\jdk21\jdk-21.0.6+7",
    [string]$NodeExe = "C:\Users\Administrator\FirstRentVerdict\tools\node-v24.14.0-win-x64\node.exe",
    [string]$TestFile = ""
)

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path -Parent $PSScriptRoot
$stamp = Get-Date -Format "yyyyMMdd_HHmmss"
$gradleOut = Join-Path $repoRoot "bootrun_8081_pw.$stamp.out.log"
$gradleErr = Join-Path $repoRoot "bootrun_8081_pw.$stamp.err.log"
$port = 8081

$env:JAVA_HOME = $JavaHome
$env:NODE_OPTIONS = "--use-system-ca"

function Stop-ProcessTree {
    param([int]$ProcessId)
    if (Get-Process -Id $ProcessId -ErrorAction SilentlyContinue) {
        cmd /c "taskkill /PID $ProcessId /T /F" | Out-Null
    }
}

$bootProc = $null
$pwReportPath = Join-Path $repoRoot "e2e\test-results\playwright_run_$stamp.json"
try {
    $existing = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue |
        Select-Object -ExpandProperty OwningProcess -Unique
    foreach ($existingPid in $existing) {
        Stop-Process -Id $existingPid -Force -ErrorAction SilentlyContinue
    }

    Push-Location $repoRoot
    $bootProc = Start-Process -FilePath ".\gradlew.bat" `
        -ArgumentList "bootRun --args=""--server.port=$port""" `
        -PassThru `
        -RedirectStandardOutput $gradleOut `
        -RedirectStandardError $gradleErr

    $ready = $false
    for ($i = 0; $i -lt 120; $i++) {
        Start-Sleep -Milliseconds 1000
        try {
            $res = Invoke-WebRequest -Uri "http://localhost:$port/RentVerdict/" -UseBasicParsing -TimeoutSec 3
            if ($res.StatusCode -ge 200) {
                $ready = $true
                break
            }
        } catch {
        }
    }

    if (-not $ready) {
        throw "Server did not become ready on port $port"
    }

    Push-Location (Join-Path $repoRoot "e2e")
    $args = @(".\node_modules\@playwright\test\cli.js", "test")
    if ($TestFile -ne "") {
        $normalizedTestFile = $TestFile -replace '^[.][/\\]', ''
        $args += $normalizedTestFile
    }
    $args += "--reporter=json"
    $pwOutput = & $NodeExe $args 2>&1
    $exitCode = $LASTEXITCODE
    $pwOutput | Out-File -FilePath $pwReportPath -Encoding utf8
    Pop-Location
    if ($exitCode -ne 0) {
        throw "Playwright tests failed with exit code $exitCode. Report: $pwReportPath"
    }
}
finally {
    if ($bootProc) {
        Stop-ProcessTree -ProcessId $bootProc.Id
    }
    Start-Sleep -Milliseconds 1200
    $listeners = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    if ($listeners) {
        Write-Warning "Port $port is still listening after test run."
    }
    while ((Get-Location).Path -ne $repoRoot -and (Get-Location).Path -like "$repoRoot*") {
        Pop-Location
    }
}

Write-Host "Playwright run completed on port $port."
Write-Host "Playwright report: $pwReportPath"
