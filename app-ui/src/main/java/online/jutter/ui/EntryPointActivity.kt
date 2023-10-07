package online.jutter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import online.jutter.ui.feature.home.HomeScreenDestination
import online.jutter.ui.theme.FractalsTheme

class EntryPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FractalsTheme {
                // A surface container using the 'background' color from the theme
                FractalsApp()
            }
        }
    }
}


@Composable
fun FractalsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.HOME_SCREEN) {
        composable(route = NavigationKeys.Route.HOME_SCREEN) {
            HomeScreenDestination(navController)
        }
//        composable(
//            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
//            arguments = listOf(navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
//                type = NavType.StringType
//            })
//        ) {
//            FoodCategoryDetailsDestination()
//        }
    }
}

object NavigationKeys {

    object Route {
        const val HOME_SCREEN = "home"
    }

}