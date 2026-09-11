package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentReturn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.FlipkartOrange

@Composable
fun OrderDetailScreen(
  order: OrderEntity?,
  onAdvanceStatus: (String) -> Unit,
  onRequestReturn: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (order == null) return

  val trackingSteps = listOf(
    "Order Confirmed",
    "Packed by Seller",
    "Shipped (EKART Express)",
    "Out for Delivery",
    "Delivered"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .verticalScroll(rememberScrollState())
      .testTag("order_detail_root")
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
        Column {
          Text(text = "Order Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Text(text = "OD# ${order.orderId}", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
        }
      }
    }

    // 1. Order Item Summary
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF1F3F6)),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(text = order.productTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FlipkartDarkNeutral)
          Spacer(modifier = Modifier.height(2.dp))
          Text(text = "Quantity: ${order.itemCount}", fontSize = 12.sp, color = Color.Gray)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "₹${formatIndianPrice(order.totalAmount)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = FlipkartDarkNeutral)
        }
      }
    }

    // 2. Real-Time Tracking Step Timeline
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Delivery Tracking", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = FlipkartDarkNeutral)
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (order.trackingStep >= 4) FlipkartGreen.copy(alpha = 0.15f) else FlipkartBlue.copy(alpha = 0.15f)
          ) {
            Text(
              text = order.status,
              color = if (order.trackingStep >= 4) FlipkartGreen else FlipkartBlue,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progressive Vertical Timeline
        trackingSteps.forEachIndexed { index, stepName ->
          val isCompleted = index <= order.trackingStep
          val isCurrent = index == order.trackingStep

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(
                    when {
                      isCompleted -> FlipkartGreen
                      else -> Color(0xFFE0E0E0)
                    }
                  ),
                contentAlignment = Alignment.Center
              ) {
                if (isCompleted) {
                  Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                } else {
                  Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
                }
              }

              if (index < trackingSteps.size - 1) {
                Box(
                  modifier = Modifier
                    .width(2.5.dp)
                    .height(36.dp)
                    .background(if (index < order.trackingStep) FlipkartGreen else Color(0xFFE0E0E0))
                )
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.padding(top = 2.dp)) {
              Text(
                text = stepName,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isCompleted) FlipkartDarkNeutral else Color.Gray
              )
              if (isCurrent) {
                Text(
                  text = "Updated today • Expected delivery by ${order.expectedDelivery}",
                  fontSize = 11.sp,
                  color = FlipkartBlue
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Simulation Action: Advance Tracking Step
        if (order.trackingStep < 4) {
          Button(
            onClick = { onAdvanceStatus(order.orderId) },
            colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("advance_order_status_btn")
          ) {
            Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Simulate Next Delivery Stage (${trackingSteps.getOrNull(order.trackingStep + 1) ?: "Delivered"})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }

    // 3. Return / Replacement Action
    if (order.isEligibleForReturn) {
      Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(text = "Return or Replacement", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Eligible for 7-day hassle free return or exchange with doorstep pickup", fontSize = 12.sp, color = Color.Gray)
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedButton(
            onClick = { onRequestReturn(order.orderId) },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("request_return_btn")
          ) {
            Icon(imageVector = Icons.Default.AssignmentReturn, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Request Return / Refund", fontWeight = FontWeight.Bold, color = FlipkartBlue)
          }
        }
      }
    }

    // 4. Shipping Details
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Default.PinDrop, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Delivery Address", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = order.recipientName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text(text = order.deliveryAddress, fontSize = 12.sp, color = Color(0xFF616161))
        Text(text = "Phone: ${order.phone}", fontSize = 11.sp, color = Color(0xFF757575))
      }
    }

    // 5. Payment & Invoice
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(text = "Payment Information", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Payment Method", fontSize = 12.sp, color = Color.Gray)
          Text(text = order.paymentMethod, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Total Paid", fontSize = 12.sp, color = Color.Gray)
          Text(text = "₹${formatIndianPrice(order.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color(0xFFE0E0E0))
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Download Tax Invoice (PDF)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FlipkartBlue)
        }
      }
    }
  }
}
