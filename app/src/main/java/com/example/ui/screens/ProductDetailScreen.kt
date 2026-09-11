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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.repository.DeliveryEstimate
import com.example.ui.components.DeliveryPinChecker
import com.example.ui.components.formatIndianPrice
import com.example.ui.components.getProductIcon
import com.example.ui.theme.FlipkartBlue
import com.example.ui.theme.FlipkartDarkNeutral
import com.example.ui.theme.FlipkartGreen
import com.example.ui.theme.FlipkartOrange
import com.example.ui.theme.FlipkartYellow

@Composable
fun ProductDetailScreen(
  product: Product?,
  isWishlisted: Boolean,
  cartCount: Int,
  deliveryEstimate: DeliveryEstimate,
  currentPincode: String,
  onWishlistToggle: () -> Unit,
  onAddToCart: () -> Unit,
  onBuyNow: () -> Unit,
  onOpenArViewer: () -> Unit,
  onCheckPincode: (String) -> Unit,
  onCartClick: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (product == null) return

  Scaffold(
    topBar = {
      Surface(
        color = FlipkartBlue,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
              )
            }
            Text(
              text = product.brand,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onWishlistToggle) {
              Icon(
                imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Wishlist",
                tint = if (isWishlisted) Color(0xFFFF5252) else Color.White
              )
            }

            IconButton(onClick = onCartClick) {
              BadgedBox(
                badge = {
                  if (cartCount > 0) {
                    Badge(containerColor = FlipkartYellow, contentColor = Color(0xFF212121)) {
                      Text(text = cartCount.toString(), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                  }
                }
              ) {
                Icon(
                  imageVector = Icons.Default.ShoppingCart,
                  contentDescription = "Cart",
                  tint = Color.White
                )
              }
            }
          }
        }
      }
    },
    bottomBar = {
      // Sticky Bottom Dual Action Bar
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Add to Cart
          Button(
            onClick = onAddToCart,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.White,
              contentColor = FlipkartDarkNeutral
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("detail_add_to_cart_btn")
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              tint = FlipkartBlue,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Add to Cart",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          // Buy Now
          Button(
            onClick = onBuyNow,
            colors = ButtonDefaults.buttonColors(
              containerColor = FlipkartOrange,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("detail_buy_now_btn")
          ) {
            Icon(
              imageVector = Icons.Default.ElectricBolt,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Buy Now",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    },
    modifier = modifier
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF1F3F6))
        .verticalScroll(rememberScrollState())
    ) {
      // 1. Large Image & 3D AR Showcase Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
          .background(Color.White),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(120.dp)
              .clip(CircleShape)
              .background(FlipkartBlue.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = getProductIcon(product.category),
              contentDescription = product.title,
              tint = FlipkartBlue,
              modifier = Modifier.size(64.dp)
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = product.brand.uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF757575)
          )
        }

        // 3D / AR Button Overlay
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color.White,
          shadowElevation = 4.dp,
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(14.dp)
            .clickable(onClick = onOpenArViewer)
            .testTag("trigger_ar_viewer_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.ViewInAr,
              contentDescription = "3D AR",
              tint = FlipkartBlue,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "View in 3D / AR",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = FlipkartBlue
            )
          }
        }
      }

      // 2. Product Title, Rating, Price Box
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .padding(16.dp)
      ) {
        // Assured Badge
        if (product.isAssured) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = FlipkartBlue,
            modifier = Modifier.padding(bottom = 6.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "f",
                color = FlipkartYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic
              )
              Text(
                text = " Assured",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
              )
            }
          }
        }

        Text(
          text = product.title,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold,
          color = FlipkartDarkNeutral,
          lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ratings & Reviews pill
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = FlipkartGreen
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = String.format("%.1f", product.rating),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(3.dp))
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = "${product.ratingsCount} ratings & 1,420 reviews",
            fontSize = 12.sp,
            color = Color(0xFF757575)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pricing Block
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "₹${formatIndianPrice(product.price)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = FlipkartDarkNeutral
          )

          if (product.originalPrice > product.price) {
            Text(
              text = "₹${formatIndianPrice(product.originalPrice)}",
              fontSize = 14.sp,
              color = Color(0xFF878787),
              textDecoration = TextDecoration.LineThrough
            )

            Text(
              text = "${product.discountPercent}% off",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = FlipkartGreen
            )
          }
        }

        Text(
          text = "+ ₹49 Protected Packaging Fee",
          fontSize = 11.sp,
          color = Color.Gray
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 3. Bank Offers & Partner Discounts Card
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.LocalOffer, contentDescription = null, tint = FlipkartGreen, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Available Offers", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FlipkartDarkNeutral)
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "• Bank Offer: 5% Unlimited Cashback on Flipkart Axis Bank Card",
            fontSize = 12.sp,
            color = FlipkartDarkNeutral,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• Special Price: Get extra ₹3,000 off (price inclusive of cashback/coupon)",
            fontSize = 12.sp,
            color = FlipkartDarkNeutral,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• Partner Offer: Sign-up for Flipkart Pay Later & get free ₹500 Gift Card",
            fontSize = 12.sp,
            color = FlipkartDarkNeutral,
            lineHeight = 18.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 4. PIN Code Delivery Estimator
      DeliveryPinChecker(
        currentPincode = currentPincode,
        estimate = deliveryEstimate,
        onCheckPincode = onCheckPincode,
        modifier = Modifier.padding(horizontal = 12.dp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 5. Product Highlights & Specs
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "Product Details & Specifications", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FlipkartDarkNeutral)

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = product.description,
            fontSize = 13.sp,
            color = Color(0xFF424242),
            lineHeight = 19.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          HorizontalDivider(color = Color(0xFFE0E0E0))

          Spacer(modifier = Modifier.height(12.dp))

          Text(text = "Key Specifications", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FlipkartDarkNeutral)
          Spacer(modifier = Modifier.height(8.dp))

          product.specs.forEach { (key, value) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = key, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1f))
              Text(text = value, color = FlipkartDarkNeutral, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 6. Seller Information & Trust Badges
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(text = "Sold by", fontSize = 11.sp, color = Color.Gray)
              Text(text = product.sellerName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FlipkartBlue)
            }
            Surface(shape = RoundedCornerShape(4.dp), color = FlipkartBlue) {
              Text(text = "4.8 ★", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.height(2.dp))
              Text(text = "7 Days Return", fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, tint = FlipkartGreen, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.height(2.dp))
              Text(text = "Cash on Delivery", fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = FlipkartBlue, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.height(2.dp))
              Text(text = "Brand Warranty", fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
