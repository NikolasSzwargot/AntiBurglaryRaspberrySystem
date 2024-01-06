import sqlite3
from flask import Flask, jsonify


def getMaxID():
    conn = sqlite3.connect("/var/www/html/recordings.db")

    cursor = conn.cursor()
    cursor.execute("SELECT MAX(id) FROM recordings")

    maxID = cursor.fetchone()[0]
    
    conn.close()

    if maxID is None:
        maxID = 0
    return maxID

def addNewRecording(recording_id, recording_name, thumbnail_name, recording_date):
    conn = sqlite3.connect("/var/www/html/recordings.db")
    cursor = conn.cursor()
    
    try:
        cursor.execute("INSERT INTO recordings (id, name, thumbnail, recording_date) VALUES (?, ?, ?, ?)",
                       (recording_id, recording_name, thumbnail_name, recording_date))
        conn.commit()
    except sqlite3.Error as e:
        print("Error: ", e)
        conn.rollback()
    finally:
        conn.close()
    
def getAllRecordings():
    conn = sqlite3.connect("/var/www/html/recordings.db")
    cursor = conn.cursor()
    
    cursor.execute("SELECT * FROM recordings")
    rows = cursor.fetchall()
        
    conn.close()
    return rows