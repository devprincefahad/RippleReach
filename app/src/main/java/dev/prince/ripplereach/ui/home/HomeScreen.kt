package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.data.ResponseData
import dev.prince.ripplereach.ui.destinations.HomeScreenDestination
import dev.prince.ripplereach.ui.destinations.PhoneAuthScreenDestination
import dev.prince.ripplereach.ui.register.RegisterViewModel

@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity

    val viewModel: HomeViewModel = hiltViewModel(activity)
    val user = viewModel.responseData?.user

    Log.d("user-data", "$user")

    if (!viewModel.isUserLoggedIn()) {
        navigator.navigate(PhoneAuthScreenDestination)
    } else {
        HomeScreenContent()
    }

    BackHandler {
        context.finish()
    }

}

@Composable
fun HomeScreenContent() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Home Screen")
    }
}
