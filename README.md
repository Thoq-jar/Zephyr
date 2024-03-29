------------------------------------------------------------------------------------------
**REMEMBER THAT THE SOURCE IS ALWAYS NEWER AND THEREFORE BLEEDING EDGE | IT WILL BE LESS STABLE THAN THE BINARY!**
(the binary is tested more extensively, the source is as soon as it works added not tested)
------------------------------------------------------------------------------------------
Multiplatform code editor written in Java and Kotlin that is free and open source.
Our goal is to be as fast as possible, which is why Zephyr uses your GPU to render all UI elements and to power the syntax highlighting and other background tasks that indirectly make it snappier! If this is too slow for you we recommend checking out Zephyr Lite which is written in C++ for maximum performance [here!](https://github.com/Thoq-jar/Zephyr-Lite/tree/main)

**If you are confused, read this:**


For MacOS, Linux, and BSD users, use the ```Unix-based systems``` guide to compile, for Windows users, use the ```DOS-Based systems``` guide to help you.

------------------------------------------------------------------------------------------

**COMPILING ZEPHYR FROM SOURCE:**

**UNIX-BASED SYSTEMS: (MacOS, Linux and BSD)**
If you are confused go to the bottom of this section and use the ```super command```, it will automate the process!


To download run 
```bash
git clone https://github.com/Thoq-jar/Zephyr.git && cd Zephyr
```


Then you need to make the scripts executable with
```bash
chmod +x clean && chmod +x compose-run && chmod +x compose
```


To just compile: Run 
```./compose```
to compile


To compile and run: Run 
```bash
./compose-run
```


To clean up extraneous unneeded files: Run 
```bash
./clean
```



EX: 
```bash
./clean && ./compose-run
```


```bash
./clean && ./compose
```
will clean up the last compiled files and make new fresh ones

This command will do all the tasks in one go and run the program for you!


**EX: Super command: You need to execute the command separately otherwise it doesn't work :/**



------------------------------------------------------------------------------------------

**DOS-BASED SYSTEMS:**
Using PowerShell :)

STEP 1: Download the source by running this command: 
```powershell
git clone https://github.com/Thoq-jar/Zephyr.git
``` 

STEP 1.5: Than CD into the directory 
```powershell
cd Zephyr
```


STEP 2: Clean the source by running this command: 
```powershell
.\clean
```


STEP 3: Then compile the source and run by running this command 
```powershell
.\compose-run.ps1
```


**(OPTIONAL)**
Move to desktop
```powershell
Move-Item Zephyr Desktop
```

**(OPTIONAL)**
STEP 3.5: To compile without running, run this command:
```powershell
.\compose.ps1
```

**(OPTIONAL)**
STEP 4: To exit the directory run:
```powershell
cd ..
```
than run
```powershell
cd ..
```
than run
```powershell
cd ..
```
I know you have to run it three times.


**(OPTIONAL)**
Exit Powershell
```powershell
exit
```


**EX. Super Command:** 
```bash
N/A
```

Note: If these steps don't work you can use a bytecode viewer like Recaf to view the code!

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
