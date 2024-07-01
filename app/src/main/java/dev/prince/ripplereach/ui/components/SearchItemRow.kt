package dev.prince.ripplereach.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.data.ResponseData
import dev.prince.ripplereach.ui.destinations.HomeScreenDestination
import dev.prince.ripplereach.ui.register.RegisterViewModel
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.util.Resource

@Composable
fun SearchItemRow(
    navigator: DestinationsNavigator,
    profession: String?,
    universityName: String?,
    onItemClick: () -> Unit
) {

    val activity = LocalContext.current as ComponentActivity

    val viewModel: RegisterViewModel = hiltViewModel(activity)

    LaunchedEffect(Unit) {
        viewModel.navigateToHome.collect {
            navigator.navigate(HomeScreenDestination)
        }
    }

    Row(
        modifier = Modifier
            .clickable {
                onItemClick()
                viewModel.registerUser()
            }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (profession ?: universityName)?.let {
            Text(
                text = it,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = quickStandFamily
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(14.dp),
            painter = painterResource(
                R.drawable.ic_arrow_right
            ),
            tint = Color.White,
            contentDescription = null
        )
    }
}