package dev.prince.ripplereach.ui.register

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.ui.components.SearchItemRow
import dev.prince.ripplereach.ui.destinations.ChooseWorkPlaceDestination
import dev.prince.ripplereach.ui.theme.Orange
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily
import dev.prince.ripplereach.util.SetSoftInputMode

@Destination
@Composable
fun ChooseUniversity(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current

    val activity = context as ComponentActivity

    val viewModel: RegisterViewModel = hiltViewModel(activity)

    val universityList = remember { mutableStateListOf<String>() }

    SetSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

    LaunchedEffect(Unit) {
        universityList.addAll(viewModel.getUniversities(context))
    }

    val filteredUniversities = remember(universityList, viewModel.university) {
        universityList.filter {
            it.contains(viewModel.university, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Text(
            text = "Select your university",
            color = Color.White,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rufinaFamily
            )
        )
        OutlinedTextField(
            value = viewModel.university,
            onValueChange = {
                if (it.length <= 40) {
                    viewModel.university = it
                }
            },
            placeholder = {
                Text(
                    text = "Search University",
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
            filteredUniversities.forEachIndexed { index, item ->
                SearchItemRow(
                    navigator = navigator,
                    profession = null,
                    userName = viewModel.selectedUsername,
                    phoneNumber = viewModel.phoneNumber,
                    companyName = null,
                    universityName = item
                ) { clickedUniversity ->
                    viewModel.university = clickedUniversity
                }
                if (index < filteredUniversities.size - 1) {
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
                    navigator.navigate(
                        ChooseWorkPlaceDestination()
                    )
                },
            text = "I'm a Professional",
            color = Orange,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )
    }
}