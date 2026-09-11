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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class LanguageOption(val code: String, val name: String, val nativeName: String)

@Composable
fun LanguagePickerDialog(
  isOpen: Boolean,
  currentLanguage: String,
  onDismiss: () -> Unit,
  onLanguageSelected: (String) -> Unit
) {
  if (!isOpen) return

  val languages = listOf(
    LanguageOption("en", "English", "English"),
    LanguageOption("hi", "Hindi", "हिन्दी"),
    LanguageOption("ta", "Tamil", "தமிழ்"),
    LanguageOption("te", "Telugu", "తెలుగు"),
    LanguageOption("bn", "Bengali", "বাংলা"),
    LanguageOption("mr", "Marathi", "मराठी")
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .testTag("language_picker_dialog")
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Choose App Language", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FlipkartDarkNeutral)
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        languages.forEach { lang ->
          val isSelected = currentLanguage.equals(lang.name, ignoreCase = true) || currentLanguage.equals(lang.code, ignoreCase = true)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onLanguageSelected(lang.name) }
              .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = isSelected,
              onClick = { onLanguageSelected(lang.name) },
              colors = RadioButtonDefaults.colors(selectedColor = FlipkartBlue)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
              Text(text = lang.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
              Text(text = lang.nativeName, fontSize = 12.sp, color = Color.Gray)
            }
          }
        }
      }
    }
  }
}
