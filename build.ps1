$ErrorActionPreference = "Stop"

$javaHome = "D:\Android studio download\jbr"
$androidHome = "D:\Chon"

if (Test-Path $javaHome) {
    $env:JAVA_HOME = $javaHome
}
if (Test-Path $androidHome) {
    $env:ANDROID_HOME = $androidHome
}

Write-Host "Đang build dự án Phone Mobile..." -ForegroundColor Cyan
& "$env:JAVA_HOME\bin\java.exe" -jar "gradle\wrapper\gradle-wrapper.jar" assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nBuild thành công! File APK tại: app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Green
} else {
    Write-Host "`nBuild thất bại với mã lỗi $LASTEXITCODE" -ForegroundColor Red
}
