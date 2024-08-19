package dev.prince.ripplereach.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import dev.prince.ripplereach.ui.destinations.ChooseNameScreenDestination
import dev.prince.ripplereach.ui.destinations.ChooseProfessionDestination
import dev.prince.ripplereach.ui.destinations.ChooseUniversityDestination
import dev.prince.ripplereach.ui.destinations.ChooseWorkPlaceDestination
import dev.prince.ripplereach.ui.destinations.CommunityScreenDestination
import dev.prince.ripplereach.ui.destinations.CommunitySelectorDestination
import dev.prince.ripplereach.ui.destinations.CreatePostScreenDestination
import dev.prince.ripplereach.ui.destinations.Destination
import dev.prince.ripplereach.ui.destinations.OTPVerifyScreenDestination
import dev.prince.ripplereach.ui.destinations.PhoneAuthScreenDestination
import dev.prince.ripplereach.ui.destinations.PostDetailScreenDestination
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

//const val BASE_URL = "https://ripplereach-0-0-1-snapshot.onrender.com/api/"
// virtual device
const val BASE_URL = "http://192.168.29.103:8080/api/"
//const val BASE_URL = "http://127.0.0.1:8080/api/"

val professions = listOf(
    "Software Engineer/Developer",
    "Product Manager",
    "Data Scientist",
    "DevOps Engineer",
    "Cybersecurity Specialist",
    "System Administrator",
    "Database Administrator",
    "Network Engineer",
    "UX/UI Designer",
    "Quality Assurance Engineer",
    "Business Analyst",
    "IT Project Manager",
    "Cloud Engineer",
    "AI/ML Engineer",
    "Technical Support Specialist",
    "Mobile App Developer",
    "Frontend Developer",
    "Backend Developer",
    "Full Stack Developer",
    "IT Consultant",
    "Site Reliability Engineer (SRE)",
    "Game Developer",
    "Systems Architect",
    "IT Auditor",
    "Scrum Master",
    "Blockchain Developer",
    "IT Trainer/Educator",
    "ERP Consultant",
    "Technical Writer",
    "Chief Information Officer (CIO)",
    "Chief Technology Officer (CTO)",
    "IT Operations Manager",
    "Technical Account Manager",
    "Release Manager",
    "Penetration Tester (Ethical Hacker)",
    "Network Security Engineer",
    "Data Engineer",
    "Hardware Engineer",
    "Embedded Systems Engineer",
    "IT Support Specialist",
    "Solutions Architect",
    "Infrastructure Engineer",
    "IT Compliance Manager",
    "Business Intelligence Analyst",
    "Information Security Analyst",
    "Telecommunications Specialist",
    "Video Game Designer",
    "VR/AR Developer",
    "Robotics Engineer",
    "Help Desk Technician"
)


@Composable
fun SetSoftInputMode(mode: Int) {
    val context = LocalContext.current
    val activity = context as? Activity
    DisposableEffect(activity) {
        val originalMode = activity?.window?.attributes?.softInputMode
        activity?.window?.setSoftInputMode(mode)
        onDispose {
            activity?.window?.setSoftInputMode(originalMode ?: WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }
}

fun <T> oneShotFlow() = MutableSharedFlow<T>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

val LocalSnackbar = compositionLocalOf<(String) -> Unit> { { } }

fun Destination.shouldShowBottomBar(): Boolean {

    return (this !in listOf(
        ChooseNameScreenDestination,
        ChooseProfessionDestination,
        ChooseUniversityDestination,
        ChooseWorkPlaceDestination,
        OTPVerifyScreenDestination,
        PhoneAuthScreenDestination,
        CommunityScreenDestination,
        PostDetailScreenDestination,
        CreatePostScreenDestination,
        CommunitySelectorDestination
    ))
}


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clickWithoutRipple(
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}