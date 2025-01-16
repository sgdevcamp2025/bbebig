package com.bbebig.user_server.presentation.dto;

public record UserModifyRequest(
        String profileImgUrl,
        String nickname
) {
}
