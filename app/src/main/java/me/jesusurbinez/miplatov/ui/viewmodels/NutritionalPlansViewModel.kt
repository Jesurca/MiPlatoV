package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import me.jesusurbinez.miplatov.data.NutritionRepository

data class NutritionalPlansUiState(
    val selectedGoal: String = "Perder peso",
    val activityLevel: String = "Activo",
    val estimatedKcal: String = "1,650 kcal"
)

class NutritionalPlansViewModel : ViewModel() {
    private val repository = NutritionRepository
    private val _uiState = MutableStateFlow(NutritionalPlansUiState())
    val uiState: StateFlow<NutritionalPlansUiState> = _uiState.asStateFlow()

    fun updateGlobalPlan() {
        val (kcal, protein, carbs, fat) = when(_uiState.value.selectedGoal) {
            "Perder peso" -> listOf(1650, 140, 150, 50)
            "Mantener" -> listOf(2100, 160, 220, 70)
            "Ganar músculo" -> listOf(2450, 180, 280, 80)
            else -> listOf(2000, 150, 200, 65)
        }
        repository.updateTargets(kcal, protein, carbs, fat)
    }

    fun onGoalSelected(goal: String) {
        val kcal = when(goal) {
            "Perder peso" -> "1,650 kcal"
            "Mantener" -> "2,100 kcal"
            "Ganar músculo" -> "2,450 kcal"
            else -> "2,000 kcal"
        }
        _uiState.value = _uiState.value.copy(selectedGoal = goal, estimatedKcal = kcal)
    }

    fun onActivityLevelSelected(level: String) {
        _uiState.value = _uiState.value.copy(activityLevel = level)
    }
}
