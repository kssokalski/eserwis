package com.example.eserwis

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//reprezentacja stanu ekranu
data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val loginSuccess: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    fun onUsernameChange(newUsername: String) {
        _state.value = _state.value.copy(username = newUsername, loginError = null)
    }

    fun onPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(password = newPassword, loginError = null)
    }

    fun login(){
        if(_state.value.username.isEmpty() || _state.value.password.isEmpty()){
            _state.value = _state.value.copy(loginError = "Pusty login lub hasło")
            return
        }

        _state.value = _state.value.copy(isLoading = true)

        //symulacja logowania TODO Firebase/REST
        viewModelScope.launch {
            delay(5000) //symulacja opoznienia

            if(_state.value.username == "test" && _state.value.password == "test"){
                _state.value = _state.value.copy(isLoading = false, loginSuccess = true)
            } else {
                _state.value = _state.value.copy(isLoading = false, loginError = "Niepoprawny login lub hasło")
            }
        }
    }
    //funkcja resetujaca stan
    fun reset(){
        _state.value = LoginState()
    }

}