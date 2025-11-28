package com.example.unscramble

data class Model(
    val score: Int = 0,
    val currentWord: String = "",
    val wordCount: Int = 1,
    val gameOver: Boolean = false
)