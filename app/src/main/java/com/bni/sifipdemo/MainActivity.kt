package com.bni.sifipdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bni.sifipdemo.navigation.BniNavGraph
import com.bni.sifipdemo.ui.theme.BniSurface
import com.bni.sifipdemo.ui.theme.BniTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BniTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BniSurface),
                ) {
                    BniNavGraph()
                }
            }
        }
    }
}
