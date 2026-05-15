package com.example.hallisanthe.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hallisanthe.data.Product
import com.example.hallisanthe.data.ProductCategory
import com.example.hallisanthe.ui.theme.DeepGreen
import com.example.hallisanthe.ui.theme.EarthWhite
import com.example.hallisanthe.ui.theme.SoftGray
import com.example.hallisanthe.ui.theme.TextMuted

@Composable
fun ProductCard(
    product: Product,
    onOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            ProduceImage(
                product = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = product.itemName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.category.label,
                color = product.category.accent,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Rs ${product.pricePerKg}/kg",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = DeepGreen
            )
            Text(
                text = "Seller: ${product.sellerName}",
                color = TextMuted,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onOrder,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepGreen)
            ) {
                Icon(Icons.Rounded.ShoppingBasket, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("Add", modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: ProductCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = category.accent.copy(alpha = 0.1f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(category.icon, contentDescription = null, tint = category.accent, modifier = Modifier.size(20.dp))
            Text(text = category.label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun InventoryRow(
    product: Product,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProduceImage(product, modifier = Modifier.size(64.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(product.itemName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text("Rs ${product.pricePerKg}/kg - ${product.category.label}", color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
            }
        }
    }
}

@Composable
private fun ProduceImage(
    product: Product,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    listOf(product.category.accent.copy(alpha = 0.15f), SoftGray.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (!product.base64Image.isNullOrBlank()) {
            val bitmap = remember(product.base64Image) {
                try {
                    val imageBytes = Base64.decode(product.base64Image, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = product.itemName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                CategoryPlaceholder(product.category)
            }
        } else {
            CategoryPlaceholder(product.category)
        }
    }
}

@Composable
private fun CategoryPlaceholder(category: ProductCategory) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(EarthWhite, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = null,
            tint = category.accent,
            modifier = Modifier.size(32.dp)
        )
    }
}

val ProductCategory.icon: ImageVector
    get() = when (this) {
        ProductCategory.Vegetables -> Icons.Rounded.Spa
        ProductCategory.Fruits -> Icons.Rounded.Agriculture
        ProductCategory.Grains -> Icons.Rounded.RiceBowl
        ProductCategory.Flowers -> Icons.Rounded.LocalFlorist
        ProductCategory.Groceries -> Icons.Rounded.ShoppingBasket
    }
