package com.example.strengthlog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strengthlog.StrengthLogApplication
import com.example.strengthlog.data.local.entity.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = (application as StrengthLogApplication).userRepository
    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(email: String, password: String, displayName: String) {
        _uiState.value = RegisterUiState.Loading
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password.trim()
                ).await()

                val firebaseUser = result.user!!

                userRepository.insertUser(
                    UserEntity(
                        id = firebaseUser.uid,
                        email = email.trim(),
                        displayName = displayName.trim(),
                        createdAt = System.currentTimeMillis()
                    )
                )

                _uiState.value = RegisterUiState.Success

            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Registration failed")
            }
        }
    }
}