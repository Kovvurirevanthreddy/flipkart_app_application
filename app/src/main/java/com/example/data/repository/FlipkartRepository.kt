package com.example.data.repository

import com.example.data.local.FlipkartDao
import com.example.data.model.AddressEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.CategoryItem
import com.example.data.model.Coupon
import com.example.data.model.OrderEntity
import com.example.data.model.Product
import com.example.data.model.UserProfileEntity
import com.example.data.model.VibePost
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class FlipkartRepository(private val dao: FlipkartDao) {

  // Reactive DB streams
  val cartItems: Flow<List<CartItemEntity>> = dao.getCartItems()
  val wishlistItems: Flow<List<WishlistItemEntity>> = dao.getWishlistItems()
  val savedAddresses: Flow<List<AddressEntity>> = dao.getAddresses()
  val orders: Flow<List<OrderEntity>> = dao.getOrders()
  val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()

  // In-memory catalog state for products (can be modified by Seller/Admin)
  private val _products = MutableStateFlow(initialProductCatalog())
  val products = _products.asStateFlow()

  // Categories list
  val categories: List<CategoryItem> = listOf(
    CategoryItem("all", "All", "widgets", "Top Offers"),
    CategoryItem("mobiles", "Mobiles", "smartphone", "From ₹6,999"),
    CategoryItem("electronics", "Electronics", "laptop", "Up to 70% Off"),
    CategoryItem("fashion", "Fashion", "checkroom", "Min 50% Off"),
    CategoryItem("home", "Home", "chair", "Trending Deals"),
    CategoryItem("appliances", "Appliances", "kitchen", "Smart Living"),
    CategoryItem("grocery", "Grocery", "shopping_basket", "Super Saver")
  )

  // Coupons
  val availableCoupons = listOf(
    Coupon("FLIP500", "₹500 Instant Discount on orders above ₹2,999", 500, 2999),
    Coupon("SUPER20", "Save 20% up to ₹350 on Fashion & Electronics", 350, 1499),
    Coupon("WELCOME100", "Flat ₹100 Off for first-time purchases", 100, 499)
  )

  // Vibes / Short Showcase Feed
  val vibePosts: List<VibePost> = listOf(
    VibePost(
      id = "vibe_1",
      creatorName = "Aarav Style",
      title = "Monochrome Streetwear Fit",
      caption = "Pairing the Urban Classic oversized hoodie with retro court sneakers. Complete high-street aesthetic!",
      likesCount = 2840,
      taggedProduct = _products.value.first { it.id == "prod_fashion_1" },
      tagBadge = "Trending #FitCheck"
    ),
    VibePost(
      id = "vibe_2",
      creatorName = "TechPulse India",
      title = "Desk Setup Transformation",
      caption = "Noise-cancelling wireless acoustics that make working from home pure bliss. Soundstage is insane!",
      likesCount = 5120,
      taggedProduct = _products.value.first { it.id == "prod_elec_1" },
      tagBadge = "Tech Must-Have"
    ),
    VibePost(
      id = "vibe_3",
      creatorName = "Priya Rao",
      title = "Morning Coffee Routine",
      caption = "Freshly brewed artisan beans with this sleek stainless-steel coffee maker. Best addition to my kitchen!",
      likesCount = 1930,
      taggedProduct = _products.value.first { it.id == "prod_groc_1" },
      tagBadge = "Morning Vibe"
    )
  )

  // Seed default data if needed
  suspend fun initializeDefaultData() {
    val existingProfile = dao.getUserProfile().firstOrNull()
    if (existingProfile == null) {
      dao.insertOrUpdateProfile(
        UserProfileEntity(
          userId = "user_default",
          name = "Revanth Reddy",
          phone = "+91 98765 43210",
          email = "revanth.reddy@example.com",
          isPlusMember = true,
          superCoins = 420,
          walletBalance = 1500,
          payLaterLimit = 25000,
          payLaterUsed = 3450,
          selectedLanguage = "English"
        )
      )
    }

    val existingAddresses = dao.getAddresses().firstOrNull()
    if (existingAddresses.isNullOrEmpty()) {
      dao.insertAddress(
        AddressEntity(
          id = "addr_1",
          name = "Revanth Reddy",
          phone = "+91 98765 43210",
          pincode = "560001",
          addressLine = "Flat 402, Sunshine Residency, 12th Cross",
          locality = "MG Road",
          city = "Bengaluru",
          state = "Karnataka",
          type = "Home",
          isDefault = true
        )
      )
      dao.insertAddress(
        AddressEntity(
          id = "addr_2",
          name = "Revanth Reddy",
          phone = "+91 98765 43210",
          pincode = "560103",
          addressLine = "Block B, Tech Park Campus, Outer Ring Rd",
          locality = "Bellandur",
          city = "Bengaluru",
          state = "Karnataka",
          type = "Work",
          isDefault = false
        )
      )
    }

    val existingOrders = dao.getOrders().firstOrNull()
    if (existingOrders.isNullOrEmpty()) {
      dao.insertOrder(
        OrderEntity(
          orderId = "OD3819283749",
          dateFormatted = "08 Sep 2026",
          productTitle = "Sony WH-1000XM5 Wireless Active Noise Cancelling Headphones",
          productCount = 1,
          totalAmount = 24990,
          savingsAmount = 5000,
          superCoinsEarned = 100,
          deliveryPincode = "560001",
          addressSummary = "Flat 402, Sunshine Residency, Bengaluru 560001",
          paymentMethod = "UPI (Google Pay)",
          status = "Delivered",
          expectedDelivery = "Delivered on 10 Sep 2026",
          trackingStep = 4,
          sellerName = "RetailNet",
          isEligibleForReturn = true
        )
      )
    }
  }

  // Search & Filter
  fun searchProducts(
    query: String = "",
    category: String = "all",
    minRating: Float = 0f,
    maxPrice: Int = Int.MAX_VALUE,
    assuredOnly: Boolean = false,
    inStockOnly: Boolean = false,
    sortBy: String = "POPULARITY" // POPULARITY, PRICE_LOW, PRICE_HIGH, RATING
  ): List<Product> {
    return _products.value.filter { product ->
      val matchesQuery = query.isBlank() ||
        product.title.contains(query, ignoreCase = true) ||
        product.brand.contains(query, ignoreCase = true) ||
        product.category.contains(query, ignoreCase = true)

      val matchesCategory = category == "all" || product.category.equals(category, ignoreCase = true)
      val matchesRating = product.rating >= minRating
      val matchesPrice = product.price <= maxPrice
      val matchesAssured = !assuredOnly || product.isAssured
      val matchesStock = !inStockOnly || product.inStock

      matchesQuery && matchesCategory && matchesRating && matchesPrice && matchesAssured && matchesStock
    }.sortedWith { a, b ->
      when (sortBy) {
        "PRICE_LOW" -> a.price.compareTo(b.price)
        "PRICE_HIGH" -> b.price.compareTo(a.price)
        "RATING" -> b.rating.compareTo(a.rating)
        else -> b.ratingsCount.compareTo(a.ratingsCount) // Popularity
      }
    }
  }

  fun getProductById(id: String): Product? {
    return _products.value.find { it.id == id }
  }

  // Cart operations
  suspend fun addToCart(product: Product, quantity: Int = 1) {
    val existing = dao.getCartItems().firstOrNull()?.find { it.productId == product.id }
    if (existing != null) {
      dao.updateCartItem(existing.copy(quantity = existing.quantity + quantity))
    } else {
      dao.insertCartItem(
        CartItemEntity(
          id = UUID.randomUUID().toString(),
          productId = product.id,
          title = product.title,
          price = product.price,
          originalPrice = product.originalPrice,
          imageUrl = product.imageUrl,
          sellerId = product.sellerId,
          sellerName = product.sellerName,
          quantity = quantity,
          isAssured = product.isAssured
        )
      )
    }
  }

  suspend fun updateCartQuantity(cartId: String, newQuantity: Int) {
    if (newQuantity <= 0) {
      dao.deleteCartItem(cartId)
    } else {
      val items = dao.getCartItems().firstOrNull() ?: return
      val item = items.find { it.id == cartId } ?: return
      dao.updateCartItem(item.copy(quantity = newQuantity))
    }
  }

  suspend fun removeFromCart(cartId: String) {
    dao.deleteCartItem(cartId)
  }

  suspend fun clearCart() {
    dao.clearCart()
  }

  // Wishlist operations
  fun isWishlisted(productId: String): Flow<Boolean> = dao.isWishlisted(productId)

  suspend fun toggleWishlist(product: Product) {
    val items = dao.getWishlistItems().firstOrNull()
    val isPresent = items?.any { it.productId == product.id } == true
    if (isPresent) {
      dao.deleteWishlistItem(product.id)
    } else {
      dao.insertWishlistItem(
        WishlistItemEntity(
          productId = product.id,
          title = product.title,
          price = product.price,
          originalPrice = product.originalPrice,
          discountPercent = product.discountPercent,
          imageUrl = product.imageUrl,
          rating = product.rating,
          sellerName = product.sellerName
        )
      )
    }
  }

  // Address operations
  suspend fun addAddress(address: AddressEntity) {
    if (address.isDefault) {
      dao.setDefaultAddress(address.id)
    }
    dao.insertAddress(address)
  }

  suspend fun deleteAddress(id: String) {
    dao.deleteAddress(id)
  }

  suspend fun setDefaultAddress(id: String) {
    dao.setDefaultAddress(id)
  }

  // Place Order
  suspend fun placeOrder(
    cartItemsList: List<CartItemEntity>,
    selectedAddress: AddressEntity,
    paymentMethod: String,
    couponApplied: Coupon?,
    useSuperCoins: Boolean
  ): OrderEntity {
    val totalAmountBeforeDiscount = cartItemsList.sumOf { it.price * it.quantity }
    val discount = couponApplied?.discountAmount ?: 0
    val coinDiscount = if (useSuperCoins) 100 else 0
    val finalTotal = (totalAmountBeforeDiscount - discount - coinDiscount).coerceAtLeast(0)
    val totalSavings = cartItemsList.sumOf { (it.originalPrice - it.price) * it.quantity } + discount + coinDiscount

    val orderId = "OD" + (1000000000L..9999999999L).random().toString()
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(Date())

    val primaryTitle = if (cartItemsList.size == 1) {
      cartItemsList.first().title
    } else {
      "${cartItemsList.first().title} and ${cartItemsList.size - 1} other item(s)"
    }

    val primarySeller = cartItemsList.firstOrNull()?.sellerName ?: "RetailNet"
    val earnedCoins = (finalTotal / 100) * 4

    val order = OrderEntity(
      orderId = orderId,
      dateFormatted = formattedDate,
      productTitle = primaryTitle,
      productCount = cartItemsList.sumOf { it.quantity },
      totalAmount = finalTotal,
      savingsAmount = totalSavings,
      superCoinsEarned = earnedCoins,
      deliveryPincode = selectedAddress.pincode,
      addressSummary = "${selectedAddress.name}, ${selectedAddress.addressLine}, ${selectedAddress.city}",
      paymentMethod = paymentMethod,
      status = "Placed",
      expectedDelivery = "Delivery in 2 Days",
      trackingStep = 0,
      sellerName = primarySeller,
      isEligibleForReturn = true
    )

    dao.insertOrder(order)
    dao.clearCart()

    // Update user SuperCoins
    val profile = dao.getUserProfile().firstOrNull()
    if (profile != null) {
      val updatedCoins = (profile.superCoins - (if (useSuperCoins) 100 else 0) + earnedCoins).coerceAtLeast(0)
      dao.updateSuperCoins(updatedCoins)
    }

    return order
  }

  suspend fun advanceOrderStatus(orderId: String) {
    val order = dao.getOrderById(orderId).firstOrNull() ?: return
    val nextStep = (order.trackingStep + 1).coerceAtMost(4)
    val newStatus = when (nextStep) {
      0 -> "Placed"
      1 -> "Packed"
      2 -> "Shipped"
      3 -> "Out for Delivery"
      4 -> "Delivered"
      else -> order.status
    }
    dao.updateOrderStatus(orderId, newStatus, nextStep)
  }

  suspend fun requestReturn(orderId: String, reason: String) {
    dao.updateOrderStatus(orderId, "Return Initiated (Refund in 24h)", 4)
  }

  // Delivery Estimator by PIN code
  fun estimateDelivery(pincode: String): DeliveryEstimate {
    val cleanPin = pincode.trim()
    return when {
      cleanPin.length != 6 || !cleanPin.all { it.isDigit() } -> {
        DeliveryEstimate(false, "Invalid PIN code. Please enter a 6-digit PIN.", 0, false)
      }
      cleanPin.startsWith("56") -> {
        DeliveryEstimate(true, "Express Delivery by Tomorrow 8 PM", 1, true, "Free Delivery with Flipkart Plus")
      }
      cleanPin.startsWith("11") || cleanPin.startsWith("40") || cleanPin.startsWith("60") -> {
        DeliveryEstimate(true, "Standard Delivery in 2 Days", 2, true, "Eligible for Cash on Delivery")
      }
      else -> {
        DeliveryEstimate(true, "Delivery in 3-4 Days", 4, true, "Cash on Delivery Available")
      }
    }
  }

  // Seller operations
  fun addNewProductBySeller(product: Product) {
    val updated = _products.value.toMutableList()
    updated.add(0, product)
    _products.value = updated
  }

  fun updateProductStock(productId: String, newStock: Int, newPrice: Int) {
    val updated = _products.value.map { p ->
      if (p.id == productId) {
        p.copy(stockCount = newStock, price = newPrice, inStock = newStock > 0)
      } else {
        p
      }
    }
    _products.value = updated
  }

  // Language update
  suspend fun setLanguage(lang: String) {
    dao.updateLanguage(lang)
  }

  // Seed Catalog
  private fun initialProductCatalog(): List<Product> {
    return listOf(
      Product(
        id = "prod_elec_1",
        title = "Sony WH-1000XM5 Wireless Noise Cancelling Headphones",
        category = "electronics",
        brand = "Sony",
        price = 26990,
        originalPrice = 34990,
        rating = 4.7f,
        ratingsCount = 14200,
        imageUrl = "",
        sellerId = "seller_retailnet",
        sellerName = "RetailNet",
        isAssured = true,
        inStock = true,
        stockCount = 38,
        description = "Industry-leading noise cancellation with two processors and 8 microphones. Magnificent Sound, engineered to perfection with the new Integrated Processor V1. Crystal clear hands-free calling with 4 beamforming microphones.",
        specs = mapOf(
          "Battery Life" to "Up to 30 Hours",
          "Charging" to "USB Type-C Quick Charge (3 min = 3 hrs)",
          "Bluetooth" to "v5.2 with LDAC support",
          "Weight" to "250 grams"
        ),
        highlights = listOf(
          "Auto Noise Canceling Optimizer",
          "Ultra-comfortable lightweight leather",
          "Speak-to-Chat technology automatically pauses music"
        )
      ),
      Product(
        id = "prod_mob_1",
        title = "Nothing Phone (2a) Plus 5G (Black, 256 GB)",
        category = "mobiles",
        brand = "Nothing",
        price = 24999,
        originalPrice = 29999,
        rating = 4.5f,
        ratingsCount = 28940,
        imageUrl = "",
        sellerId = "seller_supercom",
        sellerName = "SuperComNet",
        isAssured = true,
        inStock = true,
        stockCount = 82,
        description = "Custom Dimensity 7350 Pro 5G processor. 50 MP main + 50 MP ultra-wide camera with 50 MP front shooter. Iconic transparent back with programmable Glyph Interface LED lights.",
        specs = mapOf(
          "Display" to "6.7 inch Flexible AMOLED 120Hz",
          "Processor" to "MediaTek Dimensity 7350 Pro 5G",
          "Battery" to "5000 mAh with 50W Fast Charge",
          "RAM / Storage" to "8 GB / 256 GB"
        ),
        highlights = listOf(
          "Nothing OS 2.6 with Zero Bloatware",
          "Iconic Glyph Interface Lighting",
          "50 MP Selfie Camera with 4K recording"
        )
      ),
      Product(
        id = "prod_fashion_1",
        title = "Urban Classic Heavyweight Oversized French Terry Hoodie",
        category = "fashion",
        brand = "UrbanClassic",
        price = 1499,
        originalPrice = 3499,
        rating = 4.3f,
        ratingsCount = 6800,
        imageUrl = "",
        sellerId = "seller_fashionhub",
        sellerName = "FashionHub Lifestyle",
        isAssured = true,
        inStock = true,
        stockCount = 120,
        description = "450 GSM pure combed cotton French Terry fabric. Dropped shoulders, double-stitched kangaroo pocket, and rib-knit cuffs designed for relaxed everyday drape.",
        specs = mapOf(
          "Fabric" to "100% Combed Cotton French Terry",
          "Fit" to "Drop-Shoulder Oversized",
          "GSM" to "450 GSM Heavyweight",
          "Care" to "Machine Wash Cold"
        ),
        highlights = listOf(
          "Anti-pilling enzyme treated",
          "Pre-shrunk fabric construction",
          "Available in sizes S, M, L, XL, XXL"
        )
      ),
      Product(
        id = "prod_home_1",
        title = "ErgoSmart Executive Mesh Ergonomic Office Chair",
        category = "home",
        brand = "ErgoSmart",
        price = 7999,
        originalPrice = 16999,
        rating = 4.6f,
        ratingsCount = 9410,
        imageUrl = "",
        sellerId = "seller_omnitech",
        sellerName = "OmniLiving Corp",
        isAssured = true,
        inStock = true,
        stockCount = 24,
        description = "High-density breathable mesh back with 3D adjustable lumbar support, 2D armrests, and 135-degree synchro-tilt recline mechanism for all-day spinal support.",
        specs = mapOf(
          "Weight Capacity" to "150 kg",
          "Gas Lift" to "Class 4 Heavy Duty",
          "Base" to "Aluminium Alloy 5-star base",
          "Recline" to "90 to 135 degrees tilt lock"
        ),
        highlights = listOf(
          "BIFMA certified durability",
          "3-year manufacturer on-site warranty",
          "Self-assembly kit with video manual included"
        )
      ),
      Product(
        id = "prod_app_1",
        title = "Mi Smart TV X Series 43 inch 4K Ultra HD Dolby Vision",
        category = "appliances",
        brand = "Xiaomi",
        price = 23999,
        originalPrice = 34999,
        rating = 4.4f,
        ratingsCount = 31200,
        imageUrl = "",
        sellerId = "seller_retailnet",
        sellerName = "RetailNet",
        isAssured = true,
        inStock = true,
        stockCount = 15,
        description = "4K Ultra HD (3840 x 2160) display with Dolby Vision, HDR10, and Vivid Picture Engine. 30W stereo speakers with Dolby Audio & DTS Virtual:X sound.",
        specs = mapOf(
          "Resolution" to "4K Ultra HD (3840 x 2160)",
          "Audio" to "30 Watts Dolby Audio DTS:X",
          "OS" to "Google TV with PatchWall",
          "Ports" to "3 HDMI, 2 USB, Dual Band Wi-Fi"
        ),
        highlights = listOf(
          "Bezel-less metal frame design",
          "MEMC Reality Flow technology",
          "Hands-free Google Assistant built-in"
        )
      ),
      Product(
        id = "prod_groc_1",
        title = "Organic Whole California Almonds (Pack of 1kg)",
        category = "grocery",
        brand = "NutriPure",
        price = 749,
        originalPrice = 1200,
        rating = 4.8f,
        ratingsCount = 18400,
        imageUrl = "",
        sellerId = "seller_groceryplus",
        sellerName = "Flipkart Grocery SuperStore",
        isAssured = true,
        inStock = true,
        stockCount = 450,
        description = "100% natural, crisp California nonpareil almonds. Rich in protein, dietary fiber, vitamin E, and healthy antioxidants.",
        specs = mapOf(
          "Weight" to "1000g (1 kg)",
          "Packaging" to "Nitrogen flushed vacuum zip lock",
          "Shelf Life" to "12 Months",
          "Grade" to "A+ Premium Nonpareil"
        ),
        highlights = listOf(
          "Zero cholesterol, zero trans fat",
          "Fresh harvest guarantee",
          "Same-day delivery available in top metros"
        )
      ),
      Product(
        id = "prod_mob_2",
        title = "Apple iPhone 15 (Blue, 128 GB)",
        category = "mobiles",
        brand = "Apple",
        price = 65999,
        originalPrice = 79900,
        rating = 4.7f,
        ratingsCount = 45900,
        imageUrl = "",
        sellerId = "seller_supercom",
        sellerName = "SuperComNet",
        isAssured = true,
        inStock = true,
        stockCount = 60,
        description = "Dynamic Island bubbles up alerts and Live Activities. 48MP Main camera with 2x Telephoto. Durable color-infused glass and aluminum design with USB-C connector.",
        specs = mapOf(
          "Display" to "6.1 inch Super Retina XDR display",
          "Chip" to "A16 Bionic chip",
          "Camera" to "48MP Main + 12MP Ultra Wide",
          "Connector" to "USB-C"
        ),
        highlights = listOf(
          "Dynamic Island feature",
          "All-day battery life",
          "Emergency SOS via satellite"
        )
      ),
      Product(
        id = "prod_elec_2",
        title = "Apple MacBook Air M2 (8GB RAM, 256GB SSD, Midnight)",
        category = "electronics",
        brand = "Apple",
        price = 84990,
        originalPrice = 99900,
        rating = 4.8f,
        ratingsCount = 18900,
        imageUrl = "",
        sellerId = "seller_retailnet",
        sellerName = "RetailNet",
        isAssured = true,
        inStock = true,
        stockCount = 20,
        description = "Incredibly thin and fast. Striking 13.6-inch Liquid Retina display, MagSafe 3 charging, 1080p FaceTime HD camera, and up to 18 hours of battery life.",
        specs = mapOf(
          "Processor" to "Apple M2 8-core CPU / 8-core GPU",
          "Display" to "13.6 inch Liquid Retina 500 nits",
          "Battery" to "Up to 18 hours",
          "Weight" to "1.24 kg"
        ),
        highlights = listOf(
          "Silent fanless design",
          "Backlit Magic Keyboard with Touch ID",
          "Four-speaker sound system with Spatial Audio"
        )
      )
    )
  }
}

data class DeliveryEstimate(
  val isDeliverable: Boolean,
  val message: String,
  val days: Int,
  val isCodAvailable: Boolean,
  val badge: String = ""
)
