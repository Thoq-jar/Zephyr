import customtkinter as ctk
from tkinter import filedialog, messagebox, Menu, Text
import os
import json

class Zephyr:
    FILETYPES = [
        ("Text files", "*.txt"),
        ("Python files", "*.py"),
        ("Markdown files", "*.md"),
        ("All files", "*.*")
    ]

    def __init__(self, rt):
        # Initialize Window
        self.root = rt
        self.root.title("Zephyr")
        self.root.geometry("1300x800")

        # Load settings
        self.dark_mode = self.load_settings()
        ctk.set_appearance_mode("dark" if self.dark_mode else "light")

        # Initialize text area
        if self.dark_mode:
            self.text_area = Text(self.root, wrap='word', bg='#0A0A0A', fg='#C8C8C8')
        else:
            self.text_area = Text(self.root, wrap='word', bg='#FFFFFF', fg='#000000')
        self.text_area.pack(expand=True, fill='both')

        self.menu_bar = Menu(self.root)
        self.root.config(menu=self.menu_bar)

        # Setup file menu
        self.file_menu = Menu(self.menu_bar, tearoff=0)
        self.menu_bar.add_cascade(label="File", menu=self.file_menu)
        self.file_menu.add_command(label="Open", command=self.open_file)
        self.file_menu.add_command(label="Save", command=self.save_file)
        self.file_menu.add_separator()
        self.file_menu.add_command(label="Exit", command=self.root.quit)

        # Setup edit menu
        self.edit_menu = Menu(self.menu_bar, tearoff=0)
        self.menu_bar.add_cascade(label="Edit", menu=self.edit_menu)
        self.edit_menu.add_command(label="Select All", command=self.select_all)
        self.edit_menu.add_command(label="Copy", command=self.copy)
        self.edit_menu.add_command(label="Cut", command=self.cut)
        self.edit_menu.add_command(label="Paste", command=self.paste)
        self.edit_menu.add_command(label="Undo", command=self.undo)
        self.edit_menu.add_command(label="Redo", command=self.redo)

        # Setup Zephyr Menu
        self.zephyr_menu = Menu(self.menu_bar, tearoff=0)
        self.menu_bar.add_cascade(label="Zephyr", menu=self.zephyr_menu)
        self.zephyr_menu.add_command(label="Toggle Dark/Light Mode", command=self.toggle_mode)

        # Bind keydown events
        self.root.bind('<Control-a>', lambda event: self.select_all())
        self.root.bind('<Control-z>', lambda event: self.undo())
        self.root.bind('<Control-Shift-Z>', lambda event: self.redo())
        self.root.bind('<Control-c>', lambda event: self.copy())
        self.root.bind('<Control-x>', lambda event: self.cut())
        self.root.bind('<Control-v>', lambda event: self.paste())

        # Initialize stacks
        self.undo_stack = []
        self.redo_stack = []

    def open_file(self):
        file_path = filedialog.askopenfilename(defaultextension=".txt",
                                               filetypes=self.FILETYPES)
        if file_path:
            try:
                with open(file_path, 'r') as file:
                    code = file.read()
                    self.text_area.delete(1.0, ctk.END)
                    self.text_area.insert(ctk.END, code)
            except Exception as e:
                messagebox.showerror("Error", f"Could not open file: {e}")

    def save_file(self):
        file_path = filedialog.asksaveasfilename(defaultextension=".txt",
                                                 filetypes=self.FILETYPES)
        if file_path:
            try:
                with open(file_path, 'w') as file:
                    file.write(self.text_area.get(1.0, ctk.END))
            except Exception as e:
                messagebox.showerror("Error", f"Could not save file: {e}")

    @staticmethod
    def select_all():
        print("TODO: Implement")

    def copy(self):
        self.text_area.clipboard_clear()
        self.text_area.clipboard_append(self.text_area.get("sel.first", "sel.last"))

    def cut(self):
        self.copy()
        self.text_area.delete("sel.first", "sel.last")

    def paste(self):
        try:
            self.text_area.insert(ctk.END, self.root.clipboard_get())
        except Exception as e:
            messagebox.showerror("Error", f"Could not paste: {e}")

    def undo(self):
        try:
            current_text = self.text_area.get(1.0, ctk.END)
            self.undo_stack.append(current_text)
            if len(self.undo_stack) > 10:
                self.undo_stack.pop(0)
            self.text_area.delete(1.0, ctk.END)
            if self.redo_stack:
                self.redo_stack.pop()
        except Exception as e:
            messagebox.showerror("Error", f"Could not undo: {e}")

    def redo(self):
        if self.undo_stack:
            last_text = self.undo_stack.pop()
            self.redo_stack.append(last_text)
            self.text_area.delete(1.0, ctk.END)
            self.text_area.insert(ctk.END, last_text)

    def toggle_mode(self):
        self.dark_mode = not self.dark_mode
        ctk.set_appearance_mode("dark" if self.dark_mode else "light")

        if self.dark_mode:
            self.text_area.config(bg='#0A0A0A', fg='#C8C8C8')
        else:
            self.text_area.config(bg='#FFFFFF', fg='#000000')

        self.save_settings()

    @staticmethod
    def get_config_file_path():
        if os.name == 'nt':
            return os.path.join(os.getenv('APPDATA'), 'zephyr', 'theme_settings.json')
        else:
            return os.path.join(os.path.expanduser('~'), '.zephyr', 'theme_settings.json')

    def load_settings(self):
        config_file = self.get_config_file_path()
        if os.path.exists(config_file):
            with open(config_file, 'r') as file:
                config = json.load(file)
                return config.get("dark_mode", False)
        return False

    def save_settings(self):
        config_file = self.get_config_file_path()
        os.makedirs(os.path.dirname(config_file), exist_ok=True)
        with open(config_file, 'w', encoding='utf-8') as file:
            json.dump({"dark_mode": self.dark_mode}, file)

if __name__ == "__main__":
    root = ctk.CTk()
    editor = Zephyr(root)
    root.mainloop()