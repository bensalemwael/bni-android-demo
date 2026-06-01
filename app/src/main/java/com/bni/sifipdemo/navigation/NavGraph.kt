package com.bni.sifipdemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bni.sifipdemo.BniApplication
import com.bni.sifipdemo.DashboardViewModelFactory
import com.bni.sifipdemo.LoginViewModelFactory
import com.bni.sifipdemo.TransferViewModelFactory
import com.bni.sifipdemo.ui.dashboard.DashboardScreen
import com.bni.sifipdemo.ui.dashboard.DashboardViewModel
import com.bni.sifipdemo.ui.login.LoginScreen
import com.bni.sifipdemo.ui.login.LoginViewModel
import com.bni.sifipdemo.ui.splash.SplashScreen
import com.bni.sifipdemo.ui.transfer.TransferScreen
import com.bni.sifipdemo.ui.transfer.TransferViewModel

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val TRANSFER = "transfer"
}

@Composable
fun BniNavGraph() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as BniApplication

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onTimeout = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.LOGIN) {
            val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(app))
            LoginScreen(
                viewModel = vm,
                onAuthenticated = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.DASHBOARD) {
            val vm: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(app))
            DashboardScreen(
                viewModel = vm,
                onTransferClicked = { navController.navigate(Routes.TRANSFER) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.TRANSFER) {
            val vm: TransferViewModel = viewModel(
                factory = TransferViewModelFactory(
                    app = app,
                    msisdn = "+225071234567",
                ),
            )
            TransferScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
