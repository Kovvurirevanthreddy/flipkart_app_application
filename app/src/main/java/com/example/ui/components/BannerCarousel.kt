package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartYellow
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel(
  onBannerClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var activeIndex by remember { mutableIntStateOf(0) }
  val totalBanners = 2

  LaunchedEffect(Unit) {
    while (true) {
      delay(4000)
      activeIndex = (activeIndex + 1) % totalBanners
    }
  }

  Column(modifier = modifier.fillMaxWidth()) {
    // Flash Sale Timer Pill Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFFFF7E6))
        .padding(horizontal = 16.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.ElectricBolt,
          contentDescription = "Flash Sale",
          tint = Color(0xFFE65100),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "BIG BILLION DEALS",
          fontWeight = FontWeight.ExtraBold,
          fontSize = 11.sp,
          color = Color(0xFFE65100)
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Timer,
          contentDescription = "Ends in",
          tint = Color(0xFF424242),
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Ends in 08h : 24m : 15s",
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp,
          color = Color(0xFF424242)
        )
      }
    }

    // Carousel Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .clickable { onBannerClick(activeIndex) },
      shape = RoundedCornerShape(12.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f)
      ) {
        // Banner Image
        val bannerDrawable = if (activeIndex == 0) {
          R.drawable.flipkart_sale_banner_1789132225160
        } else {
          R.drawable.flipkart_fashion_banner_1789132241943
        }

        Image(
          painter = painterResource(id = bannerDrawable),
          contentDescription = "Promotional Festival Banner",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Overlay Gradient
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
              )
            )
        )

        // Indicator Dots
        Row(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 10.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          repeat(totalBanners) { index ->
            Box(
              modifier = Modifier
                .size(if (index == activeIndex) 18.dp to 6.dp else 6.dp to 6.dp)
                .clip(CircleShape)
                .background(if (index == activeIndex) FlipkartYellow else Color.White.copy(alpha = 0.6f))
            )
          }
        }
      }
    }
  }
}

private fun Modifier.size(sizePair: Pair<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp>): Modifier {
  return this
    .width(sizePair.first)
    .height(sizePair.second)
}
