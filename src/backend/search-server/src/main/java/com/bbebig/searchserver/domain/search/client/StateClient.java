package com.bbebig.searchserver.domain.search.client;

import com.bbebig.commonmodule.clientDto.state.CommonStateClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "state-server")
public interface StateClient {

	@PostMapping("/cache/member/{memberId}/server")
	CommonStateClientResponseDto.MemberServerListCacheResponseDto cacheMemberServerList(@PathVariable Long memberId);
}
