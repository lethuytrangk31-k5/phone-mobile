package com.example.phone_mobile.ui.screens.keypad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phone_mobile.R
import com.example.phone_mobile.data.model.Contact
import com.example.phone_mobile.ui.theme.GreenCall
import com.example.phone_mobile.ui.theme.PrimaryBlue
import com.example.phone_mobile.ui.theme.RedDelete
import com.example.phone_mobile.ui.theme.SearchBarBg
import kotlinx.coroutines.delay

@Composable
fun KeypadScreen(
    initialPhoneNumber: String = "",
    contacts: List<Contact> = emptyList(),
    isCalling: Boolean = false,
    onMakeCall: (phoneNumber: String, contactName: String?) -> Unit,
    onEndCall: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    var phoneNumber by remember { mutableStateOf(initialPhoneNumber) }

    LaunchedEffect(initialPhoneNumber) {
        if (initialPhoneNumber.isNotEmpty()) {
            phoneNumber = initialPhoneNumber
        }
    }

    // Tự động tìm kiếm liên hệ khớp với số điện thoại đang nhập
    val matchedContact = remember(phoneNumber, contacts) {
        val cleanNumber = phoneNumber.replace(" ", "")
        if (cleanNumber.isNotEmpty()) {
            contacts.find { it.phoneNumber.replace(" ", "") == cleanNumber }
        } else {
            null
        }
    }

    // Call duration timer khi đang trong cuộc gọi
    var callSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(isCalling) {
        if (isCalling) {
            callSeconds = 0
            while (true) {
                delay(1000)
                callSeconds++
            }
        } else {
            callSeconds = 0
        }
    }

    fun formatDuration(totalSec: Int): String {
        val m = totalSec / 60
        val s = totalSec % 60
        return "%02d:%02d".format(m, s)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Surface(
                color = PrimaryBlue,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Bàn phím",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Hiển thị Banner Đang Gọi (Active Calling Banner)
            AnimatedVisibility(
                visible = isCalling,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    color = Color(0xFFF1F5FD),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_call),
                                contentDescription = null,
                                tint = GreenCall,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = matchedContact?.name ?: "Đang gọi...",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = if (callSeconds > 0) "Thời lượng: ${formatDuration(callSeconds)}" else "Đang kết nối...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF555555)
                                )
                            }
                        }

                        // Nút kết thúc cuộc gọi
                        Button(
                            onClick = onEndCall,
                            colors = ButtonDefaults.buttonColors(containerColor = RedDelete),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = "Kết thúc",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Màn hình hiển thị tên & số điện thoại người được gọi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SearchBarBg, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Hiển thị tên người liên hệ nếu tìm thấy
                        if (matchedContact != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = matchedContact.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                if (matchedContact.isFavorite) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_star_filled),
                                        contentDescription = "Yêu thích",
                                        tint = Color(0xFFFFC107),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                        }

                        // Hiển thị số điện thoại
                        Text(
                            text = phoneNumber.ifEmpty { "Nhập số điện thoại" },
                            fontSize = if (phoneNumber.isEmpty()) 17.sp else 22.sp,
                            fontWeight = if (phoneNumber.isEmpty()) FontWeight.Normal else FontWeight.Bold,
                            color = if (phoneNumber.isEmpty()) Color(0xFF666666) else Color.Black,
                            maxLines = 1
                        )
                    }

                    // Nút xóa tất cả
                    if (phoneNumber.isNotEmpty()) {
                        IconButton(
                            onClick = { phoneNumber = "" },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Xóa tất cả",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bàn phím số 3x4
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hàng 1
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    KeypadButton(main = "1", sub = "") { phoneNumber += "1" }
                    KeypadButton(main = "2", sub = "ABC") { phoneNumber += "2" }
                    KeypadButton(main = "3", sub = "DEF") { phoneNumber += "3" }
                }

                // Hàng 2
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    KeypadButton(main = "4", sub = "GHI") { phoneNumber += "4" }
                    KeypadButton(main = "5", sub = "JKL") { phoneNumber += "5" }
                    KeypadButton(main = "6", sub = "MNO") { phoneNumber += "6" }
                }

                // Hàng 3
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    KeypadButton(main = "7", sub = "PQRS") { phoneNumber += "7" }
                    KeypadButton(main = "8", sub = "TUV") { phoneNumber += "8" }
                    KeypadButton(main = "9", sub = "WXYZ") { phoneNumber += "9" }
                }

                // Hàng 4
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    KeypadButton(main = "*", sub = "") { phoneNumber += "*" }
                    KeypadButton(main = "0", sub = "+") { phoneNumber += "0" }
                    KeypadButton(main = "#", sub = "") { phoneNumber += "#" }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hàng hành động Gọi điện & Xóa lùi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.size(48.dp))

                // Nút gọi điện màu xanh lá
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(GreenCall)
                        .clickable {
                            if (phoneNumber.isNotEmpty()) {
                                onMakeCall(phoneNumber, matchedContact?.name)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_call),
                        contentDescription = "Gọi",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Phím xóa lùi
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (phoneNumber.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                if (phoneNumber.isNotEmpty()) {
                                    phoneNumber = phoneNumber.dropLast(1)
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_backspace),
                                contentDescription = "Xóa ký tự",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun KeypadButton(
    main: String,
    sub: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(74.dp)
            .clip(CircleShape)
            .background(Color(0xFFF1F3F4))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = main,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            if (sub.isNotEmpty()) {
                Text(
                    text = sub,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }
    }
}
