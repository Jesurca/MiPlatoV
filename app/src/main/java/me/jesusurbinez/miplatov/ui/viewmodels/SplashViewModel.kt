package me.jesusurbinez.miplatov.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun checkAuthState(onSessionExists: () -> Unit, onNoSession: () -> Unit) {
        if (auth.currentUser != null) {
            onSessionExists()
        } else {
            onNoSession()
        }
    }
}
