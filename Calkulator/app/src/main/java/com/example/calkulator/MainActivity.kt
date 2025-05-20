package com.example.calkulator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val number1 = findViewById<EditText>(R.id.number1)
        val number2 = findViewById<EditText>(R.id.number2)
        val result = findViewById<TextView>(R.id.textResult)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            calculate(number1, number2, result) { a, b -> a + b }
        }

        findViewById<Button>(R.id.btnSubtract).setOnClickListener {
            calculate(number1, number2, result) { a, b -> a - b }
        }

        findViewById<Button>(R.id.btnMultiply).setOnClickListener {
            calculate(number1, number2, result) { a, b -> a * b }
        }

        findViewById<Button>(R.id.btnDivide).setOnClickListener {
            calculate(number1, number2, result) { a, b ->
                if (b == 0.0) {
                    Double.NaN
                } else {
                    a / b
                }
            }
        }
    }

    private fun calculate(
        number1: EditText,
        number2: EditText,
        resultView: TextView,
        operation: (Double, Double) -> Double
    ) {
        val input1 = number1.text.toString()
        val input2 = number2.text.toString()

        if (input1.isBlank() || input2.isBlank()) {
            resultView.text = "Пожалуйста, введите оба числа"
            return
        }

        try {
            val a = input1.toDouble()
            val b = input2.toDouble()
            val result = operation(a, b)
            resultView.text = "Результат: $result"
        } catch (e: NumberFormatException) {
            resultView.text = "Ошибка ввода"
        }
    }
}