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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral

@Composable
fun AddProductDialog(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onAddProduct: (
    title: String,
    category: String,
    brand: String,
    price: Int,
    originalPrice: Int,
    stockCount: Int,
    description: String
  ) -> Unit
) {
  if (!isOpen) return

  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Electronics") }
  var brand by remember { mutableStateOf("") }
  var priceText by remember { mutableStateOf("") }
  var origPriceText by remember { mutableStateOf("") }
  var stockText by remember { mutableStateOf("15") }
  var description by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("add_product_dialog")
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
          Text(text = "List New Product on Flipkart", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FlipkartDarkNeutral)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Product Title *") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category *") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          Spacer(modifier = Modifier.width(8.dp))
          OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand *") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
          OutlinedTextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Selling Price (₹) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          Spacer(modifier = Modifier.width(8.dp))
          OutlinedTextField(
            value = origPriceText,
            onValueChange = { origPriceText = it },
            label = { Text("MRP (₹) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = stockText,
          onValueChange = { stockText = it },
          label = { Text("Stock Units Available *") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Product Highlights & Description") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            val p = priceText.toIntOrNull() ?: 999
            val op = origPriceText.toIntOrNull() ?: (p * 12 / 10)
            val st = stockText.toIntOrNull() ?: 10
            if (title.isNotBlank()) {
              onAddProduct(
                title,
                category.ifBlank { "Electronics" },
                brand.ifBlank { "Generic" },
                p,
                op,
                st,
                description.ifBlank { "High quality authentic product backed by Flipkart Assured guarantee." }
              )
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("add_product_submit_btn")
        ) {
          Text(text = "Publish to Catalog", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }
    }
  }
}
