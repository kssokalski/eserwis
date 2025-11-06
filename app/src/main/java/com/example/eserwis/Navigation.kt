package com.example.eserwis

sealed class Screen(val route :String){
    object Login : Screen("login")
    object MainDashboard : Screen("mainDashboard")
    object FaultDetails : Screen("faultDetails/{faultId}"){
        fun createRoute(faultId : String) = "faultDetails/$faultId"
    }
    object ReportFault : Screen("reportFault")
}