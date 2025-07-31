package com.example.progresstracker.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(timestamp: Long?): String {
    if (timestamp == null) return "No due date"

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy") // e.g., 30 Jul 2025
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(timestamp))
}
