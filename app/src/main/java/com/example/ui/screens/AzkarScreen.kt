package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel

data class AzkarCategory(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    var currentProgress: String, // e.g. "4/4"
    val items: List<AzkarItemModel>
)

data class AzkarItemModel(
    val text: String,
    val source: String,
    val recommendation: String,
    val repeatLimit: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AzkarScreen(viewModel: MuslimViewModel) {
    // Master Lists of Supplications (Azkar)
    val categoriesList = listOf(
        AzkarCategory(
            title = "أذكار الاستيقاظ من النوم",
            icon = Icons.Default.WbSunny,
            currentProgress = "4/4",
            items = listOf(
                AzkarItemModel(
                    text = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ.",
                    source = "رواه البخاري",
                    recommendation = "يُقال مرة واحدة عند الاستيقاظ",
                    repeatLimit = 1
                ),
                AzkarItemModel(
                    text = "الْحَمْدُ لِلَّهِ الَّذِي عَافَانِي فِي جَسَدِي، وَرَدَّ عَلَيَّ رُوحِي، وَأَذِنَ لِي بِذِكْرِهِ.",
                    source = "رواه الترمذي",
                    recommendation = "يُقال مرة واحدة",
                    repeatLimit = 1
                )
            )
        ),
        AzkarCategory(
            title = "أذكار الصباح",
            icon = Icons.Default.Brightness5,
            currentProgress = "15/16",
            items = listOf(
                AzkarItemModel(
                    text = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
                    source = "رواه مسلم",
                    recommendation = "يُقال مرة واحدة في الصباح",
                    repeatLimit = 1
                ),
                AzkarItemModel(
                    text = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
                    source = "رواه الحاكم",
                    recommendation = "يُقال مرة واحدة في الصباح والموجب للخير",
                    repeatLimit = 1
                ),
                AzkarItemModel(
                    text = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ: عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ.",
                    source = "رواه مسلم",
                    recommendation = "تُكرر ثلاث مرات",
                    repeatLimit = 3
                ),
                AzkarItemModel(
                    text = "اللَّهُمَّ عافِني في بَدَني، اللَّهُمَّ عافِني في سَمْعي، اللَّهُمَّ عافِني في بَصَري، لا إلهَ إلَّا أنتَ.",
                    source = "رواه أبو داود",
                    recommendation = "تُكرر ثلاث مرات لتمام العافية",
                    repeatLimit = 3
                )
            )
        ),
        AzkarCategory(
            title = "أذكار المساء",
            icon = Icons.Default.NightsStay,
            currentProgress = "0/17",
            items = listOf(
                AzkarItemModel(
                    text = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ.",
                    source = "رواه مسلم",
                    recommendation = "يُقال مرة واحدة في المساء",
                    repeatLimit = 1
                ),
                AzkarItemModel(
                    text = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
                    source = "رواه مسلم",
                    recommendation = "تُكرر ثلاث مرات لحفظ المسلم من الهوام والآفات",
                    repeatLimit = 3
                )
            )
        ),
        AzkarCategory(
            title = "أذكار قبل النوم",
            icon = Icons.Default.Bedtime,
            currentProgress = "0/12",
            items = listOf(
                AzkarItemModel(
                    text = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
                    source = "رواه البخاري ومسلم",
                    recommendation = "يُقال مرة واحدة على فراش النوم",
                    repeatLimit = 1
                ),
                AzkarItemModel(
                    text = "اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ.",
                    source = "رواه أبو داود",
                    recommendation = "تُكرر ثلاث مرات",
                    repeatLimit = 3
                )
            )
        ),
        AzkarCategory(
            title = "أذكار بعد الصلاة المفروضة",
            icon = Icons.Default.Mosque,
            currentProgress = "7/7",
            items = listOf(
                AzkarItemModel(
                    text = "أَسْتَغْفِرُ اللَّهَ، أَسْتغْفِرُ اللَّهَ، أَسْتغْفِرُ اللَّهَ.. اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ ذَا الْجَلَالِ وَالْإِكْرَامِ.",
                    source = "رواه مسلم",
                    recommendation = "تُقال دبر كل صلاة مكتوبة",
                    repeatLimit = 1
                ),
                AzkarItemModel(
                    text = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، اللَّهُمَّ لَا مَانِعَ لِمَا أَعْطَيْتَ وَلَا مُعْطِيَ لِمَا مَنَعْتَ.",
                    source = "رواه البخاري ومسلم",
                    recommendation = "يُقال بعد السلام مباشرة",
                    repeatLimit = 1
                )
            )
        )
    )

    // State to toggle Master list or Detail Reading view
    var selectedCategory by remember { mutableStateOf<AzkarCategory?>(null) }

    AnimatedContent(
        targetState = selectedCategory,
        transitionSpec = {
            if (targetState != null) {
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> width } + fadeOut()
            } else {
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            }
        }
    ) { category ->
        if (category == null) {
            // Master Categories Page
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header (Matches Image 6)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryEmerald)
                            .statusBarsPadding()
                            .padding(vertical = 14.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "حصن المسلم والأذكار اليومية",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Top Banner Promo (Matches Image 6 book decoration banner)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        border = BorderStroke(1.dp, PrimaryEmerald.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 12.dp)
                            ) {
                                Text(
                                    text = "الأذكار المفضلة اليومية",
                                    color = PrimaryEmerald,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "تم اقتباس الذكر والتحصين من القرآن الكريم والسنة النبوية لتنقية الروح والحفظ.",
                                    color = Color.Gray,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // Book Symbol Icon
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(PrimaryEmerald, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = AccentGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    // Scrollable Categories Grid / List
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        item {
                            Text(
                                text = "قائمة الأذكار الأساسية",
                                color = PrimaryEmerald,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }

                        itemsIndexed(categoriesList) { _, cat ->
                            CategoryCardRow(
                                category = cat,
                                onClick = { selectedCategory = cat }
                            )
                        }
                    }
                }
            }
        } else {
            // Detailed reading page for selected Azkar category
            AzkarDetailReadingScreen(
                category = category,
                onBack = { selectedCategory = null }
            )
        }
    }
}

