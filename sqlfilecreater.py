import sqlite3

# SQL commands to create tables and insert data
sql_commands = [
    """
    CREATE TABLE Users (
        user_id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL UNIQUE,
        password TEXT NOT NULL,
        name TEXT NOT NULL,
        car_name TEXT NOT NULL,
        car_number TEXT NOT NULL,
        license_number TEXT NOT NULL,
        rto_branch TEXT NOT NULL,
        wallet_balance REAL NOT NULL DEFAULT 0.0
    )
    """,
    """
    CREATE TABLE Parking_Lots (
        parking_lot_id INTEGER PRIMARY KEY AUTOINCREMENT,
        location TEXT NOT NULL,
        total_spots INTEGER NOT NULL
    )
    """,
    """
    CREATE TABLE Bookings (
        booking_id INTEGER PRIMARY KEY AUTOINCREMENT,
        user_id INTEGER NOT NULL,
        parking_lot_id INTEGER NOT NULL,
        spot_number INTEGER NOT NULL,
        booking_time TEXT NOT NULL,
        duration_hours INTEGER NOT NULL,
        amount_paid REAL NOT NULL,
        exit_time TEXT,
        additional_amount REAL,
        FOREIGN KEY (user_id) REFERENCES Users(user_id),
        FOREIGN KEY (parking_lot_id) REFERENCES Parking_Lots(parking_lot_id)
    )
    """,
    """
    INSERT INTO Users (username, password, name, car_name, car_number, license_number, rto_branch, wallet_balance)
    VALUES
        ('user1', 'password1', 'John Doe', 'Toyota Camry', 'MH01AB1234', 'DL0120220001234', 'Mumbai', 1000.0),
        ('user2', 'password2', 'Jane Smith', 'Honda City', 'MH02CD5678', 'DL0120210009876', 'Mumbai', 500.0)
    """,
    """
    INSERT INTO Parking_Lots (location, total_spots)
    VALUES
        ('Seawoods Sector 48', 20),
        ('Kurla Sector 12', 20)
    """
]

# Create SQLite database file and execute SQL commands
def create_database(db_file):
    try:
        conn = sqlite3.connect(db_file)
        c = conn.cursor()
        for command in sql_commands:
            c.execute(command)
        conn.commit()
        conn.close()
        print("Database created successfully!")
    except sqlite3.Error as e:
        print(f"Error creating database: {e}")

# Main function
def main():
    db_file = "parking_lot_booking_system.db"
    create_database(db_file)

if __name__ == "__main__":
    main()
