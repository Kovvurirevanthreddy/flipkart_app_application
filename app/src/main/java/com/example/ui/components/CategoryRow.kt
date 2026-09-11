package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryItem
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral

@Composable
fun CategoryRow(
  categories: List<CategoryItem>,
  selectedCategoryId: String,
  onCategorySelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = Color.White,
    modifier = modifier.fillMaxWidth()
  ) {
    LazyRow(
      contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(categories) { cat ->
        val isSelected = cat.id == selectedCategoryId
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onCategorySelected(cat.id) }
            .testTag("category_item_${cat.id}")
        ) {
          // Icon Circle
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (isSelected) FlipkartBlue else Color(0xFFF1F3F6)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = getCategoryIcon(cat.iconName),
              contentDescription = cat.name,
              tint = if (isSelected) Color.White else FlipkartBlue,
              modifier = Modifier.size(26.dp)
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = cat.name,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) FlipkartBlue else FlipkartDarkNeutral
          )

          Text(
            text = cat.tag,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF388E3C) // Flipkart green offer text
          )
        }
      }
    }
  }
}

fun getCategoryIcon(iconName: String): ImageVector {
  return when (iconName) {
    "smartphone" -> Icons.Default.Smartphone
    "laptop" -> Icons.Default.Laptop
    "checkroom" -> Icons.Default.Checkroom
    "chair" -> Icons.Default.Chair
    "kitchen" -> Icons.Default.Kitchen
    "shopping_basket" -> Icons.Default.ShoppingBasket
    else -> Icons.Default.Category
  }
}
