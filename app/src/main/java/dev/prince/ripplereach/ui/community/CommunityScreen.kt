package dev.prince.ripplereach.ui.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            Column {

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

            }
            Column {

                commDetail?.name?.let {
                    Text(
                        text = it,
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

                commDetail?.description?.let {
                    Text(
                        text = it,
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
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(communityDetail.value?.posts?.content ?: emptyList()) { post ->
                PostItem(post = post)
            }
        }
    }
}