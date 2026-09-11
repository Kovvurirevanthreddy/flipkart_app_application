package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Product(
  val id: String,
  val title: String,
  val category: String,
  val brand: String,
  val price: Int,
  val originalPrice: Int,
  val discountPercent: Int = if (originalPrice > 0) ((originalPrice - price) * 100 / originalPrice) else 0,
  val rating: Float = 4.2f,
  val ratingsCount: Int = 1250,
  val imageUrl: String = "",
  val localDrawableName: String = "",
  val sellerId: String = "seller_retailnet",
  val sellerName: String = "RetailNet",
  val isAssured: Boolean = true,
  val inStock: Boolean = true,
  val stockCount: Int = 45,
  val description: String = "",
  val specs: Map<String, String> = emptyMap(),
  val highlights: List<String> = emptyList(),
  val arSupported: Boolean = true,
  val deliveryDaysEstimate: Int = 2
)

data class CategoryItem(
  val id: String,
  val name: String,
  val iconName: String,
  val tag: String
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
  @PrimaryKey val id: String,
  val productId: String,
  val title: String,
  val price: Int,
  val originalPrice: Int,
  val imageUrl: String,
  val sellerId: String,
  val sellerName: String,
  val quantity: Int = 1,
  val isAssured: Boolean = true,
  val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
  @PrimaryKey val productId: String,
  val title: String,
  val price: Int,
  val originalPrice: Int,
  val discountPercent: Int,
  val imageUrl: String,
  val rating: Float,
  val sellerName: String,
  val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_addresses")
data class AddressEntity(
  @PrimaryKey val id: String,
  val name: String,
  val phone: String,
  val pincode: String,
  val addressLine: String,
  val locality: String,
  val city: String,
  val state: String,
  val type: String = "Home", // Home, Work, Other
  val isDefault: Boolean = false
)

@Entity(tableName = "orders")
data class OrderEntity(
  @PrimaryKey val orderId: String,
  val dateFormatted: String,
  val productTitle: String,
  val productCount: Int,
  val totalAmount: Int,
  val savingsAmount: Int,
  val superCoinsEarned: Int,
  val deliveryPincode: String,
  val addressSummary: String,
  val paymentMethod: String,
  val status: String, // Placed, Packed, Shipped, OutForDelivery, Delivered, Returned
  val expectedDelivery: String,
  val trackingStep: Int = 0, // 0: Placed, 1: Packed, 2: Shipped, 3: Out for Delivery, 4: Delivered
  val sellerName: String = "RetailNet",
  val isEligibleForReturn: Boolean = true,
  val recipientName: String = "Rahul Sharma",
  val deliveryAddress: String = addressSummary,
  val phone: String = "+91 98765 43210"
) {
  val itemCount: Int get() = productCount
}

@Entity(tableName = "user_profile")
data class UserProfileEntity(
  @PrimaryKey val userId: String = "user_default",
  val name: String = "Revanth Reddy",
  val phone: String = "+91 98765 43210",
  val email: String = "revanth.reddy@example.com",
  val isPlusMember: Boolean = true,
  val superCoins: Int = 420,
  val walletBalance: Int = 1500,
  val payLaterLimit: Int = 25000,
  val payLaterUsed: Int = 4200,
  val selectedLanguage: String = "English"
)

data class VibePost(
  val id: String,
  val creatorName: String,
  val title: String,
  val caption: String,
  val likesCount: Int,
  val isLiked: Boolean = false,
  val taggedProduct: Product,
  val tagBadge: String = "#TrendVibe"
) {
  val creator: String get() = creatorName
  val likes: Int get() = likesCount
  val productId: String get() = taggedProduct.id
}

data class Coupon(
  val code: String,
  val description: String,
  val discountAmount: Int,
  val minCartValue: Int,
  val expiryDays: Int = 3
)

enum class AppDestination {
  HOME,
  CATEGORIES,
  VIBES,
  ORDERS,
  CART,
  ACCOUNT,
  SEARCH,
  PRODUCT_DETAIL,
  CHECKOUT,
  ORDER_DETAIL,
  WISHLIST,
  SELLER_HUB,
  ADMIN_DASHBOARD
}
