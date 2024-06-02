package dev.prince.ripplereach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import dev.prince.ripplereach.ui.PhoneAuthScreen
import dev.prince.ripplereach.ui.theme.RippleReachTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RippleReachTheme {
                PhoneAuthScreen()
            }
        }
    }
}
