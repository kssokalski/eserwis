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

    private var _technicians = MutableStateFlow<List<User>>(emptyList())
    val technicians: StateFlow<List<User>> = _technicians.asStateFlow()

    fun assignFault(faultId: String, technicianId: String) {
        viewModelScope.launch {
            try {
                val success = service.assignFault(faultId, technicianId)
                if (success) {
                    loadFaults(currentRole, currentUserUid, currentDepartment)
                } else {
                    println("DEBUG : Failed to assign fault")
                }
            } catch (e: Exception) {
                println("DEBUG : ERROR: ${e.message}")
            }
        }
    }

    fun loadTechnicians(department: String) {
        viewModelScope.launch {
            try{
                val techniciansFromDepartment = service.getTechniciansByDepartment(department)
                _technicians.value = techniciansFromDepartment
            } catch (e: Exception){
                println("DEBUG : ERROR LOADING TECHNICIANS: ${e.message}")
            }
        }
    }

    suspend fun getUsername(uid: String) : String {
        return service.getUsernameByUid(uid)
    }

    var currentRole = ""
    var currentUserUid = ""
    var currentDepartment : String? = null

    fun changeFaultStatus(faultId: String, status: String) {
        viewModelScope.launch {
            try{
                val success = service.updateFaultStatus(faultId, status)
                if(success){
                    loadFaults(currentRole, currentUserUid, currentDepartment)
                } else {
                    println("DEBUG : Failed to update fault status")
                }
            } catch (e: Exception){
                println("DEBUG : ERROR: ${e.message}")
            }
        }
    }

    fun loadFaults(
        role: String,
        currentUserUID: String,
        department: String?
    ) {
        this.currentRole = role
        this.currentUserUid = currentUserUID
        this.currentDepartment = department
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