package com.example.eserwis

import android.provider.ContactsContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//klasa do komunikacji z Firebase
class AuthService {
    private val _auth = FirebaseAuth.getInstance()
    private val _db = FirebaseFirestore.getInstance()

    //rezultat logowania
    sealed class AuthResult {
        data class Success(
            val uid: String,
            val role: String,
            val department: String
        ) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun login(username: String, password: String): Any {
        return try {
            val result = _auth.signInWithEmailAndPassword(username, password).await()
            val uid = result.user?.uid ?: return AuthResult.Error("Brak UID użytkownika")
            //pobranie danych z Firestore
            val userDoc = _db.collection("users").document(uid).get().await()
            val role = userDoc.getString("role") ?: return AuthResult.Error("Brak roli użytkownika")
            val department = userDoc.getString("department") ?: return AuthResult.Error("Brak działu użytkownika")
            AuthResult.Success(uid, role, department)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Błąd logowania")
        }
    }

    fun getCurrentUserUid(): String? {
        return _auth.currentUser?.uid
    }

}