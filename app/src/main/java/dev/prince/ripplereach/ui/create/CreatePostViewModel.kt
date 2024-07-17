package dev.prince.ripplereach.ui.create

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.ripplereach.data.Post
import dev.prince.ripplereach.local.SharedPrefHelper
import dev.prince.ripplereach.network.ApiService
import dev.prince.ripplereach.util.oneShotFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val api: ApiService,
    private val prefs: SharedPrefHelper
) : ViewModel() {

    val authorId = prefs.response?.user?.userId
    private val _postState = MutableStateFlow<Post?>(null)
    val postState: StateFlow<Post?> = _postState
    val messages = oneShotFlow<String>()

    var communityName by mutableStateOf("Select a community")

    fun createPost(
        communityId: Long,
        content: String,
        title: String,
        link: String,
        imageUris: List<Uri>,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val authorIdRequest = authorId?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
                val communityIdRequest = communityId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val contentRequest = content.toRequestBody("text/plain".toMediaTypeOrNull())
                val titleRequest = title.toRequestBody("text/plain".toMediaTypeOrNull())
                val linkRequest = link.toRequestBody("text/plain".toMediaTypeOrNull())

                val attachments = imageUris.map { uri ->
                    val file = File(uri.path!!)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("attachments", file.name, requestFile)
                }

                if (authorIdRequest != null) {
                    val response = api.createPost(
                        authorId = authorIdRequest,
                        communityId = communityIdRequest,
                        content = contentRequest,
                        title = titleRequest,
                        attachments = attachments,
                        link = linkRequest
                    )

                    _postState.value = response
                } else {
                    messages.tryEmit("Author ID is null")
                }
            } catch (e: Exception) {
                messages.tryEmit("Exception: ${e.message}")
            }
        }
    }
}