package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen
import com.example.ui.theme.FlipkartYellow

data class DisputeRecord(
  val id: String,
  val vendor: String,
  val reason: String,
  val amount: Int,
  var status: String
)

@Composable
fun AdminDashboardScreen(
  flashSaleActive: Boolean,
  commissionPercent: Int,
  onToggleFlashSale: (Boolean) -> Unit,
  onCommissionChanged: (Int) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var activeSale by remember { mutableStateOf(flashSaleActive) }
  var commissionSlider by remember { mutableFloatStateOf(commissionPercent.toFloat()) }

  val disputes = remember {
    mutableStateListOf(
      DisputeRecord("DIS-1049", "RetailNet", "Weight Mismatch on Courier Pickup (OD#9812)", 1250, "Pending Review"),
      DisputeRecord("DIS-1052", "SuperComNet", "Customer Return Damaged Goods (OD#7621)", 3499, "Pending Review"),
      DisputeRecord("DIS-1055", "IndiFash Sellers", "Delayed Dispatch Penalty Waiver", 800, "Approved")
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF1F3F6))
      .testTag("admin_dashboard_root")
  ) {
    // Top Bar
    Surface(
      color = Color(0xFF1E293B),
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
          Text(text = "Flipkart Platform Command Console", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Text(text = "Enterprise Administration & Governance", color = Color.LightGray, fontSize = 11.sp)
        }
      }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
      // 1. Platform Macro Metrics
      item {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(text = "E-Commerce Macro Financials (Q3 Live)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FlipkartDarkNeutral)
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            AdminMetricCard(
              title = "Gross GMV",
              value = "₹3.42 Cr",
              badge = "+24.8% YoY",
              icon = Icons.Default.TrendingUp,
              color = FlipkartGreen,
              modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
              title = "Total Orders",
              value = "18,490",
              badge = "98.4% Fulfilled",
              icon = Icons.Default.ShoppingCart,
              color = FlipkartBlue,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            AdminMetricCard(
              title = "Active Vendors",
              value = "342",
              badge = "12 Pending KYC",
              icon = Icons.Default.Store,
              color = Color(0xFF673AB7),
              modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
              title = "Take-Rate Revenue",
              value = "₹27.36 L",
              badge = "@ ${commissionSlider.toInt()}% Avg",
              icon = Icons.Default.MonetizationOn,
              color = Color(0xFFE65100),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // 2. Global Campaign & Commission Controls
      item {
        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "Campaign & Monetization Engine", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            // Big Billion Festival Mode
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, tint = FlipkartYellow, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(text = "Big Billion Days Festival Surge", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  Text(text = "Triggers platform-wide surge banners & discounts", fontSize = 11.sp, color = Color.Gray)
                }
              }

              Switch(
                checked = activeSale,
                onCheckedChange = {
                  activeSale = it
                  onToggleFlashSale(it)
                },
                colors = SwitchDefaults.colors(checkedThumbColor = FlipkartBlue)
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Commission Setting Slider
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Platform Take-Rate (Commission)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
              Text(text = "${commissionSlider.toInt()}% per GMV", fontWeight = FontWeight.Bold, color = FlipkartBlue, fontSize = 13.sp)
            }

            Slider(
              value = commissionSlider,
              onValueChange = {
                commissionSlider = it
                onCommissionChanged(it.toInt())
              },
              valueRange = 2f..20f,
              steps = 17,
              colors = SliderDefaults.colors(
                thumbColor = FlipkartBlue,
                activeTrackColor = FlipkartBlue
              )
            )
          }
        }
      }

      // 3. Vendor Dispute & Fraud Resolution Section
      item {
        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Gavel, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = "Vendor Dispute & Claims Resolution", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            disputes.forEach { dispute ->
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFFAFAFA),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = "${dispute.id} • ${dispute.vendor}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = FlipkartBlue)
                    Text(text = "₹${dispute.amount}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(text = dispute.reason, fontSize = 11.sp, color = FlipkartDarkNeutral)

                  Spacer(modifier = Modifier.height(6.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = if (dispute.status == "Approved") Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ) {
                      Text(
                        text = dispute.status,
                        color = if (dispute.status == "Approved") FlipkartGreen else Color(0xFFE65100),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }

                    if (dispute.status == "Pending Review") {
                      Row {
                        TextButton(
                          onClick = {
                            val idx = disputes.indexOf(dispute)
                            if (idx >= 0) disputes[idx] = dispute.copy(status = "Rejected")
                          },
                          colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                        ) {
                          Text(text = "Reject", fontSize = 11.sp)
                        }

                        Button(
                          onClick = {
                            val idx = disputes.indexOf(dispute)
                            if (idx >= 0) disputes[idx] = dispute.copy(status = "Approved")
                          },
                          colors = ButtonDefaults.buttonColors(containerColor = FlipkartGreen),
                          shape = RoundedCornerShape(4.dp)
                        ) {
                          Text(text = "Approve Claim", fontSize = 11.sp)
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

@Composable
fun AdminMetricCard(
  title: String,
  value: String,
  badge: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = title, fontSize = 11.sp, color = Color.Gray)
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = badge, fontSize = 10.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
  }
}
