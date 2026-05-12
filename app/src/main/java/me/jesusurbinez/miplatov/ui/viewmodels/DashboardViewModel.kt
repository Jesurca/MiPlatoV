package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.jesusurbinez.miplatov.data.NutritionRepository
import me.jesusurbinez.miplatov.data.FoodRecord

class DashboardViewModel : ViewModel() {
    private val repository = NutritionRepository

    var remainingCalories by mutableStateOf(2200)
        private set
    
    var consumedCalories by mutableStateOf(0)
        private set
    
    var targetCalories by mutableStateOf(2200)
        private set

    var proteinCurrent by mutableStateOf(0)
        private set
    var proteinTarget by mutableStateOf(150)
        private set

    var carbsCurrent by mutableStateOf(0)
        private set
    var carbsTarget by mutableStateOf(250)
        private set

    var fatCurrent by mutableStateOf(0)
        private set
    var fatTarget by mutableStateOf(70)
        private set

    var recentFoods by mutableStateOf<List<FoodRecord>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            repository.dailyNutrition.collect { nutrition ->
                consumedCalories = nutrition.calories
                targetCalories = nutrition.targetCalories
                remainingCalories = (nutrition.targetCalories - nutrition.calories).coerceAtLeast(0)
                
                proteinCurrent = nutrition.protein
                proteinTarget = nutrition.targetProtein
                
                carbsCurrent = nutrition.carbs
                carbsTarget = nutrition.targetCarbs
                
                fatCurrent = nutrition.fat
                fatTarget = nutrition.targetFat

                recentFoods = nutrition.foods.reversed()
            }
        }
    }
}
