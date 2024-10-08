package com.juandgaines.tasky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.juandgaines.auth.presentation.login.LoginScreenRoot
import com.juandgaines.auth.presentation.login.LoginViewModel
import com.juandgaines.auth.presentation.register.RegisterScreenRoot
import com.juandgaines.auth.presentation.register.RegisterViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    Column (
        modifier = Modifier.fillMaxSize().background(
            color = MaterialTheme.colorScheme.surface
        ),
    ){
        Box(
            modifier = Modifier
                .windowInsetsTopHeight(
                    WindowInsets.statusBars
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
        )
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "home" else "auth"
        ) {
            authGraph(navController)
            agendaGraph(navController)
        }
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = "login",
        route = "auth"
    ) {
        composable(route = "register") {
            RegisterScreenRoot(
                viewModel = hiltViewModel<RegisterViewModel>(),
                onRegisteredSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onLoginScreen = {
                    navController.navigate("login")
                }
            )
        }

        composable(route = "login") {
            LoginScreenRoot(
                viewModel = hiltViewModel<LoginViewModel>(),
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                onSignUpScreen = {
                    navController.navigate("register"){
                        popUpTo("login"){
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.agendaGraph(navController: NavHostController) {
    navigation(
        startDestination = "agenda",
        route = "home"
    ) {
        composable(route = "agenda") {
            Text(
                text = "Agenda"
            )
        }
    }
}

