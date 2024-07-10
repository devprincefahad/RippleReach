package dev.prince.ripplereach.ui.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.CommunityDetailResponse
import dev.prince.ripplereach.data.PostExchangeTokenRequest
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val api: ApiService,
    private val pref: SharedPrefHelper
) : ViewModel() {

    private val _communityDetails = MutableStateFlow<CommunityDetailResponse?>(null)
    val communityDetails: StateFlow<CommunityDetailResponse?> = _communityDetails

    fun fetchCommunityDetails(communityId: Int) {
        viewModelScope.launch {
            try {
                val token = pref.response?.auth?.token
                if (token != null) {
                    val community = api.getPostsByCommunityId("Bearer $token", communityId)
                    _communityDetails.value = community
                    Log.d("CommunityViewModel", communityDetails.value.toString())
                } else {
                    Log.e("CommunityViewModel", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    fetchCommunityDetails(communityId)
                }
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Exception: ${e.message}")
            }
        }
    }

    private suspend fun refreshToken() {
        pref.response?.auth.let {
            val request = PostExchangeTokenRequest(it?.refreshToken ?: "", it?.username ?: "")
            val authResponse = api.exchangeToken(request)
            pref.response = pref.response?.copy(auth = authResponse)
        } ?: throw IllegalStateException("Auth is null")
    }
}