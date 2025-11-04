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

    fun loadFaultDetails(faultId : String) {
        viewModelScope.launch {
            _faultDetails.value = service.getFaultById(faultId)
        }
    }

    suspend fun getUsername(uid: String): String {
        return service.getUsernameByUid(uid)
    }


}