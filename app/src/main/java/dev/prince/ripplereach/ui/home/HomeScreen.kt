package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import dev.prince.ripplereach.ui.register.RegisterViewModel

@Destination
@Composable
fun HomeScreen(
    professionItem: String,
    userName: String,
    phoneNumber: String,
    verificationId: String,
    companyName: String,
    universityName: String,
) {

    val activity = LocalContext.current as ComponentActivity

    val viewModel: HomeViewModel = hiltViewModel(activity)

    Column {

        Text(
            text = "Home screen",
            fontSize = 26.sp
        )

        Log.d("data-output","$professionItem $userName $phoneNumber $verificationId $companyName $universityName")

    }

}