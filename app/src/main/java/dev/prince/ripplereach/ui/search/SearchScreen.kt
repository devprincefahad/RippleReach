package dev.prince.ripplereach.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.ramcosta.composedestinations.result.ResultBackNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.data.Community
import dev.prince.ripplereach.ui.destinations.CommunityScreenDestination
import dev.prince.ripplereach.ui.home.PostItem
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.util.clickWithoutRipple

@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel()
) {

    SearchContent(viewModel, navigator) {
        navigator.navigate(CommunityScreenDestination(it))
    }
}

/**
 * Returns the ID of the selected community
 */
@Destination
@Composable
fun CommunitySelector(
    resultNavigator: ResultBackNavigator<Int>,
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel()
) {

    SearchContent(viewModel, navigator, false) {
        resultNavigator.navigateBack(it)
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchContent(
    viewModel: SearchViewModel,
    navigator: DestinationsNavigator,
    showSearchTextField: Boolean = true,
    onCommunityClicked: (Int) -> Unit,
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val categories by viewModel.categories.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            if (showSearchTextField) OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel.searchQuery,
                onValueChange = { newQuery ->
                    if (newQuery.length <= 25) {
                        viewModel.onSearchQueryChange(newQuery)
                    }
                },
                placeholder = {
                    Text(
                        "Search Posts",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = quickStandFamily
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else if (viewModel.searchQuery.isNotEmpty()) {
            items(searchResults) { post ->
                PostItem(post = post, navigator = navigator, truncateContent = true)
            }
        } else {
            item {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Categories",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = quickStandFamily
                    )
                )
            }

            categories.forEach { category ->

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = category.name,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = quickStandFamily
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(category.communities.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { community ->
                            CommunityCard(
                                community = community,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = if (rowItems.indexOf(community) == 0) 8.dp else 0.dp),
                                onCommunityClicked = onCommunityClicked
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }
    }
}

@Composable
fun CommunityCard(
    community: Community,
    modifier: Modifier = Modifier,
    onCommunityClicked: (Int) -> Unit,
) {
    Surface(
        modifier = modifier
            .clickWithoutRipple {
                onCommunityClicked(community.id)
            }
            .padding(top = 12.dp, end = 12.dp),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

//            val imageUrl = community.imageUrl.let {
//                "https://ripplereach-0-0-1-snapshot.onrender.com$it"
//            }

            AsyncImage(
                model = community.imageUrl,
                contentDescription = community.name,
                modifier = Modifier
                    .size(52.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = community.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = quickStandFamily
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}