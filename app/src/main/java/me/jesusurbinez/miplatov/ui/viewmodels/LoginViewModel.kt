package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newValue: String) {
        email = newValue
        errorMessage = null
        successMessage = null
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        errorMessage = null
        successMessage = null
    }

    fun login(onSuccess: () -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "Por favor, completa todos los campos"
            return
        }

        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = task.exception?.localizedMessage ?: "Error al iniciar sesión"
                }
            }
    }

    fun resetPassword() {
        if (email.isEmpty()) {
            errorMessage = "Ingresa tu correo para restablecer la contraseña"
            return
        }

        isLoading = true
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    successMessage = "Se ha enviado un correo para restablecer tu contraseña"
                    errorMessage = null
                } else {
                    errorMessage = task.exception?.localizedMessage ?: "Error al enviar el correo"
                    successMessage = null
                }
            }
    }
}
