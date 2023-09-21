package com.example.sparktech.utils

import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.getString(): String {
    return this.editText?.text.toString()
}

fun TextInputLayout.isNotEmpty(): Boolean {
    return this.editText?.text?.isNotEmpty() ?: false
}
