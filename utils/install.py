#!/usr/bin/python3

import os
import sys
import shutil


def check_sudo():
    if os.name != 'nt':
        if os.geteuid() != 0:
            print("This script requires superuser privileges. Please run with 'sudo'.")
            sys.exit(1)


def install_zephyr():
    dist_folder = 'dist'
    executable_name = 'Zephyr'

    if os.path.exists(dist_folder):
        executable_path = os.path.join(dist_folder, executable_name)

        if os.path.isfile(executable_path):
            if os.name == 'nt':  # Windows
                install_path = r'C:\Program Files\Zephyr'
            else:  # Unix-Like
                install_path = '/usr/local/bin'

            os.makedirs(install_path, exist_ok=True)
            shutil.copy(executable_path, install_path)
            print(f'Installed {executable_name} to {install_path} successfully!')
        else:
            print(f'{executable_name} not found in {dist_folder}! Please rebuild Zephyr!')
    else:
        print(f'{dist_folder} does not exist! Please rebuild Zephyr!')


def main():
    print("Welcome to the Zephyr installer!\nPress ctrl+c to interrupt! (exit)\n")
    user_input = input("Have you built Zephyr? [y/n] ")
    if user_input == "y":
        check_sudo()
        install_zephyr()
    elif user_input == "n":
        print("Please read the README.md on how to build Zephyr before continuing!")
    else:
        print("Please enter a valid option (eg. 'y' or 'n')")
        main()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print()
        print("Script interrupted by user.\nExiting...")
