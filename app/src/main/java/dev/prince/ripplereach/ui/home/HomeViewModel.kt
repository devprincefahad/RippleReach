package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.CategoryContent
import dev.prince.ripplereach.data.Community
import dev.prince.ripplereach.data.Post
import dev.prince.ripplereach.data.PostExchangeTokenRequest
import dev.prince.ripplereach.data.User
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import dev.prince.ripplereach.util.oneShotFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiService,
    private val prefs: SharedPrefHelper
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return prefs.isUserDataAvailable()
    }

    val responseData = prefs.response

    private val _categories = MutableStateFlow<List<CategoryContent>>(emptyList())
    val categories: StateFlow<List<CategoryContent>> = _categories

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts

    val messages = oneShotFlow<String>()

    val userId = prefs.response?.user?.userId

    init {
        fetchCategories()
        fetchAllPosts()
    }

    fun showSnackBarMsg(msg: String) {
        messages.tryEmit(msg)
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = api.getCategories(limit = 6, offset = 0)
                Log.d("fetchCategories", "Response: $response")
                _categories.value = response.content
            } catch (e: HttpException) {
                showSnackBarMsg("HTTP: ${e.message}")
            } catch (e: Exception) {
                showSnackBarMsg("Exception: ${e.message}")
                Log.d("api-block", "${e.message}")
            }
        }
    }

    private fun fetchAllPosts() {
        viewModelScope.launch {
            try {
                val token = prefs.response?.auth?.token
                if (token != null) {
                    val response = api.getPosts(
                        authToken = "Bearer $token",
                        limit = 20,
                        offset = 0,
                        sortBy = "createdAt,desc"
                    )
                    _posts.value = response.content
                    Log.d("api-block",posts.value.toString())
                } else {
                    Log.e("api-block", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    fetchAllPosts()
                }
                showSnackBarMsg("HTTP: ${e.message}")
            } catch (e: Exception) {
                showSnackBarMsg("Exception: ${e.message}")
                Log.d("api-block", "${e.message}")
            }
        }
    }

    fun upvotePost(postId: String, userId: String) {
        val token = prefs.response?.auth?.token
        viewModelScope.launch {
            try {
                if (token != null) {
                    api.upvotePost("Bearer $token", postId, userId)
                } else {
                    Log.e("api-block", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    upvotePost(postId, userId)
                }
                showSnackBarMsg("HTTP: ${e.message}")
            } catch (e: Exception) {
                showSnackBarMsg("Exception: ${e.message}")
                Log.d("api-block", "${e.message}")
            }
        }
    }

    fun deleteUpvote(postId: String, userId: String) {
        val token = prefs.response?.auth?.token
        viewModelScope.launch {
            try {
                if (token != null) {
                    api.deleteUpvote("Bearer $token", postId, userId)
                } else {
                    Log.e("api-block", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    deleteUpvote(postId, userId)
                }
                showSnackBarMsg("HTTP: ${e.message}")
            } catch (e: Exception) {
                showSnackBarMsg("Exception: ${e.message}")
                Log.d("api-block", "${e.message}")
            }
        }
    }

    private suspend fun refreshToken() {
        prefs.response?.auth.let {
            val request = PostExchangeTokenRequest(it?.refreshToken ?: "", it?.username ?: "")
            val authResponse = api.exchangeToken(request)
            prefs.response = prefs.response?.copy(auth = authResponse)
        } ?: throw IllegalStateException("Auth is null")
    }
}
