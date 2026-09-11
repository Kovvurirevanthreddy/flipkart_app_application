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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderEntity
import com.example.ui.components.formatIndianPrice
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen

@Composable
fun OrdersScreen(
  orders: List<OrderEntity>,
  onOrderClick: (OrderEntity) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .testTag("orders_screen_root")
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
        Text(text = "My Orders", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
      }
    }

    if (orders.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(text = "No orders found", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
        }
      }
    } else {
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orders) { order ->
          Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 6.dp)
              .clickable { onOrderClick(order) }
              .testTag("order_item_${order.orderId}")
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Row(modifier = Modifier.weight(1f)) {
                  Box(
                    modifier = Modifier
                      .size(50.dp)
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0xFFF1F3F6)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(24.dp))
                  }

                  Spacer(modifier = Modifier.width(10.dp))

                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Box(
                        modifier = Modifier
                          .size(8.dp)
                          .background(
                            if (order.status.contains("Delivered", ignoreCase = true)) FlipkartGreen else FlipkartBlue,
                            CircleShape
                          )
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = order.status,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (order.status.contains("Delivered", ignoreCase = true)) FlipkartGreen else FlipkartBlue
                      )
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = order.productTitle,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Medium,
                      color = FlipkartDarkNeutral,
                      maxLines = 1
                    )
                    Text(
                      text = "Ordered on ${order.dateFormatted}",
                      fontSize = 11.sp,
                      color = Color.Gray
                    )
                  }
                }

                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                  contentDescription = null,
                  tint = Color(0xFFBDBDBD),
                  modifier = Modifier.size(14.dp)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Order ID: ${order.orderId}",
                  fontSize = 11.sp,
                  color = Color.Gray
                )
                Text(
                  text = "₹${formatIndianPrice(order.totalAmount)}",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = FlipkartDarkNeutral
                )
              }
            }
          }
        }
      }
    }
  }
}
