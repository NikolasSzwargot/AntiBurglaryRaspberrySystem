package edu.put.rekin3

import android.net.Uri

data class MediaItem(
    val date: String,        // Representing date as a String. Consider using Date or LocalDateTime.
    val recording: String,   // File path to the recording.
    val photo: String       // File path to the photo.
)
