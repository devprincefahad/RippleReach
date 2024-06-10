package dev.prince.ripplereach.ui.auth

import android.content.Context
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
class PhoneAuthViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {

    private val _usernames = MutableStateFlow<List<String>>(emptyList())
    val usernames: StateFlow<List<String>> = _usernames

    val auth = FirebaseAuth.getInstance()

    init {
        //fetchUsernames()
    }


    fun fetchUsernames() {
        viewModelScope.launch {
            _usernames.value = api.getUsernames()
        }
    }

    fun getCompanies(context: Context): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.companies).bufferedReader().use { it.readText() }
        val gson = Gson()
        val companiesList = gson.fromJson(jsonFile, CompanyList::class.java)
        return companiesList.companies
    }

    fun getUniversities(context: Context): List<String> {
        val jsonFile = context.resources.openRawResource(R.raw.universities).bufferedReader().use { it.readText() }
        val gson = Gson()
        val universityListType: Type = object : TypeToken<UniversityList>() {}.type
        val universityList: UniversityList = gson.fromJson(jsonFile, universityListType)
        return universityList.universities?.map { it.name } ?: emptyList()
    }
}
