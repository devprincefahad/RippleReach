package dev.prince.ripplereach.ui.community

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import dev.prince.ripplereach.ui.home.PostItem
import dev.prince.ripplereach.ui.theme.quickStandFamily

@Destination
@Composable
fun CommunityScreen(
    communityId: Int,
    viewModel: CommunityViewModel = hiltViewModel()
) {

    val communityDetail = viewModel.communityDetails.collectAsState()

    if (communityDetail.value == null) {
        viewModel.fetchCommunityDetails(communityId)
    }

    val commDetail = communityDetail.value?.community
    var isExpanded by remember { mutableStateOf(false) }

    val posts = communityDetail.value?.posts?.content ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val imageUrl =
                "https://ripplereach-0-0-1-snapshot.onrender.com${commDetail?.imageUrl}"

            AsyncImage(
                model = imageUrl,
                contentDescription = commDetail?.name,
                modifier = Modifier
                    .size(52.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {

                commDetail?.name?.let {
                    Text(
                        text = it,
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = quickStandFamily,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                commDetail?.category?.name?.let {
                    Text(
                        text = "in $it",
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = quickStandFamily,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

        }

        commDetail?.description?.let {
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = it,
                color = Color.Black,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = quickStandFamily,
                ),
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
            )
            Text(
                text = if (isExpanded) "" else "See More",
                color = Color.Blue,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(top = 4.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )

        if (posts.isEmpty()) {
            Text(
                text = "No Posts Yet",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 6.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(posts) { post ->
                    PostItem(post = post)
                }
            }
        }

    }
}