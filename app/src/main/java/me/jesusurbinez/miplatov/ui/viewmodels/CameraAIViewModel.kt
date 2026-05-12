package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import me.jesusurbinez.miplatov.data.NutritionRepository

class CameraAIViewModel : ViewModel() {
    private val repository = NutritionRepository

    fun captureFood() {
        // Simulamos la detección de la IA añadiendo el plato detectado en la UI
        // En este caso "Salmón Parrilla" (320 kcal) y "Tomate Cherry" (20 kcal)
        repository.addFood(
            name = "Plato AI: Salmón y Tomates",
            calories = 340,
            protein = 32,
            carbs = 5,
            fat = 18
        )
    }
}
