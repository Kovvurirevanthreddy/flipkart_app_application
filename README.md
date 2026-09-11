# Flipkart – E-Commerce Android Application

A modern **Android e-commerce application inspired by Flipkart**, built using **Kotlin and Jetpack Compose**. The project demonstrates a complete shopping experience including product discovery, search, wishlist, cart management, checkout, order tracking, seller features, and an admin dashboard.

> **Note: This is an educational/project implementation inspired by the Flipkart shopping experience. It is not an official Flipkart application.

## 📱 Project Overview

This application recreates the core experience of a modern Indian e-commerce platform while adding several interactive features.

Users can browse products, search and filter products, view product details, add items to their wishlist or cart, apply coupons, use SuperCoins, select delivery addresses and payment methods, place orders, and track their orders.

The application also includes dedicated **Seller Hub** and **Admin Dashboard** functionality for demonstrating different roles within an e-commerce ecosystem.

## ✨ Features

### 🛍️ Shopping

* Product browsing
* Product categories
* Product details
* Product ratings and reviews information
* Discount and MRP display
* Stock availability
* Flipkart Assured-style product indicators
* Seller information
* Product highlights and specifications

### 🔎 Search & Discovery

* Product search
* Search by product name, brand, or category
* Category-based filtering
* Rating filter
* Maximum price filter
* In-stock filter
* Assured-product filter
* Sort by:

  * Popularity
  * Price: Low to High
  * Price: High to Low
  * Rating

### ❤️ Wishlist

* Add/remove products from wishlist
* Persistent wishlist storage
* Quick access to saved products

### 🛒 Cart

* Add products to cart
* Increase/decrease quantity
* Remove products
* Seller-wise cart grouping
* Price calculation
* MRP savings calculation
* Coupon discounts
* SuperCoins discount

### 💳 Checkout

* Delivery address selection
* Multiple payment methods
* Coupon application
* SuperCoins usage
* Order price calculation
* Order placement
* Order confirmation

### 📦 Orders & Tracking

* View previous orders
* Order details
* Order status
* Delivery information
* Expected delivery
* Order tracking stages
* Return request functionality
* Tax invoice interface

### ⭐ Rewards

* SuperCoins
* SuperCoins earning through orders
* SuperCoins redemption during checkout
* Wallet balance
* Pay Later information

### 🏪 Seller Hub

* Seller dashboard
* Add products
* Product management
* Seller-specific product information

### 🛠️ Admin Dashboard

* Dedicated admin dashboard
* Product/catalog management interface
* Administrative controls for the application

### 🎥 Vibes

* Product showcase feed
* Trending product content
* Product-linked posts
* Like counts and creator information

### 🎙️ Voice Search

* Voice-based product search interface
* Voice search shortcuts

### 🧊 3D / AR Product View

* Interactive 3D/AR-style product viewing interface
* Product visualization experience
* Simulated "Project in Your Room" experience

### 📍 Delivery PIN Checker

* Check estimated delivery using a PIN code
* Delivery-day estimation for products

### 🌐 Language Support

* In-app language selection
* Language picker interface

## 🧰 Tech Stack

| Technology         | Usage                        |
| ------------------ | ---------------------------- |
| Kotlin             | Primary programming language |
| Jetpack Compose    | UI development               |
| Material 3         | UI components and design     |
| Android SDK        | Application platform         |
| Room Database      | Local data persistence       |
| KSP                | Code generation              |
| ViewModel          | UI state and business logic  |
| Kotlin Coroutines  | Asynchronous operations      |
| Kotlin Flow        | Reactive data streams        |
| Coil               | Image loading                |
| Retrofit           | API/network layer            |
| OkHttp             | HTTP client                  |
| Moshi              | JSON serialization           |
| Firebase AI        | AI/Gemini integration        |
| Firebase App Check | Application protection       |
| Robolectric        | Testing                      |
| Roborazzi          | Screenshot testing           |

## 🏗️ Architecture

The application follows a separation of UI, state-management, and data-access responsibilities.

