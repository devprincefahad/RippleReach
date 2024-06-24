package dev.prince.ripplereach.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.prince.ripplereach.R
import dev.prince.ripplereach.data.CompanyList
import dev.prince.ripplereach.data.LoginRequestBody
import dev.prince.ripplereach.data.RegisterRequestBody
import dev.prince.ripplereach.data.ResponseData
import dev.prince.ripplereach.data.UniversityList
import dev.prince.ripplereach.network.ApiService
import dev.prince.ripplereach.util.Resource
import dev.prince.ripplereach.util.oneShotFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "RegisterViewModel"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val api: ApiService
) : ViewModel() {

    var phoneNumber by mutableStateOf("")
    var otp by mutableStateOf("")

    var expanded by mutableStateOf(false)

    var verificationId = ""
    private var idToken = ""

    var selectedUsername by mutableStateOf("")
    var selectedOption by mutableIntStateOf(-1)

    var university by mutableStateOf("")
    var profession by mutableStateOf("")
    var companyName by mutableStateOf("")

    private val _usernames = MutableStateFlow<List<String>>(emptyList())
    val usernames: StateFlow<List<String>> = _usernames
    private val _responseData = MutableStateFlow<Resource<ResponseData>>(Resource.Loading)
    val responseData: StateFlow<Resource<ResponseData>> = _responseData

    val navigateToOtpVerification = oneShotFlow<Unit>()
    val navigateToChooseName = oneShotFlow<Unit>()
    val navigateToHome = oneShotFlow<Unit>()

    var isLoadingForOtpSend by (mutableStateOf(false))
    var isLoadingForOtpVerify by (mutableStateOf(false))

    init {
        fetchUsernames()
    }

    fun fetchUsernames() {
        viewModelScope.launch {
            _usernames.value = api.getUsernames()
        }
    }

    fun getCompaniesFromJson(context: Context): List<String> {
        val jsonString = context.resources.openRawResource(R.raw.companies).bufferedReader()
            .use { it.readText() }
        val companiesResponse: CompanyList =
            Gson().fromJson(jsonString, object : TypeToken<CompanyList>() {}.type)
        return companiesResponse.companies
    }

    fun getUniversities(context: Context): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.universities).bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val universityListType: Type = object : TypeToken<UniversityList>() {}.type
        val universityList: UniversityList = gson.fromJson(jsonFile, universityListType)
        return universityList.universities?.map { it.name } ?: emptyList()
    }

    fun registerUser() {
        val requestBody = RegisterRequestBody(
            idToken = idToken,
            phone = phoneNumber,
            username = selectedUsername,
            company = (companyName.ifEmpty { null }),
            university = (university.ifEmpty { null }),
            profession = (profession.ifEmpty { null })
        )
        Log.d(
            "api-block",
            "from viewwmodel:- ${requestBody.company} ${requestBody.university} ${requestBody.profession}"
        )

        viewModelScope.launch {
            try {
                val response = api.register(requestBody = requestBody)
                _responseData.value =  Resource.Success(response)
                navigateToHome.tryEmit(Unit)
                Log.d("api-block", "$response")
            } catch (e: Exception) {
                Toast.makeText(context, "Registration failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.d("api-block", "${e.stackTrace} ${e.message}")
            }
        }
    }

    private fun loginUser() {

        val requestBody = LoginRequestBody(
            idToken = idToken,
            phone = phoneNumber
        )

        Log.d(
            "api-block",
            "from viewwmodel:- ${requestBody.phone} ${requestBody.idToken}"
        )

        viewModelScope.launch {
            try {
                val response = api.login(requestBody = requestBody)
                _responseData.value =  Resource.Success(response)
                Log.d(
                    "api-block",
                    "response from viewwmodel:- ${response}"
                )
                navigateToHome.tryEmit(Unit)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    navigateToChooseName.tryEmit(Unit)
                } else {
                    Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.d("api-block", "${e.message}")
            }
        }
    }

    fun sendOtp(activity: ComponentActivity) {
        isLoadingForOtpSend = true
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber("+91 $phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted: Number auto verified")
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
                    verificationId = p0
                    isLoadingForOtpSend = false
                    Log.d(
                        "auth-check",
                        "storedVerificationId from viewmodel = $verificationId"
                    )

                    Log.d("auth-check", "storedVerificationId = $p0")
                    navigateToOtpVerification.tryEmit(Unit)
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp() {
        isLoadingForOtpVerify = true
        val credential = PhoneAuthProvider.getCredential(
            verificationId,
            otp
        )

        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isLoadingForOtpVerify = false
                if (task.isSuccessful) {
                    task.result.user?.getIdToken(true)?.addOnSuccessListener { result ->
                        val idToken = result.token
                        Log.d("idToken", "user id token is :- ${idToken.toString()}")
                        this.idToken = idToken.toString()
                        loginUser()
                    }
                } else {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}