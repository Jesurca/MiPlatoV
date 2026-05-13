package me.jesusurbinez.miplatov.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.jesusurbinez.miplatov.data.GeminiFoodService
import me.jesusurbinez.miplatov.data.NutritionRepository
import me.jesusurbinez.miplatov.data.ScannedFood

sealed class CameraUIState {
    object Idle : CameraUIState()
    object Loading : CameraUIState()
    data class Success(val food: ScannedFood) : CameraUIState()
    data class Error(val message: String) : CameraUIState()
}

class CameraAIViewModel : ViewModel() {
    private val repository = NutritionRepository
    private val geminiService = GeminiFoodService()

    private val _uiState = MutableStateFlow<CameraUIState>(CameraUIState.Idle)
    val uiState: StateFlow<CameraUIState> = _uiState.asStateFlow()

    fun scanImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = CameraUIState.Loading
            val scannedFood = geminiService.scanFoodImage(bitmap)
            if (scannedFood != null) {
                _uiState.value = CameraUIState.Success(scannedFood)
            } else {
                _uiState.value = CameraUIState.Error("No se pudo analizar la imagen")
            }
        }
    }

    fun confirmFood(food: ScannedFood) {
        repository.addFood(
            name = food.name,
            calories = food.calories,
            protein = food.protein,
            carbs = food.carbs,
            fat = food.fat
        )
        _uiState.value = CameraUIState.Idle
    }

    fun resetState() {
        _uiState.value = CameraUIState.Idle
    }
}
