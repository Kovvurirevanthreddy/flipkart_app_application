package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral

@Composable
fun AddAddressDialog(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onSaveAddress: (
    name: String,
    phone: String,
    pincode: String,
    addressLine: String,
    locality: String,
    city: String,
    state: String,
    type: String,
    isDefault: Boolean
  ) -> Unit
) {
  if (!isOpen) return

  var name by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var pincode by remember { mutableStateOf("") }
  var addressLine by remember { mutableStateOf("") }
  var locality by remember { mutableStateOf("") }
  var city by remember { mutableStateOf("") }
  var state by remember { mutableStateOf("Karnataka") }
  var addressType by remember { mutableStateOf("Home") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("add_address_dialog")
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Add Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FlipkartDarkNeutral)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Full Name *") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { if (it.length <= 10) phone = it },
          label = { Text("10-digit Mobile Number *") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = pincode,
            onValueChange = { if (it.length <= 6) pincode = it },
            label = { Text("Pincode *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          Spacer(modifier = Modifier.width(8.dp))
          OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City *") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = addressLine,
          onValueChange = { addressLine = it },
          label = { Text("House No., Building Name *") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = locality,
          onValueChange = { locality = it },
          label = { Text("Road Name, Area, Colony *") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Address Type", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
          RadioButton(selected = addressType == "Home", onClick = { addressType = "Home" }, colors = RadioButtonDefaults.colors(selectedColor = FlipkartBlue))
          Text(text = "Home", fontSize = 13.sp)
          Spacer(modifier = Modifier.width(16.dp))
          RadioButton(selected = addressType == "Work", onClick = { addressType = "Work" }, colors = RadioButtonDefaults.colors(selectedColor = FlipkartBlue))
          Text(text = "Work", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (name.isNotBlank() && phone.isNotBlank() && pincode.isNotBlank()) {
              onSaveAddress(
                name,
                phone,
                pincode,
                addressLine.ifBlank { "Flat 402, Sunshine Residency" },
                locality.ifBlank { "Indiranagar" },
                city.ifBlank { "Bengaluru" },
                state,
                addressType,
                true
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("save_address_submit_btn")
        ) {
          Text(text = "Save Address", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }
    }
  }
}
