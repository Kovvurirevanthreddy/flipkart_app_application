package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.WishlistItemEntity
import com.example.ui.components.formatIndianPrice
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen

@Composable
fun WishlistScreen(
  wishlistItems: List<WishlistItemEntity>,
  allProducts: List<Product>,
  onRemoveWishlist: (Product) -> Unit,
  onAddToCart: (Product) -> Unit,
  onProductClick: (Product) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .testTag("wishlist_screen_root")
  ) {
    // Top Bar
    Surface(
      color = FlipkartBlue,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack) {
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Text(
          text = "My Wishlist (${wishlistItems.size})",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp
        )
      }
    }

    if (wishlistItems.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(text = "Your Wishlist is Empty", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Tap the heart icon on any product to save it here", fontSize = 12.sp, color = Color.Gray)
        }
      }
    } else {
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(wishlistItems) { wishItem ->
          val product = allProducts.find { it.id == wishItem.productId }

          Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 6.dp)
              .testTag("wishlist_card_${wishItem.productId}")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { product?.let { onProductClick(it) } }
              ) {
                Box(
                  modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF1F3F6)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = wishItem.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = FlipkartDarkNeutral,
                    maxLines = 2
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(text = "₹${formatIndianPrice(wishItem.price)}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (wishItem.originalPrice > wishItem.price) {
                      Text(
                        text = "₹${formatIndianPrice(wishItem.originalPrice)}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                      )
                      Text(
                        text = "${((wishItem.originalPrice - wishItem.price) * 100 / wishItem.originalPrice)}% off",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FlipkartGreen
                      )
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                TextButton(
                  onClick = {
                    product?.let { onRemoveWishlist(it) }
                  },
                  colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                ) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = "Remove", fontSize = 12.sp)
                }

                Button(
                  onClick = {
                    product?.let { onAddToCart(it) }
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(text = "Move to Cart", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }
}
