package com.example.nairobihiddengems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nairobihiddengems.core.theme.NairobiHiddenGemsTheme
import com.example.nairobihiddengems.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NairobiHiddenGemsTheme {
                MainScreen()
            }
        }
    }
}
