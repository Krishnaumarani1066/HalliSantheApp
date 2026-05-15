package com.example.hallisanthe.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HalliSantheRepository()
    private val prefs = application.getSharedPreferences("halli_santhe_login_prefs", Context.MODE_PRIVATE)

    private val _session = MutableStateFlow<UserSession?>(null)
    val session: StateFlow<UserSession?> = _session.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError.asStateFlow()
    
    val products: StateFlow<List<Product>> = repository.products
    val orders: StateFlow<List<PurchaseOrder>> = repository.orders

    fun getSavedCredentials(): Pair<String, String>? {
        val email = prefs.getString("saved_email", null)
        val pass = prefs.getString("saved_pass", null)
        return if (email != null && pass != null) email to pass else null
    }

    private fun saveCredentials(email: String, pass: String) {
        prefs.edit().putString("saved_email", email).putString("saved_pass", pass).apply()
    }

    private fun clearCredentials() {
        prefs.edit().remove("saved_email").remove("saved_pass").apply()
    }

    fun clearError() {
        _loginError.value = null
        _registerError.value = null
    }

    fun login(email: String, password: String, rememberMe: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginError.value = null
            val user = repository.getUser(email.trim())
            if (user != null) {
                if (user.password == password.trim()) {
                    _session.value = user
                    if (rememberMe) {
                        saveCredentials(email.trim(), password.trim())
                    } else {
                        clearCredentials()
                    }
                    onSuccess()
                } else {
                    _loginError.value = "Incorrect password"
                }
            } else {
                _loginError.value = "Account not found. Please register."
            }
        }
    }

    fun register(name: String, email: String, phone: String, location: String, password: String, role: UserRole, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _registerError.value = null
            
            if (name.isBlank() || email.isBlank() || phone.isBlank() || location.isBlank() || password.isBlank()) {
                _registerError.value = "Please fill all fields"
                return@launch
            }

            val existingUser = repository.getUser(email.trim())
            if (existingUser != null) {
                _registerError.value = "Email already registered"
                return@launch
            }

            val newUser = UserSession(
                name = name.trim(),
                email = email.trim(),
                phone = phone.trim(),
                location = location.trim(),
                password = password.trim(),
                role = role
            )

            repository.saveUser(newUser)
            _session.value = newUser
            onSuccess()
        }
    }

    fun logout() {
        _session.value = null
    }

    fun addProduct(itemName: String, category: ProductCategory, pricePerKg: Int, imageBase64: String?) {
        val seller = _session.value ?: return
        repository.addProduct(itemName, category, pricePerKg, seller, imageBase64)
    }

    fun removeProduct(productId: String) {
        repository.removeProduct(productId)
    }

    fun placeOrder(product: Product, quantity: String, deliveryTime: String) {
        val buyer = _session.value ?: return
        repository.placeOrder(product, quantity, deliveryTime, buyer)
    }
}
