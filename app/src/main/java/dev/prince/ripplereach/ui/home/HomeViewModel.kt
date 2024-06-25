package dev.prince.ripplereach.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.User
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import dev.prince.ripplereach.util.oneShotFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiService,
    private val prefs: SharedPrefHelper
) : ViewModel() {

    val isUserLoggedIn = prefs.isUserDataAvailable()
    val responseData = prefs.response
}
