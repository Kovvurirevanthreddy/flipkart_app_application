package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.data.model.AddressEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.Coupon
import com.example.data.model.UserProfileEntity
import com.example.ui.components.formatIndianPrice
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen
import com.example.ui.theme.FlipkartOrange

@Composable
fun CheckoutScreen(
  cartItems: List<CartItemEntity>,
  addresses: List<AddressEntity>,
  selectedAddressId: String?,
  selectedPaymentMethod: String,
  appliedCoupon: Coupon?,
  useSuperCoins: Boolean,
  userProfile: UserProfileEntity?,
  onSelectAddress: (String) -> Unit,
  onAddNewAddressClick: () -> Unit,
  onSelectPaymentMethod: (String) -> Unit,
  onConfirmOrder: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var codCaptchaInput by remember { mutableStateOf("") }
  val sampleCaptcha = "482"

  val sellingPriceSum = cartItems.sumOf { it.price * it.quantity }
  val couponDiscount = appliedCoupon?.discountAmount ?: 0
  val coinDiscount = if (useSuperCoins && (userProfile?.superCoins ?: 0) >= 100) 100 else 0
  val finalTotal = (sellingPriceSum - couponDiscount - coinDiscount).coerceAtLeast(0)

  val activeAddress = addresses.find { it.id == selectedAddressId }
    ?: addresses.firstOrNull()

  Scaffold(
    topBar = {
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
          Text(text = "Order Checkout & Payment", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
      }
    },
    bottomBar = {
      Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "Total Payable", fontSize = 11.sp, color = Color.Gray)
            Text(
              text = "₹${formatIndianPrice(finalTotal)}",
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold,
              color = FlipkartDarkNeutral
            )
          }

          Button(
            onClick = onConfirmOrder,
            colors = ButtonDefaults.buttonColors(containerColor = FlipkartOrange),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .height(48.dp)
              .testTag("checkout_pay_confirm_btn")
          ) {
            Text(
              text = "Pay & Confirm Order",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color.White
            )
          }
        }
      }
    },
    modifier = modifier
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF1F3F6))
    ) {
      // 1. Delivery Address Selection Card
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.PinDrop, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Deliver to:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }

              TextButton(onClick = onAddNewAddressClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add New", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FlipkartBlue)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            addresses.forEach { addr ->
              val isSelected = (addr.id == activeAddress?.id)
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isSelected) Color(0xFFF0F5FF) else Color(0xFFFAFAFA),
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) FlipkartBlue else Color(0xFFE0E0E0)
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clickable { onSelectAddress(addr.id) }
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  RadioButton(
                    selected = isSelected,
                    onClick = { onSelectAddress(addr.id) },
                    colors = RadioButtonDefaults.colors(selectedColor = FlipkartBlue)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(text = addr.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                      Spacer(modifier = Modifier.width(8.dp))
                      Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFE0E0E0)
                      ) {
                        Text(text = addr.type.uppercase(), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                      }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "${addr.addressLine}, ${addr.locality}, ${addr.city} - ${addr.pincode}", fontSize = 12.sp, color = Color(0xFF616161))
                    Text(text = "Phone: ${addr.phone}", fontSize = 11.sp, color = Color(0xFF757575))
                  }
                }
              }
            }
          }
        }
      }

      // 2. Order Summary Preview
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "Order Items (${cartItems.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            cartItems.take(3).forEach { item ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "${item.quantity}x ${item.title}",
                  fontSize = 12.sp,
                  maxLines = 1,
                  modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "₹${formatIndianPrice(item.price * item.quantity)}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      // 3. Payment Gateway Options
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "PAYMENT OPTIONS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF757575))
            Spacer(modifier = Modifier.height(12.dp))

            // Option: UPI
            PaymentMethodRow(
              title = "UPI (Google Pay, PhonePe, Paytm)",
              subtitle = "Instant payment via any UPI app",
              icon = Icons.Default.QrCode,
              isSelected = selectedPaymentMethod == "UPI",
              onClick = { onSelectPaymentMethod("UPI") }
            )

            // Option: Cards
            PaymentMethodRow(
              title = "Credit / Debit / ATM Card",
              subtitle = "Visa, Mastercard, RuPay & Diners",
              icon = Icons.Default.CreditCard,
              isSelected = selectedPaymentMethod == "CARD",
              onClick = { onSelectPaymentMethod("CARD") }
            )

            // Option: Flipkart Pay Later (BNPL)
            PaymentMethodRow(
              title = "Flipkart Pay Later (BNPL)",
              subtitle = "Approved limit: ₹${formatIndianPrice(userProfile?.payLaterLimit ?: 25000)} (Pay next month)",
              icon = Icons.Default.VerifiedUser,
              isSelected = selectedPaymentMethod == "PAY_LATER",
              onClick = { onSelectPaymentMethod("PAY_LATER") }
            )

            // Option: Net Banking
            PaymentMethodRow(
              title = "Net Banking",
              subtitle = "All Indian Banks (SBI, HDFC, ICICI, Axis)",
              icon = Icons.Default.AccountBalance,
              isSelected = selectedPaymentMethod == "NET_BANKING",
              onClick = { onSelectPaymentMethod("NET_BANKING") }
            )

            // Option: Wallet
            PaymentMethodRow(
              title = "Flipkart Wallet & Gift Card",
              subtitle = "Available Balance: ₹${formatIndianPrice(userProfile?.walletBalance ?: 1500)}",
              icon = Icons.Default.AccountBalanceWallet,
              isSelected = selectedPaymentMethod == "WALLET",
              onClick = { onSelectPaymentMethod("WALLET") }
            )

            // Option: Cash on Delivery (COD)
            PaymentMethodRow(
              title = "Cash on Delivery (COD)",
              subtitle = "Pay cash or scan QR at doorstep",
              icon = Icons.Default.LocalAtm,
              isSelected = selectedPaymentMethod == "COD",
              onClick = { onSelectPaymentMethod("COD") }
            )

            // Captcha verification field if COD selected
            if (selectedPaymentMethod == "COD") {
              Spacer(modifier = Modifier.height(8.dp))
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFFFF8E1),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(text = "Security Verification for COD", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  Spacer(modifier = Modifier.height(6.dp))
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .background(Color(0xFF212121), RoundedCornerShape(4.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                      Text(text = sampleCaptcha, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 4.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedTextField(
                      value = codCaptchaInput,
                      onValueChange = { codCaptchaInput = it },
                      placeholder = { Text("Enter 482", fontSize = 12.sp) },
                      singleLine = true,
                      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                      modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                    )
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
fun PaymentMethodRow(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
      .clickable(onClick = onClick),
    verticalAlignment = Alignment.CenterVertically
  ) {
    RadioButton(
      selected = isSelected,
      onClick = onClick,
      colors = RadioButtonDefaults.colors(selectedColor = FlipkartBlue)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Icon(imageVector = icon, contentDescription = null, tint = if (isSelected) FlipkartBlue else Color.Gray, modifier = Modifier.size(22.dp))
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = FlipkartDarkNeutral)
      Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
    }
  }
}
