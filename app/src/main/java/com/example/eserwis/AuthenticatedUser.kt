package com.example.eserwis

//reprezentacja uzytkownika
data class AuthenticatedUser(
    val uid: String,
    val role: String,
    val department: String
)

//obsluga stanu uzytkownika
object UserManager {
    private var currentUser: AuthenticatedUser? = null

    fun setCurrentUser(user: AuthenticatedUser) {
        currentUser = user
    }

    fun getCurrentUser(): AuthenticatedUser? {
        return currentUser
    }

    fun clearCurrentUser() {
        currentUser = null
    }
}