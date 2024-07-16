package dev.prince.ripplereach.ui.post

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.data.Comment
import dev.prince.ripplereach.ui.home.PostItem
import dev.prince.ripplereach.ui.theme.quickStandFamily

@Destination
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    postId: Int
) {

    val post = viewModel.post.collectAsState()
    val comments = viewModel.comments.collectAsState()
    val commentText = remember { mutableStateOf("") }

    Log.d("post-id", "post id is : $postId")

    LaunchedEffect(postId) {
        viewModel.getPost(
            postId = postId
        )
    }

    LaunchedEffect(postId) {
        viewModel.getCommentByPostId(
            postId = postId
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            post.value?.let {
                item {
                    PostItem(
                        post = it,
                        navigator = navigator,
                        truncateContent = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(comments.value) { comment ->
                CommentItem(comment)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val userAvatarUrl =
                "https://ripplereach-0-0-1-snapshot.onrender.com${viewModel.userImage}"

            AsyncImage(
                model = userAvatarUrl,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(28.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = commentText.value,
                onValueChange = { commentText.value = it },
                label = {
                    Text(
                        fontSize = 12.sp,
                        text = "Add a comment"
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .weight(1f)
                    .heightIn(min = 56.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(
                onClick = {
                    if (comments.value.isNotEmpty()){

                    }
                    commentText.value = ""
                }
            ) {
                Text("Post")
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Row {

            val imageUrl =
                "https://ripplereach-0-0-1-snapshot.onrender.com${comment.author.avatar}"

            AsyncImage(
                model = imageUrl,
                contentDescription = comment.author.username,
                modifier = Modifier
                    .size(28.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier.padding(horizontal = 6.dp)
            ) {
                Text(
                    text = comment.author.username,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = comment.content,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )
            }
        }
    }
}