package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.jesusurbinez.miplatov.data.NutritionRepository

import java.util.Locale

data class ProfileUiState(
    val userName: String = "Usuario",
    val memberSince: String = "Enero 2024",
    val calories: String = "0",
    val protein: String = "0g",
    val hydration: String = "2.5L"
)

class ProfileViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val repository = NutritionRepository
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchUserData()
        viewModelScope.launch {
            repository.dailyNutrition.collect { nutrition ->
                _uiState.value = _uiState.value.copy(
                    calories = String.format(Locale.getDefault(), "%,d", nutrition.calories),
                    protein = "${nutrition.protein}g"
                )
            }
        }
    }

    private fun fetchUserData() {
        val user = auth.currentUser
        user?.let {
            val creationTimestamp = it.metadata?.creationTimestamp ?: 0L
            val memberSinceDate = if (creationTimestamp != 0L) {
                val sdf = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.forLanguageTag("es-ES"))
                sdf.format(java.util.Date(creationTimestamp))
            } else {
                "Enero 2024"
            }

            _uiState.value = _uiState.value.copy(
                userName = it.displayName ?: it.email ?: "Usuario",
                memberSince = memberSinceDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
            )
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        auth.signOut()
        onLogoutSuccess()
    }
}
