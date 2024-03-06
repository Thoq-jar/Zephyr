# Clean terminal for better visibility
Clear-Host

# Watermark
Write-Host "----------------------------------"
Write-Host "Clean script written by Tristan"
Write-Host "Version 3.2.24 | Type: Development"
Write-Host "----------------------------------"

# Make new line for nicer formatting...
Write-Host "`n"

# Store the start time
$start_time = Get-Date -Format 's.fff'

# Remove directories and files
Remove-Item -Recurse -Force temp_extracted_jar -ErrorAction SilentlyContinue
Remove-Item -Force Main.jar -ErrorAction SilentlyContinue
Remove-Item -Force Zephyr.jar -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force Zephyr -ErrorAction SilentlyContinue

# Calculate elapsed time
$end_time = Get-Date -Format 's.fff'
$elapsed_time = [double]($end_time - $start_time)

# Finish the tasks
Write-Host "Finished all task(s)!"
Write-Host "Elapsed time: $($elapsed_time.ToString("F2")) seconds"
