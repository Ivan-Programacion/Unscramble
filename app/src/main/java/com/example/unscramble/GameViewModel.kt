package com.example.unscramble

import allWords
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel: ViewModel() {
    // MutableStateFlow --> para guardar los datos
    // Metemos por parámetro nuestra data class Model() para acceder a los datos
    // _[nombreAtributo] --> para atributos privados
    // model = _model.asStateFlow --> el GETTER de score
    private val _model = MutableStateFlow(Model())
    val model = _model.asStateFlow()

    // Logica para coger una palabra aleatoria y mezclar sus letras
    fun generateRandomWord(): String {
        var resultado = ""
        val randomWord = allWords.random() // Coge una palabra random del set
        val word = randomWord.toCharArray() // Pasarlo a array la palabra
        do {
            word.shuffle()
            resultado = String(word) // Lo volvemos a pasar a string
        } while(resultado == randomWord) // Si son iguales, sigue randomizando las letras
        return resultado
    }
}


