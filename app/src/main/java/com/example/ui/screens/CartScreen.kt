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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItemEntity
import com.example.data.model.Coupon
import com.example.data.model.UserProfileEntity
import com.example.ui.components.formatIndianPrice
import com.example.ui.components.getProductIcon
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen
import com.example.ui.theme.FlipkartOrange
import com.example.ui.theme.FlipkartYellow
import com.example.ui.theme.PlusCoinYellow

@Composable
fun CartScreen(
  cartItems: List<CartItemEntity>,
  userProfile: UserProfileEntity?,
  appliedCoupon: Coupon?,
  useSuperCoins: Boolean,
  onUpdateQuantity: (cartId: String, newQty: Int) -> Unit,
  onRemoveItem: (cartId: String) -> Unit,
  onApplyCoupon: (String) -> Unit,
  onRemoveCoupon: () -> Unit,
  onToggleSuperCoins: (Boolean) -> Unit,
  onCheckout: () -> Unit,
  onContinueShopping: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var couponInput by remember { mutableStateOf("") }

  // Calculations
  val originalPriceSum = cartItems.sumOf { it.originalPrice * it.quantity }
  val sellingPriceSum = cartItems.sumOf { it.price * it.quantity }
  val mrpDiscount = originalPriceSum - sellingPriceSum
  val couponDiscount = appliedCoupon?.discountAmount ?: 0
  val coinDiscount = if (useSuperCoins && (userProfile?.superCoins ?: 0) >= 100) 100 else 0
  val finalTotal = (sellingPriceSum - couponDiscount - coinDiscount).coerceAtLeast(0)
  val totalSavings = mrpDiscount + couponDiscount + coinDiscount

  // Multi-vendor grouping
  val groupedBySeller = cartItems.groupBy { it.sellerName }

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
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
          Column {
            Text(
              text = "My Cart",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            )
            Text(
              text = "${cartItems.sumOf { it.quantity }} items",
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
          }
        }
      }
    },
    bottomBar = {
      if (cartItems.isNotEmpty()) {
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
              Text(
                text = "₹${formatIndianPrice(finalTotal)}",
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FlipkartDarkNeutral
              )
              Text(
                text = "View price details",
                fontSize = 11.sp,
                color = FlipkartBlue,
                fontWeight = FontWeight.SemiBold
              )
            }

            Button(
              onClick = onCheckout,
              colors = ButtonDefaults.buttonColors(containerColor = FlipkartOrange),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier
                .height(46.dp)
                .testTag("cart_place_order_btn")
            ) {
              Text(
                text = "Place Order",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
              )
            }
          }
        }
      }
    },
    modifier = modifier
  ) { innerPadding ->
    if (cartItems.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .background(Color(0xFFF1F3F6)),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = "Empty Cart",
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(72.dp)
          )
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Your cart is empty!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Explore our top offers and add items to your cart",
            fontSize = 12.sp,
            color = Color.Gray
          )
          Spacer(modifier = Modifier.height(18.dp))
          Button(
            onClick = onContinueShopping,
            colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(text = "Shop Now", fontWeight = FontWeight.Bold)
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .background(Color(0xFFF1F3F6))
      ) {
        // Multi-Vendor Grouped Cart Items
        groupedBySeller.forEach { (sellerName, itemsInSeller) ->
          item {
            // Seller Group Header
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Seller: ",
                fontSize = 12.sp,
                color = Color.Gray
              )
              Text(
                text = sellerName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FlipkartBlue
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "(${itemsInSeller.size} item${if (itemsInSeller.size > 1) "s" else ""})",
                fontSize = 11.sp,
                color = Color.Gray
              )
            }
          }

          items(itemsInSeller) { item ->
            Card(
              shape = RoundedCornerShape(0.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
                .testTag("cart_item_${item.id}")
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                  // Thumbnail Icon
                  Box(
                    modifier = Modifier
                      .size(64.dp)
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0xFFF1F3F6)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.ShoppingBag,
                      contentDescription = item.title,
                      tint = FlipkartBlue,
                      modifier = Modifier.size(32.dp)
                    )
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = item.title,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Medium,
                      color = FlipkartDarkNeutral,
                      maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Text(
                        text = "₹${formatIndianPrice(item.price)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FlipkartDarkNeutral
                      )

                      if (item.originalPrice > item.price) {
                        Text(
                          text = "₹${formatIndianPrice(item.originalPrice)}",
                          fontSize = 12.sp,
                          color = Color.Gray,
                          textDecoration = TextDecoration.LineThrough
                        )
                        Text(
                          text = "${((item.originalPrice - item.price) * 100 / item.originalPrice)}% off",
                          fontSize = 12.sp,
                          color = FlipkartGreen,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quantity Controls & Remove
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  // Quantity Increment / Decrement
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(30.dp)
                        .border(1.dp, Color(0xFFBDBDBD), CircleShape)
                        .clickable { onUpdateQuantity(item.id, item.quantity - 1) },
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                    }

                    Text(
                      text = "${item.quantity}",
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp,
                      modifier = Modifier.padding(horizontal = 14.dp)
                    )

                    Box(
                      modifier = Modifier
                        .size(30.dp)
                        .border(1.dp, Color(0xFFBDBDBD), CircleShape)
                        .clickable { onUpdateQuantity(item.id, item.quantity + 1) },
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                    }
                  }

                  TextButton(
                    onClick = { onRemoveItem(item.id) },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                  ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Remove", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                  }
                }
              }
            }
          }
        }

        // Coupon Code Engine Card
        item {
          Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocalOffer, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Coupons & Discounts", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }

              Spacer(modifier = Modifier.height(8.dp))

              if (appliedCoupon != null) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFE8F5E9),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(text = "Coupon '${appliedCoupon.code}' Applied!", fontWeight = FontWeight.Bold, color = FlipkartGreen, fontSize = 13.sp)
                      Text(text = "Saved ₹${appliedCoupon.discountAmount} on this order", color = Color(0xFF2E7D32), fontSize = 11.sp)
                    }
                    IconButton(onClick = onRemoveCoupon, modifier = Modifier.size(28.dp)) {
                      Icon(imageVector = Icons.Default.Close, contentDescription = "Remove coupon", tint = Color.Gray)
                    }
                  }
                }
              } else {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedTextField(
                    value = couponInput,
                    onValueChange = { couponInput = it.uppercase() },
                    placeholder = { Text("Enter Coupon Code (FLIP500)", fontSize = 12.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedBorderColor = FlipkartBlue,
                      focusedLabelColor = FlipkartBlue
                    ),
                    modifier = Modifier
                      .weight(1f)
                      .height(50.dp)
                      .testTag("coupon_input_field")
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Button(
                    onClick = { onApplyCoupon(couponInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = FlipkartBlue),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("apply_coupon_btn")
                  ) {
                    Text(text = "Apply", fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }

        // SuperCoins Redemption Option
        item {
          Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 6.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = PlusCoinYellow, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(text = "Pay with SuperCoins", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  Text(text = "Use 100 SuperCoins to save ₹100", fontSize = 11.sp, color = Color.Gray)
                }
              }

              Checkbox(
                checked = useSuperCoins,
                onCheckedChange = onToggleSuperCoins,
                colors = CheckboxDefaults.colors(checkedColor = FlipkartBlue)
              )
            }
          }
        }

        // Price Details Breakdown Card
        item {
          Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "PRICE DETAILS",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF757575)
              )

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Price (${cartItems.sumOf { it.quantity }} items)", fontSize = 13.sp, color = FlipkartDarkNeutral)
                Text(text = "₹${formatIndianPrice(originalPriceSum)}", fontSize = 13.sp, color = FlipkartDarkNeutral)
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Discount", fontSize = 13.sp, color = FlipkartDarkNeutral)
                Text(text = "- ₹${formatIndianPrice(mrpDiscount)}", fontSize = 13.sp, color = FlipkartGreen, fontWeight = FontWeight.Bold)
              }

              if (appliedCoupon != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(text = "Coupon Discount", fontSize = 13.sp, color = FlipkartDarkNeutral)
                  Text(text = "- ₹${appliedCoupon.discountAmount}", fontSize = 13.sp, color = FlipkartGreen, fontWeight = FontWeight.Bold)
                }
              }

              if (useSuperCoins && coinDiscount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(text = "SuperCoins Discount", fontSize = 13.sp, color = FlipkartDarkNeutral)
                  Text(text = "- ₹$coinDiscount", fontSize = 13.sp, color = FlipkartGreen, fontWeight = FontWeight.Bold)
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Delivery Charges", fontSize = 13.sp, color = FlipkartDarkNeutral)
                Text(text = "FREE", fontSize = 13.sp, color = FlipkartGreen, fontWeight = FontWeight.Bold)
              }

              Spacer(modifier = Modifier.height(12.dp))
              HorizontalDivider(color = Color(0xFFE0E0E0))
              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Total Amount", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
                Text(text = "₹${formatIndianPrice(finalTotal)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FlipkartDarkNeutral)
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "You will save ₹${formatIndianPrice(totalSavings)} on this order",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FlipkartGreen
              )
            }
          }
        }

        // Safe & Secure Payments Guarantee
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Safe and Secure Payments. 100% Authentic Products.", fontSize = 11.sp, color = Color.Gray)
          }
        }
      }
    }
  }
}
