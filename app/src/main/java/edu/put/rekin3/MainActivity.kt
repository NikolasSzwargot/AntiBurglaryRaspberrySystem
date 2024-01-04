package edu.put.rekin3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var closeButton: Button
    private lateinit var openButton: Button
    private lateinit var cameraView: SurfaceView
    private lateinit var recordingsButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        closeButton = findViewById(R.id.buttonClose)
        openButton = findViewById(R.id.buttonOpen)
        cameraView = findViewById(R.id.camera_surface_view)
        recordingsButton = findViewById(R.id.buttonRecordings)

        closeButton.setOnClickListener {
            buttonClicked()
        }

        openButton.setOnClickListener {
            buttonClicked()
        }

        recordingsButton.setOnClickListener {
            //receiveNotification()
            val intent = Intent(this, RecordingsActivity::class.java)
            startActivity(intent)
        }

    }

    fun receiveNotification() {
        closeButton.visibility = View.VISIBLE
        openButton.visibility = View.VISIBLE
        cameraView.visibility = View.VISIBLE
        recordingsButton.visibility = View.INVISIBLE
    }

    fun buttonClicked() {
        closeButton.visibility = View.INVISIBLE
        openButton.visibility = View.INVISIBLE
        cameraView.visibility = View.INVISIBLE
        recordingsButton.visibility = View.VISIBLE
    }
}