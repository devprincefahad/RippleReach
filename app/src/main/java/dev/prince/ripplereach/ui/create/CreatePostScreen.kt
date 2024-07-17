package dev.prince.ripplereach.ui.create

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.ui.destinations.SearchScreenDestination
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.util.clickWithoutRipple

@Destination
@Composable
fun CreatePostScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePostViewModel = hiltViewModel()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

//        TextField(
//            value = communityId,
//            onValueChange = { communityId = it },
//            label = { Text("Community ID") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickWithoutRipple {
                    navigator.navigate(SearchScreenDestination)
                },
            readOnly = true,
            value = viewModel.communityName,
            onValueChange = {},
            label = {
                Text(
                    text = "Card Provider",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = quickStandFamily,
                        color = Color.Gray
                    )
                )
            },
            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(
//                    expanded = viewModel.expandedProviderField
//                )
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
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = quickStandFamily,
                color = Color.Black
            )
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") }
        )
        TextField(
            value = link,
            onValueChange = { link = it },
            label = { Text("Link") }
        )
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Select Images")
        }
        imageUris.forEach { uri ->
            Text(text = "Selected Image: ${uri.path}")
        }
        Button(onClick = {
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
        }) {
            Text(text = "Create Post")
        }
    }
}
