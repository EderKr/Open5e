package com.example.open5e.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextMarkdown(text: String) {
    Text(parseMarkdown(text))
}