package edu.put.rekin3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.net.URL
import java.io.File
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity() {

    private lateinit var closeButton: Button
    private lateinit var openButton: Button
    private lateinit var cameraView: SurfaceView
    private lateinit var recordingsButton: Button
    private lateinit var bluetoothText: TextView
    private val CHANNEL_ID = "move_notification_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            connectToService()
        }).start()

        if (intent.hasExtra("MOVE")) {
            handleMoveCommand()
        }

        // Initialize the button from the layout
        val btnStartVideo: Button = findViewById(R.id.buttonRecordings)
        val openButton: Button = findViewById(R.id.buttonOpen)
        val closeButton: Button = findViewById(R.id.buttonClose)

        // Set an onClickListener to the button to start edu.put.rekin3.VideoActivity
        btnStartVideo.setOnClickListener {
            // Create an Intent to start edu.put.rekin3.VideoActivity
            val intent = Intent(this, RecordingsActivity::class.java)
            startActivity(intent)  // Start the edu.put.rekin3.VideoActivity
        }

        openButton.setOnClickListener {
            sendMessageToService("open")
        }

        closeButton.setOnClickListener {
            sendMessageToService("close")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromService() // Disconnect when the app is closed/destroyed
    }

    fun sendMessageToService(message: String) {
        val intent = Intent(this, SocketClient::class.java)
        intent.putExtra("ACTION", "SEND_MESSAGE")
        intent.putExtra("MESSAGE", message)
        startService(intent)
    }

    // Similarly, for connecting or disconnecting
    fun connectToService() {
        val intent = Intent(this, SocketClient::class.java)
        intent.putExtra("ACTION", "CONNECT")
        startService(intent)
    }

    fun disconnectFromService() {
        val intent = Intent(this, SocketClient::class.java)
        intent.putExtra("ACTION", "DISCONNECT")
        startService(intent)
    }

    private fun handleMoveCommand() {
        val notificationManager = NotificationManagerCompat.from(this)

        // Check for POST_NOTIFICATIONS permission introduced in Android 13 (API 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Logic to handle lack of permission
                return
            }
        }

        // Create the notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)  // replace with your own icon
            .setContentTitle("Move Detected")
            .setContentText("A 'move' command was detected.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Notify using the notification manager
        notificationManager.notify(1, builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "anty" // Provide your channel name
            val descriptionText = "do antywlama" // Provide your channel description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}