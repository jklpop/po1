package com.example.data.repository

import com.example.data.local.MuslimDao
import com.example.data.models.PrayerTracking
import com.example.data.models.TasbihState
import com.example.data.models.UmmahPost
import kotlinx.coroutines.flow.Flow

class MuslimRepository(private val dao: MuslimDao) {

    // --- Prayer Trackings ---
    fun getPrayerTracking(date: String): Flow<PrayerTracking?> {
        return dao.getPrayerTracking(date)
    }

    val allPrayerTracking: Flow<List<PrayerTracking>> = dao.getAllPrayerTracking()

    suspend fun insertPrayerTracking(tracking: PrayerTracking) {
        dao.insertPrayerTracking(tracking)
    }

    // --- Tasbih ---
    val tasbihStates: Flow<List<TasbihState>> = dao.getTasbihStates()

    suspend fun insertTasbihState(state: TasbihState) {
        dao.insertTasbihState(state)
    }

    suspend fun updateTasbihState(state: TasbihState) {
        dao.updateTasbihState(state)
    }

    suspend fun updateTasbihProgress(id: Int, count: Int, rounds: Int) {
        dao.updateTasbihProgress(id, count, rounds)
    }

    suspend fun deleteTasbihState(id: Int) {
        dao.deleteTasbihState(id)
    }

    // --- Ummah Posts ---
    val ummahPosts: Flow<List<UmmahPost>> = dao.getUmmahPosts()

    suspend fun insertUmmahPost(post: UmmahPost) {
        dao.insertUmmahPost(post)
    }

    suspend fun updatePostLike(id: Int, isLiked: Boolean, likesCount: Int) {
        dao.updatePostLike(id, isLiked, likesCount)
    }
}
