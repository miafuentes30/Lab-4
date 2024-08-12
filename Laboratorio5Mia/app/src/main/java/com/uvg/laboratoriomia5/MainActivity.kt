package com.uvg.laboratorio5mia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio5AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun AppContent(modifier: Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF0F4F8))
    ) {
        UpdateNotification(
            appUrl = "https://play.google.com/store/apps/details?id=com.whatsapp"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateDisplay(
            weekDay = "Lunes",
            fullDate = "Septiembre 16"
        )
        Spacer(modifier = Modifier.height(16.dp))
        VenueCard(
            venueName = "Buta Ramen House",
            venueAddress = "16 Calle 7-17",
            venueHours = "7:00 AM - 8:00 PM",
            venueType = "Ramen",
            venuePriceRange = "QQ",
            userName = "Mia Fuentes",
            latitude = 14.594208661029253,
            longitude = -90.51001724222571,
            context = context
        )
    }
}

@Composable
fun UpdateNotification(appUrl: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3D5A80))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Notifications,
            contentDescription = "Update",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Actualizacion Disponible",
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Text(
            "Descargar",
            color = Color(0xFFEE6C4D),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun DateDisplay(weekDay: String, fullDate: String) {
    Column {
        Text(
            text = weekDay,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF293241)
        )
        Text(
            text = fullDate,
            fontSize = 16.sp,
            color = Color(0xFF3D5A80)
        )
    }
}

@Composable
fun VenueCard(
    venueName: String,
    venueAddress: String,
    venueHours: String,
    venueType: String,
    venuePriceRange: String,
    userName: String,
    latitude: Double,
    longitude: Double,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = venueName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF293241)
                )
                Icon(
                    Icons.Default.Place,
                    contentDescription = "Location",
                    tint = Color(0xFF3D5A80),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($venueName)")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
                        }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = venueAddress, color = Color(0xFF3D5A80))
            Text(text = venueHours, color = Color(0xFF3D5A80))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, userName, Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF98C1D9))
                ) {
                    Text("Inicio", color = Color.Black)
                }
                Button(
                    onClick = {
                        val message = "$venueType\n$venuePriceRange"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEE6C4D))
                ) {
                    Text("Detalles", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun Laboratorio5AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF3D5A80),
            secondary = Color(0xFFEE6C4D),
            tertiary = Color(0xFF98C1D9),
            background = Color(0xFFF0F4F8)
        ),
        typography = Typography(),
        content = content
    )
}