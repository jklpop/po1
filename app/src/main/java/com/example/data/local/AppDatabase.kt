package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.models.PrayerTracking
import com.example.data.models.TasbihState
import com.example.data.models.UmmahPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PrayerTracking::class, TasbihState::class, UmmahPost::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun muslimDao(): MuslimDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anamuslim_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            scope.launch(Dispatchers.IO) {
                var database = INSTANCE
                var attempts = 0
                while (database == null && attempts < 20) {
                    kotlinx.coroutines.delay(50)
                    database = INSTANCE
                    attempts++
                }
                database?.let {
                    populateDatabase(it.muslimDao())
                }
            }
        }

        suspend fun populateDatabase(dao: MuslimDao) {
            // --- Prefill Default Dhikr for Tasbih ---
            val defaultDhikrs = listOf(
                TasbihState(text = "سُبْحَانَ اللَّهِ", translation = "سبحان الله وبحمده - مئة مرة غفرت خطاياه ولو كانت مثل زبد البحر", targetLimit = 33),
                TasbihState(text = "الْحَمْدُ للَّهِ", translation = "الحمد لله رب العالمين - تملأ الميزان بالخيرات والبركة", targetLimit = 33),
                TasbihState(text = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ", translation = "أفضل الذكر، حرز للمسلم من الشيطان طوال اليوم وتعدل عتق رقاب", targetLimit = 33),
                TasbihState(text = "اللَّهُ أَكْبَرُ", translation = "الله أكبر كبيراً، والحمد لله كثيراً، وسبحان الله بكرة وأصيلاً", targetLimit = 33),
                TasbihState(text = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", translation = "كنز من كنوز الجنة ودواء من تسعة وتسعين داء أيسرها الهم", targetLimit = 33),
                TasbihState(text = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ", translation = "الاستغفار جالب للرزق والمغفرة ويجعل الله لك من كل هم فرجاً", targetLimit = 100),
                TasbihState(text = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", translation = "من صلى عليه صلاة واحدة صلى الله عليه بها عشراً وحلت له شفاعته", targetLimit = 100)
            )
            for (dhikr in defaultDhikrs) {
                dao.insertTasbihState(dhikr)
            }

            // --- Prefill Default Ummah Community Posts ---
            val defaultPosts = listOf(
                UmmahPost(
                    author = "الشيخ عبد الرحمن",
                    avatarColor = 0xFF4CAF50.toInt(),
                    authorSubtitle = "إمام مسجد المدينة • الآن",
                    content = "السلام عليكم ورحمة الله وبركاته. قال صلى الله عليه وسلم: 'من قرأ حرفًا من كتاب الله فله به حسنة، والحسنة بعشر أمثالها'. شاركونا تلاواتكم اليومية وسوركم المفضلة في هذه الأيام المباركة.",
                    imageResType = 1,
                    likesCount = 1450,
                    commentsCount = 89,
                    isLikedByUser = false
                ),
                UmmahPost(
                    author = "أحمد سليم",
                    avatarColor = 0xFF00bcd4.toInt(),
                    authorSubtitle = "متدرب قرآن • منذ ساعة",
                    content = "الحمد لله الذي بنعمته تتم الصالحات. أكملت اليوم حفظ وتلاوة سورة الكهف كاملة بتدبر. نسأل الله القبول والثبات للجميع 🌸📜",
                    imageResType = 2,
                    likesCount = 380,
                    commentsCount = 24,
                    isLikedByUser = false
                ),
                UmmahPost(
                    author = "البحث عن المعرفة",
                    avatarColor = 0xFFFF9800.toInt(),
                    authorSubtitle = "طالب علم • منذ ٥ ساعات",
                    content = "دعاء طيب لكل مسلم ومسلمة في هذه الساعات المباركة: 'اللهم إنك عفو تحب العفو فاعف عني وعن والدي وعن المسلمين والمسلمات الأحياء منهم والأموات'. لا تنسوا الأذكار والسنن الرواتب.",
                    imageResType = 3,
                    likesCount = 2100,
                    commentsCount = 132,
                    isLikedByUser = true
                )
            )
            for (post in defaultPosts) {
                dao.insertUmmahPost(post)
            }
        }
    }
}
