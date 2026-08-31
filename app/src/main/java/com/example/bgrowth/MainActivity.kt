package com.example.bgrowth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bgrowth.data.navigation.AppNavGraph
import com.example.bgrowth.ui.theme.BGrothTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            BGrothTheme {
                AppNavGraph()
            }
        }
    }
}
