package com.bbebig.commonmodule.global.response.exception;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.global.response.code.error.ErrorReasonDTO;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
		String errorMessage = e.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("ConstraintViolationException 메시지 추출 중 에러 발생"));

		return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY, request);
	}

	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new LinkedHashMap<>();

		e.getBindingResult().getFieldErrors().stream()
				.forEach(fieldError -> {
					String fieldName = fieldError.getField();
					String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
					errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
				});

		return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, ErrorStatus.valueOf("_BAD_REQUEST"), request, errors);
	}

	@ExceptionHandler
	public ResponseEntity<Object> exception(Exception e, WebRequest request) {
		e.printStackTrace();

		return handleExceptionInternalFalse(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(), request, e.getMessage());
	}

	// GeneralException을 처리
	@ExceptionHandler(value = GeneralException.class)
	public ResponseEntity onThrowException(GeneralException generalException, HttpServletRequest request) {
		ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
		return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
	}

	private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDTO reason,
														   HttpHeaders headers, HttpServletRequest request) {

		CommonResponse<Object> body = CommonResponse.onFailure(reason.getCode(), reason.getMessage(), null);

		WebRequest webRequest = new ServletWebRequest(request);
		return super.handleExceptionInternal(
				e,
				body,
				headers,
				reason.getHttpStatus(),
				webRequest
		);
	}

	// 공통 에러 처리 메서드 (ErrorStatus 사용)
	private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonStatus,
																HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {

		CommonResponse<Object> body = CommonResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorPoint);
		return super.handleExceptionInternal(
				e,
				body,
				headers,
				status,
				request
		);
	}

	@ExceptionHandler(CallNotPermittedException.class)
	public ResponseEntity<Object> handleCallNotPermittedException(CallNotPermittedException e, HttpServletRequest request) {
		log.warn("[ExceptionAdvice] CircuitBreaker is OPEN. Blocking call. Msg: {}", e.getMessage());

		return handleExceptionInternalFalse(
				e,
				ErrorStatus.CIRCUIT_OPEN_ERROR,
				HttpHeaders.EMPTY,
				ErrorStatus.CIRCUIT_OPEN_ERROR.getHttpStatus(), // 보통 503
				new ServletWebRequest(request),
				e.getMessage()
		);
	}

	private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorStatus errorCommonStatus,
															   WebRequest request, Map<String, String> errorArgs) {

		CommonResponse<Object> body = CommonResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorArgs);
		return super.handleExceptionInternal(
				e,
				body,
				headers,
				errorCommonStatus.getHttpStatus(),
				request
		);
	}

	private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonStatus,
																	 HttpHeaders headers, WebRequest request) {

		CommonResponse<Object> body = CommonResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
		return super.handleExceptionInternal(
				e,
				body,
				headers,
				errorCommonStatus.getHttpStatus(),
				request
		);
	}
}
