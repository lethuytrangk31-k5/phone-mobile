package com.example.phone_mobile.ui.screens.call

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
import com.example.phone_mobile.data.model.CallLog
import com.example.phone_mobile.data.model.CallType
import com.example.phone_mobile.ui.theme.IconGray
import com.example.phone_mobile.ui.theme.PrimaryBlue
import com.example.phone_mobile.ui.theme.RedDelete

@Composable
fun CallHistoryScreen(
    callLogs: List<CallLog>,
    onCallClick: (CallLog) -> Unit = {},
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
                        text = "Cuộc gọi gần đây",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        if (callLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chưa có lịch sử cuộc gọi nào",
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
                items(callLogs, key = { it.id }) { log ->
                    CallHistoryItem(
                        callLog = log,
                        onCallClick = { onCallClick(log) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CallHistoryItem(
    callLog: CallLog,
    onCallClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCallClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Icon(
            painter = painterResource(id = R.drawable.ic_user_avatar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Name, direction icon, phone & time
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = callLog.contactName,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (callLog.type == CallType.MISSED) RedDelete else Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val iconRes = when (callLog.type) {
                    CallType.INCOMING -> R.drawable.ic_call_incoming
                    CallType.OUTGOING -> R.drawable.ic_call_outgoing
                    CallType.MISSED -> R.drawable.ic_call_missed
                }

                val iconTint = when (callLog.type) {
                    CallType.INCOMING -> Color(0xFF23A26D)
                    CallType.OUTGOING -> PrimaryBlue
                    CallType.MISSED -> RedDelete
                }

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${callLog.phoneNumber}  •  ${callLog.time}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Call button
        IconButton(
            onClick = onCallClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_call),
                contentDescription = "Gọi lại",
                tint = IconGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
