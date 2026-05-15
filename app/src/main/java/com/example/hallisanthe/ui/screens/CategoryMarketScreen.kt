package com.example.hallisanthe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.ProductCategory
import com.example.hallisanthe.ui.components.*
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.EarthWhite
import com.example.hallisanthe.ui.theme.TextMuted
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryMarketScreen(
    category: ProductCategory,
    products: List<Product>,
    onBack: () -> Unit,
    onPlaceOrder: (Product, String, String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        containerColor = EarthWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(category.label, fontWeight = FontWeight.Bold)
                        Text("Village Market", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = DeepGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = EarthWhite)
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    "Fresh ${category.label} from nearby farms",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onOrder = { selectedProduct = product }
                )
            }

            if (products.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                        Text("No ${category.label.lowercase()} currently available.", color = TextMuted)
                    }
                }
            }
        }
    }

    selectedProduct?.let { product ->
        PurchaseDialog(
            product = product,
            onDismiss = { selectedProduct = null },
            onConfirm = { qty: String, time: String ->
                onPlaceOrder(product, qty, time)
                selectedProduct = null
                scope.launch {
                    snackbarHostState.showSnackbar("Request sent for ${product.itemName}")
                }
            }
        )
    }
}
