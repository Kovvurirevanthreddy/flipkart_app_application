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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.WishlistItemEntity
import com.example.ui.components.ProductCard
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen

@Composable
fun SearchScreen(
  query: String,
  products: List<Product>,
  wishlistItems: List<WishlistItemEntity>,
  minRating: Float,
  assuredOnly: Boolean,
  inStockOnly: Boolean,
  sortBy: String,
  onQueryChanged: (String) -> Unit,
  onUpdateFilters: (minRating: Float, maxPrice: Int, assuredOnly: Boolean, inStockOnly: Boolean, sort: String) -> Unit,
  onResetFilters: () -> Unit,
  onVoiceSearchClick: () -> Unit,
  onProductClick: (Product) -> Unit,
  onWishlistToggle: (Product) -> Unit,
  onAddToCart: (Product) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val focusManager = LocalFocusManager.current
  val wishlistedIds = wishlistItems.map { it.productId }.toSet()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .testTag("search_screen_root")
  ) {
    // 1. Search Bar Top Header
    Surface(
      color = FlipkartBlue,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }

        OutlinedTextField(
          value = query,
          onValueChange = onQueryChanged,
          placeholder = { Text("Search products, brands...", fontSize = 13.sp, color = Color.Gray) },
          singleLine = true,
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
          ),
          trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }, modifier = Modifier.size(28.dp)) {
                  Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                }
              }
              IconButton(onClick = onVoiceSearchClick, modifier = Modifier.size(28.dp)) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = "Voice Search", tint = FlipkartBlue)
              }
            }
          },
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .testTag("search_input_field")
        )
      }
    }

    // 2. Dynamic Filters & Sorting Row
    Surface(
      color = Color.White,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Sort Chip
        item {
          FilterChip(
            selected = sortBy != "POPULARITY",
            onClick = {
              val nextSort = when (sortBy) {
                "POPULARITY" -> "PRICE_LOW"
                "PRICE_LOW" -> "PRICE_HIGH"
                "PRICE_HIGH" -> "RATING"
                else -> "POPULARITY"
              }
              onUpdateFilters(minRating, 100000, assuredOnly, inStockOnly, nextSort)
            },
            label = {
              Text(
                text = when (sortBy) {
                  "PRICE_LOW" -> "Price: Low to High"
                  "PRICE_HIGH" -> "Price: High to Low"
                  "RATING" -> "Rating: High to Low"
                  else -> "Sort by: Popularity"
                },
                fontSize = 11.sp
              )
            },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(14.dp))
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFE8F1FF),
              selectedLabelColor = FlipkartBlue
            )
          )
        }

        // Assured Filter
        item {
          FilterChip(
            selected = assuredOnly,
            onClick = {
              onUpdateFilters(minRating, 100000, !assuredOnly, inStockOnly, sortBy)
            },
            label = { Text("f-Assured", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(14.dp))
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFE8F1FF),
              selectedLabelColor = FlipkartBlue
            )
          )
        }

        // 4★ and above filter
        item {
          FilterChip(
            selected = minRating >= 4.0f,
            onClick = {
              val nextRating = if (minRating >= 4.0f) 0f else 4.0f
              onUpdateFilters(nextRating, 100000, assuredOnly, inStockOnly, sortBy)
            },
            label = { Text("4★ & above", fontSize = 11.sp) },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = FlipkartGreen, modifier = Modifier.size(14.dp))
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFFE8F5E9),
              selectedLabelColor = FlipkartGreen
            )
          )
        }

        // In-stock only
        item {
          FilterChip(
            selected = inStockOnly,
            onClick = {
              onUpdateFilters(minRating, 100000, assuredOnly, !inStockOnly, sortBy)
            },
            label = { Text("In Stock", fontSize = 11.sp) }
          )
        }

        // Reset
        item {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF1F3F6),
            modifier = Modifier.clickable { onResetFilters() }
          ) {
            Text(
              text = "Clear Filters",
              fontSize = 11.sp,
              color = Color.Gray,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }
    }

    // 3. Results count summary
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${products.size} Products found",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF616161)
      )
      if (query.isNotEmpty()) {
        Text(
          text = "Results for \"$query\"",
          fontSize = 12.sp,
          color = FlipkartBlue,
          fontWeight = FontWeight.Medium
        )
      }
    }

    // 4. Products Grid or Empty State
    if (products.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = "No products",
            tint = Color.LightGray,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No products match your criteria",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Try searching for Sony, Nothing, iPhone, Hoodie or resetting filters",
            fontSize = 12.sp,
            color = Color.Gray
          )
        }
      }
    } else {
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(products) { product ->
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
