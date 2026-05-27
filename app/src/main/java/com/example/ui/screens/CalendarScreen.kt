package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: MuslimViewModel) {
    // Interactive calendar date selection state
    var selectedDayOffset by remember { mutableStateOf(0) }

    // Calendar math parameters
    val calendarInstance = Calendar.getInstance()
    calendarInstance.add(Calendar.DAY_OF_YEAR, selectedDayOffset)

    val currentGregorianStr = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("ar")).format(calendarInstance.time)
    val currentHijriStr = viewModel.getHijriDateArabic(selectedDayOffset)

    // Current displayed month in slider
    val monthYearGregorianStr = SimpleDateFormat("MMMM yyyy", Locale("ar")).format(calendarInstance.time)

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
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "التقويم الهجري ومحول التواريخ",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Month Slider Bar (Matches Image 9 Slider)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Month picker Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { selectedDayOffset -= 30 }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "الشهر السابق", tint = PrimaryEmerald)
                        }

                        Text(
                            text = monthYearGregorianStr,
                            color = PrimaryEmerald,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )

                        IconButton(onClick = { selectedDayOffset += 30 }) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "الشهر التالي", tint = PrimaryEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Parallel representation cards (Image 9)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PrimaryEmerald.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                            .background(Color(0xFFFCFDFB))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("التقويم الهجري", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(currentHijriStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryEmerald)
                        }

                        Icon(Icons.Default.CompareArrows, contentDescription = "تحويل", tint = AccentGold)

                        // Converted Equivalent
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("التقويم الميلادي", fontSize = 11.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(currentGregorianStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SecondaryMint)
                        }
                    }
                }
            }

            // Quick Converters Slider buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { selectedDayOffset -= 1 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("اليوم السابق", color = Color.White)
                }

                Button(
                    onClick = { selectedDayOffset = 0 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("اليوم", color = Color.Black)
                }

                Button(
                    onClick = { selectedDayOffset += 1 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("اليوم التالي", color = Color.White)
                }
            }

            // Grid representing monthly days simulation
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "محاكاة أيام الأسبوع السريعة",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryEmerald
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (i in -3..3) {
                            val targetDay = Calendar.getInstance()
                            targetDay.add(Calendar.DAY_OF_YEAR, selectedDayOffset + i)
                            val dNum = targetDay.get(Calendar.DAY_OF_MONTH)
                            val isSelected = i == 0

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) PrimaryEmerald else Color.Transparent
                                    )
                                    .clickable { selectedDayOffset += i }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = dNum.toString(),
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = SimpleDateFormat("E", Locale("ar")).format(targetDay.time),
                                    color = if (isSelected) AccentGold else Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Prayer times for selected day (Matching Image 9 bottom rows)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "مواعيد الصلوات لليوم المحدد",
                        color = PrimaryEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                item {
                    PrayerScheduleRowMini(name = "الفجر", time = "04:15 ص", highlight = true)
                }
                item {
                    PrayerScheduleRowMini(name = "الشروق", time = "05:34 ص", highlight = false)
                }
                item {
                    PrayerScheduleRowMini(name = "الظهر", time = "12:14 م", highlight = false)
                }
                item {
                    PrayerScheduleRowMini(name = "العصر", time = "03:39 م", highlight = false)
                }
                item {
                    PrayerScheduleRowMini(name = "المغرب", time = "06:52 م", highlight = false)
                }
                item {
                    PrayerScheduleRowMini(name = "العشاء", time = "08:18 م", highlight = false)
                }
            }
        }
    }
}

@Composable
fun PrayerScheduleRowMini(name: String, time: String, highlight: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) Color(0xFFE8F5E9) else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (highlight) PrimaryEmerald.copy(alpha = 0.2f) else Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(time, color = PrimaryEmerald, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(name, color = Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
