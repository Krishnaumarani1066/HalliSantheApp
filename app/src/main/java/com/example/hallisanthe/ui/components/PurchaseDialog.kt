package com.example.hallisanthe.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.TextMuted

@Composable
fun PurchaseDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var deliveryTime by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Purchase Inquiry", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                
                ProductDetailSmall(product)

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("How much quantity u want?") },
                    placeholder = { Text("e.g. 5 kg") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = deliveryTime,
                    onValueChange = { deliveryTime = it },
                    label = { Text("When u will want?") },
                    placeholder = { Text("e.g. Tomorrow morning") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { /* tel: link normally */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Icon(Icons.Rounded.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Call")
                    }
                    Button(
                        onClick = { onConfirm(quantity, deliveryTime) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepGreen),
                        enabled = quantity.isNotBlank() && deliveryTime.isNotBlank()
                    ) {
                        Text("Request")
                    }
                }
                
                TextButton(onClick = onDismiss) {
                    Text("Close", color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun ProductDetailSmall(product: Product) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(12.dp),
            color = product.category.accent.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(product.category.icon, contentDescription = null, tint = product.category.accent)
            }
        }
        Column {
            Text(product.itemName, fontWeight = FontWeight.Bold)
            Text("Price: Rs ${product.pricePerKg}/kg", color = DeepGreen, fontWeight = FontWeight.Bold)
            Text("Seller: ${product.sellerName}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
    }
}
