package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.AddressEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlipkartDao {
  // Cart
  @Query("SELECT * FROM cart_items ORDER BY addedTimestamp DESC")
  fun getCartItems(): Flow<List<CartItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCartItem(item: CartItemEntity)

  @Update
  suspend fun updateCartItem(item: CartItemEntity)

  @Query("DELETE FROM cart_items WHERE id = :id")
  suspend fun deleteCartItem(id: String)

  @Query("DELETE FROM cart_items")
  suspend fun clearCart()

  // Wishlist
  @Query("SELECT * FROM wishlist_items ORDER BY addedTimestamp DESC")
  fun getWishlistItems(): Flow<List<WishlistItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWishlistItem(item: WishlistItemEntity)

  @Query("DELETE FROM wishlist_items WHERE productId = :productId")
  suspend fun deleteWishlistItem(productId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
  fun isWishlisted(productId: String): Flow<Boolean>

  // Addresses
  @Query("SELECT * FROM saved_addresses ORDER BY isDefault DESC")
  fun getAddresses(): Flow<List<AddressEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAddress(address: AddressEntity)

  @Query("DELETE FROM saved_addresses WHERE id = :id")
  suspend fun deleteAddress(id: String)

  @Query("UPDATE saved_addresses SET isDefault = CASE WHEN id = :selectedId THEN 1 ELSE 0 END")
  suspend fun setDefaultAddress(selectedId: String)

  // Orders
  @Query("SELECT * FROM orders ORDER BY orderId DESC")
  fun getOrders(): Flow<List<OrderEntity>>

  @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
  fun getOrderById(orderId: String): Flow<OrderEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrder(order: OrderEntity)

  @Query("UPDATE orders SET status = :status, trackingStep = :step WHERE orderId = :orderId")
  suspend fun updateOrderStatus(orderId: String, status: String, step: Int)

  // User Profile
  @Query("SELECT * FROM user_profile LIMIT 1")
  fun getUserProfile(): Flow<UserProfileEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

  @Query("UPDATE user_profile SET superCoins = :coins WHERE userId = 'user_default'")
  suspend fun updateSuperCoins(coins: Int)

  @Query("UPDATE user_profile SET walletBalance = :balance WHERE userId = 'user_default'")
  suspend fun updateWallet(balance: Int)

  @Query("UPDATE user_profile SET selectedLanguage = :lang WHERE userId = 'user_default'")
  suspend fun updateLanguage(lang: String)
}
