package dev.prince.ripplereach.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.network.ApiService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

}
