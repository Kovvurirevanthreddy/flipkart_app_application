package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Product
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartYellow

@Composable
fun ArViewerDialog(
  product: Product?,
  isOpen: Boolean,
  onDismiss: () -> Unit
) {
  if (!isOpen || product == null) return

  var rotationAngle by remember { mutableFloatStateOf(0f) }
  var scaleFactor by remember { mutableFloatStateOf(1f) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .height(540.dp)
        .testTag("ar_viewer_modal")
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.ViewInAr,
              contentDescription = "AR 3D",
              tint = FlipkartYellow,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "3D & AR Interactive View",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
              Text(
                text = product.title.take(30) + "...",
                color = Color.LightGray,
                fontSize = 11.sp
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3D Canvas Visualizer Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF262626)),
          contentAlignment = Alignment.Center
        ) {
          // Dynamic Canvas rendering simulated 3D projection
          Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height / 2f)

            // AR Grid Floor
            for (i in -4..4) {
              drawLine(
                color = Color(0x33FFFFFF),
                start = Offset(centerOffset.x + i * 40f, centerOffset.y + 60f),
                end = Offset(centerOffset.x + i * 80f, size.height - 20f),
                strokeWidth = 1.5f
              )
            }

            // 3D Product Wireframe Silhouette
            rotate(rotationAngle, pivot = centerOffset) {
              drawCircle(
                color = Color(0x222874F0),
                radius = 90f * scaleFactor,
                center = centerOffset
              )
              drawCircle(
                color = Color(0xFF2874F0),
                radius = 80f * scaleFactor,
                center = centerOffset,
                style = Stroke(width = 3f)
              )
              drawRect(
                color = Color(0xFFFFE11B),
                topLeft = Offset(centerOffset.x - 45f * scaleFactor, centerOffset.y - 45f * scaleFactor),
                size = androidx.compose.ui.geometry.Size(90f * scaleFactor, 90f * scaleFactor),
                style = Stroke(width = 2.5f)
              )
            }
          }

          // Central Overlay Icon representing the product
          Box(
            modifier = Modifier
              .size(70.dp)
              .clip(CircleShape)
              .background(Color(0xFF333333)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = getProductIcon(product.category),
              contentDescription = null,
              tint = FlipkartYellow,
              modifier = Modifier.size(36.dp)
            )
          }

          // Dimensions Pill
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Black.copy(alpha = 0.7f),
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(12.dp)
          ) {
            Text(
              text = "Live Scale: 1:1 Actual Size",
              color = Color.White,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Rotation & Scale Controls
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "360° Rotate Angle: ${rotationAngle.toInt()}°", color = Color.LightGray, fontSize = 12.sp)
            IconButton(
              onClick = { rotationAngle = 0f; scaleFactor = 1f },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Reset", tint = Color.LightGray)
            }
          }

          Slider(
            value = rotationAngle,
            onValueChange = { rotationAngle = it },
            valueRange = 0f..360f,
            colors = SliderDefaults.colors(
              thumbColor = FlipkartYellow,
              activeTrackColor = FlipkartBlue
            )
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Scale / Zoom", color = Color.LightGray, fontSize = 12.sp)
            Text(text = "${(scaleFactor * 100).toInt()}%", color = Color.LightGray, fontSize = 12.sp)
          }

          Slider(
            value = scaleFactor,
            onValueChange = { scaleFactor = it },
            valueRange = 0.7f..1.5f,
            colors = SliderDefaults.colors(
              thumbColor = FlipkartYellow,
              activeTrackColor = FlipkartBlue
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // View in Your Room button
        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.ViewInAr, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Project in Your Room (AR Simulation)", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
