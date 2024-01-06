# main.py - program odpowiedzialny za wykrywanie obecności przy drzwiach i wysyłaniu danych na serwer i bazy danych.

# Potrzebne importy
import CameraUtils as camera
import RPi.GPIO as GPIO
import time
import DBUtils as db
from datetime import datetime

# Flaga, która mówi o tym czy wykryto przerwanie wiązki przy drzwiach.
burglarDetected = False
# Licznik nagrań, inicjowany poprzez dodania 1 do maksymalnego id w bazie danych. (Funkcje bazodanowe znajdują się w pliku DBUtils.)
recordingCounter = db.getMaxID() + 1

# Funkcja obsługująca przerwanie wiązki, która ustawia flagę na prawdziwą.
def IRCallback(channel):
    global burglarDetected
    if not burglarDetected:
        print("Przerwanie wiazki")
        burglarDetected = True

# Funkcja ustawiająca opcje czujnika przerwania wiązki.
def setupIR(pin = 17): 
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(pin, GPIO.IN)
    GPIO.add_event_detect(pin, GPIO.FALLING, callback=IRCallback, bouncetime=200)

# Ustawienie czujnika funkcji    
setupIR()

try:
    print("Gotowy do pracy")
    # Główna pętla programu.
    while True:
        # Sprawdzenie czy flaga jest równa True.
        if(burglarDetected):
            print("Nagrywam")
            # Nazwy plików kolejno: nagrania i zdjęcia.
            recording_name = "recording" + str(recordingCounter) + ".mp4"
            thumbnail_name = "thumbnail" + str(recordingCounter) + ".jpeg"

            # Data nagrania.
            current_time = datetime.now()
            formatted_time = current_time.strftime('%Y-%m-%d %H:%M:%S')

            # Rozpoczęcie nagrania.
            camera.recordDatabaseVideo(recording_name, thumbnail_name)
            # Dodanie nagrania do bazy danych.
            db.addNewRecording(recordingCounter, recording_name, thumbnail_name, formatted_time)
            recordingCounter = recordingCounter + 1

            # Poczekanie 15 sekund przed kolejnym wykrywaniem.
            time.sleep(15)
            print("ponowne wykrywanie")
            burglarDetected = False

#Przy przerwaniu z klawiatury, wyczyszc           
except KeyboardInterrupt:
    GPIO.cleanup()
