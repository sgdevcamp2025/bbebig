package com.bbebig.user_server.presentation.dto;
import com.bbebig.user_server.domain.CustomPresenceStatus;
import com.bbebig.user_server.domain.User;
import java.time.LocalDateTime;

public record UserSearchResponse(
         Long id,
         String name,
         String email,
         String profileImgUrl,
         String nickname,
         CustomPresenceStatus customPresenceStatus,
         LocalDateTime createdAt,
         LocalDateTime lastAccessAt
) {
        public static UserSearchResponse of(User user) {
                return new UserSearchResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getProfileImgUrl(),
                        user.getNickname(),
                        user.getCustomPresenceStatus(),
                        user.getCreatedAt(),
                        user.getLastAccessAt()
                );
        }
}
