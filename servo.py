
import RPi.GPIO as GPIO
import time

def setupServo(servo_pin = 16):
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(servo_pin, GPIO.OUT)
    
    pwm = GPIO.PWM(servo_pin, 50)
    return pwm

   # pwm.ChangeDutyCycle(7.5)
   #     pwm.ChangeDutyCycle(2.5)
   
def openDoors():
    pwm = setupServo()
    pwm.start(12.5)
    time.sleep(1)
    pwm.stop()
    GPIO.cleanup()
    
def closeDoors():
    pwm = setupServo()
    pwm.start(2.5)
    time.sleep(1)
    pwm.stop()
    GPIO.cleanup()