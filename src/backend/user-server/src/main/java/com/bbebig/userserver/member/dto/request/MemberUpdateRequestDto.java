package com.bbebig.userserver.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberUpdateRequestDto {

    @Schema(description = "멤버의 이름", example = "이소은", required = true)
    private final String name;

    @Schema(description = "멤버의 닉네임", example = "백엔드러버", required = true)
    private final String nickname;

    @Schema(description = "멤버의 생년월일", example = "", required = true)
    private final LocalDate birthdate;

    @Schema(description = "아바타 이미지 URL", example = "https://avatars.githubusercontent.com/u/77490504?v=4", required = true)
    private final String avatarUrl;

    @Schema(description = "배너 이미지 URL", example = "https://avatars.githubusercontent.com/u/77490504?v=4", required = true)
    private final String bannerUrl;

    @Schema(description = "멤버의 소개", example = "백엔드 개발자입니다.", required = true)
    private final String introduce;

}