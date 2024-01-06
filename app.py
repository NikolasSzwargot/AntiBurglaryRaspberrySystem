from flask import Flask, jsonify
import sqlite3
import DBUtils as db

app = Flask(__name__)

@app.route('/api/recordings/', methods=['GET'])
def get_recordings():
    data = db.getAllRecordings()
    return jsonify({'recordings':data})
    
@app.route('/api/hello/', methods=['GET'])
def hello():
    return "Hello!"