package com.example.unscramble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unscramble.ui.theme.Typography
import com.example.unscramble.ui.theme.UnscrambleTheme

class MainActivity : ComponentActivity() {
    // Nuestro conrolador que pasara los datos del modelo
    val controller: GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnscrambleTheme {
                Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                    UnscrambleView(
                        innerPadding,
                        controller
                    )
                }
            }
        }
    }
}

@Composable
fun UnscrambleView(
    paddingValues: PaddingValues = PaddingValues(),
    controller: GameViewModel = GameViewModel()
) {
    // Pido al controlador que me de el modelo y lo recuerdo como un estado (remember)
    // Se hace con todos los composables
    val getModel by controller.model.collectAsState()
    Column(
        Modifier
            .padding(paddingValues)
            .padding(all = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Unscramble", style = Typography.titleLarge)
        Spacer(Modifier.padding(16.dp)) // para generar un espacio
        // elevation --> Sombreado de la card
        Card(elevation = CardDefaults.cardElevation(5.dp)) { MyCard(controller) }
        Spacer(Modifier.padding(16.dp))
        // stringResource --> Para tener en cuenta el lenguaje.
        Button(
            {},
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(R.string.submit_button)) }
        // Outlinebutton --> boton blanco
        OutlinedButton(
            {},
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(R.string.skip_button)) }
        Spacer(Modifier.padding(16.dp))
        Card {
            Text(
                stringResource(R.string.score) + getModel.score, // Llamo al score
                Modifier.padding(8.dp),
                style = Typography.headlineMedium
            )
        }
    }
}

@Composable
fun MyCard(controller: GameViewModel) {
    val getModel by controller.model.collectAsState()
    Column(
        Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "0/10",
            Modifier
                .clip(shapes.medium) // Para redondear esquinas y darle el tamaño que tu quieras
                .background(colorScheme.surfaceTint) // colorSchene --> Hay más colores
                .padding(10.dp, 4.dp)
                .align(Alignment.End), // Sustituye la alinación general para un elemento
            style = Typography.titleMedium,
            color = Color.White
        )
        // getModel.currentWord --> SE CAMBIARÁ MÁS ADELANTE
        Text(getModel.currentWord, style = Typography.displayMedium)
        Text(stringResource(R.string.instruction), style = Typography.titleMedium)
        // label --> es un placeholder que se sube arriba
        OutlinedTextField(
            "",
            {},
            label = { Text("Enter your word") },
            singleLine = true,
            // TextFieldDefaults.colors --> Para el fondo del textfield
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnscrambleTheme {
        UnscrambleView()
    }
}