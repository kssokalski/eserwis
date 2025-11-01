package com.example.eserwis

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentId

data class Fault(
    @DocumentId
    val id: String,
    val title: String,
    val description: String = "",
    val location: String,
    val department: String,
    val priority: String = Priority.NORMAL.name, //domyslny priorytet jest normalny
    val status: String, //zgloszone, w trakcie pracy, zakonczone
    val assignedToUid: String? = null, //UID technika
    val reportedByUid: String, //UID zglaszajacego
    val timestamp : Long = System.currentTimeMillis(), //czas zgloszenia
    val eta: Long? = null
){
    constructor() : this(
        id = "",
        title = "",
        description = "",
        location = "",
        department = "",
        priority = Priority.NORMAL.name,
        status = "",
        assignedToUid = null,
        reportedByUid = "",
        timestamp = System.currentTimeMillis(),
        eta = null

    )
}

enum class Priority(val value: String, val color: Color) {
    CRITICAL("Krytyczna", Color.Red),
    HIGH("Wysoka", Color.Yellow),
    NORMAL("Normalna", Color.Green),
    LOW("Niska", Color.Blue);

    //funkcja pomocnicza do konwersji Stringa z FireBase na Enum
    companion object {
        fun fromString(value: String?): Priority {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: NORMAL
        }
    }

}