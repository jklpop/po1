package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UmmahPost
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryEmerald
import com.example.ui.theme.SecondaryMint
import com.example.ui.viewmodel.MuslimViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmmahScreen(viewModel: MuslimViewModel) {
    val posts by viewModel.ummahPosts.collectAsState()
    var showCreatePostSheet by remember { mutableStateOf(false) }

    // Navigation Sub Filter tags (Matches Image 8 sub-categories)
    val filters = listOf("الترندات", "بالجوار", "الدعاء", "متابعة", "مشاركة الحياة")
    var activeFilter by remember { mutableStateOf("الترندات") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header Search/Tab bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryEmerald)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search magnifier
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "بحث", tint = Color.White)
                }

                Text(
                    text = "أمة الإسلام - شارك وتدبر",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Notification Bell
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = "الإشعارات", tint = AccentGold)
                }
            }

            // Scrollable filter tabs (Image 8 Sub-filters)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { tag ->
                    val isActive = tag == activeFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isActive) PrimaryEmerald else Color(0xFFF1F8E9)
                            )
                            .clickable { activeFilter = tag }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tag,
                            color = if (isActive) Color.White else PrimaryEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // TimeLine Main Feed
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 120.dp) // Nav bar clearance
            ) {
                items(posts, key = { it.id }) { post ->
                    UmmahPostItemCard(
                        post = post,
                        onLikeClicked = { viewModel.toggleLikePost(post.id) }
                    )
                }
            }
        }

        // Hover Floating "+" button (Centered above home navigators, matching Image 8)
        FloatingActionButton(
            onClick = { showCreatePostSheet = true },
            containerColor = SecondaryMint,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 74.dp) // Floating spacing clearance
                .size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "مشاركة تدوينة", tint = AccentGold, modifier = Modifier.size(28.dp))
        }

        // Bottom compose sheet for custom publishing
        if (showCreatePostSheet) {
            WritePostDialog(
                onDismiss = { showCreatePostSheet = false },
                onPublish = { author, content ->
                    viewModel.addPost(author, content)
                    showCreatePostSheet = false
                }
            )
        }
    }
}

@Composable
fun UmmahPostItemCard(
    post: UmmahPost,
    onLikeClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Post Author Header Detail
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Follow badge trigger
                TextButton(onClick = {}) {
                    Text(
                        text = "+ متابعة",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = post.author,
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = post.authorSubtitle,
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }

                    // Rounded Avatar with initials
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(post.avatarColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.author.firstOrNull()?.toString() ?: "م",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Post content Text
            Text(
                text = post.content,
                color = Color.DarkGray,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            )

            // Dynamic Gradient background banner for Islamic aesthetic design (Image 8 style)
            if (post.imageResType > 0) {
                val bannerBrush = when (post.imageResType) {
                    1 -> Brush.linearGradient(colors = listOf(Color(0xFF1B5E20), Color(0xFF4CAF50)))
                    2 -> Brush.linearGradient(colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)))
                    3 -> Brush.linearGradient(colors = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121)))
                    else -> Brush.sweepGradient(colors = listOf(PrimaryEmerald, SecondaryMint))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(bannerBrush)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Mosque, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "﴿ وَتَعَاوَنُوا عَلَى الْبِرِّ وَالتَّقْوَىٰ ﴾",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Footer Interactor bar (Likes, Comments, Shares)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .border(0.5.dp, Color(0xFFEEEEEE), RoundedCornerShape(0.dp))
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Share
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Share, contentDescription = "نشر", tint = Color.LightGray, modifier = Modifier.size(20.dp))
                }

                // Call Comment dialog
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = post.commentsCount.toString(),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "تعليقات",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Heart Link database toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLikeClicked() }
                ) {
                    Text(
                        text = if (post.likesCount > 1000) "${(post.likesCount / 1000f).coerceAtLeast(0f)}ألف" else post.likesCount.toString(),
                        color = if (post.isLikedByUser) Color.Red else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (post.isLikedByUser) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "إعجاب",
                        tint = if (post.isLikedByUser) Color.Red else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WritePostDialog(
    onDismiss: () -> Unit,
    onPublish: (String, String) -> Unit
) {
    var textState by remember { mutableStateOf("") }
    var authorState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "كتابة مشاركة دعوية جديدة ✍️",
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
                    value = authorState,
                    onValueChange = { authorState = it },
                    label = { Text("الاسم الكريم") },
                    placeholder = { Text("مثال: فاعل خير") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("ماذا تريد أن تشارك مع المسلمين؟") },
                    placeholder = { Text("اكتب آية كريمة، دعاء، أو حكمة إسلامية...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (textState.isNotBlank()) {
                        onPublish(authorState, textState)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryEmerald)
            ) {
                Text("نشر الآن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = Color.Gray)
            }
        }
    )
}
