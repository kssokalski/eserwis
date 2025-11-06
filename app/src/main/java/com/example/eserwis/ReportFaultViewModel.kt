package com.example.eserwis

import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class ReportFaultState(
    val title : String = "",
    val description: String = "",
    val location: String = "",
    val priority: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)


class ReportFaultViewModel(
    val faultService : FaultService = FaultService()
) : ViewModel() {

    private val _state = MutableStateFlow(ReportFaultState())
    val state: StateFlow<ReportFaultState> = _state.asStateFlow()

    fun onTitleChange(title: String) {
        _state.value = _state.value.copy(
            title = title,
            error = null
        )
    }

    fun onDescriptionChange(description: String) {
        _state.value = _state.value.copy(
            description = description,
            error = null
        )
    }

    fun onLocationChange(location: String) {
        _state.value = _state.value.copy(
            location = location,
            error = null
        )
    }

    fun onPriorityChange(priority: String) {
        _state.value = _state.value.copy(
            priority = priority,
            error = null
        )
    }

    fun submitFault(
        currentUserUid : String,
        department : String
    ){
        //sprawdzanie pustych pol
        when{
            _state.value.title.isBlank() -> {
                _state.value = _state.value.copy(
                    error = "Tytuł nie może być pusty"
                )
                return
            }
            _state.value.priority.isBlank() -> {
                _state.value = _state.value.copy(
                    error = "Priorytet nie może być pusty"
                )
                return
            }
            _state.value.location.isBlank() -> {
                _state.value = _state.value.copy(
                    error = "Lokalizacja nie może być pusta"
                )
                return
            }
        }

        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try{
                val fault = Fault(
                    id = "",
                    title = _state.value.title,
                    description = _state.value.description,
                    location = _state.value.location,
                    department = department,
                    priority = _state.value.priority,
                    status = "zgłoszone",
                    assignedToUid = null,
                    reportedByUid = currentUserUid,
                    timestamp = System.currentTimeMillis(),
                    eta = null
                )
                println("DEBUG: Creating fault with:")
                println("DEBUG: - title: ${fault.title}")
                println("DEBUG: - status: ${fault.status}")
                println("DEBUG: - reportedByUid: ${fault.reportedByUid}")
                println("DEBUG: - currentUserUid: $currentUserUid")

                val success = faultService.addFault(fault)

                if(success){
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        title = "",
                        description = "",
                        location = "",
                        priority = ""
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Wystąpił błąd podczas dodawania zgłoszenia"
                    )
                }
            } catch (e: Exception){
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Błąd ${e.message}"
                )
            }
        }

    }


}