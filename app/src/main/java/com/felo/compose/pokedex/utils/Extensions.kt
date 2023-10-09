package com.felo.compose.pokedex.utils

import android.app.Activity
import android.content.Context
import android.hardware.input.InputManager
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard()
{
    if(currentFocus != null)
    {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}