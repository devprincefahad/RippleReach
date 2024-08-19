package dev.prince.ripplereach.ui.create

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.prince.ripplereach.R
import dev.prince.ripplereach.ui.components.TextFieldSeparator
import dev.prince.ripplereach.ui.destinations.CommunitySelectorDestination
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.util.clickWithoutRipple

@Destination
@Composable
fun CreatePostScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePostViewModel = hiltViewModel(),
    resultRecipient: ResultRecipient<CommunitySelectorDestination, Int>
) {

    val context = LocalContext.current

    var communityId by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            imageUris = uris
        }

    val postState by viewModel.postState.collectAsState()
    val messages = viewModel.messages
    val community = viewModel.community.collectAsState()

    LaunchedEffect(postState) {
        postState?.let {
            navigator.navigateUp()
        }
    }

    LaunchedEffect(messages) {
        messages.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    resultRecipient.onNavResult {
        when (it) {
            is NavResult.Canceled -> {
                // Do nothing
            }

            is NavResult.Value -> {
                communityId = it.value.toString()
                viewModel.getCommunity(communityId.toLong())
                Log.d("create-post-screen", "Community ID: $communityId")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

//        Button(onClick = {
//            launcher.launch("image/*")
//        }) {
//            Text(text = "Select Images")
//        }
//        imageUris.forEach { uri ->
//            Text(text = "Selected Image: ${uri.path}")
//        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickWithoutRipple {
                    navigator.popBackStack()
                },
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back Arrow"
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Create Post",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = quickStandFamily
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (communityId.isNotBlank() && content.isNotBlank() && title.isNotBlank() && link.isNotBlank() && imageUris.isNotEmpty()) {
                        viewModel.createPost(
                            communityId = communityId.toLong(),
                            content = content,
                            title = title,
                            link = link,
                            imageUris = imageUris,
                            context = context
                        )
                    } else {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Text(
                    text = "Post",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .clickWithoutRipple {
                    navigator.navigate(CommunitySelectorDestination)
                }
                .fillMaxWidth(),
            enabled = false,
            readOnly = true,
            onValueChange = {},
            value = community.value?.name ?: viewModel.communityName,
            leadingIcon = {
//                val imageUrl =
//                    "https://ripplereach-0-0-1-snapshot.onrender.com${community.value?.imageUrl}"

                AsyncImage(
                    model = community.value?.imageUrl ?: painterResource(id = R.drawable.icon_search),
                    contentDescription = community.value?.name,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(42.dp)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.Gray
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = quickStandFamily,
                color = Color.Black
            ),
            prefix = {
                TextFieldSeparator(height = 24)
            }
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = title,
            onValueChange = {
                if (it.length <= 75) {
                    title = it
                }
            },
            placeholder = {
                Text(
                    text = "Give your post a title",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            },
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontFamily = quickStandFamily,
                color = Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                cursorColor = Color.Gray,
            ),
            supportingText = {
                Text(text = "${title.length}/75")
            }
        )

        TextField(
            value = content,
            onValueChange = { content = it },
            placeholder = {
                Text(
                    text = "Post description",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = quickStandFamily,
                color = Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                cursorColor = Color.Gray,
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { }
                        .padding(end = 12.dp),
                    painter = painterResource(R.drawable.link),
                    contentDescription = "Link icon"
                )
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            launcher.launch("image/*")
                        },
                    painter = painterResource(R.drawable.gallery),
                    contentDescription = "Gallery icon"
                )
        }
    }
}
