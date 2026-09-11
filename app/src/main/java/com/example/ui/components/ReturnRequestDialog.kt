package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral

@Composable
fun ReturnRequestDialog(
  isOpen: Boolean,
  orderId: String?,
  onDismiss: () -> Unit,
  onSubmitReturn: (reason: String) -> Unit
) {
  if (!isOpen || orderId == null) return

  val reasons = listOf(
    "Defective or quality issue observed",
    "Different item delivered than pictured",
    "Missing accessories or packaging",
    "Size or fit does not match requirements",
    "No longer needed / Changed my mind"
  )

  var selectedReason by remember { mutableStateOf(reasons[0]) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("return_request_dialog")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "Request Return / Refund", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FlipkartDarkNeutral)
            Text(text = "Order ID: $orderId", fontSize = 11.sp, color = Color.Gray)
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Please select reason for return:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(6.dp))

        reasons.forEach { reason ->
          val isSelected = (reason == selectedReason)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedReason = reason }
              .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = isSelected,
              onClick = { selectedReason = reason },
              colors = RadioButtonDefaults.colors(selectedColor = FlipkartBlue)
            )
            Text(text = reason, fontSize = 12.sp, color = FlipkartDarkNeutral)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "• Refund will be automatically credited to original payment source upon doorstep courier inspection.",
          fontSize = 11.sp,
          color = Color.Gray,
          lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = { onSubmitReturn(selectedReason) },
          colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("confirm_return_submit_btn")
        ) {
          Text(text = "Confirm Return & Schedule Pickup", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }
  }
}
