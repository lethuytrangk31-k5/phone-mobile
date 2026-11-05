package com.example.phone_mobile.ui.screens.contact

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phone_mobile.R
import com.example.phone_mobile.data.model.Contact
import com.example.phone_mobile.data.model.sampleContacts
import com.example.phone_mobile.ui.theme.IconGray
import com.example.phone_mobile.ui.theme.PrimaryBlue
import com.example.phone_mobile.ui.theme.SearchBarBg
import com.example.phone_mobile.ui.theme.TextDark
import com.example.phone_mobile.util.removeVietnameseDiacritics

@Composable
fun ContactListScreen(
    contacts: List<Contact> = sampleContacts,
    onContactClick: (Contact) -> Unit = {},
    onCallClick: (Contact) -> Unit = {},
    onMessageClick: (Contact) -> Unit = {},
    onAddClick: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // Màn hình "Danh bạ trống" chuẩn 100% theo Figma (Node 5:1791)
    if (contacts.isEmpty()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                ContactTopBar(onAddClick = onAddClick)
            },
            bottomBar = bottomBar
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Minh họa thẻ danh bạ profile card màu xanh nhạt
                Icon(
                    painter = painterResource(id = R.drawable.ic_empty_contacts),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(width = 240.dp, height = 180.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Tiêu đề: "Chưa có liên hệ nào"
                Text(
                    text = "Chưa có liên hệ nào",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Hướng dẫn: "Hãy thêm liên hệ đầu tiên của bạn để bắt đầu quản lý danh bạ"
                Text(
                    text = "Hãy thêm liên hệ đầu tiên của bạn\nđể bắt đầu quản lý danh bạ",
                    fontSize = 15.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Nút "Thêm liên hệ" màu xanh có icon +
                Button(
                    onClick = onAddClick,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Thêm liên hệ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
        return
    }

    // Filter contacts based on search query (hỗ trợ tìm kiếm cả có dấu và không dấu tiếng Việt)
    val filteredContacts = remember(searchQuery, contacts) {
        if (searchQuery.isBlank()) {
            contacts
        } else {
            val normalizedQuery = searchQuery.trim().removeVietnameseDiacritics().lowercase()
            contacts.filter { contact ->
                val normalizedName = contact.name.removeVietnameseDiacritics().lowercase()
                val cleanPhone = contact.phoneNumber.replace(" ", "")
                normalizedName.contains(normalizedQuery) ||
                        cleanPhone.contains(normalizedQuery) ||
                        contact.name.contains(searchQuery, ignoreCase = true) ||
                        contact.phoneNumber.contains(searchQuery)
            }
        }
    }

    // Group contacts by the first letter of their display name
    val groupedContacts = remember(filteredContacts) {
        filteredContacts.groupBy { contact ->
            val firstChar = contact.name.trim().take(1).uppercase()
            if (firstChar.isNotEmpty() && firstChar[0].isLetter()) firstChar else "#"
        }.toSortedMap()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            ContactTopBar(onAddClick = onAddClick)
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            ContactSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )

            // Contact List grouped by Alphabet sections
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                groupedContacts.forEach { (initial, contactList) ->
                    item(key = "header_$initial") {
                        Text(
                            text = initial,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    items(contactList, key = { it.id }) { contact ->
                        ContactItemRow(
                            contact = contact,
                            onClick = { onContactClick(contact) },
                            onCallClick = { onCallClick(contact) }
                        )
                    }
                }

                if (filteredContacts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không tìm thấy liên hệ nào",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ContactTopBar(
    onAddClick: () -> Unit
) {
    Surface(
        color = PrimaryBlue,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Danh bạ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(
                onClick = onAddClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Thêm liên hệ",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ContactSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(SearchBarBg, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Tìm kiếm",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 17.sp,
                    color = Color.Black
                ),
                cursorBrush = SolidColor(PrimaryBlue),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Tìm kiếm liên hệ",
                            fontSize = 17.sp,
                            color = Color(0xFF555555)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun ContactItemRow(
    contact: Contact,
    onClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar
        Icon(
            painter = painterResource(id = R.drawable.ic_user_avatar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(58.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Name and Phone Number
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contact.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                if (contact.isFavorite) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star_filled),
                        contentDescription = "Yêu thích",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = contact.phoneNumber,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }

        // Call button
        IconButton(
            onClick = onCallClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_call),
                contentDescription = "Gọi cho ${contact.name}",
                tint = IconGray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
