package com.bbebig.user_server.presentation.dto;
import com.bbebig.user_server.domain.CustomPresenceStatus;
import com.bbebig.user_server.domain.Member;

import java.time.LocalDateTime;

public record MemberSearchResponse(
         Long id,
         String name,
         String email,
         String profileImgUrl,
         String nickname,
         CustomPresenceStatus customPresenceStatus,
         LocalDateTime createdAt,
         LocalDateTime lastAccessAt
) {
        public static MemberSearchResponse of(Member member) {
                return new MemberSearchResponse(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getProfileImgUrl(),
                        member.getNickname(),
                        member.getCustomPresenceStatus(),
                        member.getCreatedAt(),
                        member.getLastAccessAt()
                );
        }
}
