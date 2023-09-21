package com.example.sparktech.utils

import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.getString(): String {
    return this.editText?.text.toString()
}

fun TextInputLayout.isNotEmpty(): Boolean {
    return this.editText?.text?.isNotEmpty() ?: false
}

fun TextInputLayout.setError() {
    this.requestFocus()
    this.error = "Field can't be empty"
}
enum class TextInputViews {
    username, password, password2, first_name, last_name, email
}