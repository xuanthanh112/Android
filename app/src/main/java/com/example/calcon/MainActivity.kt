package com.example.calcon

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var expressionDisplay: TextView
    private var firstNumber: Int = 0
    private var secondNumber: Int = 0
    private var operator: String? = null
    private var isOperatorPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_linear)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.textView) // Result display
        expressionDisplay = findViewById(R.id.expressionTextView) // Expression display

        // Number buttons
        val buttons = listOf(
            findViewById<Button>(R.id.button23), // 0
            findViewById<Button>(R.id.button18), // 1
            findViewById<Button>(R.id.button19), // 2
            findViewById<Button>(R.id.button20), // 3
            findViewById<Button>(R.id.button14), // 4
            findViewById<Button>(R.id.button15), // 5
            findViewById<Button>(R.id.button16), // 6
            findViewById<Button>(R.id.button10), // 7
            findViewById<Button>(R.id.button11), // 8
            findViewById<Button>(R.id.button12)  // 9
        )

        // Assign click listeners for number buttons
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onNumberClick(index)
            }
        }

        // Operator buttons
        findViewById<Button>(R.id.button9).setOnClickListener { onOperatorClick("/") } // Division
        findViewById<Button>(R.id.button13).setOnClickListener { onOperatorClick("*") } // Multiplication
        findViewById<Button>(R.id.button17).setOnClickListener { onOperatorClick("-") } // Subtraction
        findViewById<Button>(R.id.button21).setOnClickListener { onOperatorClick("+") } // Addition
        findViewById<Button>(R.id.button25).setOnClickListener { onEqualClick() } // Equal

        // Special buttons
        findViewById<Button>(R.id.button6).setOnClickListener { clearEverything() } // CE
        findViewById<Button>(R.id.button7).setOnClickListener { clear() } // C
        findViewById<Button>(R.id.button8).setOnClickListener { backspace() } // BS
    }

    private fun onNumberClick(number: Int) {
        val currentText = display.text.toString()

        val newText = if (currentText == "0" || isOperatorPressed) {
            number.toString()
        } else {
            currentText + number.toString()
        }

        display.text = newText
        expressionDisplay.text = if (expressionDisplay.text.toString() == "0") {
            number.toString()
        } else {
            expressionDisplay.text.toString() + number.toString()
        }

        isOperatorPressed = false
    }

    private fun onOperatorClick(op: String) {
        if (expressionDisplay.text.toString().endsWith("=")) {
            expressionDisplay.text = display.text.toString()
        }

        firstNumber = display.text.toString().toInt()
        operator = op
        isOperatorPressed = true

        expressionDisplay.text = "${display.text} $operator "
    }

    private fun onEqualClick() {
        secondNumber = display.text.toString().toInt()
        val result: Double? = when (operator) {
            "+" -> (firstNumber + secondNumber).toDouble()
            "-" -> (firstNumber - secondNumber).toDouble()
            "*" -> (firstNumber * secondNumber).toDouble()
            "/" -> if (secondNumber != 0) firstNumber.toDouble() / secondNumber.toDouble() else null
            else -> null
        }

        display.text = when {
            result == null -> "Error"
            operator == "/" -> result.toString()
            else -> result?.toInt().toString()
        }

        expressionDisplay.text = "${display.text}"
        isOperatorPressed = false
    }



    private fun clearEverything() {
        display.text = "0"
        expressionDisplay.text = "0"
        firstNumber = 0
        secondNumber = 0
        operator = null
    }

    private fun clear() {
        display.text = "0"
    }

    private fun backspace() {
        val currentText = display.text.toString()
        if (currentText.isNotEmpty()) {
            display.text = if (currentText.length == 1) "0" else currentText.dropLast(1)
        }
    }
}
