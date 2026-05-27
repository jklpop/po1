package com.example.data.local

import androidx.room.*
import com.example.data.models.PrayerTracking
import com.example.data.models.TasbihState
import com.example.data.models.UmmahPost
import kotlinx.coroutines.flow.Flow

@Dao
interface MuslimDao {

    // --- Prayer Tracking ---
    @Query("SELECT * FROM prayer_tracking WHERE date = :date LIMIT 1")
    fun getPrayerTracking(date: String): Flow<PrayerTracking?>

    @Query("SELECT * FROM prayer_tracking")
    fun getAllPrayerTracking(): Flow<List<PrayerTracking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTracking(tracking: PrayerTracking)


    // --- Tasbih ---
    @Query("SELECT * FROM tasbih_state ORDER BY id ASC")
    fun getTasbihStates(): Flow<List<TasbihState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasbihState(state: TasbihState)

    @Update
    suspend fun updateTasbihState(state: TasbihState)

    @Query("UPDATE tasbih_state SET currentCount = :count, rounds = :rounds WHERE id = :id")
    suspend fun updateTasbihProgress(id: Int, count: Int, rounds: Int)

    @Query("DELETE FROM tasbih_state WHERE id = :id")
    suspend fun deleteTasbihState(id: Int)


    // --- Ummah Community Feed ---
    @Query("SELECT * FROM ummah_post ORDER BY id DESC")
    fun getUmmahPosts(): Flow<List<UmmahPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUmmahPost(post: UmmahPost)

    @Query("UPDATE ummah_post SET isLikedByUser = :isLiked, likesCount = :likesCount WHERE id = :id")
    suspend fun updatePostLike(id: Int, isLiked: Boolean, likesCount: Int)
}
