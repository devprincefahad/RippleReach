package dev.prince.ripplereach.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.CategoryContent
import dev.prince.ripplereach.data.Post
import dev.prince.ripplereach.data.PostExchangeTokenRequest
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: ApiService,
    private val pref: SharedPrefHelper
) : ViewModel() {

    var searchQuery by mutableStateOf("")
    private val _searchResults = MutableStateFlow<List<Post>>(emptyList())
    val searchResults: StateFlow<List<Post>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _categories = MutableStateFlow<List<CategoryContent>>(emptyList())
    val categories: StateFlow<List<CategoryContent>> = _categories

    private var searchJob: Job? = null

    init {
        fetchCategories()
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            fetchPosts()
        }
    }

    private fun fetchPosts() {
        if (searchQuery.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = pref.response?.auth?.token
                if (token != null) {
                    val response =
                        api.getPosts(
                            authToken = "Bearer $token",
                            limit = 20,
                            offset = 0,
                            sortBy = "date",
                            searchQuery = searchQuery
                        )
                    _searchResults.value = response.content
                    Log.d("api-block", searchResults.value.toString())
                } else {
                    Log.e("api-block", "Token is null")
                }
            }catch (e: HttpException){
                if (e.code() == 401) {
                    refreshToken()
                    fetchPosts()
                }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = api.getCategories(limit = 6, offset = 0)
                Log.d("fetchCategories", "Response: $response")
                _categories.value = response.content
            } catch (e: HttpException) {
                Log.d("SeachViewModel", "HTTP: ${e.message}")
            } catch (e: Exception) {
                Log.d("SeachViewModel", "Exception: ${e.message}")
                Log.d("api-block", "${e.message}")
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