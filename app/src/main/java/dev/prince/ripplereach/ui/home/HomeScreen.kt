package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import dev.prince.ripplereach.data.ResponseData
import dev.prince.ripplereach.ui.register.RegisterViewModel

@Destination
@Composable
fun HomeScreen(
//    professionItem: String,
//    userName: String,
//    phoneNumber: String,
//    idToken: String,
//    companyName: String,
//    universityName: String,
//    responseData: ResponseData
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity

    val viewModel: HomeViewModel = hiltViewModel(activity)

//    val company = companyName.ifEmpty { null }
//    val university = universityName.ifEmpty { null }
//
//    Column {
//
//        Text(
//            text = "Home screen",
//            fontSize = 26.sp
//        )
//
//        Log.d("data-output","$professionItem $userName $phoneNumber $idToken $company $university")

//    }


    BackHandler {
        (context as ComponentActivity).finish()
    }

}