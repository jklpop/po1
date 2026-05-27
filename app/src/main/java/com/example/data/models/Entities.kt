package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_tracking")
data class PrayerTracking(
    @PrimaryKey val date: String, // String: "YYYY-MM-DD"
    val fajr: Boolean = false,
    val dhuhr: Boolean = false,
    val asr: Boolean = false,
    val maghrib: Boolean = false,
    val isha: Boolean = false
)

@Entity(tableName = "tasbih_state")
data class TasbihState(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val translation: String,
    val currentCount: Int = 0,
    val targetLimit: Int = 33,
    val rounds: Int = 0
)

@Entity(tableName = "ummah_post")
data class UmmahPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val avatarColor: Int, // Hex integer color for avatar placeholder
    val authorSubtitle: String,
    val content: String,
    val imageResType: Int = 0, // Gradient / Islamic artwork index
    val timeText: String = "الآن",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLikedByUser: Boolean = false
)
