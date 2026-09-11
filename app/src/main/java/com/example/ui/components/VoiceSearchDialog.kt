package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceSearchDialog(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onQueryDetected: (String) -> Unit
) {
  if (!isOpen) return

  var transcript by remember { mutableStateOf("Listening... Speak now") }
  val suggestedVoiceQueries = listOf(
    "Sony Headphones",
    "Nothing Phone 2a",
    "Cotton Hoodie",
    "Ergonomic Chair",
    "4K Smart TV",
    "California Almonds"
  )

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.35f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("voice_search_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header with Close
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Voice Assistant",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = FlipkartDarkNeutral
          )
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Animated Mic Visualizer
        Box(contentAlignment = Alignment.Center) {
          Box(
            modifier = Modifier
              .size(96.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(FlipkartBlue.copy(alpha = 0.15f))
          )
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(FlipkartBlue),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Microphone",
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = transcript,
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          color = FlipkartDarkNeutral
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = "Or tap a voice search shortcut:",
          fontSize = 12.sp,
          color = Color(0xFF878787)
        )

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          suggestedVoiceQueries.forEach { query ->
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = Color(0xFFF1F3F6),
              modifier = Modifier.clickable {
                transcript = "Recognized: \"$query\""
                onQueryDetected(query)
                onDismiss()
              }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.GraphicEq,
                  contentDescription = null,
                  tint = FlipkartBlue,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = query,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
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
