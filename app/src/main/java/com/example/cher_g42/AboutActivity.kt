package com.example.cher_g42

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cher_g42.ui.theme.CheRg42Theme

class AboutActivity : ComponentActivity() {

    private val contactUsIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.author_1_email,R.string.author_2_email))
        putExtra(Intent.EXTRA_SUBJECT, "Contact Authors")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "AboutActivity.onCreate")
        setContent {
            CheRg42Theme() {
                AboutScreen(onNavigateBack = { finish() }, onContactUs = {handleContactUs()})
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "AboutActivity.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "AboutActivity.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "AboutActivity.onDestroy")
    }

    private fun handleContactUs() {
        val chooser = Intent.createChooser(contactUsIntent, "Choose Email App")
        startActivity(chooser)
    }

}