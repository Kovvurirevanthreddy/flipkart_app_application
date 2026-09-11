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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.VibePost
import com.example.ui.components.formatIndianPrice
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartYellow

@Composable
fun VibesScreen(
  vibes: List<VibePost>,
  allProducts: List<Product>,
  onAddToCart: (Product) -> Unit,
  onProductClick: (Product) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF121212))
      .testTag("vibes_screen_root")
  ) {
    // Header
    Surface(
      color = Color(0xFF1F1F1F),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Flipkart Vibes",
          fontWeight = FontWeight.Black,
          fontSize = 18.sp,
          color = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "• Trending Video Feed",
          fontSize = 12.sp,
          color = FlipkartYellow
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize()
    ) {
      items(vibes) { vibe ->
        val linkedProduct = allProducts.find { it.id == vibe.productId }
        var isLiked by remember { mutableStateOf(false) }
        var likesCount by remember { mutableIntStateOf(vibe.likes) }

        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("vibe_card_${vibe.id}")
        ) {
          Column {
            // Simulated Short Video Player Canvas
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                  Brush.verticalGradient(
                    colors = listOf(Color(0xFF2C3E50), Color(0xFF000000))
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              // Play Icon & Sound
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(36.dp))
              }

              // Sound track tag
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .padding(12.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = FlipkartYellow, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = "Original Audio • Trending", color = Color.White, fontSize = 10.sp)
                }
              }

              // Vertical Action buttons: Like, Comment, Share
              Column(
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                // Like
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  IconButton(
                    onClick = {
                      isLiked = !isLiked
                      likesCount += if (isLiked) 1 else -1
                    },
                    modifier = Modifier
                      .size(40.dp)
                      .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Favorite,
                      contentDescription = "Like",
                      tint = if (isLiked) Color(0xFFFF4081) else Color.White,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                  Text(text = "$likesCount", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Comment
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  IconButton(
                    onClick = {},
                    modifier = Modifier
                      .size(40.dp)
                      .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                  ) {
                    Icon(imageVector = Icons.Default.Comment, contentDescription = "Comment", tint = Color.White, modifier = Modifier.size(20.dp))
                  }
                  Text(text = "184", color = Color.White, fontSize = 11.sp)
                }

                // Share
                IconButton(
                  onClick = {},
                  modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                  Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(20.dp))
                }
              }
            }

            // Creator & Caption
            Column(modifier = Modifier.padding(12.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(FlipkartBlue),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = vibe.creator.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = vibe.creator, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = vibe.caption,
                color = Color.LightGray,
                fontSize = 12.sp,
                lineHeight = 16.sp
              )

              // Shoppable Product Card Overlay
              if (linkedProduct != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFF2C2C2C),
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductClick(linkedProduct) }
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.weight(1f)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(42.dp)
                          .clip(RoundedCornerShape(6.dp))
                          .background(Color(0xFF3D3D3D)),
                        contentAlignment = Alignment.Center
                      ) {
                        Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = FlipkartYellow, modifier = Modifier.size(20.dp))
                      }

                      Spacer(modifier = Modifier.width(10.dp))

                      Column {
                        Text(
                          text = linkedProduct.title,
                          color = Color.White,
                          fontSize = 12.sp,
                          fontWeight = FontWeight.Bold,
                          maxLines = 1
                        )
                        Text(
                          text = "₹${formatIndianPrice(linkedProduct.price)}",
                          color = FlipkartYellow,
                          fontSize = 13.sp,
                          fontWeight = FontWeight.ExtraBold
                        )
                      }
                    }

                    Button(
                      onClick = { onAddToCart(linkedProduct) },
                      colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
                      shape = RoundedCornerShape(6.dp),
                      contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                      Text(text = "Add to Cart", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
