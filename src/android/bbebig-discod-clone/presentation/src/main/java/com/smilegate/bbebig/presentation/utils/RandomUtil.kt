package com.smilegate.bbebig.presentation.utils

import androidx.compose.ui.graphics.Color
import java.security.SecureRandom

object RandomUtil {
    private val userMap = mutableMapOf<Long, Int>()

    fun generate(userId: Long): Int {
        val secureRandom = SecureRandom()
        return if (userMap.containsKey(userId)) {
            userMap[userId]!!
        } else {
            val random = secureRandom.nextInt(20)
            userMap[userId] = random
            random
        }
    }

    fun getRandomColor(number: Int): Color {
        return when (number) {
            0 -> Color(0xFF00FF7F)
            1 -> Color(0xFFFF5733)
            2 -> Color(0xFF33A1FF)
            3 -> Color(0xFFFFD700)
            4 -> Color(0xFF8A2BE2)
            5 -> Color(0xFF00BFFF)
            6 -> Color(0xFFFF4500)
            7 -> Color(0xFF32CD32)
            8 -> Color(0xFFDC143C)
            9 -> Color(0xFF4682B4)
            10 -> Color(0xFFFF8C00)
            11 -> Color(0xFF00FA9A)
            12 -> Color(0xFFDA70D6)
            13 -> Color(0xFF20B2AA)
            14 -> Color(0xFFADFF2F)
            15 -> Color(0xFF9932CC)
            16 -> Color(0xFF40E0D0)
            17 -> Color(0xFFFF69B4)
            18 -> Color(0xFF7B68EE)
            19 -> Color(0xFFB22222)
            20 -> Color(0xFF008080)
            else -> Color.Black // 기본 색상 (예외 처리)
        }
    }
}
