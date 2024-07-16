package dev.prince.ripplereach.ui.post

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
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

    val editing = remember { mutableStateOf(false) }

    val commentIdBeingEdited = remember { mutableStateOf(-1) }

    LaunchedEffect(postId) {
        viewModel.getPost(postId)
        viewModel.getCommentByPostId(postId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            // Display post item
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

            // Display comments
            items(comments.value) { comment ->
                CommentItem(
                    comment = comment,
                    viewModel = viewModel,
                    onEditComment = {
                        commentIdBeingEdited.value = comment.id
                        commentText.value = comment.content
                        editing.value = true
                    },
                    onDeleteComment = {
                        viewModel.deleteComment(it)
                    }
                )
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
                "https://ripplereach-0-0-1-snapshot.onrender.com${viewModel.user?.avatar}"

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
                    if (editing.value && commentIdBeingEdited.value != -1) {
                        if (commentText.value.isNotBlank()) {
                            viewModel.updateComment(commentText.value, commentIdBeingEdited.value)
                            editing.value = false
                            commentIdBeingEdited.value = -1
                        }
                    } else {
                        if (commentText.value.isNotBlank()) {
                            viewModel.postComment(commentText.value, postId)
                        }
                    }
                    commentText.value = ""
                }
            ) {
                Text(
                    text = if (editing.value) "Update" else "Post"
                )
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    viewModel: PostDetailViewModel,
    onEditComment: () -> Unit,
    onDeleteComment: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Spacer(modifier = Modifier.width(8.dp))

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

            Spacer(modifier = Modifier.weight(1f))

            if (viewModel.user?.username == comment.author.username) {
                Box {
                    IconButton(
                        modifier = Modifier
                            .size(26.dp)
                            .padding(end = 4.dp),
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            painter = painterResource(R.drawable.icon_more),
                            contentDescription = "Options Icon"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = quickStandFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            },
                            onClick = {
                                onEditComment()
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.icon_edit),
                                    contentDescription = "Edit Icon"
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = quickStandFamily,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            },
                            onClick = {
                                onDeleteComment(comment.id)
                                expanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(R.drawable.icon_delete),
                                    contentDescription = "Delete Icon"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}