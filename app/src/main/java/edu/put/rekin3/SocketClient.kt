package edu.put.rekin3

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity

class SocketClient : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        // Binding logic here...
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("ACTION")
        readMessage()
        when (action) {
            "CONNECT" -> connect()
            "DISCONNECT" -> disconnect()
            "SEND_MESSAGE" -> {
                val message = intent.getStringExtra("MESSAGE")
                if (message != null) sendMessage(message)
            }
            "READ_MESSAGE" -> readMessage() // Be careful with continuous tasks
            // Handle other actions
        }
        return START_STICKY
    }

    private var serverIp: String = "192.168.1.27" // Replace with your server's IP
    private var serverPort: Int = 12346 // Replace with your server's Port
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var inBuffer: BufferedReader? = null

    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                clientSocket = Socket(serverIp, serverPort)
                out = PrintWriter(clientSocket!!.getOutputStream(), true)
                inBuffer = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
                Log.d("SocketClient", "Connected to server")
            } catch (e: Exception) {
                Log.e("SocketClient", "Error connecting to server", e)
            }
        }
    }

    fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                inBuffer?.close()
                out?.close()
                clientSocket?.close()
                Log.d("SocketClient", "Disconnected from server")
            } catch (e: Exception) {
                Log.e("SocketClient", "Error disconnecting from server", e)
            }
        }
    }

    fun sendMessage(message: String) {
        if (out == null) {
            Log.e("SocketClient", "Cannot send message. Socket is not connected.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                out?.println(message)
                Log.d("SocketClient", "Message sent: $message")
            } catch (e: Exception) {
                Log.e("SocketClient", "Error sending message", e)
            }
        }
    }

    fun readMessage() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                while (true) { // Keep listening for messages
                    val message = inBuffer?.readLine()

                    if (message == null) {
                        if (!isActive) {
                            // Coroutine was cancelled, break the loop
                            break
                        }
                        // Server has closed the connection
                        Log.d("SocketClient", "Server closed the connection.")
                        break
                    } else {
                        Log.d("SocketClient", "Message received: $message") // Log or handle other messages
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    Log.d("SocketClient", "Listening for messages cancelled")
                } else {
                    Log.e("SocketClient", "Error reading message", e)
                }
            }
        }
    }

    fun handleMoveCommand() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Required when starting from a non-Activity context
        intent.putExtra("MOVE", "MOVE")
        startActivity(intent)
    }
}