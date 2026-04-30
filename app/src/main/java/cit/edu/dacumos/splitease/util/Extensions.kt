package cit.edu.dacumos.splitease.util

import android.content.Context
import android.view.View
import android.widget.Toast
import java.util.Locale

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Double.toPhp(): String {
    return String.format(Locale.getDefault(), "₱%.2f", this)
}
