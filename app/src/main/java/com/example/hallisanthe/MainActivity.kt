package com.example.hallisanthe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hallisanthe.data.ProductCategory
import com.example.hallisanthe.data.UserRole
import com.example.hallisanthe.data.UserViewModel
import com.example.hallisanthe.ui.screens.*
import com.example.hallisanthe.ui.theme.HalliSantheTheme

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HalliSantheTheme {
                HalliSantheApp(userViewModel)
            }
        }
    }
}

@Composable
private fun HalliSantheApp(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val session by userViewModel.session.collectAsState()
    val products by userViewModel.products.collectAsState()
    val orders by userViewModel.orders.collectAsState()
    val loginError by userViewModel.loginError.collectAsState()
    val registerError by userViewModel.registerError.collectAsState()

    NavHost(navController = navController, startDestination = Route.Start.path) {
        composable(Route.Start.path) {
            StartScreen(
                onLoginAsBuyer = { navController.navigate(Route.Login.path) },
                onLoginAsSeller = { navController.navigate(Route.Login.path) },
                onCreateAccount = { navController.navigate(Route.Register.path) }
            )
        }

        composable(Route.Login.path) {
            val savedCreds = remember { userViewModel.getSavedCredentials() }
            LoginScreen(
                initialEmail = savedCreds?.first ?: "",
                initialPassword = savedCreds?.second ?: "",
                error = loginError,
                onLogin = { email, password, rememberMe ->
                    userViewModel.login(email, password, rememberMe) {
                        userViewModel.session.value?.let { user ->
                            navController.navigate(user.role.dashboardRoute) {
                                popUpTo(Route.Start.path) { inclusive = false }
                            }
                        }
                    }
                },
                onClearError = { userViewModel.clearError() },
                onNavigateToRegister = { navController.navigate(Route.Register.path) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Register.path) {
            RegisterScreen(
                error = registerError,
                onRegister = { name, email, phone, location, password, role ->
                    userViewModel.register(name, email, phone, location, password, role) {
                        navController.navigate(role.dashboardRoute) {
                            popUpTo(Route.Start.path) { inclusive = false }
                        }
                    }
                },
                onClearError = { userViewModel.clearError() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Buyer.path) {
            session?.let { current ->
                BuyerDashboard(
                    session = current,
                    products = products,
                    onProfile = { navController.navigate(Route.Profile.path) },
                    onPlaceOrder = userViewModel::placeOrder,
                    onCategoryClick = { category ->
                        navController.navigate("${Route.Category.path}/${category.name}")
                    }
                )
            } ?: RedirectToStart(navController)
        }

        composable(
            route = "${Route.Category.path}/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            val category = ProductCategory.entries.find { it.name == categoryName }
            if (category != null) {
                CategoryMarketScreen(
                    category = category,
                    products = products.filter { it.category == category },
                    onBack = { navController.popBackStack() },
                    onPlaceOrder = userViewModel::placeOrder
                )
            }
        }

        composable(Route.Seller.path) {
            session?.let { current ->
                SellerDashboard(
                    session = current,
                    products = products,
                    orders = orders,
                    onProfile = { navController.navigate(Route.Profile.path) },
                    onAddProduct = userViewModel::addProduct,
                    onDeleteProduct = userViewModel::removeProduct,
                    onSeeAllMarket = { navController.navigate(Route.Buyer.path) }
                )
            } ?: RedirectToStart(navController)
        }

        composable(Route.Profile.path) {
            session?.let { current ->
                ProfileScreen(
                    session = current,
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        userViewModel.logout()
                        navController.navigate(Route.Start.path) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } ?: RedirectToStart(navController)
        }
    }
}

@Composable
private fun RedirectToStart(navController: NavHostController) {
    LaunchedEffect(Unit) {
        navController.navigate(Route.Start.path) {
            popUpTo(0) { inclusive = true }
        }
    }
}

private enum class Route(val path: String) {
    Start("start"),
    Login("login"),
    Register("register"),
    Buyer("buyer"),
    Category("category"),
    Seller("seller"),
    Profile("profile")
}

private val UserRole.dashboardRoute: String
    get() = when (this) {
        UserRole.Buyer -> Route.Buyer.path
        UserRole.Seller -> Route.Seller.path
    }
