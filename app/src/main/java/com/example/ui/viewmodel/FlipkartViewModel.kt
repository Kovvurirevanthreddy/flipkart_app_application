package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AddressEntity
import com.example.data.model.AppDestination
import com.example.data.model.CartItemEntity
import com.example.data.model.Coupon
import com.example.data.model.OrderEntity
import com.example.data.model.Product
import com.example.data.model.UserProfileEntity
import com.example.data.model.WishlistItemEntity
import com.example.data.repository.DeliveryEstimate
import com.example.data.repository.FlipkartRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class FlipkartViewModel(application: Application) : AndroidViewModel(application) {

  private val database = AppDatabase.getDatabase(application)
  val repository = FlipkartRepository(database.flipkartDao())

  // Navigation state
  private val _currentDestination = MutableStateFlow(AppDestination.HOME)
  val currentDestination = _currentDestination.asStateFlow()

  // Selected product & order
  private val _selectedProduct = MutableStateFlow<Product?>(null)
  val selectedProduct = _selectedProduct.asStateFlow()

  private val _selectedOrder = MutableStateFlow<OrderEntity?>(null)
  val selectedOrder = _selectedOrder.asStateFlow()

  // Search & Filter State
  private val _searchQuery = MutableStateFlow("")
  val searchQuery = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("all")
  val selectedCategory = _selectedCategory.asStateFlow()

  private val _minRatingFilter = MutableStateFlow(0f)
  val minRatingFilter = _minRatingFilter.asStateFlow()

  private val _maxPriceFilter = MutableStateFlow(100000)
  val maxPriceFilter = _maxPriceFilter.asStateFlow()

  private val _assuredOnlyFilter = MutableStateFlow(false)
  val assuredOnlyFilter = _assuredOnlyFilter.asStateFlow()

  private val _inStockOnlyFilter = MutableStateFlow(false)
  val inStockOnlyFilter = _inStockOnlyFilter.asStateFlow()

  private val _sortBy = MutableStateFlow("POPULARITY") // POPULARITY, PRICE_LOW, PRICE_HIGH, RATING
  val sortBy = _sortBy.asStateFlow()

  // Filtered Products
  val filteredProducts = combine(
    repository.products,
    searchQuery,
    selectedCategory,
    minRatingFilter,
    maxPriceFilter,
    assuredOnlyFilter,
    inStockOnlyFilter,
    sortBy
  ) { args: Array<Any> ->
    val query = args[1] as String
    val category = args[2] as String
    val minRating = args[3] as Float
    val maxPrice = args[4] as Int
    val assuredOnly = args[5] as Boolean
    val inStockOnly = args[6] as Boolean
    val sort = args[7] as String

    repository.searchProducts(
      query = query,
      category = category,
      minRating = minRating,
      maxPrice = maxPrice,
      assuredOnly = assuredOnly,
      inStockOnly = inStockOnly,
      sortBy = sort
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Database states
  val cartItems = repository.cartItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  val wishlistItems = repository.wishlistItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  val savedAddresses = repository.savedAddresses.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  val orders = repository.orders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  val userProfile = repository.userProfile.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Dialog States
  val isVoiceSearchOpen = MutableStateFlow(false)
  val isArViewerOpen = MutableStateFlow(false)
  val isReturnDialogOpen = MutableStateFlow(false)
  val isAddAddressDialogOpen = MutableStateFlow(false)
  val isOrderSuccessDialogOpen = MutableStateFlow(false)
  val isLanguagePickerOpen = MutableStateFlow(false)
  val isAddProductDialogOpen = MutableStateFlow(false)

  // Checkout State
  val selectedAddressId = MutableStateFlow<String?>(null)
  val selectedPaymentMethod = MutableStateFlow("UPI")
  val appliedCoupon = MutableStateFlow<Coupon?>(null)
  val useSuperCoins = MutableStateFlow(false)
  val deliveryPincodeInput = MutableStateFlow("560001")
  val deliveryEstimate = MutableStateFlow(repository.estimateDelivery("560001"))

  // Notification / Toast channel
  private val _toastMessages = MutableSharedFlow<String>()
  val toastMessages = _toastMessages.asSharedFlow()

  // Admin / Seller state
  val flashSaleActive = MutableStateFlow(true)
  val platformCommissionPercent = MutableStateFlow(8) // 8%

  init {
    viewModelScope.launch {
      repository.initializeDefaultData()
    }
  }

  // Navigation
  fun navigateTo(destination: AppDestination) {
    _currentDestination.value = destination
  }

  fun selectProduct(product: Product) {
    _selectedProduct.value = product
    _currentDestination.value = AppDestination.PRODUCT_DETAIL
  }

  fun selectOrder(order: OrderEntity) {
    _selectedOrder.value = order
    _currentDestination.value = AppDestination.ORDER_DETAIL
  }

  // Search & Filters
  fun onSearchQueryChanged(newQuery: String) {
    _searchQuery.value = newQuery
  }

  fun onCategorySelected(categoryId: String) {
    _selectedCategory.value = categoryId
  }

  fun updateFilters(
    minRating: Float = _minRatingFilter.value,
    maxPrice: Int = _maxPriceFilter.value,
    assuredOnly: Boolean = _assuredOnlyFilter.value,
    inStockOnly: Boolean = _inStockOnlyFilter.value,
    sort: String = _sortBy.value
  ) {
    _minRatingFilter.value = minRating
    _maxPriceFilter.value = maxPrice
    _assuredOnlyFilter.value = assuredOnly
    _inStockOnlyFilter.value = inStockOnly
    _sortBy.value = sort
  }

  fun resetFilters() {
    _minRatingFilter.value = 0f
    _maxPriceFilter.value = 100000
    _assuredOnlyFilter.value = false
    _inStockOnlyFilter.value = false
    _sortBy.value = "POPULARITY"
  }

  // Cart actions
  fun addToCart(product: Product, quantity: Int = 1) {
    viewModelScope.launch {
      repository.addToCart(product, quantity)
      _toastMessages.emit("Added ${product.title.take(24)}... to Cart")
    }
  }

  fun updateCartItemQuantity(cartId: String, qty: Int) {
    viewModelScope.launch {
      repository.updateCartQuantity(cartId, qty)
    }
  }

  fun removeFromCart(cartId: String) {
    viewModelScope.launch {
      repository.removeFromCart(cartId)
      _toastMessages.emit("Item removed from Cart")
    }
  }

  // Wishlist actions
  fun toggleWishlist(product: Product) {
    viewModelScope.launch {
      repository.toggleWishlist(product)
      val isNowWishlisted = wishlistItems.value.none { it.productId == product.id }
      _toastMessages.emit(if (isNowWishlisted) "Saved to Wishlist" else "Removed from Wishlist")
    }
  }

  // PIN code estimator
  fun checkPincode(pin: String) {
    deliveryPincodeInput.value = pin
    deliveryEstimate.value = repository.estimateDelivery(pin)
  }

  // Coupons
  fun applyCoupon(code: String) {
    val matched = repository.availableCoupons.find { it.code.equals(code.trim(), ignoreCase = true) }
    if (matched != null) {
      val cartTotal = cartItems.value.sumOf { it.price * it.quantity }
      if (cartTotal >= matched.minCartValue) {
        appliedCoupon.value = matched
        viewModelScope.launch { _toastMessages.emit("Coupon ${matched.code} applied! Saved ₹${matched.discountAmount}") }
      } else {
        viewModelScope.launch { _toastMessages.emit("Add items worth ₹${matched.minCartValue - cartTotal} more to apply this coupon") }
      }
    } else {
      viewModelScope.launch { _toastMessages.emit("Invalid coupon code. Try FLIP500 or SUPER20") }
    }
  }

  fun removeCoupon() {
    appliedCoupon.value = null
  }

  fun toggleUseSuperCoins(use: Boolean) {
    useSuperCoins.value = use
  }

  // Address
  fun selectAddress(id: String) {
    selectedAddressId.value = id
  }

  fun addNewAddress(
    name: String,
    phone: String,
    pincode: String,
    addressLine: String,
    locality: String,
    city: String,
    state: String,
    type: String,
    isDefault: Boolean
  ) {
    viewModelScope.launch {
      val newAddr = AddressEntity(
        id = "addr_" + UUID.randomUUID().toString().take(8),
        name = name,
        phone = phone,
        pincode = pincode,
        addressLine = addressLine,
        locality = locality,
        city = city,
        state = state,
        type = type,
        isDefault = isDefault
      )
      repository.addAddress(newAddr)
      selectedAddressId.value = newAddr.id
      isAddAddressDialogOpen.value = false
      _toastMessages.emit("New delivery address added successfully")
    }
  }

  // Checkout & Place Order
  fun placeOrderNow() {
    val items = cartItems.value
    if (items.isEmpty()) {
      viewModelScope.launch { _toastMessages.emit("Cart is empty!") }
      return
    }

    val address = savedAddresses.value.find { it.id == selectedAddressId.value }
      ?: savedAddresses.value.firstOrNull()

    if (address == null) {
      viewModelScope.launch { _toastMessages.emit("Please select or add a delivery address") }
      return
    }

    viewModelScope.launch {
      val createdOrder = repository.placeOrder(
        cartItemsList = items,
        selectedAddress = address,
        paymentMethod = selectedPaymentMethod.value,
        couponApplied = appliedCoupon.value,
        useSuperCoins = useSuperCoins.value
      )
      _selectedOrder.value = createdOrder
      appliedCoupon.value = null
      useSuperCoins.value = false
      isOrderSuccessDialogOpen.value = true
      _toastMessages.emit("Order Placed Successfully! OD#${createdOrder.orderId}")
    }
  }

  // Advance order status simulation
  fun advanceStatus(orderId: String) {
    viewModelScope.launch {
      repository.advanceOrderStatus(orderId)
      // refresh selected order
      _selectedOrder.value?.let { current ->
        val nextStep = (current.trackingStep + 1).coerceAtMost(4)
        val newStatus = when (nextStep) {
          1 -> "Packed"
          2 -> "Shipped"
          3 -> "Out for Delivery"
          4 -> "Delivered"
          else -> current.status
        }
        _selectedOrder.value = current.copy(trackingStep = nextStep, status = newStatus)
      }
      _toastMessages.emit("Order status advanced")
    }
  }

  fun requestReturnOrder(orderId: String, reason: String) {
    viewModelScope.launch {
      repository.requestReturn(orderId, reason)
      _selectedOrder.value?.let { current ->
        _selectedOrder.value = current.copy(status = "Return Initiated (Doorstep Pickup)", isEligibleForReturn = false)
      }
      isReturnDialogOpen.value = false
      _toastMessages.emit("Return initiated. Refund of ₹${_selectedOrder.value?.totalAmount} will be credited upon doorstep pickup.")
    }
  }

  // Language
  fun changeLanguage(lang: String) {
    viewModelScope.launch {
      repository.setLanguage(lang)
      isLanguagePickerOpen.value = false
      _toastMessages.emit("App language changed to $lang")
    }
  }

  // Seller Hub
  fun addProductAsSeller(
    title: String,
    category: String,
    brand: String,
    price: Int,
    originalPrice: Int,
    stockCount: Int,
    description: String
  ) {
    val newProd = Product(
      id = "prod_seller_" + UUID.randomUUID().toString().take(8),
      title = title,
      category = category,
      brand = brand,
      price = price,
      originalPrice = originalPrice,
      rating = 4.5f,
      ratingsCount = 1,
      stockCount = stockCount,
      description = description,
      sellerId = "seller_current",
      sellerName = userProfile.value?.name ?: "Verified Seller",
      isAssured = true,
      inStock = stockCount > 0
    )
    repository.addNewProductBySeller(newProd)
    isAddProductDialogOpen.value = false
    viewModelScope.launch { _toastMessages.emit("Product '$title' listed successfully in Flipkart Catalog!") }
  }

  fun updateProductStockAndPrice(productId: String, stock: Int, price: Int) {
    repository.updateProductStock(productId, stock, price)
    viewModelScope.launch { _toastMessages.emit("Inventory & price updated successfully") }
  }
}
