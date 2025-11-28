package com.example.unscramble

import MAX_NO_OF_WORDS
import SCORE_INCREASE
import allWords
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    // MutableStateFlow --> para guardar los datos
    // Metemos por parámetro nuestra data class Model() para acceder a los datos
    // _[nombreAtributo] --> para atributos privados
    // model = _model.asStateFlow --> el GETTER de score
    private val _model = MutableStateFlow(Model())
    val model = _model.asStateFlow()
    var usedWords = mutableSetOf<String>()
    var userAnswer by mutableStateOf("") // No se pone remember porque NO ES COMPOSE
    var correctAnswer = "" // MIRAR LUEGO

    // Despues de pasar por el contructor, pasa por init
    init {
        resetGame()
    }

    // Logica para coger una palabra aleatoria y mezclar sus letras
    fun generateRandomWord(): String {
        var resultado = ""
        var randomWord = ""
        do {
            randomWord = allWords.random() // Coge una palabra random del set
        } while (usedWords.contains(randomWord)) // Pon palabra random si la palabra ya se utilizó
        usedWords.add(randomWord)
        correctAnswer = randomWord

        val word = randomWord.toCharArray() // Pasarlo a array la palabra
        do {
            word.shuffle()
            resultado = String(word) // Lo volvemos a pasar a string
        } while (resultado == randomWord) // Si son iguales, sigue randomizando las letras
        return resultado
    }

    fun resetGame() {
        // Reseteamos la lista de las palabras
        usedWords.clear()
        // PARA ACTUALIZAR EL ESTADO
        // currentState --> El estado actual se le hace una copia del mismo para poder hacer cambios
        // Se traduce como "currentState se actualiza a currentState.copy"
        // En este caso es la actualización cuando se resetea el juego
        _model.update { currentState ->
            currentState.copy(
                score = 0,
                currentWord = generateRandomWord(),
                wordCount = 1,
                gameOver = false
            )
        }
    }

    fun checkAnswer(context: Context) {
        if (userAnswer.equals(correctAnswer, true)) {
            // Mensaje tipo toast
            Toast.makeText(context, "Has acertado!", Toast.LENGTH_SHORT).show()
            updateGameState(SCORE_INCREASE)
        } else {
            Toast.makeText(context, "Has fallado", Toast.LENGTH_SHORT).show()
            updateGameState(0)
        }
        userAnswer = ""
    }

    fun updateGameState(newScore: Int) {
        if (usedWords.size < MAX_NO_OF_WORDS) { // Si no son 10 palabras aun
            _model.update { currentState ->
                currentState.copy(
                    score = currentState.score + newScore,
                    currentWord = generateRandomWord(),
                    wordCount = currentState.wordCount + 1
                )
            }
        } else {
            _model.update { currentState ->
                currentState.copy(score = currentState.score + newScore, gameOver = true)
            }
        }
    }

    fun skipWord() {
        updateGameState(0)
        userAnswer = ""
    }
}


