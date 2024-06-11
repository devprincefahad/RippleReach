package dev.prince.ripplereach.ui.register

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.ui.destinations.ChooseNameScreenDestination
import dev.prince.ripplereach.ui.theme.Orange
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily

@Destination
@Composable
fun OTPVerifyScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val activity = LocalContext.current as ComponentActivity

    val viewModel: RegisterViewModel = hiltViewModel(activity)

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
            text = "Verification code sent",
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
            text = "to +91 ${viewModel.phoneNumber}",
            color = Color.Gray,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )

        OutlinedTextField(
            value = viewModel.otp,
            onValueChange = {
                if (it.length <= 6) {
                    viewModel.otp = it
                } else {
                    Toast.makeText(context, "6 digit otp", Toast.LENGTH_SHORT).show()
                }
            },
            placeholder = {
                Text(
                    text = "Enter 6 digit OTP",
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
            )
        )

        Row {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = "Don't receive the OTP?",
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
                text = " Resend",
                color = Orange,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = quickStandFamily
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            onClick = {
//                if (viewModel.otp.isNotEmpty() && viewModel.storedVerificationId.isNotEmpty()) {
                val credential = PhoneAuthProvider.getCredential(
                    viewModel.verificationId,
                    viewModel.otp
                )

                viewModel.auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                            Log.d("auth-check", "verf otp from viewmodel ${viewModel.otp}")
                            Log.d(
                                "auth-check",
                                "verf id from viewmodel ${viewModel.verificationId}"
                            )
                            navigator.navigate(ChooseNameScreenDestination())
                        } else {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                        }
                    }
//                } else {
//                    Toast.makeText(context, "Please enter OTP", Toast.LENGTH_SHORT).show()
//                }
            }
        ) {
            Text(
                text = "Verify",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = quickStandFamily
                )
            )
        }
    }
}
