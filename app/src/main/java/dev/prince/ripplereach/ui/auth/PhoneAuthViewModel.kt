package dev.prince.ripplereach.ui.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor() : ViewModel() {

//    var phoneNumberState by mutableStateOf("")
//    var codeSent by mutableStateOf(false)
//    var phoneNumber by mutableStateOf("")
//    var userId by mutableStateOf("")
//    var otp by mutableStateOf("")
//    var storedVerificationId by mutableStateOf("")

    val auth = FirebaseAuth.getInstance()

//    fun startPhoneNumberVerification(
//        context: Context,
//        activity: ComponentActivity,
//        phoneNumber: String,
//        auth: FirebaseAuth
//    ) {
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(activity)
//            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    Log.d("auth-check","onVerificationCompleted ${credential.smsCode} ${credential.signInMethod}")
//                    // This callback will be invoked in two situations:
//                    // 1 - Instant verification. In some cases the phone number can be instantly
//                    //     verified without needing to send or enter a verification code.
//                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                    //     detect the incoming verification SMS and perform verification without
//                    //     user action.
//                    signInWithPhoneAuthCredential(context, auth, credential)
//                }
//
//                override fun onVerificationFailed(e: FirebaseException) {
//                    when (e) {
//                        is FirebaseAuthInvalidCredentialsException -> {
//                            // Invalid request
//                            Log.d("auth-check","onVerificationFailed")
//                        }
//
//                        is FirebaseTooManyRequestsException -> {
//                            // The SMS quota for the project has been exceeded
//                        }
//
//                        else -> {
//                            // Handle other errors
//                        }
//                    }
//                }
//
//                override fun onCodeSent(
//                    verificationId: String,
//                    token: PhoneAuthProvider.ForceResendingToken,
//                ) {
//                    Log.d("auth-check","onCodeSent $verificationId $token")
//                    // The SMS verification code has been sent to the provided phone number, we
//                    // now need to ask the user to enter the code and then construct a credential
//                    // by combining the code with a verification ID.
//                    Toast.makeText(activity, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
//                    codeSent = true
//
////                // Save verification ID and resending token so we can use them later
//                  storedVerificationId = verificationId
////                resendToken = token
//                }
//            })
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    fun signInWithPhoneAuthCredential(
//        context: Context,
//        auth: FirebaseAuth,
//        credential: PhoneAuthCredential
//    ) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d("auth-check","task success")
//                    // Sign in success, update UI with the signed-in user's information
//                    val user = task.result.user
//                    phoneNumber = user?.phoneNumber.toString()
//                    userId = user?.uid.toString()
//                } else {
//                    Log.d("auth-check","task fail")
//                    // Sign in failed, display a message to the user.
//                }
//            }
//    }

}
