package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen
import com.example.ui.theme.FlipkartYellow

@Composable
fun ProductCard(
  product: Product,
  isWishlisted: Boolean,
  onProductClick: () -> Unit,
  onWishlistClick: () -> Unit,
  onAddToCartClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onProductClick)
      .testTag("product_card_${product.id}")
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      // Top Area: Product Visual Representation & Wishlist Button
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xFFF9F9F9)),
        contentAlignment = Alignment.Center
      ) {
        // High fidelity icon illustration representing the product
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(FlipkartBlue.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = getProductIcon(product.category),
              contentDescription = product.title,
              tint = FlipkartBlue,
              modifier = Modifier.size(32.dp)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = product.brand.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF878787)
          )
        }

        // Wishlist Heart Icon
        IconButton(
          onClick = onWishlistClick,
          modifier = Modifier
            .align(Alignment.TopEnd)
            .size(34.dp)
            .testTag("wishlist_btn_${product.id}")
        ) {
          Icon(
            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Wishlist",
            tint = if (isWishlisted) Color(0xFFD32F2F) else Color(0xFF9E9E9E),
            modifier = Modifier.size(20.dp)
          )
        }

        // Assured Badge on top-left if applicable
        if (product.isAssured) {
          Surface(
            shape = RoundedCornerShape(topStart = 6.dp, bottomEnd = 6.dp),
            color = FlipkartBlue,
            modifier = Modifier.align(Alignment.TopStart)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "f",
                color = FlipkartYellow,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
              )
              Text(
                text = " Assured",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Product Title
      Text(
        text = product.title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = FlipkartDarkNeutral,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Ratings Pill
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = FlipkartGreen
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = String.format("%.1f", product.rating),
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Rating",
              tint = Color.White,
              modifier = Modifier.size(10.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
          text = "(${product.ratingsCount})",
          fontSize = 11.sp,
          color = Color(0xFF878787)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Pricing Row
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Text(
          text = "₹${formatIndianPrice(product.price)}",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = FlipkartDarkNeutral
        )

        if (product.originalPrice > product.price) {
          Text(
            text = "₹${formatIndianPrice(product.originalPrice)}",
            fontSize = 12.sp,
            color = Color(0xFF878787),
            textDecoration = TextDecoration.LineThrough
          )

          Text(
            text = "${product.discountPercent}% off",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartGreen
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Free Delivery Tag
      Text(
        text = "Free delivery by Flipkart Plus",
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF388E3C)
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Quick Add to Cart button
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFF1F3F6),
        modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onAddToCartClick)
          .testTag("add_to_cart_${product.id}")
      ) {
        Row(
          modifier = Modifier.padding(vertical = 7.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = "Add to Cart",
            tint = FlipkartBlue,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Add to Cart",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartBlue
          )
        }
      }
    }
  }
}

fun formatIndianPrice(amount: Int): String {
  return String.format("%,d", amount)
}

fun getProductIcon(category: String): androidx.compose.ui.graphics.vector.ImageVector {
  return when (category.lowercase()) {
    "electronics" -> getCategoryIcon("laptop")
    "mobiles" -> getCategoryIcon("smartphone")
    "fashion" -> getCategoryIcon("checkroom")
    "home" -> getCategoryIcon("chair")
    "appliances" -> getCategoryIcon("kitchen")
    "grocery" -> getCategoryIcon("shopping_basket")
    else -> getCategoryIcon("category")
  }
}