@Composable
fun CategoryCardRow(category: AzkarCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left progress badge
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = category.currentProgress,
                    color = PrimaryEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            // Right Info Column
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = category.title,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Right
                    )
                    Text(
                        text = "محصنات يومية دينية",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Right
                    )
                }

                // Rounded Category Logo
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFE8F5E9), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.title,
                        tint = PrimaryEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AzkarDetailReadingScreen(category: AzkarCategory, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryEmerald)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                }

                Text(
                    text = category.title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(48.dp))
            }

            // Scrollable cards list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
            ) {
                itemsIndexed(category.items) { _, item ->
                    AzkarReadItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun AzkarReadItemCard(item: AzkarItemModel) {
    var countsRemaining by remember { mutableStateOf(item.repeatLimit) }
    val isFinished = countsRemaining <= 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Source citation row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.recommendation,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Text(
                    text = item.source,
                    color = SecondaryMint,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Text of Dhikr
            Text(
                text = item.text,
                color = if (isFinished) Color.LightGray else Color(0xFF1B5E20),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                lineHeight = 32.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Decresing repeat button counter
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isFinished) Color(0xFFE8F5E9) else PrimaryEmerald
                    )
                    .clickable {
                        if (countsRemaining > 0) {
                            countsRemaining--
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isFinished) {
                        Icon(Icons.Default.DoneOutline, contentDescription = "مكتمل", tint = PrimaryEmerald)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تم الاكتمال ومضاعفة الأجر والمثوبة", color = PrimaryEmerald, fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = "اضغط لتسجيل قراءة التكرار: تتبقى ($countsRemaining) مرات",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
