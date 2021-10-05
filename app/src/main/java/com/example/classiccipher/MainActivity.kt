package com.example.classiccipher

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.classiccipher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    private fun translate(key: Int, input: String, mode: String): String {
        val stringMap = mapOf(
            "a" to 0, "b" to 1, "c" to 2, "d" to 3, "e" to 4,
            "f" to 5, "g" to 6, "h" to 7, "i" to 8, "j" to 9,
            "k" to 10, "l" to 11, "m" to 12, "n" to 13, "o" to 14,
            "p" to 15, "q" to 16, "r" to 17, "s" to 18, "t" to 19,
            "u" to 20, "v" to 21, "w" to 22, "x" to 23, "y" to 24,
            "z" to 25, " " to 26
        )
        val numberMap = listOf(
            "a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y",
            "z", " "
        )
        val output = input.toCharArray()

        return try {
            val temp = input.chunked(1) { x: CharSequence -> stringMap[x.toString()] ?: error("") }
                .toMutableList()

            if (mode == "encode") {
                for (index in temp.indices) if (temp[index] != 26) temp[index] = (temp[index] + key) % 26
            } else {
                for (index in temp.indices) if (temp[index] != 26) temp[index] = (temp[index] + 26 - key) % 26
            }

            for (index in output.indices) output[index] = numberMap[temp[index]].single()

            String(output)
        } catch (e: Exception) {
            "ERROR!\nInput text cannot contain any characters other than English alphabet!"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.encodeButton.setOnClickListener(this)
        binding.clearButton.setOnClickListener(this)
        binding.decodeButton.setOnClickListener(this)
        binding.copyButton.setOnClickListener(this)

        if (savedInstanceState != null) {
            val result = savedInstanceState.getString(STATE_RESULT)
            binding.outputText.text = result
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, binding.outputText.text.toString())
    }

    override fun onClick(v: View?) {

        val key = binding.inputKey.text.toString().trim()
        val input = binding.inputText.text.toString().trim().lowercase()
        val output = binding.outputText.text.toString()

        if (v?.id == R.id.encodeButton) {

            var isEmptyFields = false

            if (key.isEmpty()) {
                isEmptyFields = true
                binding.inputKey.error = "Please insert a key!"
            }

            if (input.isEmpty()) {
                isEmptyFields = true
                binding.inputText.error = "Please insert some text!"
            }

            if (!isEmptyFields) {
                val keyNumber = key.toInt() % 26
                binding.outputText.text = translate(keyNumber, input, "encode")
            }
        }

        if (v?.id == R.id.clearButton) {
            binding.inputKey.setText("")
            binding.inputText.setText("")
            binding.outputText.text = ""
        }

        if (v?.id == R.id.decodeButton) {

            var isEmptyFields = false

            if (key.isEmpty()) {
                isEmptyFields = true
                binding.inputKey.error = "Please insert a key!"
            }

            if (input.isEmpty()) {
                isEmptyFields = true
                binding.inputText.error = "Please insert some text!"
            }

            if (!isEmptyFields) {
                val keyNumber = key.toInt() % 26
                binding.outputText.text = translate(keyNumber, input, "decode")
            }
        }

        if (v?.id == R.id.copyButton) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", output)

            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(this, "Text is successfully copied!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val about = Intent(this@MainActivity, AboutMe::class.java)
        startActivity(about)
        return super.onOptionsItemSelected(item)
    }
}