```text
com.example
│
├── MainActivity.kt
│
├── data
│   ├── local
│   │   ├── AppDatabase.kt
│   │   └── FlipkartDao.kt
│   │
│   ├── model
│   │   └── Models.kt
│   │
│   └── repository
│       └── FlipkartRepository.kt
│
└── ui
    ├── components
    │   ├── ProductCard.kt
    │   ├── FlipkartHeader.kt
    │   ├── FlipkartBottomBar.kt
    │   ├── BannerCarousel.kt
    │   ├── CategoryRow.kt
    │   ├── VoiceSearchDialog.kt
    │   ├── ArViewerDialog.kt
    │   ├── DeliveryPinChecker.kt
    │   └── ...
    │
    ├── screens
    │   ├── HomeScreen.kt
    │   ├── SearchScreen.kt
    │   ├── ProductDetailScreen.kt
    │   ├── CartScreen.kt
    │   ├── WishlistScreen.kt
    │   ├── CheckoutScreen.kt
    │   ├── OrdersScreen.kt
    │   ├── OrderDetailScreen.kt
    │   ├── AccountScreen.kt
    │   ├── SellerHubScreen.kt
    │   ├── AdminDashboardScreen.kt
    │   └── VibesScreen.kt
    │
    ├── theme
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    │
    └── viewmodel
        └── FlipkartViewModel.kt
```

## 💾 Local Data Storage

The application uses **Room Database** for persistent local data.

The database stores information such as:

* Cart items
* Wishlist items
* Saved addresses
* Orders
* User profile

Product catalog and certain application states are maintained through the repository layer.

## 🔐 Configuration

The project contains an `.env.example` file for configuration.

If Gemini AI functionality is enabled, configure the required API key through the appropriate environment/secrets configuration.

```env
GEMINI_API_KEY=YOUR_GEMINI_API_KEY
```

**Never commit your actual API key or other secrets to GitHub.**

## 🚀 Getting Started

### Prerequisites

* Android Studio
* JDK 11 or compatible Android Studio JDK
* Android SDK
* Android device or emulator
* Internet connection for features that require network/API access

### Installation

1. Clone the repository:

```bash
git clone YOUR_REPOSITORY_URL
```

2. Open the project in **Android Studio**.

3. Allow Gradle to sync and download the required dependencies.

4. Create the required `.env` configuration if Gemini functionality is being used.

5. Select an Android emulator or connect a physical Android device.

6. Build and run the application.

## 📋 Application Modules

| Module          | Description                               |
| --------------- | ----------------------------------------- |
| Home            | Product discovery and promotional content |
| Categories      | Browse products by category               |
| Search          | Search, filter and sort products          |
| Product Details | Detailed product information              |
| Wishlist        | Save products for later                   |
| Cart            | Manage selected products                  |
| Checkout        | Address, payment and discounts            |
| Orders          | View purchased products                   |
| Order Tracking  | Track delivery status                     |
| Account         | User profile and account features         |
| Vibes           | Product-focused social feed               |
| Seller Hub      | Seller product management                 |
| Admin Dashboard | Administrative interface                  |

## 🧪 Testing

The project contains Android and unit testing support using:

* JUnit
* Robolectric
* AndroidX testing
* Compose UI testing
* Roborazzi screenshot testing

Test files are located under:

```text
app/src/test/
app/src/androidTest/
```

## 📸 Screenshots

Add screenshots of the application here to make the repository more attractive and easier to understand.

Recommended screenshots:

1. Home screen
2. Search screen
3. Product details
4. Wishlist
5. Cart
6. Checkout
7. Order tracking
8. Seller Hub
9. Admin Dashboard
10. Vibes

Example:

```markdown
## 📸 Screenshots

| Home | Product Details |
|---|---|
| ![Home](screenshots/home.png) | ![Product](screenshots/product.png) |

| Cart | Checkout |
|---|---|
| ![Cart](screenshots/cart.png) | ![Checkout](screenshots/checkout.png) |
```

## 🔮 Future Improvements

Possible future enhancements include:

* Real-time backend integration
* Real user authentication
* Cloud-based product catalog
* Real payment gateway integration
* Real-time order tracking
* Firebase/Firestore-based synchronization
* Advanced recommendation system
* Production-ready AR implementation
* Push notifications
* Seller analytics
* Advanced admin controls

## ⚠️ Disclaimer

This project is developed for **educational and portfolio purposes** and is inspired by the user experience of Flipkart.

The project is **not affiliated with, sponsored by, or officially connected to Flipkart or Walmart**.

All trademarks, logos, and brand names belong to their respective owners.

## 👨‍💻 Author

**Revanth Reddy**

B.Tech – Computer Science & Engineering (AI & ML)

---

⭐ If you found this project interesting, consider giving the repository a star!
