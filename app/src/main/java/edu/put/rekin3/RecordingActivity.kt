package edu.put.rekin3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class RecordingActivity : AppCompatActivity() {

    private lateinit var dateText: TextView
    private lateinit var cameraSurfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        dateText = findViewById(R.id.dateTextViewSingle)
        cameraSurfaceView = findViewById(R.id.cameraSurfaceViewSingle)

        val recording = intent.getStringExtra("RECORDING")
        val date = intent.getStringExtra("DATE")

        if (recording != null && date != null) {
            // Use the extras if they are present
            dateText.text = date
        } else {
            // Handle the case where extras are not as expected
            // For example, show an error message or close the activity
            showErrorAndFinish()
        }

        dateText.text = date.toString()
    }

    private fun showErrorAndFinish() {
        Toast.makeText(this, "Required data not found.", Toast.LENGTH_LONG).show()
        finish() // Close the activity
    }
}