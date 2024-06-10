package dev.prince.ripplereach.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun HomeScreen() {
    Text(
        text = "Home screen",
        fontSize = 26.sp
    )
}