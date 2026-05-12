package me.jesusurbinez.miplatov.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

data class FoodRecord(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val time: String
)

data class DailyNutrition(
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    val targetCalories: Int = 2200,
    val targetProtein: Int = 150,
    val targetCarbs: Int = 250,
    val targetFat: Int = 70,
    val foods: List<FoodRecord> = emptyList()
)

object NutritionRepository {
    private val _dailyNutrition = MutableStateFlow(DailyNutrition())
    val dailyNutrition: StateFlow<DailyNutrition> = _dailyNutrition.asStateFlow()

    fun addFood(name: String, calories: Int, protein: Int, carbs: Int, fat: Int) {
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val newFood = FoodRecord(name, calories, protein, carbs, fat, currentTime)
        
        _dailyNutrition.update { current ->
            current.copy(
                calories = current.calories + calories,
                protein = current.protein + protein,
                carbs = current.carbs + carbs,
                fat = current.fat + fat,
                foods = current.foods + newFood
            )
        }
    }

    fun updateTargets(calories: Int, protein: Int, carbs: Int, fat: Int) {
        _dailyNutrition.update { current ->
            current.copy(
                targetCalories = calories,
                targetProtein = protein,
                targetCarbs = carbs,
                targetFat = fat
            )
        }
    }
}
