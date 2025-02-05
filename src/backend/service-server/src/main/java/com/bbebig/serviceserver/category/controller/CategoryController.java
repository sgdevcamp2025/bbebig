package com.bbebig.serviceserver.category.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.serviceserver.category.dto.request.CategoryCreateRequestDto;
import com.bbebig.serviceserver.category.dto.request.CategoryUpdateRequestDto;
import com.bbebig.serviceserver.category.dto.response.CategoryCreateResponseDto;
import com.bbebig.serviceserver.category.dto.response.CategoryDeleteResponseDto;
import com.bbebig.serviceserver.category.dto.response.CategoryReadResponseDto;
import com.bbebig.serviceserver.category.dto.response.CategoryUpdateResponseDto;
import com.bbebig.serviceserver.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
@Tag(name = "카테고리", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성 (서버장만 가능)", description = "카테고리를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("")
    public CommonResponse<CategoryCreateResponseDto> createCategory(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @RequestBody CategoryCreateRequestDto categoryCreateRequestDto
    ) {
        log.info("[Service] 카테고리 생성 요청: memberId = {}, serverId = {}", passport.getMemberId(), categoryCreateRequestDto.getServerId());
        return CommonResponse.onSuccess(categoryService.createCategory(passport.getMemberId(), categoryCreateRequestDto));
    }

    @Operation(summary = "카테고리 업데이트 (서버장만 가능)", description = "카테고리를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PutMapping("/{categoryId}")
    public CommonResponse<CategoryUpdateResponseDto> updateCategory(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long categoryId,
            @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto
    ) {
        log.info("[Service] 카테고리 업데이트 요청: memberId = {}, categoryId = {}", passport.getMemberId(), categoryId);
        return CommonResponse.onSuccess(categoryService.updateCategory(passport.getMemberId(), categoryId, categoryUpdateRequestDto));
    }

    @Operation(summary = "카테고리 삭제 (서버장만 가능)", description = "카테고리를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{categoryId}")
    public CommonResponse<CategoryDeleteResponseDto> deleteCategory(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long categoryId
    ) {
        log.info("[Service] 카테고리 삭제 요청: memberId = {}, categoryId = {}", passport.getMemberId(), categoryId);
        return CommonResponse.onSuccess(categoryService.deleteCategory(passport.getMemberId(), categoryId));
    }

    @Operation(summary = "카테고리 정보 조회", description = "카테고리 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{categoryId}")
    public CommonResponse<CategoryReadResponseDto> readCategory(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long categoryId
    ) {
        log.info("[Service] 카테고리 정보 조회 요청: memberId = {}, categoryId = {}", passport.getMemberId(), categoryId);
        return CommonResponse.onSuccess(categoryService.readCategory(categoryId));
    }
}
