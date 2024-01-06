package edu.put.rekin3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class RecordingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecordingsAdapter
    private var recordings = mutableListOf<MediaItem>()  // Initialize with an empty list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordings)

        // Fetch and process data on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            fetchData()
        }
    }

    private suspend fun fetchData() {
        val url = "http://192.168.1.27/videos/dates.txt"
        try {
            // Use URL to download the data
            val resultText = URL(url).readText()

            // Process the text file line by line
            val lines = resultText.lines()  // Each line represents a date
            val newRecordings = mutableListOf<MediaItem>()
            for ((index, date) in lines.withIndex()) {
                if (date.isNotBlank()) {  // Make sure the line is not empty
                    newRecordings.add(MediaItem(date, "http://192.168.1.27/videos/recording${index + 1}.mp4", "http://192.168.1.27/videos/thumbnail${index + 1}.jpeg"))
                }
            }

            // Update UI on the main thread
            withContext(Dispatchers.Main) {
                initializeRecyclerView(newRecordings)
            }

        } catch (e: Exception) {
            // Handle exceptions, could be network error, file not found etc.
            e.printStackTrace()
        }
    }

    private fun initializeRecyclerView(newRecordings: MutableList<MediaItem>) {
        // Set the recordings with the newly received recordings
        recordings = newRecordings

        // Initialize RecyclerView and Adapter with the received data
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = RecordingsAdapter(recordings) { item ->
            // Define the click listener behavior here
            val intent = Intent(this, VideoActivity::class.java).apply {
                putExtra("VIDEO_URL", item.recording)
                putExtra("DATE", item.date)
                // Add other extras if necessary
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}