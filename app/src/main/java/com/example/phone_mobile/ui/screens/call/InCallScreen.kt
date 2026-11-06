package com.example.phone_mobile.ui.screens.call

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phone_mobile.R
import kotlinx.coroutines.delay

@Composable
fun InCallScreen(
    phoneNumber: String,
    contactName: String?,
    onEndCall: (duration: String) -> Unit
) {
    val context = LocalContext.current

    // Trạng thái cuộc gọi
    var isConnected by remember { mutableStateOf(false) }
    var callSeconds by remember { mutableIntStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var showInCallKeypad by remember { mutableStateOf(false) }
    var keypadInput by remember { mutableStateOf("") }

    // Giả lập kết nối sau 1.5 giây
    LaunchedEffect(Unit) {
        delay(1500)
        isConnected = true
        while (true) {
            delay(1000)
            callSeconds++
        }
    }

    fun formatDuration(totalSec: Int): String {
        val m = totalSec / 60
        val s = totalSec % 60
        return "%02d:%02d".format(m, s)
    }

    val durationText = if (!isConnected) "đang gọi..." else formatDuration(callSeconds)

    // Xử lý nút Back của Android
    BackHandler {
        if (showInCallKeypad) {
            showInCallKeypad = false
        } else {
            onEndCall(formatDuration(callSeconds))
        }
    }

    // Nền tối chuẩn phong cách iOS
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF16181D),
                        Color(0xFF0F1014),
                        Color(0xFF0A0B0D)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 1. Tên người liên hệ
            Text(
                text = contactName?.takeIf { it.isNotBlank() } ?: phoneNumber,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Trạng thái cuộc gọi / Thời gian
            Text(
                text = durationText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = if (!isConnected) Color(0xFFB0B0B8) else Color(0xFFE5E5EA),
                textAlign = TextAlign.Center
            )

            // Số điện thoại phụ nếu có tên
            if (!contactName.isNullOrBlank() && contactName != phoneNumber) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "di động $phoneNumber",
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. Avatar lớn nếu không mở bàn phím số
            if (!showInCallKeypad) {
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C2C2E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user_avatar),
                        contentDescription = "Avatar",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(70.dp)
                    )
                }
            } else {
                // Khi mở bàn phím thì hiển thị các số đã gõ
                Text(
                    text = keypadInput.ifEmpty { " " },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.height(48.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Nội dung điều khiển: Lưới 6 nút chức năng HOẶC Bàn phím số DTMF
            if (!showInCallKeypad) {
                // Lưới 6 nút iOS (3 cột x 2 hàng)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Hàng 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IosCallActionButton(
                            iconRes = if (isMuted) R.drawable.ic_mic_off else R.drawable.ic_mic,
                            label = if (isMuted) "bỏ tắt tiếng" else "tắt tiếng",
                            isActive = isMuted,
                            onClick = {
                                isMuted = !isMuted
                                Toast.makeText(
                                    context,
                                    if (isMuted) "Đã tắt tiếng micro" else "Đã bật micro",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )

                        IosCallActionButton(
                            iconRes = R.drawable.ic_keypad,
                            label = "bàn phím",
                            isActive = false,
                            onClick = {
                                showInCallKeypad = true
                            }
                        )

                        IosCallActionButton(
                            iconRes = R.drawable.ic_volume_up,
                            label = "loa ngoài",
                            isActive = isSpeakerOn,
                            onClick = {
                                isSpeakerOn = !isSpeakerOn
                                Toast.makeText(
                                    context,
                                    if (isSpeakerOn) "Đã chuyển sang loa ngoài" else "Đã tắt loa ngoài",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }

                    // Hàng 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IosCallActionButton(
                            iconRes = R.drawable.ic_add,
                            label = "thêm cuộc gọi",
                            isActive = false,
                            onClick = {
                                Toast.makeText(context, "Thêm cuộc gọi mới...", Toast.LENGTH_SHORT).show()
                            }
                        )

                        IosCallActionButton(
                            iconRes = R.drawable.ic_video_call,
                            label = "FaceTime",
                            isActive = false,
                            onClick = {
                                Toast.makeText(context, "Chuyển sang FaceTime...", Toast.LENGTH_SHORT).show()
                            }
                        )

                        IosCallActionButton(
                            iconRes = R.drawable.ic_contact_book,
                            label = "danh bạ",
                            isActive = false,
                            onClick = {
                                Toast.makeText(context, "Mở danh bạ...", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            } else {
                // Bàn phím số tương tác khi đang gọi (DTMF In-Call Keypad)
                InCallKeypad(
                    onDigitClick = { digit ->
                        keypadInput += digit
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 5. Nút Cúp máy đỏ to ở cuối (kèm nút "Ẩn" nếu đang mở bàn phím)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                // Nút kết thúc cuộc gọi
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF3B30)) // Màu đỏ iOS
                        .clickable {
                            onEndCall(formatDuration(callSeconds))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_call_end),
                        contentDescription = "Kết thúc cuộc gọi",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Nếu đang mở bàn phím, hiển thị nút "Ẩn" bên phải
                if (showInCallKeypad) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .clickable { showInCallKeypad = false }
                    ) {
                        Text(
                            text = "Ẩn",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Nút hành động phong cách iOS: hình tròn đường kính 70dp với nhãn văn bản bên dưới
 */
@Composable
private fun IosCallActionButton(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(85.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(if (isActive) Color.White else Color.White.copy(alpha = 0.15f))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = if (isActive) Color.Black else Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * Bàn phím quay số số khi đang gọi điện (DTMF Dialer)
 */
@Composable
private fun InCallKeypad(
    onDigitClick: (String) -> Unit
) {
    val keys = listOf(
        listOf("1" to "", "2" to "ABC", "3" to "DEF"),
        listOf("4" to "GHI", "5" to "JKL", "6" to "MNO"),
        listOf("7" to "PQRS", "8" to "TUV", "9" to "WXYZ"),
        listOf("*" to "", "0" to "+", "#" to "")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { (digit, sub) ->
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f))
                            .clickable { onDigitClick(digit) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = digit,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                lineHeight = 30.sp
                            )
                            if (sub.isNotEmpty()) {
                                Text(
                                    text = sub,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.7f),
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
