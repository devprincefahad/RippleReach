package dev.prince.ripplereach.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import dev.prince.ripplereach.data.ResponseData
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(
    private val pref: SharedPreferences
) {

    private val RESPONSE_DATA = "response_data"
    private val KEY_ID_TOKEN = "access_token"

    var response: ResponseData?
        get() = getResponseData(RESPONSE_DATA)
        set(value) {
            saveResponseData(value)
        }

    private fun saveResponseData(value: ResponseData?) {
        val json = Gson().toJson(value)
        pref.edit {
            putString(RESPONSE_DATA, json)
        }
    }

    private fun getResponseData(key: String): ResponseData? {
        val json = pref.getString(key, null)
        return Gson().fromJson(json, ResponseData::class.java)
    }

    fun isUserDataAvailable(): Boolean {
        return pref.contains(RESPONSE_DATA)
    }

    fun saveIdToken(idToken: String) {
        pref.edit {
            putString(KEY_ID_TOKEN, idToken)
        }
    }

    fun getIdToken(): String? {
        return pref.getString(KEY_ID_TOKEN, null)
    }

}