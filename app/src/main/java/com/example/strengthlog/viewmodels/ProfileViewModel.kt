package com.example.strengthlog.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strengthlog.StrengthLogApplication
import com.example.strengthlog.data.local.entity.UserEntity
import com.example.strengthlog.data.preferences.PreferencesManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = (application as StrengthLogApplication).userRepository
    private val preferencesManager = (application as StrengthLogApplication).preferencesManager
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _weightUnit = MutableStateFlow(preferencesManager.weightUnit)
    val weightUnit: StateFlow<String> = _weightUnit

    init {
        loadUser()
    }

    private fun loadUser() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _user.value = userRepository.getUserById(uid)
        }
    }

    fun setWeightUnit(unit: String) {
        preferencesManager.weightUnit = unit
        _weightUnit.value = unit
    }
}