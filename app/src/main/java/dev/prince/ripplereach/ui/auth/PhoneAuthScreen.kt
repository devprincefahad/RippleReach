package dev.prince.ripplereach.ui.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.ui.auth.destinations.OTPVerifyScreenDestination
import dev.prince.ripplereach.ui.theme.Orange
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily
import java.util.concurrent.TimeUnit

@Destination(start = true)
@Composable
fun PhoneAuthScreen(
    navigator: DestinationsNavigator,
    viewModel: PhoneAuthViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val phoneNumber = remember{ mutableStateOf("") }
    val verificationId = remember{ mutableStateOf("") }

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
            value = phoneNumber.value,
            onValueChange = {
                if (it.length <= 10) {
                    phoneNumber.value = it
                }else{
                    Toast.makeText(context,"10 digit num",Toast.LENGTH_SHORT).show()
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
                        val options = PhoneAuthOptions.newBuilder(viewModel.auth)
                            .setPhoneNumber("+91 ${phoneNumber.value}")
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(activity)
                            .setCallbacks(object :
                                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                                }

                                override fun onVerificationFailed(p0: FirebaseException) {
                                    Toast.makeText(context, "please try again", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                override fun onCodeSent(
                                    p0: String,
                                    p1: PhoneAuthProvider.ForceResendingToken
                                ) {
                                    super.onCodeSent(p0, p1)
                                    verificationId.value = p0
                                    Log.d(
                                        "auth-check",
                                        "storedVerificationId from viewmodel = ${verificationId.value }"
                                    )

                                    Log.d("auth-check", "storedVerificationId = $p0")
                                    navigator.navigate(
                                        OTPVerifyScreenDestination(
                                            phoneNumber.value,
                                            verificationId.value
                                        )
                                    )
                                }

                            }).build()
                        PhoneAuthProvider.verifyPhoneNumber(options)
//                        viewModel.startPhoneNumberVerification(
//                            context,
//                            context as ComponentActivity,
//                            phoneNumber,
//                            auth
//                        )
////                        if (viewModel.codeSent) {
//                            navigator.navigate(
//                                OTPVerifyScreenDestination(
//                                    viewModel.phoneNumber,
//                                    viewModel.userId,
//                                    viewModel.codeSent
//                                )
//                            )
////                        }
                }
            ) {
                Text(
                    text = "Request OTP",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = quickStandFamily
                    )
                )
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
