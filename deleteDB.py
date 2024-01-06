import sqlite3

conn = sqlite3.connect('recordings.db')
cursor = conn.cursor()

cursor.execute('''DROP TABLE recordings''')
               
conn.commit()
conn.close()

CameraUtils.py

import os
from picamera2 import Picamera2
from picamera2.encoders import H264Encoder
from picamera2.outputs import FfmpegOutput
import time

def recordDatabaseVideo(file_name, thumbnail_name, bitrate = 1000000000):
    video_dir = "/var/www/html/videos"
    picam2 = Picamera2()

    picam2.configure(picam2.create_video_configuration())
    encoder = H264Encoder(bitrate = bitrate)
    picam2.start_recording(encoder, FfmpegOutput(os.path.join(video_dir,file_name)))
    picam2.capture_file(os.path.join(video_dir, thumbnail_name))
    
    time.sleep(20)
    picam2.stop_recording()
    print("Recorded a video!")