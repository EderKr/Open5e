package com.example.open5e

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.open5e.ui.account.AccountScreen
import com.example.open5e.ui.creatures.CreaturesScreen
import com.example.open5e.ui.details.ItemDetailScreen
import com.example.open5e.ui.details.MonsterDetailScreen
import com.example.open5e.ui.details.SpellDetailScreen
import com.example.open5e.ui.home.HomeScreen
import com.example.open5e.ui.items.ItemsScreen
import com.example.open5e.ui.login.LoginScreen
import com.example.open5e.ui.signup.SignUpScreen
import com.example.open5e.ui.spells.SpellsScreen
import com.example.open5e.ui.theme.Draft2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Draft2Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        // LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                onSignUp = { navController.navigate("signup") }
            )
        }

        // SIGN UP
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = { navController.navigate("login") }
            )
        }

        // HOME
        composable("home") {
            HomeScreen(
                onCreaturesClick = { navController.navigate("creatures") },
                onSpellsClick = { navController.navigate("spells") },
                onItemsClick = { navController.navigate("items") },
                onAccountClick = { navController.navigate("account") },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // CREATURE LIST
        composable("creatures") {
            CreaturesScreen(
                viewModel = viewModel(),
                navController = navController
            )
        }

        // SPELL LIST
        composable("spells") {
            SpellsScreen(
                viewModel = viewModel(),
                navController = navController
            )
        }

        // ITEM LIST
        composable("items") {
            ItemsScreen(
                viewModel = viewModel(),
                navController = navController
            )
        }

        // ACCOUNT
        composable("account") {
            AccountScreen(viewModel = viewModel())
        }

        // DETAILS -------------------------------------------------------------

        // MONSTER DETAIL
        composable("monsterDetail/{slug}") { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug") ?: ""
            MonsterDetailScreen(slug = slug)
        }

        // SPELL DETAIL
        composable("spellDetail/{slug}") { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug") ?: ""
            SpellDetailScreen(slug = slug)
        }

        // ITEM DETAIL
        composable("itemDetail/{slug}") { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug") ?: ""
            ItemDetailScreen(slug = slug)
        }
    }
}