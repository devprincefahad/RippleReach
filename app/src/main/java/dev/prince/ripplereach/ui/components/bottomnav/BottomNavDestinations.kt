package dev.prince.ripplereach.ui.components.bottomnav

import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import dev.prince.ripplereach.R
import dev.prince.ripplereach.ui.destinations.HomeScreenDestination
import dev.prince.ripplereach.ui.destinations.NotificationScreenDestination
import dev.prince.ripplereach.ui.destinations.SearchScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, R.drawable.ic_home, R.string.home),
    Search(
        SearchScreenDestination,
        R.drawable.icon_search,
        R.string.search
    ),
    Notifications(NotificationScreenDestination, R.drawable.ic_notification, R.string.notification)
}