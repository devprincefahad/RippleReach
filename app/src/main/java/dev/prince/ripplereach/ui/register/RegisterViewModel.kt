package dev.prince.ripplereach.ui.register

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.R
import dev.prince.ripplereach.data.CompanyList
import dev.prince.ripplereach.data.UniversityList
import dev.prince.ripplereach.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()

    var phoneNumber by mutableStateOf("")
    var verificationId by mutableStateOf("")
    var otp by mutableStateOf("")

    var selectedUsername by mutableStateOf("")
    var selectedOption by mutableIntStateOf(-1)

    var profession by mutableStateOf("")

    var companyName  by mutableStateOf("")
    var expanded by mutableStateOf(false)

    private val _usernames = MutableStateFlow<List<String>>(emptyList())
    val usernames: StateFlow<List<String>> = _usernames

    init {
        fetchUsernames()
    }

    fun fetchUsernames() {
        viewModelScope.launch {
            _usernames.value = api.getUsernames()
        }
    }

    fun getCompanies(context: Context): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.companies).bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val companiesList = gson.fromJson(jsonFile, CompanyList::class.java)
        return companiesList.companies
    }

    fun getUniversities(context: Context): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.universities).bufferedReader()
            .use { it.readText() }
        val gson = Gson()
        val universityListType: Type = object : TypeToken<UniversityList>() {}.type
        val universityList: UniversityList = gson.fromJson(jsonFile, universityListType)
        return universityList.universities?.map { it.name } ?: emptyList()
    }
}