package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDestination
import com.example.data.model.CategoryItem
import com.example.data.model.Product
import com.example.data.model.UserProfileEntity
import com.example.data.model.WishlistItemEntity
import com.example.ui.components.BannerCarousel
import com.example.ui.components.CategoryRow
import com.example.ui.components.ProductCard
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartYellow
import com.example.ui.theme.PlusCoinYellow

@Composable
fun HomeScreen(
  categories: List<CategoryItem>,
  selectedCategory: String,
  products: List<Product>,
  wishlistItems: List<WishlistItemEntity>,
  userProfile: UserProfileEntity?,
  onCategorySelected: (String) -> Unit,
  onProductClick: (Product) -> Unit,
  onWishlistToggle: (Product) -> Unit,
  onAddToCart: (Product) -> Unit,
  onNavigate: (AppDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  val wishlistedIds = wishlistItems.map { it.productId }.toSet()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .testTag("home_screen_feed")
  ) {
    // 1. Categories Row
    item {
      CategoryRow(
        categories = categories,
        selectedCategoryId = selectedCategory,
        onCategorySelected = onCategorySelected
      )
    }

    // 2. Promotional Festival Banner Carousel
    item {
      BannerCarousel(
        onBannerClick = {
          onNavigate(AppDestination.SEARCH)
        }
      )
    }

    // 3. SuperCoins & Plus Strip
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2027)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.MonetizationOn,
              contentDescription = "SuperCoins",
              tint = PlusCoinYellow,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "${userProfile?.superCoins ?: 420} SuperCoins Available",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Text(
                text = "Use coins for extra discounts on checkout",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = FlipkartYellow,
            modifier = Modifier.clickable { onNavigate(AppDestination.ACCOUNT) }
          ) {
            Text(
              text = "Coin Zone",
              color = Color(0xFF212121),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }
        }
      }
    }

    // 4. Quick Portal Switcher (Seller Hub & Admin Console)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigate(AppDestination.SELLER_HUB) }
            .testTag("portal_seller_hub")
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(FlipkartBlue.copy(alpha = 0.1f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = FlipkartBlue,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(text = "Seller Hub", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
              Text(text = "Manage Inventory", fontSize = 9.sp, color = Color.Gray)
            }
          }
        }

        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigate(AppDestination.ADMIN_DASHBOARD) }
            .testTag("portal_admin_dashboard")
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(text = "Admin Console", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
              Text(text = "Platform Analytics", fontSize = 9.sp, color = Color.Gray)
            }
          }
        }
      }
    }

    // 5. Deals of the Day Header & Horizontal Showcase
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp)
          .background(Color.White)
          .padding(vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Deals of the Day",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = FlipkartDarkNeutral
            )
            Text(
              text = "Handpicked special offers with instant bank discounts",
              fontSize = 11.sp,
              color = Color.Gray
            )
          }

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = FlipkartBlue,
            modifier = Modifier.clickable { onNavigate(AppDestination.SEARCH) }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "View All",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(3.dp))
              Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Products Carousel
        LazyRow(
          contentPadding = PaddingValues(horizontal = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(products) { product ->
            Box(modifier = Modifier.width(200.dp)) {
              ProductCard(
                product = product,
                isWishlisted = wishlistedIds.contains(product.id),
                onProductClick = { onProductClick(product) },
                onWishlistClick = { onWishlistToggle(product) },
                onAddToCartClick = { onAddToCart(product) }
              )
            }
          }
        }
      }
    }

    // 6. Section: Suggested for You (2-column grid format)
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 12.dp)
          .background(Color.White)
          .padding(14.dp)
      ) {
        Text(
          text = "Suggested For You",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = FlipkartDarkNeutral
        )
        Text(
          text = "Based on your browsing history & trending purchases",
          fontSize = 11.sp,
          color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2-column paired layout
        val chunkedProducts = products.chunked(2)
        chunkedProducts.forEach { pair ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(modifier = Modifier.weight(1f)) {
              ProductCard(
                product = pair[0],
                isWishlisted = wishlistedIds.contains(pair[0].id),
                onProductClick = { onProductClick(pair[0]) },
                onWishlistClick = { onWishlistToggle(pair[0]) },
                onAddToCartClick = { onAddToCart(pair[0]) }
              )
            }

            if (pair.size > 1) {
              Box(modifier = Modifier.weight(1f)) {
                ProductCard(
                  product = pair[1],
                  isWishlisted = wishlistedIds.contains(pair[1].id),
                  onProductClick = { onProductClick(pair[1]) },
                  onWishlistClick = { onWishlistToggle(pair[1]) },
                  onAddToCartClick = { onAddToCart(pair[1]) }
                )
              }
            } else {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
