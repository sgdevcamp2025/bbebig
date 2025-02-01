package com.bbebig.chatserver.domain.chat.client;

import com.bbebig.chatserver.domain.chat.dto.response.AuthResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-server") // Eureka에 등록된 Node.js 서버의 서비스 이름
public interface AuthClient {

	@PostMapping("/auth-server/verify-token")
	AuthResponseDto verifyToken(@RequestHeader("Authorization") String token);
}