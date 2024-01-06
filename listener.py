# listener.py

import socket
import DBUtils as db
import servo
import RPi.GPIO as GPIO
import time

# Parametry połączenia
host = "192.168.1.27"
port = 12346

#Ustawienie socketu serwerowego i nasłuchiwanie.
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_socket.bind((host, port))
server_socket.listen()
print(f"server listening on {host}:{port}")
        
try:
    while True:
        # Akceptacja połączenia z klientem.
        client_socket, addr = server_socket.accept()
        # Wyłączenie funkcji blokujących.
        client_socket.setblocking(0)
        print(f"Connection from {addr}")
        
        try:
            while True:
                try:
                    
                    # Odebranie danych od klienta.
                    data = client_socket.recv(1024)
                    # Przerwanie gdy klient się rozłączył.
                    if not data:
                        break
                        
                    #Dekodowanie wiadomości.
                    message = data.decode()
                    print(f"Received from client: {message}")
                    
                    # Gdy komunikat to "open" otwórz drzwi.
                    if message.strip().lower() == "open":
                        servo.openDoors()
                        reactionFlag = True
                    # Gdy komunikat to "close" zamknij drzwi.
                    elif message.strip().lower() == "close":
                        servo.closeDoors()
                        reactionFlag = True
                except BlockingIOError:
                    pass
        finally:
            #Zamknij socket gdy klient się rozłączy.
            client_socket.close()
            print("Client disconnected")
#Zamknięcie programu przy przerwa
except KeyboardInterrupt:
    client_socket.close()
    print("Keyboard interrup")
