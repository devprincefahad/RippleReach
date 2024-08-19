package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import dev.prince.ripplereach.ui.destinations.CreatePostScreenDestination
import dev.prince.ripplereach.ui.destinations.PhoneAuthScreenDestination
import dev.prince.ripplereach.ui.destinations.PostDetailScreenDestination
import dev.prince.ripplereach.ui.theme.Orange
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {

//    val posts by viewModel.posts.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val posts by viewModel.posts.collectAsState()

    val sortOrder by viewModel.sortOrder.collectAsState()

    val sortedPosts by remember(sortOrder, posts) {
        derivedStateOf {
            when (sortOrder) {
                HomeViewModel.SortOrder.POPULAR -> posts.sortedByDescending { it.totalUpvotes }
                HomeViewModel.SortOrder.NEW -> posts.sortedByDescending { it.createdAt }
            }
        }
    }
    val pagerState = rememberPagerState {
        2
    }
    val isVisible = rememberSaveable { mutableStateOf(true) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Hide FAB
                if (available.y < -1) {
                    isVisible.value = false
                }

                // Show FAB
                if (available.y > 1) {
                    isVisible.value = true
                }

                return Offset.Zero
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisible.value,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                ExtendedFloatingActionButton(
                    containerColor = Orange,
                    onClick = {
                        navigator.navigate(CreatePostScreenDestination)
                    },
                    icon = {
                        Icon(
                            painterResource(id = R.drawable.ic_add),
                            tint = Color.White,
                            contentDescription = "Extended floating action button."
                        )
                    },
                    text = {
                        Text(
                            text = "Create Post",
                            color = Color.White,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = quickStandFamily
                            )
                        )
                    },
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection)
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

            TabRow(
                modifier = Modifier.width(200.dp),
                selectedTabIndex = selectedTab,
                divider = { },
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Orange,
                        )
                    }
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        viewModel.setSortOrder(HomeViewModel.SortOrder.NEW)
                    },
                    text = {
                        Text(
                            text = "Top",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = quickStandFamily
                            )
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        viewModel.setSortOrder(HomeViewModel.SortOrder.POPULAR)
                    },
                    text = {
                        Text(
                            text = "Recent",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = quickStandFamily
                            )
                        )
                    }
                )
            }
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = pagerState
            ) { index ->

                PostList(posts = sortedPosts, navigator = navigator)

            }

            LaunchedEffect(pagerState.currentPage) {
                selectedTab = pagerState.currentPage
            }
        }
    }
}

@Composable
fun PostItem(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    post: Post,
    truncateContent: Boolean
) {

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
//            val imageUrl =
//                "https://ripplereach-0-0-1-snapshot.onrender.com${post.author.avatar}"

            AsyncImage(
                model = post.author.avatar,
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
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upvote),
                        contentDescription = "Upvote",
                        tint = if (post.upvotedByUser) Color.Blue else Color.Gray
                    )
                }
                Text(
                    text = post.totalUpvotes.toString(),
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

    val randomCommunities =
        remember(categories) { categories.flatMap { it.communities }.shuffled().take(4) }

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

//        val imageUrl = community.imageUrl.let {
//            "https://ripplereach-0-0-1-snapshot.onrender.com$it"
//        }

        AsyncImage(
            model = community.imageUrl,
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