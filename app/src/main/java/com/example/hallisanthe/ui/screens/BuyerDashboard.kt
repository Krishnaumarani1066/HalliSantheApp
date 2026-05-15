package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.ProductCategory
import com.example.hallisanthe.data.UserSession
import com.example.hallisanthe.ui.components.*
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.EarthWhite
import com.example.hallisanthe.ui.theme.TextMuted

@Composable
fun BuyerDashboard(
    session: UserSession,
    products: List<Product>,
    onProfile: () -> Unit,
    onPlaceOrder: (Product, String, String) -> Unit,
    onCategoryClick: (ProductCategory) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = products.filter {
        it.itemName.contains(searchQuery, ignoreCase = true) ||
                it.sellerLocation.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = EarthWhite,
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                BuyerHeader(session, onProfile)
                Spacer(Modifier.height(12.dp))
                SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                TickerTape(text = "Fresh Tomato at ₹20/kg in Mandya Market • Organic Carrots arriving from Ooty •")
            }

            item {
                Text(
                    "Categories",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ProductCategory.entries) { category ->
                        CategoryChip(category = category, onClick = { onCategoryClick(category) })
                    }
                }
            }

            item {
                Text(
                    "Fresh Arrivals",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    filteredProducts.chunked(2).forEach { rowProducts ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowProducts.forEach { product ->
                                var showDialog by remember { mutableStateOf(false) }
                                
                                ProductCard(
                                    product = product,
                                    onOrder = { showDialog = true },
                                    modifier = Modifier.weight(1f)
                                )

                                if (showDialog) {
                                    PurchaseDialog(
                                        product = product,
                                        onDismiss = { showDialog = false },
                                        onConfirm = { qty: String, time: String ->
                                            onPlaceOrder(product, qty, time)
                                            showDialog = false
                                        }
                                    )
                                }
                            }
                            if (rowProducts.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                    if (filteredProducts.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No products found", color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BuyerHeader(session: UserSession, onProfile: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppLogo(size = 40.dp)
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text("Deliver to", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            Text(session.location, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        UserPill(name = session.name, onClick = onProfile)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search vegetables, fruits...") },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = DeepGreen) },
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = DeepGreen,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true
    )
}
