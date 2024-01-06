package edu.put.rekin3

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class VideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var dateText: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private var videoUrl: String? = null
    private var date: String? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.my_video_view)
        dateText = findViewById(R.id.dateTextView)
        mediaPlayer = MediaPlayer()
        val intent = intent

        // Extract the video URL from the intent
        videoUrl = intent.getStringExtra("VIDEO_URL")
        date = intent.getStringExtra("DATE")
        dateText.text = date

        if(videoUrl != null) {
            logMessage("Starting download...")
            downloadAndPlayVideo()
        } else {
            logMessage("videoUrl is NULL")
        }
    }

    private fun logMessage(message: String) {
        val tag = "VideoActivityLogs"
        Log.d(tag, message) // You don't necessarily need to launch this on the main thread
    }

    private fun downloadAndPlayVideo() {
        val outputFile = getInternalStorageOutputFile(this)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                downloadFile(videoUrl, outputFile.absolutePath)
                // Ensure this log and the following playback function run on the main thread
                withContext(Dispatchers.Main) {
                    logMessage("Download Complete. Playing Video...")
                    playVideo(Uri.fromFile(outputFile))
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    logMessage("Download Failed: ${e.message}")
                }
            }
        }
    }

    private fun playVideo(videoUri: Uri) {
        try {
            videoView.setVideoURI(videoUri)
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true  // Set MediaPlayer to loop video
                videoView.start()  // Start playback
                startLoggingPlayback()  // Start logging playback every 5 seconds
            }

            videoView.setOnErrorListener { _, _, _ ->
                logMessage("Error in playing video")
                true  // Return true if the error has been handled
            }

            videoView.setOnCompletionListener {
                logMessage("Video playback complete")
            }

        } catch (e: Exception) {
            logMessage("Error setting video: ${e.message}")
        }
    }

    private fun startLoggingPlayback() {
        // Cancel any existing jobs first to avoid duplication
        logPlaybackJob?.cancel()

        logPlaybackJob = lifecycleScope.launch(Dispatchers.IO) {
            while (videoView.isPlaying) {
                delay(5000)  // Delay for 5 seconds
                Log.d("VideoActivityLogs", "Video is playing...")  // Log that the video is playing
            }
        }
    }

    // Declare this variable at the class level to be able to cancel it when needed
    private var logPlaybackJob: Job? = null

    private fun getInternalStorageOutputFile(context: Context): File {
        val videoDirectory = File(context.filesDir, "videos")
        if (!videoDirectory.exists()) {
            videoDirectory.mkdirs()
        }
        val timeStamp = System.currentTimeMillis()
        return File(videoDirectory, "VID_$timeStamp.mp4")
    }

    suspend fun downloadFile(fileUrl: String?, outputPath: String) {
        var connection: HttpURLConnection? = null
        var inputStream: BufferedInputStream? = null
        var output: FileOutputStream? = null
        try {
            val url = URL(fileUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("HTTP error code: ${connection.responseCode}")
            }

            inputStream = BufferedInputStream(connection.inputStream)
            output = FileOutputStream(outputPath)

            val data = ByteArray(1024)
            var count: Int
            while (inputStream.read(data).also { count = it } != -1) {
                output.write(data, 0, count)
            }
        } finally {
            output?.flush()
            output?.close()
            inputStream?.close()
            connection?.disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}