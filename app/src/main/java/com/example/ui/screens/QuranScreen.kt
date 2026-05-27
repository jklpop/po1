package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel

data class QuranSurah(
    val id: Int,
    val name: String,
    val englishName: String,
    val type: String, // مكة or مدينة
    val verses: List<String>,
    val translations: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(viewModel: MuslimViewModel) {
    val isPlaying by viewModel.isAudioPlaying.collectAsState()
    val speed by viewModel.recitationSpeed.collectAsState()

    // Loaded Surahs Database
    val surahsList = listOf(
        QuranSurah(
            id = 1,
            name = "الفاتحة",
            englishName = "Al-Fatihah",
            type = "مكية",
            verses = listOf(
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                "الرَّحْمَٰنِ الرَّحِيمِ",
                "مَالِكِ يَوْمِ الدِّينِ",
                "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
            ),
            translations = listOf(
                "باسم الله الرحمن الرحيم - أبدأ قراءة القرآن مستعيناً بالله تعالى.",
                "الحمد والثناء لله الكامل ذي النعمة العظيمة، رب الخلق أجمعين.",
                "الرحمن الذي وسعت رحمته كل شيء، الرحيم بعباده المؤمنين.",
                "مالك ومُتصرف في يوم القيامة والجزاء العادل.",
                "نعبدك وحدك سبحانك بالخضوع والمحبة، ونستعين بك في أعمالنا.",
                "أرشدنا وثبتنا على الطريق المستقيم الواضح للحق.",
                "طريق الأنبياء والصالحين الذين أنعمت عليهم، غير المغضوب عليهم ولا الضالين."
            )
        ),
        QuranSurah(
            id = 112,
            name = "الإخلاص",
            englishName = "Al-Ikhlas",
            type = "مكية",
            verses = listOf(
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "قُلْ هُوَ اللَّهُ أَحَدٌ",
                "اللَّهُ الصَّمَدُ",
                "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                "وَلَمْ يَكُنْ لَّهُ كُفُوًا أَحَدٌ"
            ),
            translations = listOf(
                "باسم الله الرحمن الرحيم.",
                "قل يا محمد للناس: الله سبحانه وتعالى منفرد في ألوهيته لا شريك له.",
                "الله المقصود وحده في الحوائج والعبادات واللجوء إليه.",
                "ليس له ولد سبحانه ولا والد ولا صاحبة.",
                "وليس له أشباه ولا مماثل له في عظمته أحد."
            )
        ),
        QuranSurah(
            id = 113,
            name = "الفلق",
            englishName = "Al-Falaq",
            type = "مدنية",
            verses = listOf(
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
                "مِن شَرِّ مَا خَلَقَ",
                "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
                "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
                "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدٌ"
            ),
            translations = listOf(
                "باسم الله الرحمن الرحيم.",
                "قل: ألتجئ وأحتمي برب الصبح وأستعين بنوره.",
                "من شر وأذى كافه المخلوقات والشرور.",
                "ومن أذى الظلام الدامس عندما ينتشر.",
                "ومن أذى السحرة الذين ينفثون في العقد لإيقاع الأذى.",
                "ومن شر كل حاقد حاسد يتمنى زوال النعمة."
            )
        ),
        QuranSurah(
            id = 114,
            name = "الناس",
            englishName = "Al-Nas",
            type = "مكية",
            verses = listOf(
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                "مَلِكِ النَّاسِ",
                "إِلَٰهِ النَّاسِ",
                "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
                "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
                "مِنَ الْجِنَّةِ وَالنَّاسِ"
            ),
            translations = listOf(
                "باسم الله الرحمن الرحيم.",
                "قل: أحتمي وألتجئ إلى خالق البشر والحافظ لهم.",
                "مالك الناس وحده المتصرف في شؤونهم.",
                "المعبود الحق للناس لا إله غيره.",
                "من أذى الشيطان الموسوس الذي يختفي ويهرب عند ذكر الله.",
                "الشيطان الذي يبث الوساوس والشكوك والشهوات في عقول الخلق.",
                "سواء كان هذا الموسوس من شياطين الجن أو رفقاء السوء من البشر."
            )
        ),
        QuranSurah(
            id = 67,
            name = "الملك",
            englishName = "Al-Mulk",
            type = "مكية",
            verses = listOf(
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
                "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ"
            ),
            translations = listOf(
                "باسم الله الرحمن الرحيم.",
                "كثر خير وبركة الله المالك المتصرف في الأكوان والقدرة المطلقة.",
                "الذي أوجد الموت والحياة ليختبركم في الطاعة والبر وهو الغفور لمن تاب.",
                "الذي رفع السموات السبع متناسقة لا تفاوت فيها ولا عيوب، فانظر للكون هل تجد ثغرات؟"
            )
        )
    )

    var selectedSurahIndex by remember { mutableStateOf(0) }
    val currentSurah = surahsList[selectedSurahIndex]
    var showSurahPicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header (Matches Image 7 scroll selector)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryEmerald)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown selector of chapters
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x33FFFFFF))
                            .clickable { showSurahPicker = true }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "سورة ${currentSurah.name}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "اختر السورة", tint = Color.White)
                    }

                    DropdownMenu(
                        expanded = showSurahPicker,
                        onDismissRequest = { showSurahPicker = false },
                        modifier = Modifier
                            .background(Color.White)
                            .width(200.dp)
                    ) {
                        surahsList.forEachIndexed { index, surah ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(surah.name, color = PrimaryEmerald, fontWeight = FontWeight.Bold)
                                        Text(surah.type, color = Color.Gray, fontSize = 12.sp)
                                    }
                                },
                                onClick = {
                                    selectedSurahIndex = index
                                    showSurahPicker = false
                                }
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "القرآن الكريم",
                        color = AccentGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${currentSurah.englishName} • الجزء ١",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            // Beautiful Calligraphy Card Frame (Matching Image 7 decoration)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, PrimaryEmerald.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFFFCFDFB), Color(0xFFF1F8E9))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "سُورَةُ ${currentSurah.name}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryEmerald,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "آياتها ${currentSurah.verses.size} • نزلت بـ ${currentSurah.type}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Scrollable List of Verses
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                itemsIndexed(currentSurah.verses) { index, verse ->
                    VerseItem(
                        index = index + 1,
                        arabicText = verse,
                        meaningText = currentSurah.translations.getOrNull(index) ?: ""
                    )
                }
            }
        }

        // Quran Media Controller Footer (Interactive - Image 7 Bottom controller)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 56.dp) // Bottom Navigation Safe clearance
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0))
                .padding(vertical = 14.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Multiplier selector speed
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F8E9))
                        .clickable {
                            val nextSpeed = when (speed) {
                                1.0f -> 1.25f
                                1.25f -> 1.5f
                                1.5f -> 2.0f
                                else -> 1.0f
                            }
                            viewModel.setRecitationSpeed(nextSpeed)
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${speed}x",
                        color = PrimaryEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Previous Button
                IconButton(
                    onClick = {
                        if (selectedSurahIndex > 0) {
                            selectedSurahIndex--
                        }
                    }
                ) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "السابق", tint = PrimaryEmerald)
                }

                // Active Play Button
                IconButton(
                    onClick = { viewModel.toggleAudioPlaying() },
                    modifier = Modifier
                        .background(PrimaryEmerald, CircleShape)
                        .size(50.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "تشغيل التلاوة",
                        tint = AccentGold,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Next Button
                IconButton(
                    onClick = {
                        if (selectedSurahIndex < surahsList.size - 1) {
                            selectedSurahIndex++
                        }
                    }
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "التالي", tint = PrimaryEmerald)
                }

                // Repeat toggle
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Repeat, contentDescription = "تكرار الآية", tint = SecondaryMint)
                }
            }
        }
    }
}

@Composable
fun VerseItem(index: Int, arabicText: String, meaningText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Verse Header Row with circular symbol identifier
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Actions (Share/Bookmark)
                Row {
                    IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Share, contentDescription = "مشاركة", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "حفظ", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    }
                }

                // Circular Ayah Index Ornament
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color(0xFFE8F5E9), CircleShape)
                        .border(1.dp, PrimaryEmerald.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = index.toString(),
                        color = PrimaryEmerald,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Uthmani Arab Scripture
            Text(
                text = arabicText,
                color = Color(0xFF1B5E20),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                lineHeight = 40.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sub-Translation/Tafsir Text
            Text(
                text = meaningText,
                color = Color.DarkGray,
                fontSize = 13.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Right,
                lineHeight = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9FBF9), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            )
        }
    }
}
