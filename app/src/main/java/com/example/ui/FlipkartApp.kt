package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppDestination
import com.example.ui.components.AddAddressDialog
import com.example.ui.components.AddProductDialog
import com.example.ui.components.ArViewerDialog
import com.example.ui.components.FlipkartBottomBar
import com.example.ui.components.FlipkartHeader
import com.example.ui.components.LanguagePickerDialog
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.ReturnRequestDialog
import com.example.ui.components.VoiceSearchDialog
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderDetailScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SellerHubScreen
import com.example.ui.screens.VibesScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.viewmodel.FlipkartViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FlipkartApp(
  viewModel: FlipkartViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val snackbarHostState = remember { SnackbarHostState() }

  val destination by viewModel.currentDestination.collectAsState()
  val selectedProduct by viewModel.selectedProduct.collectAsState()
  val selectedOrder by viewModel.selectedOrder.collectAsState()

  val products by viewModel.filteredProducts.collectAsState()
  val allProducts by viewModel.repository.products.collectAsState()
  val categories = viewModel.repository.categories
  val vibes = viewModel.repository.vibePosts

  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val minRating by viewModel.minRatingFilter.collectAsState()
  val assuredOnly by viewModel.assuredOnlyFilter.collectAsState()
  val inStockOnly by viewModel.inStockOnlyFilter.collectAsState()
  val sortBy by viewModel.sortBy.collectAsState()

  val cartItems by viewModel.cartItems.collectAsState()
  val wishlistItems by viewModel.wishlistItems.collectAsState()
  val savedAddresses by viewModel.savedAddresses.collectAsState()
  val orders by viewModel.orders.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()

  val selectedAddressId by viewModel.selectedAddressId.collectAsState()
  val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()
  val appliedCoupon by viewModel.appliedCoupon.collectAsState()
  val useSuperCoins by viewModel.useSuperCoins.collectAsState()
  val currentPincode by viewModel.deliveryPincodeInput.collectAsState()
  val deliveryEstimate by viewModel.deliveryEstimate.collectAsState()

  val flashSaleActive by viewModel.flashSaleActive.collectAsState()
  val commissionPercent by viewModel.platformCommissionPercent.collectAsState()

  // Dialog States
  val isVoiceSearchOpen by viewModel.isVoiceSearchOpen.collectAsState()
  val isArViewerOpen by viewModel.isArViewerOpen.collectAsState()
  val isReturnDialogOpen by viewModel.isReturnDialogOpen.collectAsState()
  val isAddAddressDialogOpen by viewModel.isAddAddressDialogOpen.collectAsState()
  val isOrderSuccessDialogOpen by viewModel.isOrderSuccessDialogOpen.collectAsState()
  val isLanguagePickerOpen by viewModel.isLanguagePickerOpen.collectAsState()
  val isAddProductDialogOpen by viewModel.isAddProductDialogOpen.collectAsState()

  // Toast events
  LaunchedEffect(Unit) {
    viewModel.toastMessages.collectLatest { msg ->
      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
  }

  // Handle Android back button
  BackHandler(enabled = destination != AppDestination.HOME) {
    when (destination) {
      AppDestination.PRODUCT_DETAIL,
      AppDestination.ORDER_DETAIL -> viewModel.navigateTo(AppDestination.HOME)
      AppDestination.CHECKOUT -> viewModel.navigateTo(AppDestination.CART)
      AppDestination.SELLER_HUB,
      AppDestination.ADMIN_DASHBOARD,
      AppDestination.ORDERS,
      AppDestination.WISHLIST -> viewModel.navigateTo(AppDestination.ACCOUNT)
      else -> viewModel.navigateTo(AppDestination.HOME)
    }
  }

  val totalCartItemCount = cartItems.sumOf { it.quantity }
  val isTopDestination = destination in listOf(
    AppDestination.HOME,
    AppDestination.SEARCH,
    AppDestination.VIBES,
    AppDestination.ACCOUNT,
    AppDestination.CART
  )

  Scaffold(
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      if (destination == AppDestination.HOME) {
        FlipkartHeader(
          cartCount = totalCartItemCount,
          selectedLanguage = userProfile?.selectedLanguage ?: "English",
          onSearchClick = { viewModel.navigateTo(AppDestination.SEARCH) },
          onVoiceSearchClick = { viewModel.isVoiceSearchOpen.value = true },
          onCartClick = { viewModel.navigateTo(AppDestination.CART) },
          onLanguageClick = { viewModel.isLanguagePickerOpen.value = true }
        )
      }
    },
    bottomBar = {
      if (isTopDestination) {
        FlipkartBottomBar(
          currentDestination = destination,
          cartCount = totalCartItemCount,
          onNavigate = { dest -> viewModel.navigateTo(dest) }
        )
      }
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF1F3F6))
    ) {
      when (destination) {
        AppDestination.HOME -> {
          HomeScreen(
            categories = categories,
            selectedCategory = selectedCategory,
            products = products,
            wishlistItems = wishlistItems,
            userProfile = userProfile,
            onCategorySelected = { catId ->
              viewModel.onCategorySelected(catId)
              viewModel.navigateTo(AppDestination.SEARCH)
            },
            onProductClick = { prod -> viewModel.selectProduct(prod) },
            onWishlistToggle = { prod -> viewModel.toggleWishlist(prod) },
            onAddToCart = { prod -> viewModel.addToCart(prod) },
            onNavigate = { dest -> viewModel.navigateTo(dest) }
          )
        }

        AppDestination.CATEGORIES,
        AppDestination.SEARCH -> {
          SearchScreen(
            query = searchQuery,
            products = products,
            wishlistItems = wishlistItems,
            minRating = minRating,
            assuredOnly = assuredOnly,
            inStockOnly = inStockOnly,
            sortBy = sortBy,
            onQueryChanged = { viewModel.onSearchQueryChanged(it) },
            onUpdateFilters = { r, p, a, s, sort -> viewModel.updateFilters(r, p, a, s, sort) },
            onResetFilters = { viewModel.resetFilters() },
            onVoiceSearchClick = { viewModel.isVoiceSearchOpen.value = true },
            onProductClick = { prod -> viewModel.selectProduct(prod) },
            onWishlistToggle = { prod -> viewModel.toggleWishlist(prod) },
            onAddToCart = { prod -> viewModel.addToCart(prod) },
            onBack = { viewModel.navigateTo(AppDestination.HOME) }
          )
        }

        AppDestination.PRODUCT_DETAIL -> {
          ProductDetailScreen(
            product = selectedProduct,
            isWishlisted = wishlistItems.any { it.productId == selectedProduct?.id },
            cartCount = totalCartItemCount,
            deliveryEstimate = deliveryEstimate,
            currentPincode = currentPincode,
            onWishlistToggle = { selectedProduct?.let { viewModel.toggleWishlist(it) } },
            onAddToCart = { selectedProduct?.let { viewModel.addToCart(it) } },
            onBuyNow = {
              selectedProduct?.let { prod ->
                viewModel.addToCart(prod)
                viewModel.navigateTo(AppDestination.CHECKOUT)
              }
            },
            onOpenArViewer = { viewModel.isArViewerOpen.value = true },
            onCheckPincode = { pin -> viewModel.checkPincode(pin) },
            onCartClick = { viewModel.navigateTo(AppDestination.CART) },
            onBack = { viewModel.navigateTo(AppDestination.HOME) }
          )
        }

        AppDestination.CART -> {
          CartScreen(
            cartItems = cartItems,
            userProfile = userProfile,
            appliedCoupon = appliedCoupon,
            useSuperCoins = useSuperCoins,
            onUpdateQuantity = { cartId, qty -> viewModel.updateCartItemQuantity(cartId, qty) },
            onRemoveItem = { cartId -> viewModel.removeFromCart(cartId) },
            onApplyCoupon = { code -> viewModel.applyCoupon(code) },
            onRemoveCoupon = { viewModel.removeCoupon() },
            onToggleSuperCoins = { viewModel.toggleUseSuperCoins(it) },
            onCheckout = { viewModel.navigateTo(AppDestination.CHECKOUT) },
            onContinueShopping = { viewModel.navigateTo(AppDestination.HOME) },
            onBack = { viewModel.navigateTo(AppDestination.HOME) }
          )
        }

        AppDestination.CHECKOUT -> {
          CheckoutScreen(
            cartItems = cartItems,
            addresses = savedAddresses,
            selectedAddressId = selectedAddressId,
            selectedPaymentMethod = selectedPaymentMethod,
            appliedCoupon = appliedCoupon,
            useSuperCoins = useSuperCoins,
            userProfile = userProfile,
            onSelectAddress = { viewModel.selectAddress(it) },
            onAddNewAddressClick = { viewModel.isAddAddressDialogOpen.value = true },
            onSelectPaymentMethod = { viewModel.selectedPaymentMethod.value = it },
            onConfirmOrder = { viewModel.placeOrderNow() },
            onBack = { viewModel.navigateTo(AppDestination.CART) }
          )
        }

        AppDestination.ORDERS -> {
          OrdersScreen(
            orders = orders,
            onOrderClick = { ord -> viewModel.selectOrder(ord) },
            onBack = { viewModel.navigateTo(AppDestination.ACCOUNT) }
          )
        }

        AppDestination.ORDER_DETAIL -> {
          OrderDetailScreen(
            order = selectedOrder,
            onAdvanceStatus = { ordId -> viewModel.advanceStatus(ordId) },
            onRequestReturn = { viewModel.isReturnDialogOpen.value = true },
            onBack = { viewModel.navigateTo(AppDestination.ORDERS) }
          )
        }

        AppDestination.WISHLIST -> {
          WishlistScreen(
            wishlistItems = wishlistItems,
            allProducts = allProducts,
            onRemoveWishlist = { prod -> viewModel.toggleWishlist(prod) },
            onAddToCart = { prod -> viewModel.addToCart(prod) },
            onProductClick = { prod -> viewModel.selectProduct(prod) },
            onBack = { viewModel.navigateTo(AppDestination.ACCOUNT) }
          )
        }

        AppDestination.VIBES -> {
          VibesScreen(
            vibes = vibes,
            allProducts = allProducts,
            onAddToCart = { prod -> viewModel.addToCart(prod) },
            onProductClick = { prod -> viewModel.selectProduct(prod) }
          )
        }

        AppDestination.ACCOUNT -> {
          AccountScreen(
            userProfile = userProfile,
            onNavigate = { dest -> viewModel.navigateTo(dest) },
            onOpenLanguagePicker = { viewModel.isLanguagePickerOpen.value = true }
          )
        }

        AppDestination.SELLER_HUB -> {
          SellerHubScreen(
            products = allProducts,
            onUpdateProductStockAndPrice = { id, st, pr -> viewModel.updateProductStockAndPrice(id, st, pr) },
            onOpenAddProductDialog = { viewModel.isAddProductDialogOpen.value = true },
            onBack = { viewModel.navigateTo(AppDestination.ACCOUNT) }
          )
        }

        AppDestination.ADMIN_DASHBOARD -> {
          AdminDashboardScreen(
            flashSaleActive = flashSaleActive,
            commissionPercent = commissionPercent,
            onToggleFlashSale = { viewModel.flashSaleActive.value = it },
            onCommissionChanged = { viewModel.platformCommissionPercent.value = it },
            onBack = { viewModel.navigateTo(AppDestination.ACCOUNT) }
          )
        }
      }
    }
  }

  // Floating Dialogs
  VoiceSearchDialog(
    isOpen = isVoiceSearchOpen,
    onDismiss = { viewModel.isVoiceSearchOpen.value = false },
    onQueryDetected = { recognizedQuery ->
      viewModel.onSearchQueryChanged(recognizedQuery)
      viewModel.navigateTo(AppDestination.SEARCH)
    }
  )

  ArViewerDialog(
    product = selectedProduct,
    isOpen = isArViewerOpen,
    onDismiss = { viewModel.isArViewerOpen.value = false }
  )

  AddAddressDialog(
    isOpen = isAddAddressDialogOpen,
    onDismiss = { viewModel.isAddAddressDialogOpen.value = false },
    onSaveAddress = { n, ph, pin, line, loc, city, st, type, def ->
      viewModel.addNewAddress(n, ph, pin, line, loc, city, st, type, def)
    }
  )

  LanguagePickerDialog(
    isOpen = isLanguagePickerOpen,
    currentLanguage = userProfile?.selectedLanguage ?: "English",
    onDismiss = { viewModel.isLanguagePickerOpen.value = false },
    onLanguageSelected = { lang -> viewModel.changeLanguage(lang) }
  )

  AddProductDialog(
    isOpen = isAddProductDialogOpen,
    onDismiss = { viewModel.isAddProductDialogOpen.value = false },
    onAddProduct = { title, cat, brand, price, orig, stock, desc ->
      viewModel.addProductAsSeller(title, cat, brand, price, orig, stock, desc)
    }
  )

  OrderSuccessDialog(
    isOpen = isOrderSuccessDialogOpen,
    order = selectedOrder,
    onTrackOrder = {
      viewModel.isOrderSuccessDialogOpen.value = false
      viewModel.navigateTo(AppDestination.ORDER_DETAIL)
    },
    onContinueShopping = {
      viewModel.isOrderSuccessDialogOpen.value = false
      viewModel.navigateTo(AppDestination.HOME)
    }
  )

  ReturnRequestDialog(
    isOpen = isReturnDialogOpen,
    orderId = selectedOrder?.orderId,
    onDismiss = { viewModel.isReturnDialogOpen.value = false },
    onSubmitReturn = { reason ->
      selectedOrder?.let { viewModel.requestReturnOrder(it.orderId, reason) }
    }
  )
}
