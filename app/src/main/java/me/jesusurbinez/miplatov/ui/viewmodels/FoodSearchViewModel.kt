package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.jesusurbinez.miplatov.data.NutritionRepository

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.jesusurbinez.miplatov.data.GeminiFoodService
import me.jesusurbinez.miplatov.data.ScannedFood

data class AIResult(
    val title: String, 
    val subtitle: String, 
    val kcal: Int, 
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val tags: List<String>
)

data class FoodSearchUiState(
    val searchQuery: String = "",
    val aiPrompt: String = "",
    val isRecommending: Boolean = false,
    val recommendations: List<AIResult> = emptyList(),
    val suggestions: List<String> = listOf("Manzana", "Salmón", "Arroz", "Huevo", "Café"),
    val aiResults: List<AIResult> = listOf(
        AIResult("Arroz Blanco", "Porción de 150g", 200, 4, 45, 1, listOf("CARBOS", "ENERGÍA")),
        AIResult("Huevos Revueltos", "2 unidades", 145, 13, 1, 10, listOf("PROTEÍNA", "KETO")),
        AIResult("Pechuga de Pollo", "A la plancha (150g)", 250, 46, 0, 6, listOf("ALTO PROTEÍNA")),
        AIResult("Aguacate Hass", "Media unidad (100g)", 160, 2, 9, 15, listOf("GRASAS SALUDABLES")),
        AIResult("Carne de Res", "Bistec (150g)", 300, 39, 0, 15, listOf("HIERRO", "FUERZA"))
    )
)

class FoodSearchViewModel : ViewModel() {
    private val repository = NutritionRepository
    private val geminiService = GeminiFoodService()
    private val _uiState = MutableStateFlow(FoodSearchUiState())
    val uiState: StateFlow<FoodSearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
    }

    fun onAiPromptChange(newPrompt: String) {
        _uiState.value = _uiState.value.copy(aiPrompt = newPrompt)
    }

    fun getAiRecommendation() {
        val prompt = _uiState.value.aiPrompt
        if (prompt.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRecommending = true)
            val result = geminiService.recommendFood(prompt)
            if (result != null) {
                val newAiResult = AIResult(
                    title = result.name,
                    subtitle = "Recomendado por IA",
                    kcal = result.calories,
                    protein = result.protein,
                    carbs = result.carbs,
                    fat = result.fat,
                    tags = listOf("IA", "RECOMENDADO")
                )
                _uiState.value = _uiState.value.copy(
                    recommendations = listOf(newAiResult) + _uiState.value.recommendations,
                    aiPrompt = "",
                    isRecommending = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isRecommending = false)
            }
        }
    }

    fun addFood(food: AIResult) {
        repository.addFood(
            name = food.title,
            calories = food.kcal,
            protein = food.protein,
            carbs = food.carbs,
            fat = food.fat
        )
    }
}
