# Clean terminal for better visibility
Clear-Host

# Watermark
Write-Host "----------------------------------"
Write-Host "Compose script written by Tristan"
Write-Host "Version 3.2.24 | Type: Development"
Write-Host "----------------------------------"

# Make new line for nicer formatting...
Write-Host "`n"

# Store the start time
$start_time = Get-Date -Format 's.fff'

# Locate the source files
Write-Host "Getting ready..."
Set-Location "Kotlin/src" -ErrorAction Stop
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue Zephyr

# Compile the jar
Write-Host "Beginning compile..."
kotlinc Zephyr.kt -include-runtime -d Main.jar

# Compile the assets
Write-Host "Compile complete, moving assets..."
New-Item -ItemType Directory -Force temp_extracted_jar | Out-Null
Expand-Archive -Path Main.jar -DestinationPath temp_extracted_jar
Copy-Item splash.png -Destination temp_extracted_jar -Force
Copy-Item icon.png -Destination temp_extracted_jar -Force

# Package the jar
Write-Host "Repackaging JAR file..."
Set-Location temp_extracted_jar
Compress-Archive -Path * -DestinationPath "../Main.jar" -Force
Set-Location ..

# Make the jar runnable
Remove-Item -Recurse -Force temp_extracted_jar
New-Item -ItemType Directory -Force temp_extracted_jar | Out-Null
Expand-Archive -Path Main.jar -DestinationPath temp_extracted_jar
Set-Location temp_extracted_jar
Compress-Archive -Path * -DestinationPath "../Zephyr.jar" -Force
Set-Location ..

# Add Assets
New-Item -ItemType Directory -Force Zephyr | Out-Null
Copy-Item splash.png -Destination Zephyr -Force
Copy-Item icon.png -Destination Zephyr -Force
Copy-Item Zephyr.jar -Destination Zephyr -Force

# Clean up
Remove-Item -Recurse -Force temp_extracted_jar
Remove-Item -Force Main.jar

# Calculate elapsed time
$end_time = Get-Date -Format 's.fff'
$elapsed_time = [double]($end_time - $start_time)

# Finish the task(s)
Write-Host "Finished all task(s)!"
Write-Host "Elapsed time: $($elapsed_time.ToString("F2")) seconds"

# Run the jarfile
java -jar Zephyr.jar

# Finish the script
Write-Host "Run(s) finished!"
