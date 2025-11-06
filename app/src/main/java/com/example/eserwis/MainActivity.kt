package com.example.eserwis

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.eserwis.ui.theme.EserwisTheme

object UserRoles{
    const val MANAGER = "kierownik"
    const val TECHNICIAN = "serwisant"
    const val OPERATOR = "operator"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EserwisTheme {
                AppNavigation()
                /*var currentUser by remember { mutableStateOf<AuthenticatedUser?>(null) }

                if(currentUser == null){
                    LoginScreen(
                        onLoginSuccess = { user ->
                            currentUser = user
                        }
                    )
                } else {
                    MainDashboardScreen(
                        userRole = currentUser!!.role,
                        currentUserUID = currentUser!!.uid,
                        department = currentUser!!.department.toString(),
                        onLogout = {
                            currentUser = null
                        },

                    )
                }*/
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {user ->
                    navController.navigate(Screen.MainDashboard.route)
                }
            )
        }

        composable(Screen.MainDashboard.route) {
            val currentUser = remember { UserManager.getCurrentUser() }

            if(currentUser != null){
                MainDashboardScreen(
                    userRole = currentUser.role,
                    currentUserUID = currentUser.uid,
                    department = currentUser.department.toString(),
                    onLogout = {
                        UserManager.clearCurrentUser()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onFaultClick = { faultId ->
                        navController.navigate(Screen.FaultDetails.createRoute(faultId))
                    },
                    onReportFault = {
                        navController.navigate(Screen.ReportFault.route)
                    }
                )
            } else {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }
            }
        }

        composable(Screen.FaultDetails.route) { fault ->
            val currentUser = remember { UserManager.getCurrentUser() }
            val faultId = fault.arguments?.getString("faultId") ?: ""

            if (currentUser != null) {
                FaultDetailsScreen(
                    faultId = faultId,
                    userRole = currentUser.role,
                    currentUserUid = currentUser.uid,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Screen.ReportFault.route) {
            val currentUser = remember { UserManager.getCurrentUser() }
            if(currentUser != null){
                ReportFaultScreen(
                    userRole = currentUser.role,
                    currentUserUid = currentUser.uid,
                    department = currentUser.department,
                    onBack = {
                        navController.popBackStack()
                    },
                    onFaultReported = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
