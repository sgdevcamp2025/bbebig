package com.bbebig.user_server.presentation.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PageRequestDto(Integer page, Integer size) {
    public Pageable toPageRequest() {
        return PageRequest.of(page, size);
    }
}
