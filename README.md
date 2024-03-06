------------------------------------------------------------------------------------------
**REMEMBER THAT THE SOUCE IS ALWAYS NEWER AND THEREFORE BLEEDING EDGE | IT WILL BE LESS STABLE THAN THE BINARY!**
(the binary is tested more etenivly, the source is as soon as it works its added not tested)
------------------------------------------------------------------------------------------
Multiplatform code editor written in Java and Kotlin that is free and open source.
Our goal is to be as fast as possible, which is why Zephyr uses your GPU to render all ui elements and to power the syntax highloghting and other background tasks that indirectly make it snappier! If this is too slow for you we recommend checking out Zephyr Lite which is written in C++ for maximum performance [here!](https://github.com/Thoq-jar/Zephyr-Lite/tree/main)

------------------------------------------------------------------------------------------

**COMPILING ZEPHYR FROM SOURCE:**

**UNIX-BASED SYSTEMS:**
To download run ```git clone https://github.com/Thoq-jar/Zephyr.git && cd Zephyr```
Then you need to make the scripts executable with ```chmod +x clean && chmod +x compose-run```
To just compile: Run ```./compose``` to compile
To compile and run: Run ```./compose-run```
To clean up extraneous unneeded files: Run ```./clean```

EX: ```./clean && ./compose-run``` will clean up the last compiled files and make new fresh ones
**EX: Super command:** ```git clone https://github.com/Thoq-jar/Zephyr.git && cd Zephyr && chmod +x clean && chmod +x compose-run && ./clean && ./compose-run```
------------------------------------------------------------------------------------------

**DOS-BASED SYSTEMS:**
Compiling on windows is an absolute nightmare so if possible
we recommend compiling on a UNIX-based system or using WSL2 on Windows 11:

PART 1: Enable WSL2:
1) Go to search and search for turn windows fetures on or off.
2) Find anything called 'Windows subsystem for linux' and click the check next to it.
3) Restart your computer.
4) Go to microsoft store and search for Ubuntu and install any version

PART 2: Setup Ubuntu:
1) Enter the command: ```sudo apt-get update && sudo apt-get upgrade && sudo apt install git```
2) Enter this command: ```git clone https://github.com/Thoq-jar/Zephyr.git && cd Zephyr```
3) Enter this command: ```chmod +x ./clean && chmod +x ./compose```
4) Enter this command: ```./clean && ./compose```
5) Now open your file explorer go to the bottom of the left-hand side and find ```Linux``` (you may need to scroll)
6) Now open the directory and find your Ubuntu install (ex: Ubuntu, Ubuntu-20.04, Ubuntu-Preview)
7) Navigate to a folder called ```Home``` then inside that your username and finally the folder called Zephyr
8) Now inside the folder there should be a folder called Kotlin and inside that src and inside that Zephyer containing some images and a jarfile.
9) Move it to your desktop and run the jar
10) Enjoy!

Note: If these steps don't work you can use a bytecode viewer like recaf to view the code or try this command: ```cd C:/ && cd Zephyr && cd Kotlin && cd src && cd Zephyr && java -jar Zephyr.jar```

------------------------------------------------------------------------------------------


License overview:

1) Any Hobbyist is free to copy, modify, publish, use, compile, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, non-commercial, ONLY.
2) No company is permitted to use this code for commercial purposes or to sell it.
3) No Individual is permitted to sell this code or any derivative works thereof under any means!

4) In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

5) THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more info regarding modification or anything else, please check our more detailed license!
-----------------------------------------------------------------------------------------
