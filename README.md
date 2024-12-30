# Zephyr
## Work at the speed of light.

Zephyr is a lightning fast code editor written in
[Kotlin](https://kotlinlang.org) and [JavaFx](https://openjfx.io)


## Getting started
### Building
**NOTE for Windows replace ./gradew with .\gradlew.bat in Powershell**

Prerequisites:
- [Java JDK 17](https://adoptium.net/temurin/releases/?version=17)
- [Kotlin](https://kotlinlang.org/docs/getting-started.html#install-kotlin)

Building:
```bash
./gradlew build
```

Running:
```bash
./gradlew run
```

Packaging:
```bash
./gradlew build
```
(exe) Output: `build/dustributions/`

Installing / Updating:

Does *NOT* work on Windows unless using WSL2
```bash
rm -rf $HOME/bin/zephyr
git pull
./gradlew clean build && mkdir -p "$HOME/bin/zephyr" && \
cd build/distributions && unzip "Zephyr-*.zip" -d "$HOME/bin/zephyr" && \
echo -e '#!/bin/bash\n'"$HOME/bin/zephyr/Zephyr-*/bin/Zephyr" | sudo tee /usr/local/bin/zephyr > /dev/null && \
sudo chmod +x /usr/local/bin/zephyr
```
If there were no errors, you may now run `zephyr` in your teminal.
There will be a more elegant way and Windows support later on!

## License
This project uses the
[MIT](https://en.wikipedia.org/wiki/MIT_License)
license. See the [license](LICENSE.md) for more details.