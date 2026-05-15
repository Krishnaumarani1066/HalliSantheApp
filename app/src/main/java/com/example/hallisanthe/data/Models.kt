package com.example.hallisanthe.data

import androidx.compose.ui.graphics.Color

enum class UserRole(val label: String) {
    Buyer("Buyer"),
    Seller("Seller")
}

enum class ProductCategory(
    val label: String,
    val accent: Color
) {
    Vegetables("Vegetables", Color(0xFF2E7D32)),
    Fruits("Fruits", Color(0xFFE86A33)),
    Grains("Grains", Color(0xFFB7791F)),
    Flowers("Flowers", Color(0xFFC2185B)),
    Groceries("Groceries", Color(0xFF1976D2))
}

data class UserSession(
    val name: String,
    val email: String,
    val phone: String,
    val location: String,
    val password: String,
    val role: UserRole
) {
    val initials: String
        get() = name
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercaseChar().toString() }
            .ifBlank { "HS" }
}

data class Product(
    val id: String,
    val itemName: String,
    val category: ProductCategory,
    val pricePerKg: Int,
    val sellerName: String,
    val sellerLocation: String,
    val sellerPhone: String = "0000000000",
    val imageSeed: Int,
    val base64Image: String? = null
)

data class PurchaseOrder(
    val id: String,
    val productId: String,
    val productName: String,
    val productCategory: ProductCategory,
    val quantity: String,
    val deliveryTime: String,
    val buyerName: String,
    val buyerPhone: String,
    val sellerName: String
)
