package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainHostScreen(viewModel: MuslimViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val showUpdateDialog by viewModel.showUpdateSuccessDialog.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Elegant Emerald Custom Bottom Bar
            NavigationBar(
                containerColor = PrimaryEmerald,
                contentColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .height(64.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                // Tab 1: Home
                NavigationBarItem(
                    selected = currentScreen == "home",
                    onClick = { viewModel.navigateTo("home") },
                    icon = { Icon(if (currentScreen == "home") Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "الرئيسية") },
                    label = { Text("الرئيسية", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentGold,
                        selectedTextColor = AccentGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = SecondaryMint
                    )
                )

                // Tab 2: Prayer
                NavigationBarItem(
                    selected = currentScreen == "prayer",
                    onClick = { viewModel.navigateTo("prayer") },
                    icon = { Icon(Icons.Filled.Mosque, contentDescription = "الصلاة") },
                    label = { Text("الصلاة", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentGold,
                        selectedTextColor = AccentGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = SecondaryMint
                    )
                )

                // Tab 3: Ummah
                NavigationBarItem(
                    selected = currentScreen == "ummah",
                    onClick = { viewModel.navigateTo("ummah") },
                    icon = { Icon(if (currentScreen == "ummah") Icons.Filled.People else Icons.Outlined.People, contentDescription = "الأمة") },
                    label = { Text("الأمة", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentGold,
                        selectedTextColor = AccentGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = SecondaryMint
                    )
                )

                // Tab 4: Tasbih
                NavigationBarItem(
                    selected = currentScreen == "tasbih",
                    onClick = { viewModel.navigateTo("tasbih") },
                    icon = { Icon(Icons.Filled.Fingerprint, contentDescription = "التسبيح") },
                    label = { Text("التسبيح", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentGold,
                        selectedTextColor = AccentGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = SecondaryMint
                    )
                )

                // Tab 5: Azkar
                NavigationBarItem(
                    selected = currentScreen == "azkar",
                    onClick = { viewModel.navigateTo("azkar") },
                    icon = { Icon(if (currentScreen == "azkar") Icons.Filled.Book else Icons.Outlined.Book, contentDescription = "الأذكار") },
                    label = { Text("الأذكار", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentGold,
                        selectedTextColor = AccentGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f),
                        indicatorColor = SecondaryMint
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()) // Custom offset to fit the bar beautifully
        ) {
            // Elegant Animated transitions between layouts
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    slideInVertically { height -> height / 2 } + fadeIn() togetherWith
                            slideOutVertically { height -> -height / 2 } + fadeOut()
                }
            ) { target ->
                when (target) {
                    "home" -> HomeDashboardScreen(viewModel = viewModel)
                    "prayer" -> PrayerScreen(viewModel = viewModel)
                    "ummah" -> UmmahScreen(viewModel = viewModel)
                    "quran" -> QuranScreen(viewModel = viewModel)
                    "tasbih" -> TasbihScreen(viewModel = viewModel)
                    "azkar" -> AzkarScreen(viewModel = viewModel)
                    "qibla" -> QiblaScreen(viewModel = viewModel)
                    "calendar" -> CalendarScreen(viewModel = viewModel)
                    else -> HomeDashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    // System-wide elegant App Upgrade Custom Modal Dialog
    if (showUpdateDialog) {
        AppUpdateSuccessDialog(
            versionName = com.example.BuildConfig.VERSION_NAME,
            onDismiss = { viewModel.dismissUpdateDialog() }
        )
    }
}

@Composable
fun AppUpdateSuccessDialog(
    versionName: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "تقبل الله طاعاتكم! ابدأ الاستكشاف 🚀",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎉", fontSize = 32.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "تم الترقية والتحديث بنجاح!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryEmerald,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = "الإصدار المفعّل v$versionName",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "أهلاً بك في النسخة المطورة من تطبيق \"أنا مسلم\"! لقد قمنا ببعض التحسينات والإضافات لخدمة عبادتك اليومية بشكل طاهر ومستقر:",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Right,
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // Detailed changelog bulletins
                ChangelogLine(icon = "🕋", title = "معايرة بوصلة القبلة التفاعلية", desc = "تم تحسين وزيادة دقة محدد القبلة التفاعلية لتناسب تغيرات زاوية انحراف الهاتف بدقة.")
                ChangelogLine(icon = "📅", title = "التقويم ومواقيت الصلوات", desc = "عرض ومزامنة التقويم الهجري والميلادي مع التحويل السريع لتواريخ الفرائض بيسر وسهولة.")
                ChangelogLine(icon = "🗄️", title = "العبادة وقاعدة البيانات (Room)", desc = "مزامنة كاملة لحصيفة الصلوات وحفظ منشورات مجتمع الأمة على الهاتف لتعمل بدون شبكة.")
                ChangelogLine(icon = "💬", title = "منشورات الأمة الإسلامية", desc = "إمكانية نشر التدوينات الدعوية والحكم الإسلامية وتفاعل مجتمع المسلمين بالخير.")
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
fun ChangelogLine(icon: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryEmerald,
                textAlign = androidx.compose.ui.text.style.TextAlign.Right
            )
            Text(
                text = desc,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Right,
                lineHeight = 14.sp
            )
        }
        Text(icon, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
    }
}
