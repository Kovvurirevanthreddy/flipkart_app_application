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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDestination
import com.example.data.model.UserProfileEntity
import com.example.ui.components.formatIndianPrice
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartYellow
import com.example.ui.theme.PlusCoinYellow

@Composable
fun AccountScreen(
  userProfile: UserProfileEntity?,
  onNavigate: (AppDestination) -> Unit,
  onOpenLanguagePicker: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .verticalScroll(rememberScrollState())
      .testTag("account_screen_root")
  ) {
    // 1. Profile Top Banner
    Surface(
      color = FlipkartBlue,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = (userProfile?.name?.take(1) ?: "R"),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Text(
                text = userProfile?.name ?: "Rahul Sharma",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
              )
              Text(
                text = userProfile?.phone ?: "+91 98765 43210",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
              )
              Text(
                text = userProfile?.email ?: "rahul.sharma@example.com",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = FlipkartYellow
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Plus Gold",
                color = Color(0xFF212121),
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }

    // 2. SuperCoins & Pay Later Dual Cards
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // SuperCoins Card
      Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.weight(1f)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = PlusCoinYellow, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "SuperCoins", fontSize = 12.sp, color = Color.Gray)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${userProfile?.superCoins ?: 420}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )
        }
      }

      // Flipkart Pay Later Card
      Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.weight(1f)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CreditCard, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Pay Later", fontSize = 12.sp, color = Color.Gray)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "₹${formatIndianPrice(userProfile?.payLaterLimit ?: 25000)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )
        }
      }
    }

    // 3. Main Navigation Actions
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column {
        AccountOptionRow(
          icon = Icons.Default.LocalShipping,
          title = "Orders",
          subtitle = "Check your order history and track deliveries",
          onClick = { onNavigate(AppDestination.ORDERS) }
        )

        HorizontalDivider(color = Color(0xFFF1F3F6))

        AccountOptionRow(
          icon = Icons.Default.Favorite,
          title = "Wishlist",
          subtitle = "Your curated saved items and price alerts",
          onClick = { onNavigate(AppDestination.WISHLIST) }
        )

        HorizontalDivider(color = Color(0xFFF1F3F6))

        AccountOptionRow(
          icon = Icons.Default.PinDrop,
          title = "Saved Addresses",
          subtitle = "Manage delivery locations (Home, Work, Other)",
          onClick = { onNavigate(AppDestination.CHECKOUT) }
        )

        HorizontalDivider(color = Color(0xFFF1F3F6))

        AccountOptionRow(
          icon = Icons.Default.Language,
          title = "Choose Language (${userProfile?.selectedLanguage ?: "English"})",
          subtitle = "Vernacular: English, हिन्दी, தமிழ், తెలుగు, বাংলা, मराठी",
          onClick = onOpenLanguagePicker
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // 4. Enterprise Roles (Seller Hub & Admin Console)
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column {
        AccountOptionRow(
          icon = Icons.Default.Storefront,
          title = "Seller Hub & Inventory Management",
          subtitle = "List new products, manage stock counts, view sales analytics",
          onClick = { onNavigate(AppDestination.SELLER_HUB) }
        )

        HorizontalDivider(color = Color(0xFFF1F3F6))

        AccountOptionRow(
          icon = Icons.Default.AdminPanelSettings,
          title = "Admin Platform Dashboard",
          subtitle = "Gross GMV, platform commission setting, flash sales toggle",
          onClick = { onNavigate(AppDestination.ADMIN_DASHBOARD) }
        )

        HorizontalDivider(color = Color(0xFFF1F3F6))

        AccountOptionRow(
          icon = Icons.Default.HelpCenter,
          title = "24x7 Customer Help Centre",
          subtitle = "Quick resolution for payments, refunds, and delivery queries",
          onClick = {}
        )
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

@Composable
fun AccountOptionRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = FlipkartBlue,
      modifier = Modifier.size(22.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = FlipkartDarkNeutral
      )
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = Color.Gray
      )
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = Color(0xFFBDBDBD),
      modifier = Modifier.size(14.dp)
    )
  }
}
