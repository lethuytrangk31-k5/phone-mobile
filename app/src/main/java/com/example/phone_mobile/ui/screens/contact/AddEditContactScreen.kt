package com.example.phone_mobile.ui.screens.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phone_mobile.R
import com.example.phone_mobile.data.model.Contact
import com.example.phone_mobile.ui.theme.BorderGray
import com.example.phone_mobile.ui.theme.ErrorRed
import com.example.phone_mobile.ui.theme.PlaceholderGray
import com.example.phone_mobile.ui.theme.PrimaryBlue

@Composable
fun AddEditContactScreen(
    contactToEdit: Contact? = null,
    existingContacts: List<Contact> = emptyList(),
    onBackClick: () -> Unit,
    onSaveContact: (Contact) -> Unit
) {
    val isEditMode = contactToEdit != null

    var name by remember { mutableStateOf(contactToEdit?.name ?: "") }
    var phoneNumber by remember { mutableStateOf(contactToEdit?.phoneNumber ?: "") }
    var email by remember { mutableStateOf(contactToEdit?.email ?: "") }
    var address by remember { mutableStateOf(contactToEdit?.address ?: "") }
    var birthday by remember { mutableStateOf(contactToEdit?.birthday ?: "") }
    var note by remember { mutableStateOf(contactToEdit?.note ?: "") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Quay lại",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditMode) "Chỉnh sửa liên hệ" else "Thêm liên hệ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar picker
            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user_avatar),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(120.dp)
                )

                // Camera icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue)
                        .clickable { /* Select avatar photo */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Thêm ảnh",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = if (isEditMode) "Đổi ảnh đại diện" else "Thêm ảnh",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Form Fields
            ContactInputField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = null
                },
                placeholder = "Nhập họ tên",
                iconRes = R.drawable.ic_user_avatar,
                errorMessage = nameError
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactInputField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                    phoneError = null
                },
                placeholder = "Nhập số điện thoại",
                iconRes = R.drawable.ic_call,
                errorMessage = phoneError
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Nhập email",
                iconRes = R.drawable.ic_mail
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactInputField(
                value = address,
                onValueChange = { address = it },
                placeholder = "Nhập địa chỉ",
                iconRes = R.drawable.ic_location
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactInputField(
                value = birthday,
                onValueChange = { birthday = it },
                placeholder = "Chọn ngày sinh",
                iconRes = R.drawable.ic_calendar
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactInputField(
                value = note,
                onValueChange = { note = it },
                placeholder = "Nhập ghi chú",
                iconRes = R.drawable.ic_notes
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Save Button
            Button(
                onClick = {
                    var hasError = false

                    if (name.isBlank()) {
                        nameError = "Họ tên không được để trống"
                        hasError = true
                    } else {
                        nameError = null
                    }

                    if (phoneNumber.isBlank()) {
                        phoneError = "Số điện thoại không được để trống"
                        hasError = true
                    } else {
                        val trimmedPhone = phoneNumber.trim().replace(" ", "").replace("-", "")
                        val isValidPhone = trimmedPhone.all { it.isDigit() || it == '+' } && 
                                trimmedPhone.count { it.isDigit() } in 9..15
                        if (!isValidPhone) {
                            phoneError = "Số điện thoại không hợp lệ"
                            hasError = true
                        } else {
                            val isDuplicate = existingContacts.any { existing ->
                                existing.id != contactToEdit?.id &&
                                        existing.phoneNumber.trim().replace(" ", "").replace("-", "") == trimmedPhone
                            }
                            if (isDuplicate) {
                                phoneError = "Số điện thoại đã tồn tại trong danh bạ"
                                hasError = true
                            } else {
                                phoneError = null
                            }
                        }
                    }

                    if (hasError) return@Button

                    val updatedContact = Contact(
                        id = contactToEdit?.id ?: System.currentTimeMillis().toString(),
                        name = name.trim(),
                        phoneNumber = phoneNumber.trim(),
                        email = email.trim(),
                        address = address.trim(),
                        birthday = birthday.trim(),
                        note = note.trim(),
                        isFavorite = contactToEdit?.isFavorite ?: false
                    )
                    onSaveContact(updatedContact)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = if (isEditMode) "Lưu thay đổi" else "Lưu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun ContactInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconRes: Int,
    errorMessage: String? = null
) {
    val isError = errorMessage != null
    val borderColor = if (isError) ErrorRed else BorderGray

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .border(2.dp, borderColor, RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = if (isError) Color.Black else PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 17.sp,
                        color = Color.Black
                    ),
                    cursorBrush = SolidColor(if (isError) ErrorRed else PrimaryBlue),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 17.sp,
                                color = PlaceholderGray
                            )
                        }
                        innerTextField()
                    }
                )

                // Icon cảnh báo đỏ carbon:warning-filled khi có lỗi (giữ nguyên màu trắng của dấu ! bên trong)
                if (isError) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_warning_filled),
                        contentDescription = "Lỗi",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Dòng chữ báo lỗi đỏ ngay dưới ô nhập liệu
        if (isError) {
            Text(
                text = errorMessage ?: "",
                color = ErrorRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }
    }
}
