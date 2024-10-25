"""
File: build.py
Purpose: Zephyr's build system
Author: Thoq
Copyright: (C) 2024 Thoq
License: MIT
"""

import PyInstaller.__main__

PyInstaller.__main__.run([
    'main.py',
    '--name=Zephyr',
    '--onefile',
    '--windowed',
    '--clean',
])