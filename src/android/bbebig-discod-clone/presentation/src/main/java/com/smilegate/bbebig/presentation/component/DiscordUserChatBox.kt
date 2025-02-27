package com.smilegate.bbebig.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smilegate.bbebig.presentation.utils.RandomUtil
import com.smilegate.devcamp.presentation.R

@Composable
fun DiscordUserChatBox(
    modifier: Modifier,
    userName: String,
    date: String,
    chatContent: String,
    userIconUrl: String,
    colorNumber: Int? = null,
) {
    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(
                    color = if (userIconUrl.isEmpty()) {
                        RandomUtil.getRandomColor(colorNumber ?: 0)
                    } else {
                        Color.Transparent
                    },
                )
                .padding(5.dp),
            model = if (userIconUrl.isEmpty()) {
                R.drawable.ic_logo
            } else {
                null
            },
            contentDescription = null,
            colorFilter = if (userIconUrl.isEmpty()) {
                ColorFilter.tint(Color.White)
            } else {
                null
            },
        )
        Column(
            modifier = Modifier
                .padding(start = 7.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            UserNameContainer(
                modifier = Modifier.padding(bottom = 3.dp),
                userName = userName,
                date = date,
            )
            Text(
                modifier = Modifier,
                text = chatContent,
            )
        }
    }
}

@Composable
private fun UserNameContainer(modifier: Modifier, userName: String, date: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            modifier = Modifier
                .padding(end = 5.dp)
                .alignByBaseline(),
            text = userName,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.alignByBaseline(),
            text = date,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
private fun DiscordUserChatBoxPreview() {
    DiscordUserChatBox(
        modifier = Modifier,
        userName = "userName",
        date = "2021-09-01",
        chatContent = "chatContent sdadasdsadsafsafasnklfasfkja\n" +
            "asfdasfasf\n" +
            "asfasfasfas\n" +
            "asfasfsaf\n" +
            "asfasfasfasssssss\n" +
            "asfasfasf\n" +
            "asfasfasasdf",
        userIconUrl = "https://cdn.discordapp.com/avatars/1234567890/1234567890.png",
    )
}
