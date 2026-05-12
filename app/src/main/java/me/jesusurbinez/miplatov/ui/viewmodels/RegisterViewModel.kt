package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var fullName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    fun onFullNameChange(newValue: String) { fullName = newValue; errorMessage = null }
    fun onEmailChange(newValue: String) { email = newValue; errorMessage = null }
    fun onPasswordChange(newValue: String) { password = newValue; errorMessage = null }
    fun onConfirmPasswordChange(newValue: String) { confirmPassword = newValue; errorMessage = null }

    fun register(onSuccess: () -> Unit) {
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage = "Todos los campos son obligatorios"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            isLoading = false
                            if (profileTask.isSuccessful) {
                                onSuccess()
                            } else {
                                errorMessage = "Error al guardar el nombre"
                            }
                        }
                } else {
                    isLoading = false
                    errorMessage = task.exception?.localizedMessage ?: "Error en el registro"
                }
            }
    }
}
