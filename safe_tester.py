import tkinter as tk
from tkinter import ttk, messagebox
import sqlite3
import datetime
from tkinter import simpledialog


# Database connection
conn = sqlite3.connect('parking_lot_booking_system.db')
c = conn.cursor()

# Global variables
current_user = None
selected_parking_lot = None
selected_spot = None
booking_id = None

# Functions
def login():
    global current_user

    username = username_entry.get()
    password = password_entry.get()

    c.execute("SELECT * FROM Users WHERE username = ? AND password = ?", (username, password))
    user = c.fetchone()

    if user:
        current_user = user
        username_entry.delete(0, tk.END)
        password_entry.delete(0, tk.END)
        show_dashboard()
    else:
        messagebox.showerror("Login Failed", "Invalid username or password.")

def show_dashboard():
    global dashboard_frame, content_frame  # Add content_frame to global variables

    # Clear the current frame
    for widget in main_frame.winfo_children():
        widget.destroy()

    # Create the dashboard frame
    dashboard_frame = tk.Frame(main_frame)
    dashboard_frame.pack(fill=tk.BOTH, expand=True)

    # Header bar
    header_bar = tk.Frame(dashboard_frame, bg="gray")
    header_bar.pack(fill=tk.X, pady=10)

    wallet_balance_label = tk.Label(header_bar, text=f"Wallet Balance: ₹{current_user[8]:.2f}", font=("Arial", 14), bg="gray", fg="white")
    wallet_balance_label.pack(side=tk.RIGHT, padx=10)

    # Left sidebar
    sidebar_frame = tk.Frame(dashboard_frame, bg="light gray")
    sidebar_frame.pack(side=tk.LEFT, fill=tk.BOTH)

    book_parking_button = tk.Button(sidebar_frame, text="Book Parking Lot", font=("Arial", 12), bg="white", command=lambda: show_book_parking(content_frame))
    book_parking_button.pack(pady=10, padx=10, fill=tk.X)

    booking_history_button = tk.Button(sidebar_frame, text="Booking History", font=("Arial", 12), bg="white", command=lambda: show_booking_history(content_frame))  # Pass content_frame here
    booking_history_button.pack(pady=10, padx=10, fill=tk.X)

    load_balance_button = tk.Button(sidebar_frame, text="Load Balance", font=("Arial", 12), bg="white", command=show_load_balance)
    load_balance_button.pack(pady=10, padx=10, fill=tk.X)

    profile_button = tk.Button(sidebar_frame, text="Profile", font=("Arial", 12), bg="white", command=show_profile)
    profile_button.pack(pady=10, padx=10, fill=tk.X)

    logout_button = tk.Button(sidebar_frame, text="Logout", font=("Arial", 12), bg="white", command=logout)
    logout_button.pack(pady=10, padx=10, fill=tk.X)

    # Main content area
    content_frame = tk.Frame(dashboard_frame, bg="white")
    content_frame.pack(fill=tk.BOTH, expand=True)

    welcome_label = tk.Label(content_frame, text=f"Welcome, {current_user[3]}!", font=("Arial", 18), bg="white")
    welcome_label.pack(pady=20)

# ... (Additional function definitions will be added later)

def show_book_parking(content_frame):  # Add content_frame as a parameter
    global selected_parking_lot

    # Clear the content frame
    for widget in content_frame.winfo_children():
        widget.destroy()

    # Parking lot selection dropdown
    parking_lot_label = tk.Label(content_frame, text="Select Parking Lot:", font=("Arial", 14))
    parking_lot_label.pack(pady=10)

    parking_lot_dropdown = ttk.Combobox(content_frame, font=("Arial", 14), state="readonly")
    parking_lot_dropdown["values"] = ["Seawoods Sector 48", "Kurla Sector 12"]
    parking_lot_dropdown.current(0)
    parking_lot_dropdown.pack(pady=10)

    # Select button
    select_button = tk.Button(content_frame, text="Select", font=("Arial", 14), command=lambda: show_parking_spots(content_frame, parking_lot_dropdown.get()))  # Pass content_frame here
    select_button.pack(pady=10)


