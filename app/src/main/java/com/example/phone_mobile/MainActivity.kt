package com.example.phone_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.phone_mobile.ui.MainApp
import com.example.phone_mobile.ui.theme.PhonemobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhonemobileTheme {
                MainApp()
            }
        }
    }
}
