package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.data.CategoryContent
import dev.prince.ripplereach.data.Community
import dev.prince.ripplereach.data.Post
import dev.prince.ripplereach.ui.destinations.CommunityScreenDestination
import dev.prince.ripplereach.ui.destinations.PhoneAuthScreenDestination
import dev.prince.ripplereach.ui.destinations.PostDetailScreenDestination
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.ui.theme.rufinaFamily
import dev.prince.ripplereach.util.clickWithoutRipple

@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity

    val viewModel: HomeViewModel = hiltViewModel(activity)
    val user = viewModel.responseData?.user

    Log.d("user-data", "$user")

    if (!viewModel.isUserLoggedIn()) {
        navigator.navigate(PhoneAuthScreenDestination)
    } else {
        HomeScreenContent(navigator)
    }

    BackHandler {
        context.finish()
    }

}

@Composable
fun HomeScreenContent(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val posts by viewModel.posts.collectAsState()
    val categories by viewModel.categories.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            text = "Ripple Reach",
            color = Color.Black,
            style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = rufinaFamily
            )
        )

        CategoriesList(navigator, categories)

        PostList(posts = posts, navigator = navigator)

    }
}

@Composable
fun PostItem(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    post: Post,
    truncateContent: Boolean
) {
//    val upvotedPosts by viewModel.upvotedPosts.collectAsState()
//    val isUpvoted = upvotedPosts.contains(post.id.toString())
    var upvotes by remember { mutableIntStateOf(post.totalUpvotes) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickWithoutRipple {
                navigator.navigate(PostDetailScreenDestination(post.id))
            }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl =
                "https://ripplereach-0-0-1-snapshot.onrender.com${post.author.avatar}"

            AsyncImage(
                model = imageUrl,
                contentDescription = post.author.username,
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = post.author.username,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quickStandFamily
                    )
                )

                Text(
                    text = post.postCommunity.name,
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = quickStandFamily
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = post.title,
            color = Color.Black,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (truncateContent) post.content.take(150) + "..." else post.content,
            color = Color.Black,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = quickStandFamily
            ),
            maxLines = if (truncateContent) 4 else Int.MAX_VALUE,
            overflow = if (truncateContent) TextOverflow.Ellipsis else TextOverflow.Clip,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (!post.upvotedByUser) {
                            viewModel.upvotePost(post.id.toString(), viewModel.userId.toString())
                        } else {
                            viewModel.deleteUpvote(post.id.toString(), viewModel.userId.toString())
                        }
//                        if (isUpvoted) {
//                            viewModel.deleteUpvote(post.id.toString(), viewModel.userId.toString())
//                            upvotes -= 1
//                        } else {
//                            viewModel.upvotePost(post.id.toString(), viewModel.userId.toString())
//                            upvotes += 1
//                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upvote),
                        contentDescription = "Upvote",
                        tint = if (post.upvotedByUser) Color.Blue else Color.Gray
                    )
                }
                Text(
                    text = upvotes.toString(),
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = quickStandFamily
                    )
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navigator.navigate(PostDetailScreenDestination(post.id))
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment),
                        contentDescription = "Comments",
                        tint = Color.Gray
                    )
                }
                Text(
                    text = post.totalComments.toString(),
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = quickStandFamily
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "Share",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PostList(posts: List<Post>, navigator: DestinationsNavigator) {
    LazyColumn {
        items(posts) { post ->
            PostItem(post = post, navigator = navigator, truncateContent = true)
        }
    }
}

@Composable
fun CategoriesList(
    navigator: DestinationsNavigator,
    categories: List<CategoryContent>
) {

    val randomCommunities = categories.flatMap { it.communities }.shuffled().take(4)

    Log.d("HomeScreen", "randomCommunities:- $randomCommunities")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        items(randomCommunities.size) { index ->
            val community = randomCommunities[index]
            CommunityItem(navigator, community = community)
        }
//        item {
//            AllCategoryItem()
//        }
    }
}

@Composable
fun CommunityItem(
    navigator: DestinationsNavigator,
    community: Community
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .width(68.dp)
            .clickWithoutRipple {
                navigator.navigate(CommunityScreenDestination(community.id))
            }
    ) {

        val imageUrl = community.imageUrl.let {
            "https://ripplereach-0-0-1-snapshot.onrender.com$it"
        }

        AsyncImage(
            model = imageUrl,
            contentDescription = community.name,
            modifier = Modifier
                .size(52.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = community.name,
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = quickStandFamily,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Composable
fun AllCategoryItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(58.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_all),
            contentDescription = "All",
            modifier = Modifier
                .size(52.dp)
                .aspectRatio(1f),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "All",
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = quickStandFamily,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}