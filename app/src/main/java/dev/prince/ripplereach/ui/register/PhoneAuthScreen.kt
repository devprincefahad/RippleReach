package dev.prince.ripplereach.ui.register

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.ui.destinations.OTPVerifyScreenDestination
import dev.prince.ripplereach.ui.theme.Orange
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily
import dev.prince.ripplereach.util.LocalSnackbar

@Destination
@Composable
fun PhoneAuthScreen(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: RegisterViewModel = hiltViewModel(activity)
    val snackBar = LocalSnackbar.current

    LaunchedEffect(Unit) {
        viewModel.messages.collect {
            snackBar(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToOtpVerification.collect {
            navigator.navigate(OTPVerifyScreenDestination)
        }
    }

    Column(
        modifier = Modifier
            .imePadding()
            .background(Color.Black)
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        val focusRequester = remember { FocusRequester() }

        Text(
            modifier = Modifier
                .padding(end = 36.dp),
            text = "Get Started with your phone number",
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
            text = "You will receive an OTP for verification...",
            color = Color.Gray,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )

        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = {
                if (it.length <= 10) {
                    viewModel.phoneNumber = it
                } else {
                    viewModel.showSnackBarMsg("10 digit num")
                }
            },
            placeholder = {
                Text(
                    text = "Phone Number",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(top = 14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.Gray
            ),
            prefix = {
                Text(
                    text = "+ 91 ",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                onClick = {
                    viewModel.sendOtp(activity)
                }
            ) {
                AnimatedContent(viewModel.isLoadingForOtpSend, label = "") {
                    if (it) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Request OTP",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = quickStandFamily
                            )
                        )
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(bottom = 14.dp),
                text = buildAnnotatedString {
                    append("By entering your number, you're agreeing to our ")
                    pushStyle(SpanStyle(color = Orange))
                    append("terms of service")
                    pop()
                    append(" & ")
                    pushStyle(SpanStyle(color = Orange))
                    append("privacy policy")
                    pop()
                },
                color = Color.Gray,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = quickStandFamily
                )
            )
        }
    }
}
