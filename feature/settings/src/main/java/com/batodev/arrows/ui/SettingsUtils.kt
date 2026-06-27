package com.batodev.arrows.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import dev.andrax.arrows.core.resources.R

object SettingsUtils {
    fun launchBrowser(context: Context, url: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        } catch (_: Exception) {
            val error = context.getString(R.string.error_could_not_open_browser)
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
}
