import sqlite3

conn = sqlite3.connect('recordings.db')
cursor = conn.cursor()

cursor.execute('''CREATE TABLE IF NOT EXISTS recordings (
                        id INTEGER PRIMARY KEY,
                        name TEXT,
                        thumbnail TEXT,
                        recording_date TEXT
                        )''')
               
conn.commit()
conn.close()
