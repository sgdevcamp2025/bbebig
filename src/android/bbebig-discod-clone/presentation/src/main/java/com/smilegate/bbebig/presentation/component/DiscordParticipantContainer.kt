package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smilegate.bbebig.presentation.theme.Gray50
import com.smilegate.bbebig.presentation.theme.Gray80
import com.smilegate.bbebig.presentation.utils.StableImage
import com.smilegate.bbebig.presentation.utils.rippleClick
import com.smilegate.devcamp.presentation.R

@Composable
fun DiscordParticipantContainer(
    modifier: Modifier,
    userIconResId: Int,
    userName: String,
    userIconSize: Dp = 50.dp,
    isInviteType: Boolean = false,
    onClickInvite: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserContainer(
            modifier = Modifier,
            userIconResId = userIconResId,
            userName = userName,
            userIconSize = userIconSize,
        )
        if (isInviteType) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Gray50)
                    .rippleClick { onClickInvite() }
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                text = stringResource(R.string.invite),
                color = Gray80,
            )
        }
    }
}

@Composable
private fun UserContainer(
    modifier: Modifier,
    userIconResId: Int,
    userName: String,
    userIconSize: Dp,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        StableImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(userIconSize),
            drawableResId = userIconResId,
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = userName,
            fontSize = 13.sp,
        )
    }
}
