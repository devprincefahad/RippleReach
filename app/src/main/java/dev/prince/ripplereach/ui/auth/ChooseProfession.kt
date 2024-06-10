package dev.prince.ripplereach.ui.auth

import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.ui.destinations.ChooseUniversityDestination
import dev.prince.ripplereach.ui.destinations.HomeScreenDestination
import dev.prince.ripplereach.ui.theme.Orange
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily
import dev.prince.ripplereach.util.SetSoftInputMode
import dev.prince.ripplereach.util.professions

@Destination
@Composable
fun ChooseProfession(
    navigator: DestinationsNavigator,
) {

    val context = LocalContext.current
    val profession = remember { mutableStateOf("") }

    SetSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

    val filteredProfessions = remember(profession.value) {
        professions.filter {
            it.contains(profession.value, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Text(
            text = "Select your profession",
            color = Color.White,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rufinaFamily
            )
        )
        OutlinedTextField(
            value = profession.value,
            onValueChange = {
                if (it.length <= 40) {
                    profession.value = it
                }
            },
            placeholder = {
                Text(
                    text = "Search Profession",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = R.drawable.icon_search
                    ),
                    tint = Color.Gray,
                    contentDescription = null
                )
            }
        )

        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            filteredProfessions.forEachIndexed { index, item ->
                SearchItemRow(navigator, item) { clickedProfession ->
                    profession.value = clickedProfession
                }
                if (index < filteredProfessions.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        thickness = 1.dp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .clickable {
                    navigator.navigate(ChooseUniversityDestination)
                },
            text = "I'm a Student",
            color = Orange,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )
    }
}

@Composable
fun SearchItemRow(
    navigator: DestinationsNavigator,
    professionItem: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onItemClick(professionItem)
                navigator.navigate(HomeScreenDestination)
            }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = professionItem,
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )
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