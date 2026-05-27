package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel
import kotlin.math.roundToInt

@Composable
fun QiblaScreen(viewModel: MuslimViewModel) {
    val deviceHeading = viewModel.deviceHeading
    val selectedCity by viewModel.selectedCity.collectAsState()

    // Determine Kaaba Azimuth from north based on selected city
    val kaabaAzimuth = when (selectedCity) {
        "دبي" -> 262f
        "القاهرة" -> 136f
        "القدس الشريف" -> 165f
        "المدينة المنورة" -> 180f
        "بغداد" -> 196f
        "الرباط" -> 98f
        else -> 0f // Mecca itself
    }

    // Relative rotation angle for the compass gold pointer
    val relativeAngle = (kaabaAzimuth - deviceHeading + 360f) % 360f

    // Smooth animator for heading shifts
    val animatedAngle by animateFloatAsState(
        targetValue = relativeAngle,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "قراءة القبلة"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    text = "محدد وقبلة الصلاة التفاعلية",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Top Info metrics - (Image 2 Stats replica)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("الزاوية المقاسة", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${deviceHeading.roundToInt()}°",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryEmerald
                        )
                    }
                }

                // Card 2
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("قبلة $selectedCity", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${kaabaAzimuth.roundToInt()}° N",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SecondaryMint
                        )
                    }
                }

                // Card 3
                Card(
                    modifier = Modifier.weight(1.5f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("دقة المستشعر", fontSize = 10.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = AccentGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ممتازة", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryEmerald)
                            }
                        }
                    }
                }
            }

            // Big Direction Indicator text (Image 2)
            Text(
                text = "زاوية انحراف الجهاز نحو الكعبة المشرفة",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "${relativeAngle.roundToInt()}°",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = PrimaryEmerald
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Compass Dial container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // outer glow
                Box(
                    modifier = Modifier
                        .size(290.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFFE8F5E9), Color.Transparent)
                            )
                        )
                )

                // The Compass Physical Surface
                Card(
                    modifier = Modifier
                        .size(260.dp)
                        .border(4.dp, PrimaryEmerald.copy(alpha = 0.4f), CircleShape),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Drawing static directions (N, S, E, W) and Gold arrow needle
                        CompassDialCanvas(
                            relativePointerAngle = animatedAngle,
                            deviceHeading = deviceHeading,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Kaaba Icon Center representation
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PrimaryEmerald),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🕋",
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }

            // Helper notice
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 56.dp), // Nav bar clearance
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "قم بوضع الهاتف بشكل أفقي بعيداً عن المغناطيس أو الأسطح المعدنية الأخرى للحصول على أعلى دقة اتجاه.",
                        color = PrimaryEmerald,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.CompassCalibration, contentDescription = null, tint = AccentGold)
                }
            }
        }
    }
}

@Composable
fun CompassDialCanvas(
    relativePointerAngle: Float,
    deviceHeading: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val radius = width / 2f
        val center = Offset(width / 2f, height / 2f)

        // 1. Draw Ring graduations
        for (degree in 0 until 360 step 15) {
            val angleRad = Math.toRadians((degree - 90).toDouble())
            val startX = (center.x + (radius - 12f) * Math.cos(angleRad)).toFloat()
            val startY = (center.y + (radius - 12f) * Math.sin(angleRad)).toFloat()
            val endX = (center.x + (radius - 4f) * Math.cos(angleRad)).toFloat()
            val endY = (center.y + (radius - 4f) * Math.sin(angleRad)).toFloat()

            drawLine(
                color = if (degree % 90 == 0) PrimaryEmerald else Color.LightGray,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = if (degree % 90 == 0) 4f else 2f
            )
        }

        // 2. Draw Cardinal direction letter text under rotation (Rotating with phone)
        rotate(-deviceHeading, center) {
            val cardinalDirections = listOf("N", "E", "S", "W")
            for (i in cardinalDirections.indices) {
                val dirAngle = i * 90f - 90f
                val dirRad = Math.toRadians(dirAngle.toDouble())
                val letterX = (center.x + (radius - 32f) * Math.cos(dirRad)).toFloat()
                val letterY = (center.y + (radius - 32f) * Math.sin(dirRad)).toFloat()

                // Draw solid pointer dots for major axes
                drawCircle(
                    color = if (i == 0) Color.Red else PrimaryEmerald,
                    radius = 4f,
                    center = Offset(letterX, letterY)
                )
            }
        }

        // 3. Draw Rotating Gold Kaaba Compass Arrow Pointer Needle
        rotate(relativePointerAngle, center) {
            // Gold Pointer arrow shape
            val needlePath = Path().apply {
                moveTo(center.x, center.y - (radius - 50f)) // Tip of needle
                lineTo(center.x + 14f, center.y - 10f)      // Right shoulder
                lineTo(center.x, center.y - 20f)           // Inner dip
                lineTo(center.x - 14f, center.y - 10f)      // Left shoulder
                close()
            }

            drawPath(
                path = needlePath,
                brush = Brush.verticalGradient(
                    colors = listOf(AccentGold, Color(0xFFD4AF37), Color(0xFF996515))
                )
            )

            // Draw North warning needle matching the red theme
            val northNeedlePath = Path().apply {
                moveTo(center.x, center.y + (radius - 50f)) // Bottom tip
                lineTo(center.x + 10f, center.y + 10f)
                lineTo(center.x, center.y + 16f)
                lineTo(center.x - 10f, center.y + 10f)
                close()
            }
            drawPath(
                path = northNeedlePath,
                color = Color.Red.copy(alpha = 0.5f)
            )
        }
    }
}
