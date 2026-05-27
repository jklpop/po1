package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreen(viewModel: MuslimViewModel) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
    val nextPrayerTimeLeft by viewModel.nextPrayerTimeLeft.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val prayerTracking by viewModel.currentPrayerTracking.collectAsState()

    var showCityMenu by remember { mutableStateOf(false) }
    var showFullAzanView by remember { mutableStateOf(false) }

    if (showFullAzanView) {
        FullAzanOverlayView(
            viewModel = viewModel,
            onClose = { showFullAzanView = false }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Background Mosque Canvas Drawing
            DomeBackgroundCanvas(modifier = Modifier.fillMaxSize())

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showFullAzanView = true },
                            modifier = Modifier
                                .background(Color(0x33FFFFFF), CircleShape)
                                .size(42.dp)
                        ) {
                            Icon(Icons.Default.Mosque, contentDescription = "عرض الأذان", tint = AccentGold)
                        }

                        // City Selector
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0x221B5E20))
                                    .clickable { showCityMenu = true }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = "الموقع", tint = PrimaryEmerald, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedCity,
                                    color = PrimaryEmerald,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "قائمة المدن", tint = PrimaryEmerald)
                            }

                            DropdownMenu(
                                expanded = showCityMenu,
                                onDismissRequest = { showCityMenu = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                viewModel.cities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city, color = PrimaryEmerald, fontWeight = FontWeight.Bold) },
                                        onClick = {
                                            viewModel.selectCity(city)
                                            showCityMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Countdown Timer Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x99102810)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "الصلاة القادمة: $nextPrayerName",
                                color = AccentGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = nextPrayerTimeLeft,
                                color = Color.White,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "متبقي على موعد الأذان",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Weekday Logging Progress Checklist
                item {
                    WeeklyAttendanceRow(viewModel = viewModel)
                }

                // List of Prayer Times
                item {
                    Text(
                        text = "مواقيت الأذان اليوم",
                        color = PrimaryEmerald,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Right
                    )
                }

                items(prayerTimes) { prayer ->
                    val isChecked = when (prayer.name) {
                        "Fajr" -> prayerTracking?.fajr ?: false
                        "Dhuhr" -> prayerTracking?.dhuhr ?: false
                        "Asr" -> prayerTracking?.asr ?: false
                        "Maghrib" -> prayerTracking?.maghrib ?: false
                        "Isha" -> prayerTracking?.isha ?: false
                        else -> false
                    }

                    PrayerTimeItem(
                        prayer = prayer,
                        isChecked = isChecked,
                        onCheckChanged = {
                            val key = when (prayer.name) {
                                "Fajr" -> "fajr"
                                "Dhuhr" -> "dhuhr"
                                "Asr" -> "asr"
                                "Maghrib" -> "maghrib"
                                "Isha" -> "isha"
                                else -> ""
                            }
                            if (key.isNotEmpty()) viewModel.togglePrayerLogger(key)
                        }
                    )
                }

                // Double Quick Access Tabs (Qibla, Quran)
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo("quran") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = AccentGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("القرآن الكريم", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.navigateTo("qibla") },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryMint),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            Icon(Icons.Default.Explore, contentDescription = null, tint = AccentGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("اتجاه القبلة", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerTimeItem(
    prayer: com.example.ui.viewmodel.MuslimViewModel.PrayerTime,
    isChecked: Boolean,
    onCheckChanged: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logging checkmark / button (Only for actual obligatories, exclude Sunrise)
            if (prayer.name != "Sunrise") {
                IconButton(
                    onClick = onCheckChanged,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (isChecked) PrimaryEmerald else Color(0xFFF1F8E9),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isChecked) Icons.Default.Check else Icons.Outlined.Check,
                        contentDescription = "تمت الصلاة",
                        tint = if (isChecked) AccentGold else PrimaryEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(36.dp))
            }

            // Notification / Bell Alarm slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isMuted = !isMuted }) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "كتم الأذان",
                        tint = if (isMuted) Color.LightGray else SecondaryMint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Time String
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = prayer.time,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = if (prayer.isAm) "ص" else "م",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Logo representation
                val icon = when (prayer.name) {
                    "Fajr" -> Icons.Default.Brightness3
                    "Sunrise" -> Icons.Default.WbTwilight
                    "Dhuhr" -> Icons.Default.WbSunny
                    "Asr" -> Icons.Default.FilterDrama
                    "Maghrib" -> Icons.Default.Brightness4
                    else -> Icons.Default.NightsStay
                }

                Icon(
                    imageVector = icon,
                    contentDescription = prayer.arabicName,
                    tint = AccentGold,
                    modifier = Modifier
                        .background(Color(0xFFE8F5E9), CircleShape)
                        .padding(8.dp)
                        .size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Prayer Name In Arabic
                Text(
                    text = prayer.arabicName,
                    color = PrimaryEmerald,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Composable
fun WeeklyAttendanceRow(viewModel: MuslimViewModel) {
    val weekDays = listOf("الجمعة", "السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس")
    val todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) // SUNDAY=1, SATURDAY=7
    // Map Java Calendar day indexes to our Arabic days: Fri=6, Sat=7, Sun=1, Mon=2, Tue=3, Wed=4, Thu=5

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x1F1B5E20)),
        border = BorderStroke(1.dp, PrimaryEmerald.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "حالة تسجيل الصلوات الأسبوعية",
                color = PrimaryEmerald,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEachIndexed { index, day ->
                    val isToday = when (day) {
                        "الجمعة" -> todayIndex == Calendar.FRIDAY
                        "السبت" -> todayIndex == Calendar.SATURDAY
                        "الأحد" -> todayIndex == Calendar.SUNDAY
                        "الإثنين" -> todayIndex == Calendar.MONDAY
                        "الثلاثاء" -> todayIndex == Calendar.TUESDAY
                        "الأربعاء" -> todayIndex == Calendar.WEDNESDAY
                        "الخميس" -> todayIndex == Calendar.THURSDAY
                        else -> false
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = day.take(3), // abbreviation
                            fontSize = 11.sp,
                            color = if (isToday) AccentGold else PrimaryEmerald,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Simulated status checkboxes (matching Image 1 checklist)
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isToday) PrimaryEmerald else Color.White
                                )
                                .border(1.dp, PrimaryEmerald, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (index == 0) { // Green Check mark
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(14.dp))
                            } else if (index == 1) { // Red Cross mark
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red, modifier = Modifier.size(14.dp))
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(
                                            if (isToday) AccentGold else Color.LightGray,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DomeBackgroundCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Background dark sky gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF031403), Color(0xFF0C240C), Color(0xFFE8F5E9))
            ),
            size = size
        )

        // Draw dynamic stars
        val random = java.util.Random(42)
        for (i in 0..40) {
            val starX = random.nextFloat() * width
            val starY = random.nextFloat() * (height * 0.45f)
            val starSize = random.nextFloat() * 2f + 1f
            val opacity = random.nextFloat() * 0.7f + 0.3f
            drawCircle(
                color = Color.White.copy(alpha = opacity),
                radius = starSize,
                center = Offset(starX, starY)
            )
        }

        // Beautiful luxury mosque silhouette paths
        val silhouetteColor = Color(0xFF102810).copy(alpha = 0.5f)

        // Dome 1 (Left Dome)
        val domeLeft = Path().apply {
            moveTo(0f, height * 0.65f)
            cubicTo(width * 0.05f, height * 0.55f, width * 0.2f, height * 0.55f, width * 0.25f, height * 0.65f)
            lineTo(width * 0.25f, height)
            lineTo(0f, height)
            close()
        }
        drawPath(domeLeft, silhouetteColor)

        // Small minaret needle left
        drawRect(
            color = silhouetteColor,
            topLeft = Offset(width * 0.12f, height * 0.52f),
            size = androidx.compose.ui.geometry.Size(width * 0.015f, height * 0.13f)
        )

        // Central Mosque Dome
        val mainDome = Path().apply {
            moveTo(width * 0.2f, height * 0.75f)
            cubicTo(width * 0.3f, height * 0.48f, width * 0.7f, height * 0.48f, width * 0.8f, height * 0.75f)
            lineTo(width * 0.8f, height)
            lineTo(width * 0.2f, height)
            close()
        }
        drawPath(mainDome, Color(0xFF133613).copy(alpha = 0.6f))

        // Center needle & crescent star decoration
        drawCircle(
            color = AccentGold.copy(alpha = 0.8f),
            radius = 6f,
            center = Offset(width * 0.5f, height * 0.46f)
        )
    }
}

