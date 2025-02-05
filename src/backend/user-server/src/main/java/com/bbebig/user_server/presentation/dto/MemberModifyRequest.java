package com.bbebig.user_server.presentation.dto;

public record MemberModifyRequest(
        String profileImgUrl,
        String nickname
) {
}
