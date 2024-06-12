package dev.prince.ripplereach.ui.register

import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.ui.destinations.ChooseProfessionDestination
import dev.prince.ripplereach.ui.destinations.ChooseUniversityDestination
import dev.prince.ripplereach.ui.theme.Orange
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily
import dev.prince.ripplereach.util.SetSoftInputMode

@Destination
@Composable
fun ChooseWorkPlace(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current as Activity

    val activity = LocalContext.current as ComponentActivity

    val viewModel: RegisterViewModel = hiltViewModel(activity)

    val density = LocalDensity.current

    val screenHeightPx = with(density) { context.resources.displayMetrics.heightPixels.toDp() }

    SetSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

    val companies = remember { mutableStateListOf<String>() }

    val filteredCompanies = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        companies.addAll(viewModel.getCompaniesFromJson(context))
    }

    LaunchedEffect(viewModel.companyName) {
        filteredCompanies.clear()
        filteredCompanies.addAll(
            companies.filter {
                it.contains(viewModel.companyName, ignoreCase = true)
            }
        )
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {

        Text(
            modifier = Modifier
                .padding(end = 36.dp),
            text = "Where do you work?",
            color = Color.White,
            style = TextStyle(
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rufinaFamily
            )
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = //"Get access to a private community with your co-workers, and more.",

            buildAnnotatedString {
                append("Get access to a ")
                pushStyle(SpanStyle(color = Orange))
                append("private community")
                pop()
                append(" with your co-workers, and more.")
            },
            color = Color.Gray,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = "(This cannot be changed for 60 days)",
            color = Color.DarkGray,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )

//        Image(
//            alignment = Alignment.Center,
//            modifier = Modifier
//                .padding(16.dp)
//                .size(120.dp)
//                .align(Alignment.CenterHorizontally),
//            painter = painterResource(R.drawable.briefcase),
//            contentDescription = null,
//        )

        SearchCompanies(
            filteredCompanies = filteredCompanies,
            screenHeightPx = screenHeightPx,
            activity = activity
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navigator.navigate(
                            ChooseUniversityDestination()
                        )
                    },
                text = "I'm a Student",
                color = Orange,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = quickStandFamily
                )
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                onClick = {
                    if (viewModel.companyName.isEmpty()) {
                        Toast.makeText(context, "Please select a company", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        navigator.navigate(
                            ChooseProfessionDestination()
                        )
                    }
                },
            ) {
                Text(
                    text = "Continue",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = quickStandFamily
                    )
                )
            }
        }

    }
    BackHandler {
        // expanded.value = false
    }
}

@Composable
fun SearchCompanies(
    filteredCompanies: List<String>,
    screenHeightPx: Dp,
    activity: ComponentActivity,
) {

    val viewModel: RegisterViewModel = hiltViewModel(activity)

    OutlinedTextField(
        value = viewModel.companyName,
        onValueChange = {
            if (it.length <= 40) {
                viewModel.companyName = it
                viewModel.expanded = true
            }
        },
        placeholder = {
            Text(
                text = "Type your company name",
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
            cursorColor = Color.Gray,
            focusedLeadingIconColor = Color.White
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_work
                ),
                tint = Color.Gray,
                contentDescription = null
            )
        }
    )

    if (viewModel.expanded && filteredCompanies.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxWidth()
                .heightIn(0.dp, screenHeightPx * 0.4f)
        ) {
            items(filteredCompanies) { company ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.companyName = company
                        viewModel.expanded = false
                    },
                    text = {
                        Text(
                            text = company,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = quickStandFamily
                            )
                        )
                    },
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
            }
        }
    } else {
        viewModel.expanded = false
    }
}

@Composable
fun TextFieldSeparator(
    height: Int
) {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .height(height.dp)
            .width(1.dp)
            .background(color = Color.LightGray),
        contentAlignment = Alignment.Center
    ) {}
}