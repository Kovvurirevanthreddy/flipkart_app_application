package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartYellow

@Composable
fun FlipkartHeader(
  cartCount: Int,
  selectedLanguage: String,
  onSearchClick: () -> Unit,
  onVoiceSearchClick: () -> Unit,
  onCartClick: () -> Unit,
  onLanguageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = FlipkartBlue,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      // Top bar: Logo + Plus + Language + Notifications + Cart
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Flipkart Brand Logo
        Row(verticalAlignment = Alignment.CenterVertically) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Flipkart",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "✦",
                color = FlipkartYellow,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Explore ",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Italic
              )
              Text(
                text = "Plus",
                color = FlipkartYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
              )
              Spacer(modifier = Modifier.width(2.dp))
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(FlipkartYellow, CircleShape)
              )
            }
          }
        }

        // Action Icons: Language Pill + Notification + Cart
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Language Switcher Pill
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(Color.White.copy(alpha = 0.18f))
              .clickable(onClick = onLanguageClick)
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Translate,
              contentDescription = "Language",
              tint = Color.White,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = selectedLanguage.take(3),
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          // Cart with Badge
          IconButton(
            onClick = onCartClick,
            modifier = Modifier
              .size(40.dp)
              .testTag("cart_icon_button")
          ) {
            BadgedBox(
              badge = {
                if (cartCount > 0) {
                  Badge(
                    containerColor = FlipkartYellow,
                    contentColor = Color(0xFF212121)
                  ) {
                    Text(
                      text = cartCount.toString(),
                      fontWeight = FontWeight.Bold,
                      fontSize = 10.sp
                    )
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // High-Density Flipkart Search Bar
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .clickable(onClick = onSearchClick)
          .testTag("header_search_bar")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = Color(0xFF878787),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Search for Products, Brands and More",
              color = Color(0xFF878787),
              fontSize = 13.sp,
              maxLines = 1
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Voice Search",
              tint = FlipkartBlue,
              modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onVoiceSearchClick)
            )
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = "Visual Search",
              tint = Color(0xFF878787),
              modifier = Modifier.size(19.dp)
            )
          }
        }
      }
    }
  }
}
