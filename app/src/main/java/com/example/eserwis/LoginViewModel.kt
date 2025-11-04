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
    val loginSuccess: Boolean = false,
    val authenticatedUser: AuthenticatedUser? = null
)

class LoginViewModel(
    private val authService: AuthService = AuthService()
) : ViewModel() {
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

        viewModelScope.launch {
            val result = authService.login(_state.value.username, _state.value.password)
            when (result) {
                is AuthService.AuthResult.Success -> {
                    //zapisanie uzytkownika w stanie ViewModel
                    _state.value = _state.value.copy(
                        isLoading = false,
                        loginSuccess = true,
                        authenticatedUser = result.user
                    )

                }
                is AuthService.AuthResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, loginError = result.message, authenticatedUser = null)
                }
            }
        }


    }
    //funkcja resetujaca stan
    fun reset(){
        _state.value = LoginState()
    }

}