package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.jesusurbinez.miplatov.data.NutritionRepository

data class DayInfo(val name: String, val num: String)
data class FoodItem(val name: String, val kcal: Int, val protein: Int)

data class NutritionalHistoryUiState(
    val selectedDayNum: String = "15",
    val days: List<DayInfo> = listOf(
        DayInfo("Lun", "12"), DayInfo("Mar", "13"), DayInfo("Mié", "14"),
        DayInfo("Jue", "15"), DayInfo("Vie", "16"), DayInfo("Sáb", "17"), DayInfo("Dom", "18")
    ),
    val totalCalories: Int = 0,
    val targetCalories: Int = 2200,
    val proteinGrams: Int = 0,
    val targetProtein: Int = 150,
    val breakfastItems: List<FoodItem> = emptyList(),
    val lunchItems: List<FoodItem> = emptyList()
)

class NutritionalHistoryViewModel : ViewModel() {
    private val repository = NutritionRepository
    private val _uiState = MutableStateFlow(NutritionalHistoryUiState())
    val uiState: StateFlow<NutritionalHistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.dailyNutrition.collect { nutrition ->
                _uiState.value = _uiState.value.copy(
                    totalCalories = nutrition.calories,
                    targetCalories = nutrition.targetCalories,
                    proteinGrams = nutrition.protein,
                    targetProtein = nutrition.targetProtein,
                    breakfastItems = nutrition.foods.map { FoodItem(it.name, it.calories, it.protein) }
                )
            }
        }
    }

    fun onDaySelected(dayNum: String) {
        _uiState.value = _uiState.value.copy(selectedDayNum = dayNum)
    }
}
