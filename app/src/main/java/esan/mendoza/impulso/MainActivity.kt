package esan.mendoza.impulso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import esan.mendoza.impulso.presentation.homeScreen.HomeScreen
import esan.mendoza.impulso.ui.theme.ImpulsoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpulsoTheme {
                HomeScreen()
            }
        }
    }
}
