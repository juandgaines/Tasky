package com.juandgaines.tasky

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "auth"
    ) {
        authGraph(navController)
        agendaGraph(navController)
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
                    navController.navigate("login"){
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onBackClick = {
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
                onSingUpClick = {
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

