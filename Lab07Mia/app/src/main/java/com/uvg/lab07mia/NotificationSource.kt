package com.uvg.lab07mia

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun NotificationType.Icon(): ImageVector {
    return when (this) {
        NotificationType.GENERAL -> Icons.Default.Notifications
        NotificationType.NEW_POST -> Icons.Default.Share
        NotificationType.NEW_MESSAGE -> Icons.Default.Info
        NotificationType.NEW_LIKE -> Icons.Default.Favorite
    }
}


enum class NotificationType(val displayName: String) {
    GENERAL("General"),
    NEW_POST("Publicaciones"),
    NEW_MESSAGE("Mensajes"),
    NEW_LIKE("Likes")
}

data class Notification(
    val id: Int,
    val title: String,
    val body: String,
    val sendAt: String,
    val type: NotificationType
)

fun generateFakeNotifications(): List<Notification> {
    val notifications = mutableListOf<Notification>()
    val titles = listOf(
        "Nueva versión disponible",
        "Nuevo post de Juan",
        "Mensaje de Maria",
        "Te ha gustado una publicación"
    )
    val bodies = listOf(
        "La aplicación ha sido actualizada a v1.0.2. Ve a la PlayStore y actualízala!",
        "Te han etiquetado en un nuevo post. ¡Míralo ahora!",
        "No te olvides de asistir a esta capacitación mañana, a las 6pm, en el Intecap.",
        "A Juan le ha gustado tu publicación. ¡Revisa tu perfil!"
    )
    val types = NotificationType.entries.toTypedArray()

    val currentDate = LocalDate.now()
    for (i in 1..50) {
        val daysAgo = (0..10).random()
        val hoursAgo = (0..23).random()
        val minutesAgo = (0..59).random()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        notifications.add(
            Notification(
                id = i,
                title = titles.random(),
                body = bodies.random(),
                sendAt = dateFormat.format(calendar.time),
                type = types.random()
            )
        )
    }
    return notifications
}
