package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(viewModel: MuslimViewModel) {
    val states by viewModel.tasbihStates.collectAsState()
    val activeIndex by viewModel.activeTasbihIndex.collectAsState()
    val activeState = states.getOrNull(activeIndex)

    var showCreateDialog by remember { mutableStateOf(false) }

    // Jewelry Gemstone Palette Themes!
    val gemThemes = listOf(
        Color(0xFF00C853), // Emerald Green
        Color(0xFFFFB300), // Amber Gold
        Color(0xFF00B0FF), // Sapphire Blue
        Color(0xFFE91E63), // Ruby Red
        Color(0xFF008080)  // Turquoise
    )
    var selectedGemColor by remember { mutableStateOf(gemThemes[0]) }

    // Dynamic Bead position offset animator
    val animatedBeadOffset = remember { Animatable(0f) }
    var triggerCountState by remember { mutableStateOf(0) }

    // Trigger bead slide pulse on count change
    LaunchedEffect(activeState?.currentCount) {
        val count = activeState?.currentCount ?: 0
        triggerCountState = count
        animatedBeadOffset.snapTo(0f)
        animatedBeadOffset.animateTo(
            targetValue = 24f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen Header Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryEmerald)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                IconButton(
                    onClick = { viewModel.resetActiveTasbih() },
                    modifier = Modifier.background(Color(0x22FFFFFF), CircleShape)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "تصفير العداد", tint = Color.White)
                }

                Text(
                    text = "مسبحة الذكر الإلكترونية",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Plus Add Dhikr Button
                IconButton(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier.background(Color(0x22FFFFFF), CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "إضافة ذكر", tint = AccentGold)
                }
            }

            // Category Slider of Predefined Dhikr (Image 5 Select scroll)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(states) { index, dhikr ->
                    val isActive = index == activeIndex
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isActive) PrimaryEmerald else Color(0xFFE8F5E9)
                            )
                            .clickable { viewModel.selectTasbihIndex(index) }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (dhikr.text.length > 18) dhikr.text.take(16) + "..." else dhikr.text,
                            color = if (isActive) Color.White else PrimaryEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Central Board Card Frame with Islamic Ornaments (Image 5 Decoration)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Arabesque Frame Outline Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 130.dp)
                            .border(1.dp, PrimaryEmerald.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .background(Color(0xFFFCFCFA))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = activeState?.text ?: "سُبْحَانَ اللَّهِ",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryEmerald,
                                textAlign = TextAlign.Center,
                                lineHeight = 34.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = activeState?.translation ?: "Glory be to Allah",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Counter circular badges (Image 5: Counters "0 / 33" and "Rounds")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Total Counts Badge
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("الرقم المستهدف", fontSize = 11.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${activeState?.currentCount ?: 0} / ${activeState?.targetLimit ?: 33}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryEmerald
                                )
                            }
                        }

                        // Sessions / Rounds Badge
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("الجولات المكتملة", fontSize = 11.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "الجولة: ${activeState?.rounds ?: 0}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryMint
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Clickable Bead Board Surface
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { viewModel.incrementTasbih() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Large Bead Jewelry Graphic Canvas
                    RosaryBeadsCanvas(
                        gemColor = selectedGemColor,
                        slideOffset = animatedBeadOffset.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    // Big Round Push Button Interactor
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(selectedGemColor.copy(alpha = 0.2f), selectedGemColor)
                                )
                            )
                            .border(6.dp, Color.White, CircleShape)
                            .clickable { viewModel.incrementTasbih() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Fingerprint,
                                contentDescription = "اضغط",
                                tint = AccentGold,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "اضغط للتسبيح",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Bottom Palette / Theme customizer (Image 5 Bottom right floating control)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 56.dp), // Screen navigation clearance
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color Theme circles
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    gemThemes.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    if (selectedGemColor == color) 2.dp else 0.dp,
                                    AccentGold,
                                    CircleShape
                                )
                                .clickable { selectedGemColor = color }
                        )
                    }
                }

                Text(
                    text = "تغيير لون خرز المسبحة 🎨",
                    color = PrimaryEmerald,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Add Custom Dhikr Dialog Popup
        if (showCreateDialog) {
            var customText by remember { mutableStateOf("") }
            var customMeaning by remember { mutableStateOf("") }
            var customTarget by remember { mutableStateOf("33") }

            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = {
                    Text(
                        text = "إضافة ذكر مخصص للمسبحة",
                        color = PrimaryEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = customText,
                            onValueChange = { customText = it },
                            label = { Text("نص الذكر الشريف") },
                            placeholder = { Text("مثال: اللَّهُمَّ لَكَ الْحَمْدُ") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = customMeaning,
                            onValueChange = { customMeaning = it },
                            label = { Text("فضل أو ترجمة الذكر") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = customTarget,
                            onValueChange = { customTarget = it },
                            label = { Text("العدد المستهدف (مثال: ٣٣، ١٠٠)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val limit = customTarget.toIntOrNull() ?: 33
                            if (customText.isNotBlank()) {
                                viewModel.createDhikr(customText, customMeaning, limit)
                                showCreateDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald)
                    ) {
                        Text("إضافة")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("إلغاء", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun RosaryBeadsCanvas(
    gemColor: Color,
    slideOffset: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // 1. Thread Path (Horizontal curve)
        val pathY = height * 0.5f
        drawLine(
            color = Color.LightGray.copy(alpha = 0.5f),
            start = Offset(0f, pathY),
            end = Offset(width, pathY),
            strokeWidth = 3f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )

        // 2. Draw gemstone beads sitting on the string, animated
        val totalBeadsCount = 12
        val itemSpacer = width / (totalBeadsCount - 1)

        for (i in 0 until totalBeadsCount) {
            // Apply wave layout physics and shift slide offset
            val basePositionX = i * itemSpacer
            val currentPositionX = (basePositionX + slideOffset) % (width + itemSpacer)

            // Make beads lift slightly on center to create realistic circular rotation
            val verticalWaveOffset = sin((currentPositionX / width) * Math.PI).toFloat() * 16f
            val beadCenterY = pathY - verticalWaveOffset

            // Draw Gemstone 3D Shadow Backdrop
            drawCircle(
                color = Color.Black.copy(alpha = 0.15f),
                radius = 21f,
                center = Offset(currentPositionX + 2f, beadCenterY + 4f)
            )

            // Draw 3D Gradient Gemstone Sphere (Bead)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, gemColor, gemColor.copy(alpha = 0.8f)),
                    center = Offset(currentPositionX - 4f, beadCenterY - 4f),
                    radius = 18f
                ),
                radius = 18f,
                center = Offset(currentPositionX, beadCenterY)
            )

            // Glass/Crystal Shimmer highlight line
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = 3f,
                center = Offset(currentPositionX - 6f, beadCenterY - 6f)
            )
        }
    }
}
