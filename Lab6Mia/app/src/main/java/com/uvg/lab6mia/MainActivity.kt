package com.uvg.lab6mia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaAplicacionContador {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaContador()
                }
            }
        }
    }
}

@Composable
fun PantallaContador() {
    var valor by rememberSaveable { mutableStateOf(0) }
    var aumentos by rememberSaveable { mutableStateOf(0) }
    var disminuciones by rememberSaveable { mutableStateOf(0) }
    var maximo by rememberSaveable { mutableStateOf(0) }
    var minimo by rememberSaveable { mutableStateOf(0) }
    var cambiosTotales by rememberSaveable { mutableStateOf(0) }
    var historial by remember { mutableStateOf(listOf<Pair<Int, Boolean>>()) }
    var context = LocalContext.current

    fun actualizarContador(incrementar: Boolean) {
        if (cambiosTotales == 25) {
            Toast.makeText(context,"Historial lleno", Toast.LENGTH_SHORT).show()
            return
        }
        val valorAnterior = valor
        if (incrementar) {
            valor++
            aumentos++
        } else {
            valor--
            disminuciones++
        }
        historial = (listOf(Pair(valor, valor > valorAnterior)) + historial).take(25)
        maximo = historial.map { it.first }.maxOrNull() ?: 0
        minimo = historial.map { it.first }.minOrNull() ?: 0
        cambiosTotales++


    }

    fun reiniciar() {
        valor = 0
        aumentos = 0
        disminuciones = 0
        maximo = 0
        minimo = 0
        cambiosTotales = 0
        historial = emptyList()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aplicación Contador",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ControlesContador(
            valor = valor,
            alIncrementar = { actualizarContador(true) },
            alDisminuir = { actualizarContador(false) }
        )

        PanelEstadisticas(
            aumentos = aumentos,
            disminuciones = disminuciones,
            maximo = maximo,
            minimo = minimo,
            cambiosTotales = cambiosTotales
        )

        Text(
            text = "Historial",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        RegistroHistorial(historial = historial)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { reiniciar() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Reiniciar", color = Color.White)
        }
    }
}

@Composable
fun ControlesContador(
    valor: Int,
    alIncrementar: () -> Unit,
    alDisminuir: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Button(
            onClick = alDisminuir,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
        ) {
            Text("-", fontSize = 24.sp)
        }
        Text(
            text = valor.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Button(
            onClick = alIncrementar,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
        ) {
            Text("+", fontSize = 24.sp)
        }
    }
}

@Composable
fun PanelEstadisticas(
    aumentos: Int,
    disminuciones: Int,
    maximo: Int,
    minimo: Int,
    cambiosTotales: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FilaEstadistica("Total Aumentos", aumentos)
            FilaEstadistica("Total Disminuciones", disminuciones)
            FilaEstadistica("Valor Máximo", maximo)
            FilaEstadistica("Valor Mínimo", minimo)
            FilaEstadistica("Cambios Totales", cambiosTotales)
        }
    }
}

@Composable
fun FilaEstadistica(etiqueta: String, valor: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, color = Color(0xFF1A237E))
        Text(valor.toString(), color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
    }
}


@Composable
fun RegistroHistorial(historial: List<Pair<Int, Boolean>>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5), // Adjust the number of columns if needed
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(historial) { (valor, incremento) ->
            val color = when {
                incremento -> Color(0xFF81C784)
                else -> Color(0xFFE57373)
            }
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = valor.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TemaAplicacionContador(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1A237E),
            secondary = Color(0xFF4CAF50),
            tertiary = Color(0xFFE57373)
        ),
        content = content
    )
}