# Function to show parking spots
def show_parking_spots(content_frame, location):  # Add content_frame as a parameter
    global selected_parking_lot, selected_spot

    selected_parking_lot = None
    selected_spot = None

    # Clear the content frame
    for widget in content_frame.winfo_children():
        widget.destroy()

    # Get the parking lot ID from the database
    c.execute("SELECT parking_lot_id, total_spots FROM Parking_Lots WHERE location = ?", (location,))
    parking_lot = c.fetchone()

    if parking_lot:
        selected_parking_lot = parking_lot[0]
        total_spots = parking_lot[1]

        # Create a frame to display the parking spots
        spots_frame = tk.Frame(content_frame)
        spots_frame.pack(pady=10)

        # Create buttons for each parking spot
        for i in range(total_spots):
            spot_number = i + 1
            spot_button = tk.Button(spots_frame, text=str(spot_number), font=("Arial", 14), bg="green", width=4, height=2, command=lambda spot=spot_number: select_spot(content_frame, spot))
            spot_button.grid(row=i // 5, column=i % 5, padx=5, pady=5)

            # Check if the spot is already booked
            c.execute("SELECT 1 FROM Bookings WHERE parking_lot_id = ? AND spot_number = ? AND exit_time IS NULL", (selected_parking_lot, spot_number))
            if c.fetchone():
                spot_button.config(bg="red")

            # Check if the current user has booked the spot
            c.execute("SELECT 1 FROM Bookings WHERE user_id = ? AND parking_lot_id = ? AND spot_number = ? AND exit_time IS NULL", (current_user[0], selected_parking_lot, spot_number))
            if c.fetchone():
                spot_button.config(bg="blue")

        # Book button
        book_button = tk.Button(content_frame, text="Book", font=("Arial", 14), command=book_spot)
        book_button.pack(pady=10)

    else:
        error_label = tk.Label(content_frame, text="Invalid parking lot location.", font=("Arial", 14))
        error_label.pack(pady=10)

# Function to select a parking spot

def select_spot(content_frame, spot_number):  # Add content_frame as a parameter
    global selected_spot

    if selected_spot:
        # Reset the background color of the previously selected spot
        spot_button = content_frame.winfo_children()[0].winfo_children()[selected_spot - 1]
        spot_button.config(bg="green")

    selected_spot = spot_number
    # Change the background color of the currently selected spot
    spot_button = content_frame.winfo_children()[0].winfo_children()[spot_number - 1]
    spot_button.config(bg="yellow")

    print("Spot selected:", selected_spot)


def book_spot():
    global booking_id

    if selected_spot:
        # Get the duration in hours
        duration = simpledialog.askinteger("Booking Duration", "Enter the number of hours:")

        if duration:
            amount = duration * 20  # Assuming a rate of ₹20 per hour

            # Check if the user has enough balance
            if amount <= current_user[8]:
                # Deduct the amount from the user's wallet
                c.execute("UPDATE Users SET wallet_balance = wallet_balance - ? WHERE user_id = ?", (amount, current_user[0]))
                conn.commit()

                # Insert the booking into the database
                booking_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                c.execute("INSERT INTO Bookings (user_id, parking_lot_id, spot_number, booking_time, duration_hours, amount_paid) VALUES (?, ?, ?, ?, ?, ?)",
                          (current_user[0], selected_parking_lot, selected_spot, booking_time, duration, amount))
                conn.commit()
                booking_id = c.lastrowid

                # Update the spot button color
                spot_button = content_frame.winfo_children()[1].winfo_children()[selected_spot - 1]
                spot_button.config(bg="blue")

                messagebox.showinfo("Booking Successful", f"Spot {selected_spot} booked for {duration} hours. Amount paid: ₹{amount:.2f}")
            else:
                messagebox.showerror("Insufficient Balance", "You don't have enough balance in your wallet.")
        else:
            messagebox.showerror("Invalid Duration", "Please enter a valid duration in hours.")
    else:
        messagebox.showerror("No Spot Selected", "Please select an available spot to book.")

def show_booking_history(content_frame):  # Add content_frame as a parameter
    # Clear the content frame
    for widget in content_frame.winfo_children():
        widget.destroy()

    # Create a treeview to display the booking history
    booking_history_tree = ttk.Treeview(content_frame, columns=("Parking Lot", "Spot", "Duration", "Amount Paid", "Exit", "Invoice"), show="headings")
    booking_history_tree.heading("Parking Lot", text="Parking Lot")
    booking_history_tree.heading("Spot", text="Spot")
    booking_history_tree.heading("Duration", text="Duration (Hours)")
    booking_history_tree.heading("Amount Paid", text="Amount Paid")
    booking_history_tree.heading("Exit", text="Exit")
    booking_history_tree.heading("Invoice", text="Invoice")
    booking_history_tree.pack(pady=10)

    # Fetch the user's booking history from the database
    c.execute("SELECT b.booking_id, p.location, b.spot_number, b.duration_hours, b.amount_paid, b.exit_time, b.additional_amount FROM Bookings b JOIN Parking_Lots p ON b.parking_lot_id = p.parking_lot_id WHERE b.user_id = ?", (current_user[0],))
    bookings = c.fetchall()

    # Insert the booking data into the treeview
    for booking in bookings:
        booking_id, location, spot_number, duration_hours, amount_paid, exit_time, additional_amount = booking

        if exit_time:
            total_amount = amount_paid + (additional_amount or 0)
            exit_button = ttk.Button(booking_history_tree, text="Exit", command=lambda bid=booking_id: exit_parking(content_frame, bid))
            invoice_button = ttk.Button(booking_history_tree, text="View Invoice", command=lambda bid=booking_id: view_invoice(content_frame, bid))
            booking_history_tree.insert("", tk.END, values=(location, spot_number, duration_hours, f"₹{amount_paid:.2f}", "-", ""))
            booking_history_tree.window_configure(booking_history_tree.get_children()[-1], column="Exit", window=exit_button)
            booking_history_tree.window_configure(booking_history_tree.get_children()[-1], column="Invoice", window=invoice_button)
        else:
            exit_button = ttk.Button(booking_history_tree, text="Exit", command=lambda bid=booking_id: exit_parking(content_frame, bid))
            invoice_button = ttk.Button(booking_history_tree, text="View Invoice", command=lambda bid=booking_id: view_invoice(content_frame, bid))
            booking_history_tree.insert("", tk.END, values=(location, spot_number, duration_hours, f"₹{amount_paid:.2f}", ""))
            booking_history_tree.window_configure(booking_history_tree.get_children()[-1], column="Exit", window=exit_button)
            booking_history_tree.window_configure(booking_history_tree.get_children()[-1], column="Invoice", window=invoice_button)

def exit_parking(content_frame, booking_id):  # Add content_frame as a parameter
    # Get the booking details from the database
    c.execute("SELECT b.parking_lot_id, b.spot_number, b.duration_hours, b.booking_time FROM Bookings b WHERE b.booking_id = ?", (booking_id,))
    booking = c.fetchone()

    if booking:
        parking_lot_id, spot_number, duration_hours, booking_time = booking

        # Calculate the total duration and additional amount
        exit_time = datetime.datetime.now()
        booking_time = datetime.datetime.strptime(booking_time, "%Y-%m-%d %H:%M:%S")
        total_duration = (exit_time - booking_time).total_seconds() // 3600
        additional_hours = max(0, total_duration - duration_hours)
        additional_amount = additional_hours * 20

        # Update the booking record in the database
        c.execute("UPDATE Bookings SET exit_time = ?, additional_amount = ? WHERE booking_id = ?", (exit_time.strftime("%Y-%m-%d %H:%M:%S"), additional_amount, booking_id))
        conn.commit()

        # Generate the invoice
        generate_invoice(content_frame, booking_id)  # Pass content_frame here

        # Refresh the booking history
        show_booking_history(content_frame)  # Pass content_frame here

def view_invoice(content_frame, booking_id):  # Add content_frame as a parameter
    # Fetch the booking details from the database
    c.execute("SELECT b.parking_lot_id, b.spot_number, b.booking_time, b.duration_hours, b.amount_paid, b.exit_time, b.additional_amount, p.location FROM Bookings b JOIN Parking_Lots p ON b.parking_lot_id = p.parking_lot_id WHERE b.booking_id = ?", (booking_id,))
    booking = c.fetchone()

    if booking:
        parking_lot_id, spot_number, booking_time, duration_hours, amount_paid, exit_time, additional_amount, location = booking

        # Create a new window to display the invoice
        invoice_window = tk.Toplevel(content_frame)
        invoice_window.title("Invoice")

        # Display the booking details
        booking_label = tk.Label(invoice_window, text="Booking Details", font=("Arial", 16, "bold"))
        booking_label.pack(pady=10)

        parking_lot_label = tk.Label(invoice_window, text=f"Parking Lot: {location}", font=("Arial", 14))
        parking_lot_label.pack(pady=5)

        spot_label = tk.Label(invoice_window, text=f"Spot Number: {spot_number}", font=("Arial", 14))
        spot_label.pack(pady=5)

        booking_time_label = tk.Label(invoice_window, text=f"Booking Time: {booking_time}", font=("Arial", 14))
        booking_time_label.pack(pady=5)

        duration_label = tk.Label(invoice_window, text=f"Duration (Hours): {duration_hours}", font=("Arial", 14))
        duration_label.pack(pady=5)

        amount_paid_label = tk.Label(invoice_window, text=f"Amount Paid: ₹{amount_paid:.2f}", font=("Arial", 14))
        amount_paid_label.pack(pady=5)

        if exit_time:
            exit_time_label = tk.Label(invoice_window, text=f"Exit Time: {exit_time}", font=("Arial", 14))
            exit_time_label.pack(pady=5)

            if additional_amount:
                additional_amount_label = tk.Label(invoice_window, text=f"Additional Amount: ₹{additional_amount:.2f}", font=("Arial", 14))
                additional_amount_label.pack(pady=5)

                total_amount_label = tk.Label(invoice_window, text=f"Total Amount: ₹{amount_paid + additional_amount:.2f}", font=("Arial", 14, "bold"))
                total_amount_label.pack(pady=5)


def generate_invoice(content_frame, booking_id):  # Add content_frame as a parameter
    # Fetch the booking details from the database
    c.execute("SELECT b.parking_lot_id, b.spot_number, b.booking_time, b.duration_hours, b.amount_paid, b.exit_time, b.additional_amount, p.location FROM Bookings b JOIN Parking_Lots p ON b.parking_lot_id = p.parking_lot_id WHERE b.booking_id = ?", (booking_id,))
    booking = c.fetchone()

    if booking:
        parking_lot_id, spot_number, booking_time, duration_hours, amount_paid, exit_time, additional_amount, location = booking

        # Write the invoice details to a file
        with open(f"invoice_{booking_id}.txt", "w") as f:
            f.write(f"Parking Lot: {location}\n")
            f.write(f"Spot Number: {spot_number}\n")
            f.write(f"Booking Time: {booking_time}\n")
            f.write(f"Duration (Hours): {duration_hours}\n")
            f.write(f"Amount Paid: ₹{amount_paid:.2f}\n")

            if exit_time:
                f.write(f"Exit Time: {exit_time}\n")

                if additional_amount:
                    f.write(f"Additional Amount: ₹{additional_amount:.2f}\n")
                    f.write(f"Total Amount: ₹{amount_paid + additional_amount:.2f}\n")



def show_load_balance():
    # Clear the content frame
    for widget in content_frame.winfo_children():
        widget.destroy()

    # Amount entry
    amount_label = tk.Label(content_frame, text="Enter amount to add:", font=("Arial", 14))
    amount_label.pack(pady=10)

    amount_entry = tk.Entry(content_frame, font=("Arial", 14))
    amount_entry.pack(pady=10)

    # Add balance button
    add_balance_button = tk.Button(content_frame, text="Add Balance", font=("Arial", 14), command=lambda: add_balance(amount_entry.get()))
    add_balance_button.pack(pady=10)

def add_balance(amount):
    try:
        amount = float(amount)
        if amount > 0:
            # Update the user's wallet balance in the database
            c.execute("UPDATE Users SET wallet_balance = wallet_balance + ? WHERE user_id = ?", (amount, current_user[0]))
            conn.commit()

            # Update the wallet balance label in the header bar
            header_bar = dashboard_frame.winfo_children()[0]
            wallet_balance_label = header_bar.winfo_children()[0]
            wallet_balance_label.config(text=f"Wallet Balance: ₹{current_user[8] + amount:.2f}")

            messagebox.showinfo("Balance Added", f"₹{amount:.2f} has been added to your wallet.")
        else:
            messagebox.showerror("Invalid Amount", "Please enter a positive amount.")
    except ValueError:
        messagebox.showerror("Invalid Amount", "Please enter a valid amount.")

def show_profile():
    # Clear the content frame
    for widget in content_frame.winfo_children():
        widget.destroy()

    # Fetch the user's profile data from the database
    c.execute("SELECT name, car_name, car_number, license_number, rto_branch FROM Users WHERE user_id = ?", (current_user[0],))
    profile_data = c.fetchone()

    # Display the profile data
    name_label = tk.Label(content_frame, text=f"Name: {profile_data[0]}", font=("Arial", 14))
    name_label.pack(pady=5)

    car_name_label = tk.Label(content_frame, text=f"Car Name: {profile_data[1]}", font=("Arial", 14))
    car_name_label.pack(pady=5)

    car_number_label = tk.Label(content_frame, text=f"Car Number: {profile_data[2]}", font=("Arial", 14))
    car_number_label.pack(pady=5)

    license_number_label = tk.Label(content_frame, text=f"License Number: {profile_data[3]}", font=("Arial", 14))
    license_number_label.pack(pady=5)

    rto_branch_label = tk.Label(content_frame, text=f"RTO Branch: {profile_data[4]}", font=("Arial", 14))
    rto_branch_label.pack(pady=5)

def logout():
    global current_user

    # Clear the current user data
    current_user = None

    # Clear the dashboard frame
    for widget in dashboard_frame.winfo_children():
        widget.destroy()

    # Show the login frame
    login_frame.pack()


# Tkinter setup
root = tk.Tk()
root.title("Parking Lot Booking System")    
root.geometry("800x600")

# Login frame
login_frame = tk.Frame(root)
login_frame.pack(pady=100)

username_label = tk.Label(login_frame, text="Username:", font=("Arial", 14))
username_label.grid(row=0, column=0, padx=10, pady=10)

username_entry = tk.Entry(login_frame, font=("Arial", 14))
username_entry.grid(row=0, column=1, padx=10, pady=10)

password_label = tk.Label(login_frame, text="Password:", font=("Arial", 14))
password_label.grid(row=1, column=0, padx=10, pady=10)

password_entry = tk.Entry(login_frame, font=("Arial", 14), show="*")
password_entry.grid(row=1, column=1, padx=10, pady=10)

login_button = tk.Button(login_frame, text="Login", font=("Arial", 14), command=login)
login_button.grid(row=2, column=0, columnspan=2, pady=10)

# Main frame
main_frame = tk.Frame(root)
main_frame.pack(fill=tk.BOTH, expand=True)

root.mainloop() 