@Composable
fun FullAzanOverlayView(
    viewModel: MuslimViewModel,
    onClose: () -> Unit
) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val tracking by viewModel.currentPrayerTracking.collectAsState()
    val hijriDate by viewModel.hijriDate.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    var isMuted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Starry night with elegant luxury design of Sheikh Zayed Grand Mosque inside full overlay
        DomeBackgroundCanvas(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sound Button
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .background(Color(0x33FFFFFF), CircleShape)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "الصوت",
                        tint = Color.White
                    )
                }

                // City Name
                Text(
                    text = selectedCity,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Close Button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .background(Color(0x33FFFFFF), CircleShape)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إغلاق",
                        tint = Color.White
                    )
                }
            }

            // Time & Date Display (Matches Image 1 precisely)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "الظهر",
                    color = AccentGold,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "12:27",
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "م",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Hijri Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x44FFFFFF))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Mosque, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = hijriDate, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Footer Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Circular weekdays progress status (Image 1 replica)
                WeeklyAttendanceRow(viewModel = viewModel)

                Spacer(modifier = Modifier.height(12.dp))

                // Register check in button (Image 1: "اضغط للتسجيل")
                Button(
                    onClick = {
                        viewModel.togglePrayerLogger("dhuhr")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "اضغط للتسجيل",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Qibla Compass & Quran side action buttons (Image 1 bottom widgets)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            onClose()
                            viewModel.navigateTo("qibla")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Explore, contentDescription = null, tint = AccentGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("القبلة", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onClose()
                            viewModel.navigateTo("quran")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = AccentGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("القرآن", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
