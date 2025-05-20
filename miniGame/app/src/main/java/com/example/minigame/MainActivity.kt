package com.example.minigame

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var secretNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberInput = findViewById<EditText>(R.id.numberInput)
        val resultText = findViewById<TextView>(R.id.resultText)
        val guessButton = findViewById<Button>(R.id.guessButton)

        generateSecretNumber()

        guessButton.setOnClickListener {
            val inputText = numberInput.text.toString()

            if (inputText.isEmpty()) {
                resultText.text = "Пожалуйста, введите число"
                return@setOnClickListener
            }

            val guess = inputText.toIntOrNull()
            if (guess == null) {
                resultText.text = "Некорректный ввод"
                return@setOnClickListener
            }

            when {
                guess < secretNumber -> resultText.text = "Слишком маленькое!"
                guess > secretNumber -> resultText.text = "Слишком большое!"
                else -> {
                    resultText.text = "Поздравляю, ты угадал!"
                    generateSecretNumber()
                }
            }

            numberInput.text.clear()
        }
    }

    private fun generateSecretNumber() {
        secretNumber = Random.nextInt(1, 101) // от 1 до 100 включительно
    }
}