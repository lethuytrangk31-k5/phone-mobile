package com.example.phone_mobile.ui.screens.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.phone_mobile.R
import com.example.phone_mobile.data.model.Contact
import com.example.phone_mobile.ui.theme.ActionBlueLight
import com.example.phone_mobile.ui.theme.ActionBlueText
import com.example.phone_mobile.ui.theme.BorderGray
import com.example.phone_mobile.ui.theme.InputBg
import com.example.phone_mobile.ui.theme.PrimaryBlue
import com.example.phone_mobile.ui.theme.RedDelete

@Composable
fun ContactDetailScreen(
    contact: Contact,
    assignedSpeedDialKey: Int? = null,
    allSpeedDials: Map<Int, String> = emptyMap(),
    allContacts: List<Contact> = emptyList(),
    onAssignSpeedDial: (key: Int) -> Unit = {},
    onRemoveSpeedDial: () -> Unit = {},
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onCallClick: () -> Unit,
    onVideoCallClick: () -> Unit = {},
    onMessageClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showSpeedDialDialog by remember { mutableStateOf(false) }
    var keyToOverwrite by remember { mutableStateOf<Int?>(null) }

    // Hộp thoại gán phím gọi nhanh khẩn cấp (0-9)
    if (showSpeedDialDialog) {
        Dialog(onDismissRequest = { showSpeedDialDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                modifier = Modifier.width(340.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header icon tia chớp
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF4E5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_flash),
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Gán phím gọi nhanh khẩn cấp",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Chọn phím số (0-9) để gán cho \"${contact.name}\". Khi ở bàn phím, nhấn giữ hoặc bấm số để gọi ngay.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Lưới 10 phím (1-9 và 0)
                    val keys = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Hàng 1: 1, 2, 3
                        SpeedDialKeyRow(
                            keys = listOf(1, 2, 3),
                            currentAssignedKey = assignedSpeedDialKey,
                            allSpeedDials = allSpeedDials,
                            allContacts = allContacts,
                            currentContactId = contact.id,
                            onKeySelected = { key ->
                                val existingId = allSpeedDials[key]
                                if (existingId != null && existingId != contact.id) {
                                    keyToOverwrite = key
                                } else {
                                    onAssignSpeedDial(key)
                                    showSpeedDialDialog = false
                                }
                            }
                        )

                        // Hàng 2: 4, 5, 6
                        SpeedDialKeyRow(
                            keys = listOf(4, 5, 6),
                            currentAssignedKey = assignedSpeedDialKey,
                            allSpeedDials = allSpeedDials,
                            allContacts = allContacts,
                            currentContactId = contact.id,
                            onKeySelected = { key ->
                                val existingId = allSpeedDials[key]
                                if (existingId != null && existingId != contact.id) {
                                    keyToOverwrite = key
                                } else {
                                    onAssignSpeedDial(key)
                                    showSpeedDialDialog = false
                                }
                            }
                        )

                        // Hàng 3: 7, 8, 9
                        SpeedDialKeyRow(
                            keys = listOf(7, 8, 9),
                            currentAssignedKey = assignedSpeedDialKey,
                            allSpeedDials = allSpeedDials,
                            allContacts = allContacts,
                            currentContactId = contact.id,
                            onKeySelected = { key ->
                                val existingId = allSpeedDials[key]
                                if (existingId != null && existingId != contact.id) {
                                    keyToOverwrite = key
                                } else {
                                    onAssignSpeedDial(key)
                                    showSpeedDialDialog = false
                                }
                            }
                        )

                        // Hàng 4: 0 ở giữa
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SpeedDialKeyButton(
                                key = 0,
                                isAssignedToThis = assignedSpeedDialKey == 0,
                                assignedContactName = allSpeedDials[0]?.let { id ->
                                    allContacts.find { it.id == id }?.name
                                },
                                isAssignedToOther = allSpeedDials[0] != null && allSpeedDials[0] != contact.id,
                                onClick = {
                                    val existingId = allSpeedDials[0]
                                    if (existingId != null && existingId != contact.id) {
                                        keyToOverwrite = 0
                                    } else {
                                        onAssignSpeedDial(0)
                                        showSpeedDialDialog = false
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nút gỡ bỏ nếu đang gán
                    if (assignedSpeedDialKey != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF0F0))
                                .clickable {
                                    onRemoveSpeedDial()
                                    showSpeedDialDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Gỡ phím gọi nhanh (${assignedSpeedDialKey})",
                                color = RedDelete,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Nút Đóng
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF1F3F4))
                            .clickable { showSpeedDialDialog = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Đóng",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Hộp thoại xác nhận ghi đè khi phím đã được gán cho người khác
    if (keyToOverwrite != null) {
        val targetKey = keyToOverwrite!!
        val otherName = allSpeedDials[targetKey]?.let { id ->
            allContacts.find { it.id == id }?.name
        } ?: "liên hệ khác"

        Dialog(onDismissRequest = { keyToOverwrite = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Thay đổi phím gọi nhanh",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Phím $targetKey hiện đang được gán cho \"$otherName\". Bạn có muốn chuyển phím này cho \"${contact.name}\" không?",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1F3F4))
                                .clickable { keyToOverwrite = null },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Hủy", color = Color.Black, fontSize = 14.sp)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryBlue)
                                .clickable {
                                    onAssignSpeedDial(targetKey)
                                    keyToOverwrite = null
                                    showSpeedDialDialog = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Đồng ý",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Hộp thoại xóa liên hệ chuẩn 100% theo thiết kế Figma
    if (showDeleteConfirmDialog) {
        Dialog(onDismissRequest = { showDeleteConfirmDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                modifier = Modifier.width(320.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Biểu tượng thùng rác trong hình tròn đỏ
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(RedDelete),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tiêu đề
                    Text(
                        text = "Xóa liên hệ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Nội dung xác nhận
                    Text(
                        text = "Bạn có chắc chắn muốn xóa liên hệ này không?",
                        fontSize = 15.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 2 nút Hủy và Xóa
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        // Nút Hủy
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(38.dp)
                                .border(1.dp, Color(0xFFC7D3E3), RoundedCornerShape(8.dp))
                                .clickable { showDeleteConfirmDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Hủy",
                                color = ActionBlueText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Nút Xóa
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(38.dp)
                                .border(1.dp, RedDelete, RoundedCornerShape(8.dp))
                                .clickable {
                                    showDeleteConfirmDialog = false
                                    onDeleteClick()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Xóa",
                                color = RedDelete,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Quay lại",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Chi tiết liên hệ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Nút Sửa
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(10.dp))
                            .clickable(onClick = onEditClick)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sửa",
                            color = PrimaryBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Card (Avatar + Name)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, BorderGray, RoundedCornerShape(20.dp))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user_avatar),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = contact.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Information Card (Phone, Email, Address, Birthday, Note)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, BorderGray, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    DetailInfoRow(
                        iconRes = R.drawable.ic_call,
                        value = contact.phoneNumber.ifEmpty { "Chưa có số điện thoại" }
                    )

                    DetailInfoRow(
                        iconRes = R.drawable.ic_mail,
                        value = contact.email.ifEmpty { "Chưa có email" }
                    )

                    DetailInfoRow(
                        iconRes = R.drawable.ic_location,
                        value = contact.address.ifEmpty { "Chưa có địa chỉ" }
                    )

                    if (contact.birthday.isNotBlank()) {
                        DetailInfoRow(
                            iconRes = R.drawable.ic_calendar,
                            value = contact.birthday
                        )
                    }

                    if (contact.note.isNotBlank()) {
                        DetailInfoRow(
                            iconRes = R.drawable.ic_notes,
                            value = contact.note
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Row gồm chính xác 2 nút: [Icon + Gọi điện] và [Icon + Gọi video]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Nút Gọi điện (Icon + Gọi điện)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(ActionBlueLight, RoundedCornerShape(10.dp))
                        .clickable(onClick = onCallClick),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_call),
                            contentDescription = null,
                            tint = ActionBlueText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gọi điện",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ActionBlueText
                        )
                    }
                }

                // Nút Gọi video (Icon + Gọi video)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(ActionBlueLight, RoundedCornerShape(10.dp))
                        .clickable(onClick = onVideoCallClick),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_video_call),
                            contentDescription = null,
                            tint = ActionBlueText,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gọi video",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ActionBlueText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gửi tin nhắn
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(ActionBlueLight, RoundedCornerShape(10.dp))
                    .clickable(onClick = onMessageClick),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_message),
                        contentDescription = null,
                        tint = ActionBlueText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gửi tin nhắn",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ActionBlueText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Yêu thích
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(ActionBlueLight, RoundedCornerShape(10.dp))
                    .clickable(onClick = onToggleFavorite),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(
                            id = if (contact.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star
                        ),
                        contentDescription = null,
                        tint = if (contact.isFavorite) Color(0xFFFFC107) else ActionBlueText,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (contact.isFavorite) "Đã yêu thích" else "Yêu thích",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ActionBlueText
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gọi nhanh khẩn cấp (Phím 0-9)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (assignedSpeedDialKey != null) Color(0xFFFFF4E5) else ActionBlueLight,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { showSpeedDialDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_flash),
                        contentDescription = null,
                        tint = if (assignedSpeedDialKey != null) Color(0xFFFF9800) else ActionBlueText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (assignedSpeedDialKey != null) {
                            "Gọi nhanh khẩn cấp (Phím $assignedSpeedDialKey)"
                        } else {
                            "Gán phím gọi nhanh khẩn cấp (0-9)"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (assignedSpeedDialKey != null) Color(0xFFE65100) else ActionBlueText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Xóa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(RedDelete, RoundedCornerShape(10.dp))
                    .clickable { showDeleteConfirmDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Xóa",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailInfoRow(
    iconRes: Int,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .background(InputBg, RoundedCornerShape(10.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun SpeedDialKeyRow(
    keys: List<Int>,
    currentAssignedKey: Int?,
    allSpeedDials: Map<Int, String>,
    allContacts: List<Contact>,
    currentContactId: String,
    onKeySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        keys.forEach { key ->
            val assignedId = allSpeedDials[key]
            val isThis = currentAssignedKey == key
            val isOther = assignedId != null && assignedId != currentContactId
            val name = assignedId?.let { id ->
                allContacts.find { it.id == id }?.name
            }

            SpeedDialKeyButton(
                key = key,
                isAssignedToThis = isThis,
                assignedContactName = name,
                isAssignedToOther = isOther,
                onClick = { onKeySelected(key) }
            )
        }
    }
}

@Composable
private fun SpeedDialKeyButton(
    key: Int,
    isAssignedToThis: Boolean,
    assignedContactName: String?,
    isAssignedToOther: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isAssignedToThis -> Color(0xFFFF9800)
        isAssignedToOther -> Color(0xFFF1F3F4)
        else -> Color(0xFFE8F0FE)
    }

    val textColor = when {
        isAssignedToThis -> Color.White
        isAssignedToOther -> Color.DarkGray
        else -> PrimaryBlue
    }

    Box(
        modifier = Modifier
            .size(width = 82.dp, height = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = key.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = when {
                    isAssignedToThis -> "Đang gán"
                    isAssignedToOther -> assignedContactName?.take(7) ?: "Đã gán"
                    else -> "Trống"
                },
                fontSize = 10.sp,
                fontWeight = if (isAssignedToThis) FontWeight.Bold else FontWeight.Normal,
                color = if (isAssignedToThis) Color.White else Color.Gray,
                maxLines = 1
            )
        }
    }
}
