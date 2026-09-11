package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.OrderEntity
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen
import com.example.ui.theme.PlusCoinYellow

@Composable
fun OrderSuccessDialog(
  isOpen: Boolean,
  order: OrderEntity?,
  onTrackOrder: () -> Unit,
  onContinueShopping: () -> Unit
) {
  if (!isOpen || order == null) return

  Dialog(onDismissRequest = onContinueShopping) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("order_success_dialog")
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Success Green Circle
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color(0xFFE8F5E9)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Success",
            tint = FlipkartGreen,
            modifier = Modifier.size(42.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Order Placed Successfully!",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = FlipkartDarkNeutral
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Order ID: ${order.orderId}",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // SuperCoins earned strip
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFFFF9C4),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = PlusCoinYellow, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "You earned +20 SuperCoins on this order!",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFF5D4037)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Delivering to ${order.recipientName} by ${order.expectedDelivery}",
          fontSize = 12.sp,
          color = FlipkartDarkNeutral
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Actions
        Button(
          onClick = onTrackOrder,
          colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("track_order_btn")
        ) {
          Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Track Order Details", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = onContinueShopping,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "Continue Shopping", color = FlipkartBlue, fontWeight = FontWeight.SemiBold)
        }
      }
    }
  }
}
