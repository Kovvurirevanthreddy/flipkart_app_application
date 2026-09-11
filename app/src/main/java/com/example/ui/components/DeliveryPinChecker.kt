package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.DeliveryEstimate
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen

@Composable
fun DeliveryPinChecker(
  currentPincode: String,
  estimate: DeliveryEstimate,
  onCheckPincode: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var pinInput by remember(currentPincode) { mutableStateOf(currentPincode) }
  val focusManager = LocalFocusManager.current

  Card(
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = "Delivery Estimator",
            tint = FlipkartBlue,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Delivery Details & PIN Code",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = pinInput,
          onValueChange = {
            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
              pinInput = it
            }
          },
          label = { Text("Enter 6-digit PIN Code", fontSize = 12.sp) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onCheckPincode(pinInput)
          }),
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.PinDrop,
              contentDescription = null,
              tint = FlipkartBlue,
              modifier = Modifier.size(18.dp)
            )
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FlipkartBlue,
            focusedLabelColor = FlipkartBlue
          ),
          modifier = Modifier
            .weight(1f)
            .testTag("pincode_input_field")
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextButton(
          onClick = {
            focusManager.clearFocus()
            onCheckPincode(pinInput)
          },
          colors = ButtonDefaults.textButtonColors(contentColor = FlipkartBlue),
          modifier = Modifier.testTag("pincode_check_btn")
        ) {
          Text("Check", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Result Feedback Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            if (estimate.isDeliverable) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
            shape = RoundedCornerShape(6.dp)
          )
          .padding(10.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = if (estimate.isDeliverable) FlipkartGreen else Color(0xFFD32F2F),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = estimate.message,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (estimate.isDeliverable) FlipkartGreen else Color(0xFFD32F2F)
            )
          }

          if (estimate.isDeliverable && estimate.badge.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "✓ ${estimate.badge}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = FlipkartDarkNeutral
            )
          }
        }
      }
    }
  }
}
