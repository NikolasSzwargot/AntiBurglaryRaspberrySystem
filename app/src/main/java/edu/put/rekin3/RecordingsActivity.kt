package edu.put.rekin3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecordingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordings)

        val recordings = listOf(
            MediaItem("15.09.2023", "getge", "gergger"),
            MediaItem("31.12.2023", "getge", "gergger"),
            MediaItem("04.05.2022",  "getge", "gergger"))

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecordingsAdapter(recordings) { item ->
            Intent(this, RecordingActivity::class.java).apply {
                putExtra("RECORDING", item.recording)
                putExtra("DATE", item.date)
            }.also { startActivity(it) }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}