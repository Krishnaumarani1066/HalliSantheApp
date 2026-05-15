package com.example.hallisanthe.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.ProductCategory
import com.example.hallisanthe.data.PurchaseOrder
import com.example.hallisanthe.data.UserSession
import com.example.hallisanthe.ui.components.AppLogo
import com.example.hallisanthe.ui.components.InventoryRow
import com.example.hallisanthe.ui.components.UserPill
import com.example.hallisanthe.ui.theme.*
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun SellerDashboard(
    session: UserSession,
    products: List<Product>,
    orders: List<PurchaseOrder>,
    onProfile: () -> Unit,
    onAddProduct: (String, ProductCategory, Int, String?) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onSeeAllMarket: () -> Unit
) {
    val myProducts = products.filter { it.sellerName == session.name }
    val myOrders = orders.filter { it.sellerName == session.name }

    Scaffold(containerColor = EarthWhite) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SellerHeader(session, onProfile)
            }
            
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatsSummaryCard(
                        modifier = Modifier.weight(1f),
                        count = myProducts.size,
                        label = "My Products",
                        icon = Icons.Rounded.Inventory2
                    )
                    StatsSummaryCard(
                        modifier = Modifier.weight(1f),
                        count = myOrders.size,
                        label = "New Orders",
                        icon = Icons.Rounded.NotificationsActive,
                        containerColor = Color(0xFFE65100)
                    )
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = onSeeAllMarket,
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = DeepGreen),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(Icons.Rounded.Storefront, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("See Market")
                    }
                    Button(
                        onClick = { /* Scroll or focus logic could go here */ },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add New")
                    }
                }
            }

            item {
                AddProductCard(onAddProduct = onAddProduct)
            }

            if (myOrders.isNotEmpty()) {
                item {
                    Text("Purchase Inquiries", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                items(myOrders) { order ->
                    OrderInquiryCard(order)
                }
            }

            item {
                Column {
                    Text("My Listed Items", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        if (myProducts.isEmpty()) "You haven't listed any items yet."
                        else "Managing ${myProducts.size} items in the market",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            items(myProducts, key = { it.id }) { product ->
                InventoryRow(product = product, onDelete = { onDeleteProduct(product.id) })
            }
            
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun OrderInquiryCard(order: PurchaseOrder) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = order.productCategory.accent.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Icon(order.productCategory.icon, contentDescription = null, tint = order.productCategory.accent)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(order.productName, fontWeight = FontWeight.Bold)
                Text("Qty: ${order.quantity}", style = MaterialTheme.typography.bodySmall)
                Text("Buyer: ${order.buyerName}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Text("Time: ${order.deliveryTime}", style = MaterialTheme.typography.bodySmall, color = DeepGreen)
            }
            IconButton(
                onClick = { /* tel: link */ },
                colors = IconButtonDefaults.iconButtonColors(containerColor = SoftMint, contentColor = DeepGreen)
            ) {
                Icon(Icons.Rounded.Phone, contentDescription = "Call Buyer")
            }
        }
    }
}

@Composable
private fun StatsSummaryCard(
    count: Int,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = DeepGreen
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(count.toString(), color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun SellerHeader(
    session: UserSession,
    onProfile: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppLogo(size = 48.dp)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text("Seller Panel", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            Text(session.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        UserPill(name = session.name, onClick = onProfile)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddProductCard(
    onAddProduct: (String, ProductCategory, Int, String?) -> Unit
) {
    var itemName by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBase64 by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                val byteArray = outputStream.toByteArray()
                imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Rounded.AddCircleOutline, contentDescription = null, tint = DeepGreen)
                Text("List New Product", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftGray.copy(alpha = 0.3f))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    val bitmap = remember(imageUri) {
                        try {
                            val inputStream = context.contentResolver.openInputStream(imageUri!!)
                            BitmapFactory.decodeStream(inputStream).asImageBitmap()
                        } catch (e: Exception) {
                            null
                        }
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { imageUri = null; imageBase64 = null },
                        modifier = Modifier.align(Alignment.TopEnd).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = null, tint = Color.White)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.AddPhotoAlternate, contentDescription = null, tint = DeepGreen, modifier = Modifier.size(40.dp))
                        Text("Add Product Image", color = DeepGreen, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
            
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Product Name") },
                placeholder = { Text("e.g. Fresh Carrots") },
                leadingIcon = { Icon(Icons.Rounded.ShoppingBag, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter(Char::isDigit) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Price") },
                    prefix = { Text("₹ ") },
                    suffix = { Text("/kg") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1.2f)
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.label ?: "Select Category",
                        onValueChange = {},
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ProductCategory.entries.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.label) },
                                onClick = {
                                    selectedCategory = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val cat = selectedCategory
                    if (itemName.isNotBlank() && price.isNotBlank() && cat != null) {
                        onAddProduct(itemName, cat, price.toIntOrNull() ?: 0, imageBase64)
                        itemName = ""
                        price = ""
                        selectedCategory = null
                        imageUri = null
                        imageBase64 = null
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreen),
                enabled = itemName.isNotBlank() && price.isNotBlank() && selectedCategory != null
            ) {
                Icon(Icons.Rounded.CloudUpload, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Publish Listing", fontWeight = FontWeight.Bold)
            }
        }
    }
}
