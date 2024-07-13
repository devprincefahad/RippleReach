package dev.prince.ripplereach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import dev.prince.ripplereach.ui.NavGraphs
import dev.prince.ripplereach.ui.appCurrentDestinationAsState
import dev.prince.ripplereach.ui.components.bottomnav.BottomBar
import dev.prince.ripplereach.ui.startAppDestination
import dev.prince.ripplereach.ui.theme.RippleReachTheme
import dev.prince.ripplereach.util.LocalSnackbar
import dev.prince.ripplereach.util.shouldShowBottomBar
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RippleReachTheme {

                ScaffoldDefaults.contentWindowInsets
                val engine = rememberNavHostEngine()
                val navController = engine.rememberNavController()
                val destination = navController.appCurrentDestinationAsState().value
                    ?: NavGraphs.root.startRoute.startAppDestination

                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                val onSnackbarMessageReceived = fun(message: String) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (destination.shouldShowBottomBar()) {
                            BottomBar(navController)
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(snackbarHostState)
                    }
                ) { contentPadding ->
                    CompositionLocalProvider(
                        LocalSnackbar provides onSnackbarMessageReceived
                    ) {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            DestinationsNavHost(
                                modifier = Modifier
                                    .fillMaxSize().padding(contentPadding),
                                navGraph = NavGraphs.root,
                                navController = navController,
                                engine = engine
                            )
                        }

                    }
                }
            }
        }
    }
}