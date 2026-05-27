package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel

data class DashboardToolItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val screenKey: String,
    val iconBgColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDashboardScreen(viewModel: MuslimViewModel) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val gregorianDate by viewModel.gregorianDate.collectAsState()
    val hijriDate by viewModel.hijriDate.collectAsState()
    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
    val nextPrayerTimeLeft by viewModel.nextPrayerTimeLeft.collectAsState()

    var showSystemInfoDialog by remember { mutableStateOf(false) }
    val checkForUpdateState by viewModel.checkForUpdateState.collectAsState()

    // 7 Grid Category Apps
    val toolsList = listOf(
        DashboardToolItem("أوقات الصلاة", "مواقيت الأذن والمنبه", Icons.Default.Mosque, "prayer", Color(0xFFE8F5E9)),
        DashboardToolItem("القرآن الكريم", "محاورة السور بالتجويد", Icons.Default.MenuBook, "quran", Color(0xFFFFF3E0)),
        DashboardToolItem("حصن أذكار", "الأذكار والتحصينات", Icons.Default.Book, "azkar", Color(0xFFE3F2FD)),
        DashboardToolItem("بوصلة القبلة", "البوصلة والزاوية الحركية", Icons.Default.Explore, "qibla", Color(0xFFF3E5F5)),
        DashboardToolItem("مسبحة إلكترونية", "عداد وتدوين التسبيح", Icons.Default.Fingerprint, "tasbih", Color(0xFFF1F8E9)),
        DashboardToolItem("أمة المسلمين", "روابط ومنشورات الإسلام", Icons.Default.People, "ummah", Color(0xFFECEFF1)),
        DashboardToolItem("التحويل الهجري", "التقويم الهجري-الميلادي", Icons.Default.CalendarMonth, "calendar", Color(0xFFFFFDE7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Elegant Welcome Banner Header Card with custom emerald dome backdrop drawing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PrimaryEmerald, Color(0xFF0C240C))
                        )
                    )
            ) {
                // Drawing Silhouette Dome directly
                DomeBackgroundCanvas(modifier = Modifier.matchParentSize())

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Row 1: Profile and Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { 
                                viewModel.resetCheckForUpdateState()
                                showSystemInfoDialog = true 
                            },
                            modifier = Modifier
                                .background(Color(0x22FFFFFF), CircleShape)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "تحديثات وتفضيلات النظام", tint = AccentGold)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "أنا مسلم",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentGold
                            )
                            Text(
                                text = "دليلك الطاهر الحافظ للعبادات",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Row 2: Gregorian and Lunar Date Badge (Image 1 replica)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x221B5E20))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AccentGold, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(selectedCity, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = hijriDate,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = gregorianDate,
                                    color = AccentGold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Row 3: Live Countdown Info banner quick link (Prayer countdown clock)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo("prayer") }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "باقي على أذان $nextPrayerName: $nextPrayerTimeLeft • اضغط للتفاصيل 🕌",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }

            // Quick Access Greeting Alert Quote card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(0.5.dp, PrimaryEmerald.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "آية التذكرة والرحمة 🌸",
                            color = PrimaryEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Grid Layout Apps list (The 7 primary applications)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 120.dp) // Nav bar Safe clearance
            ) {
                items(toolsList) { tool ->
                    ToolGridItemCard(
                        item = tool,
                        onClicked = { viewModel.navigateTo(tool.screenKey) }
                    )
                }
            }
        }
    }

    if (showSystemInfoDialog) {
        SystemInfoAndUpdatesDialog(
            viewModel = viewModel,
            checkForUpdateState = checkForUpdateState,
            onDismiss = { showSystemInfoDialog = false }
        )
    }
}

@Composable
fun SystemInfoAndUpdatesDialog(
    viewModel: MuslimViewModel,
    checkForUpdateState: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = PrimaryEmerald)
            ) {
                Text("إغلاق نافذة النظام 🚪", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "معلومات النظام والتحديثات",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = PrimaryEmerald,
                    textAlign = TextAlign.Right
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Settings, contentDescription = null, tint = PrimaryEmerald, modifier = Modifier.size(20.dp))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Application Info Badge
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "اسم التطبيق: أنا مسلم 🕋",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "رقم الإصدار: ${com.example.BuildConfig.VERSION_NAME} (${com.example.BuildConfig.VERSION_CODE})",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "نوع البناء: التطوير والتحديث المستمر",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFEEEEEE))

                Text(
                    text = "مركز التحديثات والتحقق الذكي:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )

                // Render checking states dynamically
                when (checkForUpdateState) {
                    "IDLE" -> {
                        Button(
                            onClick = { viewModel.checkRemoteUpdates() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("التحقق من وجود تحديثات جديدة 🔄", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    "CHECKING" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryEmerald,
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "جاري التحقق من خوادم التحديث والاتصال بالشبكة...",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    "UP_TO_DATE" -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "التطبيق محدث بالفعل! أنت تستخدم أحدث إصدار v${com.example.BuildConfig.VERSION_NAME}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    textAlign = TextAlign.Right
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🎯", fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { viewModel.resetCheckForUpdateState() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("إعادة الفحص والتحقق ثانية", fontSize = 10.sp, color = PrimaryEmerald)
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFF0F0F0))

                // Simulator Trigger Action Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFF59D))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "قسم التجربة ومحاكاة المطور 🧪",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57F17)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "لمشاهدة كيف سيعمل شعار التحديث التلقائي بشكل فوري على الشاشة عندما تقوم برفع إصدار التطبيق مستقبلاً، انقر على الزر التجريبي أسفله:",
                            fontSize = 10.sp,
                            color = Color.DarkGray,
                            lineHeight = 14.sp,
                            textAlign = TextAlign.Right
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { 
                                viewModel.simulateNewUpgradeAlert()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57F17)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("محاكاة نافذة ظهور التحديث الجديد 🎉", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}

@Composable
fun ToolGridItemCard(
    item: DashboardToolItem,
    onClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(116.dp)
            .clickable { onClicked() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            // Rounded Icon representation in colored circle container
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = null,
                    tint = Color.LightGray.copy(alpha = 0.5f),
                    modifier = Modifier.size(12.dp)
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = PrimaryEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Text Labels In Arabic
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Right
                )
                Text(
                    text = item.subtitle,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}
