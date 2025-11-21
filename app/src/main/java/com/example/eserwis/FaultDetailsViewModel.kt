package com.example.eserwis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FaultDetailsViewModel(
    val service : FaultService = FaultService()
) : ViewModel() {

    private val _faultDetails = MutableStateFlow<Fault?>(null)
    val faultDetails: StateFlow<Fault?> = _faultDetails.asStateFlow()

    fun updateFault(faultId: String, updatedFault: Fault) {
        viewModelScope.launch {
            try {
                //println("DEBUG: FaultDetailsViewModel: updateFault called with faultId: $faultId")
                //println("DEBUG: priority: ${updatedFault.priority}")
                val success = service.updateFault(faultId, updatedFault)
                if (success) {
                    //println("DEBUG: FaultDetailsViewModel: updateFault successful")
                    loadFaultDetails(faultId)
                }
            } catch (e: Exception) {
                println("DEBUG : FaultDetailsViewModel: Error updating fault: ${e.message}")
            }
        }
    }

    fun loadFaultDetails(faultId : String) {
        viewModelScope.launch {
            _faultDetails.value = service.getFaultById(faultId)
        }
    }

    suspend fun getUsername(uid: String): String {
        return service.getUsernameByUid(uid)
    }


}