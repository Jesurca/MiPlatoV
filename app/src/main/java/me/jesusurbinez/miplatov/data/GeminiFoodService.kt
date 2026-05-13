package me.jesusurbinez.miplatov.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.jesusurbinez.miplatov.BuildConfig
import org.json.JSONObject

data class ScannedFood(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

class GeminiFoodService {
    private val apiKey = BuildConfig.GEMINI_API_KEY
    
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun scanFoodImage(bitmap: Bitmap): ScannedFood? = withContext(Dispatchers.IO) {
        val prompt = """
            Analyze this food image and provide the nutritional information.
            Respond ONLY with a JSON object in the following format:
            {
              "name": "Food Name",
              "calories": 0,
              "protein": 0,
              "carbs": 0,
              "fat": 0
            }
            If there are multiple items, provide the total for the plate.
            Be as accurate as possible.
        """.trimIndent()

        try {
            val response = generativeModel.generateContent(
                content {
                    image(bitmap)
                    text(prompt)
                }
            )
            
            val jsonText = response.text?.replace("```json", "")?.replace("```", "")?.trim()
            if (jsonText != null) {
                val json = JSONObject(jsonText)
                return@withContext ScannedFood(
                    name = json.getString("name"),
                    calories = json.getInt("calories"),
                    protein = json.getInt("protein"),
                    carbs = json.getInt("carbs"),
                    fat = json.getInt("fat")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}
