package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.components.formatIndianPrice
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen

@Composable
fun SellerHubScreen(
  products: List<Product>,
  onUpdateProductStockAndPrice: (productId: String, stock: Int, price: Int) -> Unit,
  onOpenAddProductDialog: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
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
          Column {
            Text(text = "Flipkart Seller Hub", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(text = "Vendor Inventory & Fulfillment Console", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
          }
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = onOpenAddProductDialog,
        containerColor = FlipkartBlue,
        contentColor = Color.White,
        modifier = Modifier.testTag("seller_add_product_fab")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product")
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "List Product", fontWeight = FontWeight.Bold)
        }
      }
    },
    modifier = modifier
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF1F3F6))
    ) {
      // 1. KPI Metric Cards
      item {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(text = "Performance Overview (Last 30 Days)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FlipkartDarkNeutral)
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            SellerMetricCard(
              title = "Gross Revenue",
              value = "₹4,82,500",
              sub = "+18.4% vs last mo",
              icon = Icons.Default.TrendingUp,
              color = FlipkartGreen,
              modifier = Modifier.weight(1f)
            )
            SellerMetricCard(
              title = "Orders Dispatched",
              value = "112",
              sub = "100% on-time",
              icon = Icons.Default.LocalShipping,
              color = FlipkartBlue,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            SellerMetricCard(
              title = "Fulfillment Health",
              value = "99.2%",
              sub = "Tier 1 Top Rated",
              icon = Icons.Default.Inventory,
              color = Color(0xFF673AB7),
              modifier = Modifier.weight(1f)
            )
            SellerMetricCard(
              title = "Net Payouts",
              value = "₹4,24,600",
              sub = "Next payout: Friday",
              icon = Icons.Default.AttachMoney,
              color = Color(0xFFE65100),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // 2. Inventory Management Section
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Live Catalog & Stock Management", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FlipkartDarkNeutral)
          Text(text = "${products.size} Listings", fontSize = 12.sp, color = Color.Gray)
        }
      }

      items(products) { prod ->
        SellerInventoryItem(
          product = prod,
          onSave = { newStock, newPrice ->
            onUpdateProductStockAndPrice(prod.id, newStock, newPrice)
          }
        )
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

@Composable
fun SellerMetricCard(
  title: String,
  value: String,
  sub: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = title, fontSize = 11.sp, color = Color.Gray)
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = sub, fontSize = 10.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
  }
}

@Composable
fun SellerInventoryItem(
  product: Product,
  onSave: (stock: Int, price: Int) -> Unit
) {
  var isEditing by remember { mutableStateOf(false) }
  var stockText by remember(product.stockCount) { mutableStateOf(product.stockCount.toString()) }
  var priceText by remember(product.price) { mutableStateOf(product.price.toString()) }

  Card(
    shape = RoundedCornerShape(0.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 6.dp)
      .testTag("seller_item_${product.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(text = product.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1)
          Text(text = "SKU: ${product.id} • ${product.category}", fontSize = 11.sp, color = Color.Gray)
        }

        IconButton(onClick = { isEditing = !isEditing }) {
          Icon(
            imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
            contentDescription = "Edit",
            tint = FlipkartBlue,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      if (isEditing) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = stockText,
            onValueChange = { stockText = it },
            label = { Text("Stock Units", fontSize = 10.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
          )

          OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Price (₹)", fontSize = 10.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .weight(1f)
              .height(52.dp)
          )

          Button(
            onClick = {
              val s = stockText.toIntOrNull() ?: product.stockCount
              val p = priceText.toIntOrNull() ?: product.price
              onSave(s, p)
              isEditing = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(text = "Save", fontSize = 12.sp)
          }
        }
      } else {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Current Stock: ${product.stockCount} units",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (product.stockCount > 5) FlipkartGreen else Color(0xFFE65100)
          )
          Text(
            text = "Listing Price: ₹${formatIndianPrice(product.price)}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )
        }
      }
    }
  }
}
