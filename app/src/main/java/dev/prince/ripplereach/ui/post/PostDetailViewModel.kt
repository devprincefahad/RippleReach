package dev.prince.ripplereach.ui.post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.Comment
import dev.prince.ripplereach.data.CommentRequest
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

    val user = prefs.response?.user

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

    fun postComment(content: String, postId: Int) {
        val userId = prefs.response?.user?.userId
        viewModelScope.launch {
            try {
                val token = prefs.response?.auth?.token
                if (token != null) {
                    val requestBody = CommentRequest(content, postId, userId!!.toInt())
                    val response = api.postComment(
                        authToken = "Bearer $token",
                        commentRequest = requestBody
                    )
                    Log.d("PostViewModel", response.toString())
                } else {
                    Log.e("PostViewModel", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    postComment(content, postId)
                }
                Log.e("PostViewModel", "HTTP Exception: ${e.message}")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            val token = prefs.response?.auth?.token
            try {
                if (token != null) {
                    api.deleteComment(
                        authToken = "Bearer $token",
                        commentId = commentId
                    )
                    _comments.value = _comments.value.filter { it.id != commentId }
                    Log.d("PostViewModel", "comment deleted")
                } else {
                    Log.e("PostViewModel", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    deleteComment(commentId)
                }
                Log.e("PostViewModel", "HTTP Exception: ${e.message}")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun updateComment(newComment: String, commentId: Int) {
        viewModelScope.launch {
            val token = prefs.response?.auth?.token
            try {
                val trimmedText = newComment.trim()
                if (token != null) {
                    val updatedComment = api.updateComment(
                        authToken = "Bearer $token",
                        commentId,
                        trimmedText
                    )
                    _comments.value = _comments.value.map {
                        (if (it.id == commentId) updatedComment else it) as Comment
                    }
                    Log.d("PostViewModel", "Comment updated")
                } else {
                    Log.e("PostViewModel", "Token is null")
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    refreshToken()
                    updateComment(newComment, commentId)
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