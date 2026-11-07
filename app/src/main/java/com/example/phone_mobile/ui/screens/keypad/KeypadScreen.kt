package com.example.phone_mobile.ui.screens.keypad

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.phone_mobile.R
import com.example.phone_mobile.data.model.Contact
import com.example.phone_mobile.ui.theme.GreenCall
import com.example.phone_mobile.ui.theme.PrimaryBlue
import com.example.phone_mobile.ui.theme.SearchBarBg

@Composable
fun KeypadScreen(
    initialPhoneNumber: String = "",
    contacts: List<Contact> = emptyList(),
    speedDialMap: Map<Int, String> = emptyMap(),
    onMakeCall: (phoneNumber: String, contactName: String?) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf(initialPhoneNumber) }
    var showSpeedDialListDialog by remember { mutableStateOf(false) }

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

    // Kiểm tra xem số đang gõ có phải là 1 chữ số được gán phím gọi nhanh không (0-9)
    val singleDigit = phoneNumber.toIntOrNull()
    val speedDialContact = remember(phoneNumber, speedDialMap, contacts) {
        if (phoneNumber.length == 1 && singleDigit != null) {
            speedDialMap[singleDigit]?.let { contactId ->
                contacts.find { it.id == contactId }
            }
        } else {
            null
        }
    }

    // Hàm gọi nhanh khi nhấn giữ phím số 0-9
    fun handleSpeedDialLongClick(key: Int) {
        val contactId = speedDialMap[key]
        val targetContact = contactId?.let { id -> contacts.find { it.id == id } }
        if (targetContact != null) {
            Toast.makeText(
                context,
                "⚡ Gọi nhanh khẩn cấp phím $key: ${targetContact.name}",
                Toast.LENGTH_SHORT
            ).show()
            onMakeCall(targetContact.phoneNumber, targetContact.name)
        } else {
            Toast.makeText(
                context,
                "Phím $key chưa được gán liên hệ gọi nhanh",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Hộp thoại xem tổng quan 10 phím gọi nhanh khẩn cấp (0-9)
    if (showSpeedDialListDialog) {
        Dialog(onDismissRequest = { showSpeedDialListDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF4E5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_flash),
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "10 Phím gọi nhanh khẩn cấp",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Nhấn giữ phím 0-9 trên bàn phím để gọi",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val allKeys = (1..9).toList() + listOf(0)
                        items(allKeys) { key ->
                            val contactId = speedDialMap[key]
                            val contact = contactId?.let { id -> contacts.find { it.id == id } }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (contact != null) Color(0xFFF6F8FC) else Color(0xFFFAFAFA)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Số phím
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (contact != null) Color(0xFFFF9800) else Color(0xFFE0E0E0)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key.toString(),
                                        color = if (contact != null) Color.White else Color.DarkGray,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = contact?.name ?: "Chưa gán liên hệ",
                                        fontSize = 15.sp,
                                        fontWeight = if (contact != null) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (contact != null) Color.Black else Color.Gray
                                    )
                                    Text(
                                        text = contact?.phoneNumber ?: "Vào chi tiết danh bạ để gán",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }

                                if (contact != null) {
                                    IconButton(
                                        onClick = {
                                            showSpeedDialListDialog = false
                                            onMakeCall(contact.phoneNumber, contact.name)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_call),
                                            contentDescription = "Gọi ngay",
                                            tint = GreenCall,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryBlue)
                            .clickable { showSpeedDialListDialog = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Đóng",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
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

                    // Nút mở danh sách 10 phím gọi nhanh khẩn cấp trên TopBar
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { showSpeedDialListDialog = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_flash),
                                contentDescription = "Gọi nhanh",
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Gọi nhanh (0-9)",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
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
            // Màn hình hiển thị tên & số điện thoại người được gọi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SearchBarBg, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Nếu gõ 1 chữ số và số đó là phím gọi nhanh:
                    if (speedDialContact != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_flash),
                                contentDescription = null,
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Gọi nhanh: ${speedDialContact.name} (${speedDialContact.phoneNumber})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                        }
                    } else if (matchedContact != null) {
                        // Nếu khớp liên hệ theo số điện thoại
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = matchedContact.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                            if (matchedContact.isFavorite) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_star_filled),
                                    contentDescription = "Yêu thích",
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .size(16.dp)
                                )
                            }
                        }
                    }

                    // Số điện thoại hiển thị to
                    Text(
                        text = if (phoneNumber.isEmpty()) " " else phoneNumber,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        letterSpacing = 1.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bàn phím số 3x4 (Hỗ trợ bấm để gõ và nhấn giữ để gọi nhanh)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hàng 1: 1, 2, 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    KeypadButton(
                        main = "1",
                        sub = "",
                        hasSpeedDial = speedDialMap.containsKey(1),
                        onClick = { phoneNumber += "1" },
                        onLongClick = { handleSpeedDialLongClick(1) }
                    )
                    KeypadButton(
                        main = "2",
                        sub = "ABC",
                        hasSpeedDial = speedDialMap.containsKey(2),
                        onClick = { phoneNumber += "2" },
                        onLongClick = { handleSpeedDialLongClick(2) }
                    )
                    KeypadButton(
                        main = "3",
                        sub = "DEF",
                        hasSpeedDial = speedDialMap.containsKey(3),
                        onClick = { phoneNumber += "3" },
                        onLongClick = { handleSpeedDialLongClick(3) }
                    )
                }

                // Hàng 2: 4, 5, 6
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    KeypadButton(
                        main = "4",
                        sub = "GHI",
                        hasSpeedDial = speedDialMap.containsKey(4),
                        onClick = { phoneNumber += "4" },
                        onLongClick = { handleSpeedDialLongClick(4) }
                    )
                    KeypadButton(
                        main = "5",
                        sub = "JKL",
                        hasSpeedDial = speedDialMap.containsKey(5),
                        onClick = { phoneNumber += "5" },
                        onLongClick = { handleSpeedDialLongClick(5) }
                    )
                    KeypadButton(
                        main = "6",
                        sub = "MNO",
                        hasSpeedDial = speedDialMap.containsKey(6),
                        onClick = { phoneNumber += "6" },
                        onLongClick = { handleSpeedDialLongClick(6) }
                    )
                }

                // Hàng 3: 7, 8, 9
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    KeypadButton(
                        main = "7",
                        sub = "PQRS",
                        hasSpeedDial = speedDialMap.containsKey(7),
                        onClick = { phoneNumber += "7" },
                        onLongClick = { handleSpeedDialLongClick(7) }
                    )
                    KeypadButton(
                        main = "8",
                        sub = "TUV",
                        hasSpeedDial = speedDialMap.containsKey(8),
                        onClick = { phoneNumber += "8" },
                        onLongClick = { handleSpeedDialLongClick(8) }
                    )
                    KeypadButton(
                        main = "9",
                        sub = "WXYZ",
                        hasSpeedDial = speedDialMap.containsKey(9),
                        onClick = { phoneNumber += "9" },
                        onLongClick = { handleSpeedDialLongClick(9) }
                    )
                }

                // Hàng 4: *, 0, #
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    KeypadButton(main = "*", sub = "", onClick = { phoneNumber += "*" })
                    KeypadButton(
                        main = "0",
                        sub = "+",
                        hasSpeedDial = speedDialMap.containsKey(0),
                        onClick = { phoneNumber += "0" },
                        onLongClick = { handleSpeedDialLongClick(0) }
                    )
                    KeypadButton(main = "#", sub = "", onClick = { phoneNumber += "#" })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hàng phím Gọi điện & Xóa
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
                            if (speedDialContact != null) {
                                // Nếu nhập 1 chữ số gọi nhanh -> gọi thẳng cho liên hệ đó
                                onMakeCall(speedDialContact.phoneNumber, speedDialContact.name)
                            } else if (phoneNumber.isNotEmpty()) {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KeypadButton(
    main: String,
    sub: String,
    hasSpeedDial: Boolean = false,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .size(74.dp)
            .clip(CircleShape)
            .background(Color(0xFFF1F3F4))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
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
                color = Color.Black,
                lineHeight = 30.sp
            )
            if (sub.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = sub,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    if (hasSpeedDial) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF9800))
                        )
                    }
                }
            } else if (hasSpeedDial) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF9800))
                )
            }
        }
    }
}
