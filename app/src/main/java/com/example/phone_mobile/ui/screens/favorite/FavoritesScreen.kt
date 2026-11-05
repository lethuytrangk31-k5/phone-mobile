package com.example.phone_mobile.ui.screens.favorite

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phone_mobile.R
import com.example.phone_mobile.data.model.Contact
import com.example.phone_mobile.ui.theme.IconGray
import com.example.phone_mobile.ui.theme.PrimaryBlue

@Composable
fun FavoritesScreen(
    favoriteContacts: List<Contact>,
    onContactClick: (Contact) -> Unit,
    onCallClick: (Contact) -> Unit,
    onMessageClick: (Contact) -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
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
                        text = "Danh bạ yêu thích",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        if (favoriteContacts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có liên hệ yêu thích nào",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(favoriteContacts, key = { it.id }) { contact ->
                    FavoriteItemRow(
                        contact = contact,
                        onClick = { onContactClick(contact) },
                        onCallClick = { onCallClick(contact) },
                        onMessageClick = { onMessageClick(contact) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteItemRow(
    contact: Contact,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Yellow Star
        Icon(
            painter = painterResource(id = R.drawable.ic_star_filled),
            contentDescription = "Yêu thích",
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Avatar
        Icon(
            painter = painterResource(id = R.drawable.ic_user_avatar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Name and Phone
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = contact.phoneNumber,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }

        // Cặp icon Gọi điện & Tin nhắn theo Figma Group 71
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Nút Gọi điện
            IconButton(
                onClick = onCallClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_call),
                    contentDescription = "Gọi cho ${contact.name}",
                    tint = IconGray,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Nút Gửi tin nhắn
            IconButton(
                onClick = onMessageClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chat_bubble),
                    contentDescription = "Nhắn tin cho ${contact.name}",
                    tint = IconGray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
