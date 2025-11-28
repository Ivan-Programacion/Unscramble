package com.example.unscramble

import MAX_NO_OF_WORDS
import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    viewModel: GameViewModel = GameViewModel()
) {
    // Pido al controlador que me de el modelo y lo recuerdo como un estado (remember)
    // Se hace con todos los composables
    val getModel by viewModel.model.collectAsState()
    // Necesitamos el cotexto para comprobar si esta correcta la palabra
    val context = LocalContext.current
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
        Card(elevation = CardDefaults.cardElevation(5.dp)) { MyCard(viewModel) }
        Spacer(Modifier.padding(16.dp))
        // stringResource --> Para tener en cuenta el lenguaje.
        Button(
            { viewModel.checkAnswer(context) },
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(R.string.submit_button)) }
        // Outlinebutton --> boton blanco
        OutlinedButton(
            { viewModel.skipWord() },
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
        if (getModel.gameOver) {
            GameOverDialog(viewModel)
        }
    }
}

@Composable
fun MyCard(viewModel: GameViewModel) {
    val getModel by viewModel.model.collectAsState()
    Column(
        Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "${getModel.wordCount}/$MAX_NO_OF_WORDS",
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
            viewModel.userAnswer,
            { viewModel.userAnswer = it },
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

@SuppressLint("ContextCastToActivity")
@Composable
fun GameOverDialog(viewModel: GameViewModel = GameViewModel()) {
    val getModel = viewModel.model.collectAsState()
    val thisActivity = LocalContext.current as Activity // Para referirte a la actividad
    AlertDialog(
        onDismissRequest = {},
        title = { Text("GAME OVER!") },
        text = { Text("You scored: ${getModel.value.score}") },
        confirmButton = { TextButton({viewModel.resetGame()}) { Text("Play again!") } },
        // thisActivity.finish() --> Como system.exit
        dismissButton = { TextButton({thisActivity.finish()}) { Text("Exit") } }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnscrambleTheme {
        UnscrambleView()
    }
}