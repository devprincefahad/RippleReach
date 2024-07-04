package dev.prince.ripplereach.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.prince.ripplereach.R
import dev.prince.ripplereach.data.Category
import dev.prince.ripplereach.ui.destinations.PhoneAuthScreenDestination
import dev.prince.ripplereach.ui.theme.quickStandFamily
import dev.prince.ripplereach.util.BASE_URL
import org.jetbrains.annotations.Async

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
        HomeScreenContent()
    }

    BackHandler {
        context.finish()
    }

}

@Composable
fun HomeScreenContent(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        CategoriesList()
        Text(text = "Home screen")
    }
}

@Composable
fun CategoriesList(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val categories by viewModel.categories.collectAsState()

    Log.d("HomeScreen", "$categories")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(categories.take(6)) { categoryContent ->
            CategoryItem(category = categoryContent.category)
        }
        item {
            AllCategoryItem()
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(58.dp)
    ) {

        val imageUrl =
            category.communities.firstOrNull()?.imageUrl?.let { "https://ripplereach-0-0-1-snapshot.onrender.com/$it" }
                ?: ""

        AsyncImage(
            model = imageUrl,
            contentDescription = category.name,
            modifier = Modifier
                .size(52.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.name,
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