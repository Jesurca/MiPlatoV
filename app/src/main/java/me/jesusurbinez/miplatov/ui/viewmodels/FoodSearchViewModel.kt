package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.jesusurbinez.miplatov.data.NutritionRepository

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
    val suggestions: List<String> = listOf("Manzana", "Salmón", "Arroz", "Huevo", "Café"),
    val aiResults: List<AIResult> = listOf(
        AIResult("Ensalada de Quinoa y Aguacate", "Detectado hace 5 min", 340, 12, 45, 14, listOf("ALTA FIBRA", "VEGANO")),
        AIResult("Bowl de Pollo y Brócoli", "Detectado hace 2 horas", 420, 35, 30, 10, listOf("ALTO PROTEÍNA")),
        AIResult("Mix de Frutos Secos", "Detectado ayer", 180, 6, 8, 15, listOf("SNACK SALUDABLE")),
        AIResult("Manzana Roja", "Fruta fresca", 95, 0, 25, 0, listOf("NATURAL")),
        AIResult("Huevo Cocido", "Proteína rápida", 78, 6, 1, 5, listOf("KETO"))
    )
)

class FoodSearchViewModel : ViewModel() {
    private val repository = NutritionRepository
    private val _uiState = MutableStateFlow(FoodSearchUiState())
    val uiState: StateFlow<FoodSearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
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
