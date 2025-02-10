package com.bbebig.userserver.member.dto.request;

public record MemberModifyRequest(
        String profileImgUrl,
        String nickname
) {
}
