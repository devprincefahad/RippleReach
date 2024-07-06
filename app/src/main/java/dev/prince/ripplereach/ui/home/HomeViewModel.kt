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
           try{
               val response = api.getPosts(
                   limit = 10,
                   offset = 0,
                   sortBy = "createdAt,desc"
               )
               _posts.value = response.content
           } catch (e: HttpException) {
               showSnackBarMsg("HTTP: ${e.message}")
           } catch (e: Exception) {
               showSnackBarMsg("Exception: ${e.message}")
               Log.d("api-block", "${e.message}")
           }
        }
    }

}
