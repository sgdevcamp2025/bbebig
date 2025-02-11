package com.bbebig.userserver.member.dto.response;
import com.bbebig.userserver.member.entity.CustomPresenceStatus;
import com.bbebig.userserver.member.entity.Member;

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
                        member.getProfileImageUrl(),
                        member.getNickname(),
                        member.getCustomPresenceStatus(),
                        member.getCreatedAt(),
                        member.getLastAccessAt()
                );
        }
}
