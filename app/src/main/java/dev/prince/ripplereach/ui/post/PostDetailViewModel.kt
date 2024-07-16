package dev.prince.ripplereach.ui.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.Comment
import dev.prince.ripplereach.data.Post
import dev.prince.ripplereach.data.PostExchangeTokenRequest
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val api: ApiService,
    private val prefs: SharedPrefHelper
) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> get() = _post

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    val userImage = prefs.response?.user?.avatar

    fun getPost(postId: Int) {
        viewModelScope.launch {
            try {
                val token = prefs.response?.auth?.token
                if (token != null) {
                    val post = api.getPostById(
                        authToken = "Bearer $token",
                        postId.toString()
                    )
                    _post.value = post
                    Log.d("PostViewModel", postId.toString())
                } else {
                    Log.e("PostViewModel", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    getPost(postId)
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun getCommentByPostId(postId: Int) {
        viewModelScope.launch {
            try {
                val token = prefs.response?.auth?.token
                if (token != null) {
                    val response = api.getComments(
                        authToken = "Bearer $token",
                        postId = postId.toString(),
                        limit = 10
                    )
                    _comments.value = response.content
                    Log.d("PostViewModel", "${comments.value} ${postId.toString()}")
                } else {
                    Log.e("PostViewModel", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    getCommentByPostId(postId)
                }
                Log.e("PostViewModel", "HTTP Exception: ${e.message}")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Exception: ${e.message}")
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