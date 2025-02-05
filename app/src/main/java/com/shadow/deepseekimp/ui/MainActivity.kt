package com.shadow.deepseekimp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shadow.deepseekimp.ui.nav.AppNavGraph
import com.shadow.deepseekimp.ui.nav.rememberNavigationController
import com.shadow.deepseekimp.ui.theme.Deepseek_impTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
               viewModel.loadingRemoteConfig
            }
        }
        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavigationController()
            Deepseek_impTheme(
                darkTheme = true
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(viewModel.snackBarHostState) }
                    ) { innerPadding ->
                    AppNavGraph(
                        modifier =  Modifier.padding(innerPadding),
                        navigationController = navigationController
                    )
                }
            }
        }
    }
}