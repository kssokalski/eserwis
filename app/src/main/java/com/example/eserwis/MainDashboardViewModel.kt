package com.example.eserwis

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch


class MainDashboardViewModel(
    private val service : FaultService = FaultService()
) : ViewModel() {

    private val _faults = MutableStateFlow<List<Fault>>(emptyList())
    val faults: StateFlow<List<Fault>> = _faults.asStateFlow()

    suspend fun getUsername(uid: String) : String {
        return service.getUsernameByUid(uid)
    }

    fun loadFaults(
        role: String,
        currentUserUID: String,
        department: String?
    ) {
        viewModelScope.launch {
            //val currentUser = FirebaseAuth.getInstance().currentUser

            service.getActiveFaults(role, currentUserUID, department).collectLatest { result ->
                _faults.value = result

            }
        }
    }





    //usterki filtrowane są teraz w FireBase
    fun filterFaults(): List<Fault> {
        return _faults.value
        /*val activeFaults = _faults.value //aktualna lista

        return when (role){

            //operator widzi wszystkie usterki
            UserRoles.OPERATOR -> {
                activeFaults
            }

            //kierownik widzi wszystkie usterki swojego wydzialu
            UserRoles.MANAGER -> {
                activeFaults.filter { it.department == department }
            }

            //technik widzi swoje usterki
            UserRoles.TECHNICIAN -> {
                activeFaults.filter { it.assignedTo == currentUserUID }
            }

            //inna rola zwraca pusta liste
            else -> emptyList()

        }*/

    }

}