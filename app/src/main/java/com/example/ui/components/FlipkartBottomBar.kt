package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDestination
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartYellow

@Composable
fun FlipkartBottomBar(
  currentDestination: AppDestination,
  cartCount: Int,
  onNavigate: (AppDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = Color.White,
    tonalElevation = 8.dp,
    modifier = modifier.testTag("flipkart_bottom_navigation")
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentDestination == AppDestination.HOME,
      onClick = { onNavigate(AppDestination.HOME) },
      icon = {
        Icon(imageVector = Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(24.dp))
      },
      label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = FlipkartBlue,
        selectedTextColor = FlipkartBlue,
        indicatorColor = Color(0xFFE8F1FF),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575)
      ),
      modifier = Modifier.testTag("nav_item_home")
    )

    // 2. Categories / Search
    NavigationBarItem(
      selected = currentDestination == AppDestination.SEARCH,
      onClick = { onNavigate(AppDestination.SEARCH) },
      icon = {
        Icon(imageVector = Icons.Default.Category, contentDescription = "Categories", modifier = Modifier.size(24.dp))
      },
      label = { Text("Categories", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = FlipkartBlue,
        selectedTextColor = FlipkartBlue,
        indicatorColor = Color(0xFFE8F1FF),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575)
      ),
      modifier = Modifier.testTag("nav_item_categories")
    )

    // 3. Vibes (Short Videos & Feeds)
    NavigationBarItem(
      selected = currentDestination == AppDestination.VIBES,
      onClick = { onNavigate(AppDestination.VIBES) },
      icon = {
        Icon(imageVector = Icons.Default.OndemandVideo, contentDescription = "Vibes", modifier = Modifier.size(24.dp))
      },
      label = { Text("Vibes", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = FlipkartBlue,
        selectedTextColor = FlipkartBlue,
        indicatorColor = Color(0xFFE8F1FF),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575)
      ),
      modifier = Modifier.testTag("nav_item_vibes")
    )

    // 4. Account
    NavigationBarItem(
      selected = currentDestination == AppDestination.ACCOUNT,
      onClick = { onNavigate(AppDestination.ACCOUNT) },
      icon = {
        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Account", modifier = Modifier.size(24.dp))
      },
      label = { Text("Account", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = FlipkartBlue,
        selectedTextColor = FlipkartBlue,
        indicatorColor = Color(0xFFE8F1FF),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575)
      ),
      modifier = Modifier.testTag("nav_item_account")
    )

    // 5. Cart
    NavigationBarItem(
      selected = currentDestination == AppDestination.CART,
      onClick = { onNavigate(AppDestination.CART) },
      icon = {
        BadgedBox(
          badge = {
            if (cartCount > 0) {
              Badge(
                containerColor = FlipkartYellow,
                contentColor = FlipkartDarkNeutral
              ) {
                Text(text = cartCount.toString(), fontWeight = FontWeight.Bold, fontSize = 10.sp)
              }
            }
          }
        ) {
          Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", modifier = Modifier.size(24.dp))
        }
      },
      label = { Text("Cart", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = FlipkartBlue,
        selectedTextColor = FlipkartBlue,
        indicatorColor = Color(0xFFE8F1FF),
        unselectedIconColor = Color(0xFF757575),
        unselectedTextColor = Color(0xFF757575)
      ),
      modifier = Modifier.testTag("nav_item_cart")
    )
  }
}
