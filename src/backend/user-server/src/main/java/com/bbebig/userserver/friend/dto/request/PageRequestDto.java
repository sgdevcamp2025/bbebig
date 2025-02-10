package com.bbebig.userserver.friend.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PageRequestDto(Integer page, Integer size) {
    public Pageable toPageRequest() {
        return PageRequest.of(page, size);
    }
}
