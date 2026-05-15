package com.example.hallisanthe.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class HalliSantheRepository {
    private var db: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        Log.e("HalliSanthe", "Firebase not initialized, using local storage only", e)
        null
    }

    private val usersCollection = db?.collection("users")
    private val productsCollection = db?.collection("products")
    private val ordersCollection = db?.collection("orders")

    // Use companion object to persist data across repository instances and ensure it's shared
    companion object {
        private val localUsers = mutableMapOf<String, UserSession>()
        private val localProducts = mutableListOf<Product>()
        private val localOrders = mutableListOf<PurchaseOrder>()
    }

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _orders = MutableStateFlow<List<PurchaseOrder>>(emptyList())
    val orders: StateFlow<List<PurchaseOrder>> = _orders.asStateFlow()

    init {
        observeProducts()
        observeOrders()
    }

    private fun observeProducts() {
        productsCollection?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("HalliSanthe", "Firestore products listen failed.", e)
                _products.value = localProducts
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val firestoreList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ProductDto::class.java)?.toProduct(doc.id)
                }
                // Merge Firestore data with local data, favoring Firestore IDs if they conflict
                _products.value = (firestoreList + localProducts).distinctBy { it.id }
            }
        } ?: run {
             _products.value = localProducts
        }
    }

    private fun observeOrders() {
        ordersCollection?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("HalliSanthe", "Firestore orders listen failed.", e)
                _orders.value = localOrders
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val firestoreList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(PurchaseOrderDto::class.java)?.toPurchaseOrder(doc.id)
                }
                _orders.value = (firestoreList + localOrders).distinctBy { it.id }
            }
        } ?: run {
            _orders.value = localOrders
        }
    }

    suspend fun getUser(email: String): UserSession? {
        val normalizedEmail = email.lowercase().trim()
        Log.d("HalliSanthe", "Attempting to get user: $normalizedEmail")
        
        // Try Firestore first with a 3-second timeout
        val firestoreUser = usersCollection?.let {
            try {
                withTimeoutOrNull(3000) {
                    val snapshot = it.document(normalizedEmail).get().await()
                    val user = snapshot.toObject(UserSessionDto::class.java)?.toUserSession()
                    Log.d("HalliSanthe", "Firestore user found: ${user != null}")
                    user
                }
            } catch (e: Exception) {
                Log.e("HalliSanthe", "Firestore error getting user: ${e.message}")
                null
            }
        }

        val finalUser = firestoreUser ?: localUsers[normalizedEmail]
        Log.d("HalliSanthe", "Final user result: ${finalUser?.name ?: "Not found"}")
        return finalUser
    }

    suspend fun saveUser(user: UserSession) {
        val normalizedEmail = user.email.lowercase().trim()
        Log.d("HalliSanthe", "Saving user locally and to Firestore: $normalizedEmail")
        
        // Always save locally first to ensure immediate functionality
        localUsers[normalizedEmail] = user
        
        try {
            // Try saving to Firestore with a 3-second timeout
            usersCollection?.let {
                withTimeoutOrNull(3000) {
                    it.document(normalizedEmail).set(UserSessionDto.from(user)).await()
                    Log.d("HalliSanthe", "User saved to Firestore successfully")
                } ?: Log.w("HalliSanthe", "Firestore save timed out, will rely on local data")
            }
        } catch (e: Exception) {
            Log.e("HalliSanthe", "Firestore error saving user: ${e.message}")
        }
    }

    fun addProduct(itemName: String, category: ProductCategory, pricePerKg: Int, seller: UserSession, imageBase64: String?) {
        val dto = ProductDto(
            itemName = itemName.trim(),
            category = category.name,
            pricePerKg = pricePerKg,
            sellerName = seller.name,
            sellerLocation = seller.location,
            sellerPhone = seller.phone,
            imageSeed = (1..100).random(),
            base64Image = imageBase64
        )
        
        if (productsCollection != null) {
            productsCollection.add(dto).addOnFailureListener {
                Log.e("HalliSanthe", "Failed to add product to Firestore, saving locally")
                saveProductLocally(dto)
            }
        } else {
            saveProductLocally(dto)
        }
    }

    private fun saveProductLocally(dto: ProductDto) {
        val local = dto.toProduct("local_${System.currentTimeMillis()}")
        localProducts.add(local)
        _products.value = (localProducts + _products.value).distinctBy { it.id }
    }

    fun removeProduct(productId: String) {
        if (productsCollection != null && !productId.startsWith("local_")) {
            productsCollection.document(productId).delete()
        } else {
            localProducts.removeAll { it.id == productId }
            _products.value = _products.value.filter { it.id != productId }
        }
    }

    fun placeOrder(product: Product, quantity: String, deliveryTime: String, buyer: UserSession) {
        val dto = PurchaseOrderDto(
            productId = product.id,
            productName = product.itemName,
            productCategory = product.category.name,
            quantity = quantity,
            deliveryTime = deliveryTime,
            buyerName = buyer.name,
            buyerPhone = buyer.phone,
            sellerName = product.sellerName
        )
        
        if (ordersCollection != null) {
            ordersCollection.add(dto).addOnFailureListener {
                Log.e("HalliSanthe", "Failed to place order in Firestore, saving locally")
                saveOrderLocally(dto)
            }
        } else {
            saveOrderLocally(dto)
        }
    }

    private fun saveOrderLocally(dto: PurchaseOrderDto) {
        val local = dto.toPurchaseOrder("local_order_${System.currentTimeMillis()}")
        localOrders.add(local)
        _orders.value = (localOrders + _orders.value).distinctBy { it.id }
    }
}

// Data Transfer Objects for Firestore
data class UserSessionDto(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val password: String = "",
    val role: String = ""
) {
    fun toUserSession() = UserSession(name, email, phone, location, password, UserRole.valueOf(role))
    companion object {
        fun from(u: UserSession) = UserSessionDto(u.name, u.email, u.phone, u.location, u.password, u.role.name)
    }
}

data class ProductDto(
    val itemName: String = "",
    val category: String = "",
    val pricePerKg: Int = 0,
    val sellerName: String = "",
    val sellerLocation: String = "",
    val sellerPhone: String = "",
    val imageSeed: Int = 0,
    val base64Image: String? = null
) {
    fun toProduct(docId: String) = Product(
        id = docId,
        itemName = itemName,
        category = ProductCategory.valueOf(category),
        pricePerKg = pricePerKg,
        sellerName = sellerName,
        sellerLocation = sellerLocation,
        sellerPhone = sellerPhone,
        imageSeed = imageSeed,
        base64Image = base64Image
    )
}

data class PurchaseOrderDto(
    val productId: String = "",
    val productName: String = "",
    val productCategory: String = "",
    val quantity: String = "",
    val deliveryTime: String = "",
    val buyerName: String = "",
    val buyerPhone: String = "",
    val sellerName: String = ""
) {
    fun toPurchaseOrder(docId: String) = PurchaseOrder(
        id = docId,
        productId = productId,
        productName = productName,
        productCategory = ProductCategory.valueOf(productCategory),
        quantity = quantity,
        deliveryTime = deliveryTime,
        buyerName = buyerName,
        buyerPhone = buyerPhone,
        sellerName = sellerName
    )
}
