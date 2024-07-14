package dev.prince.ripplereach.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val api: ApiService,
    private val prefs: SharedPrefHelper
) : ViewModel() {

}