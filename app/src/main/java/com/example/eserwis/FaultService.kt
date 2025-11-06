package com.example.eserwis


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FaultService {

    private val _db = FirebaseFirestore.getInstance()
    private val faultsCollection = _db.collection("faults")

    suspend fun addFault(fault: Fault) : Boolean {
        return try {
            faultsCollection.add(fault).await()
            true
        } catch (e : Exception) {
            false
        }
    }

    suspend fun getUsernameByUid(uid: String) : String {
        return try {
            val document = _db.collection("users").document(uid).get().await()
            document.getString("username") ?: "Nieznany użytkownik"
        } catch (e : Exception) {
            "Błąd ładowania"
        }
    }

    suspend fun getFaultById(faultId : String): Fault?{
        return try {
            val document = faultsCollection.document(faultId).get().await()
            document.toObject(Fault::class.java)
        } catch (e : Exception) {
            null
        }
    }


    fun getActiveFaults(
        role: String,
        currentUserUID: String,
        department: String?
    ): Flow<List<Fault>> = callbackFlow {
        println("DEBUG: Starting query - role: $role, user: $currentUserUID, department: $department")

        var query = faultsCollection

        val subscription = query.addSnapshotListener { value, error ->
            if (error != null) {
                println("DEBUG: ERROR: ${error.message}")
                when {
                    error.code == FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                        println("DEBUG: PERMISSION DENIED - problem with Firestore rules")
                        trySend(emptyList())
                    }
                    else -> {
                        close(error)
                    }
                }
                return@addSnapshotListener
            }

            if(value != null) {
                val allFaults = value.toObjects(Fault::class.java)
                println("DEBUG: SUCCESS - got ${allFaults.size} faults")
                allFaults.forEach { fault ->
                    println("DEBUG: Fault: ${fault.title}, dept: ${fault.department}, assignedTo: ${fault.assignedToUid}")
                }
                trySend(allFaults)
            } else {
                println("DEBUG: No data received")
                trySend(emptyList())
            }
        }

        awaitClose { subscription.remove() }
    }

}
