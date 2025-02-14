package com.bbebig.commonmodule.passport.resolver;

import com.bbebig.commonmodule.passport.PassportValidator;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class PassportArgumentResolver implements HandlerMethodArgumentResolver {

    private final PassportValidator passportValidator;

    @Value("${eas.passport.header}")
    private String passportHeader;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PassportUser.class)
                && parameter.getParameterType().equals(Passport.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String base64Passport = request.getHeader(passportHeader);
        log.info("[PassportArgumentResolver] base64Passport header: {} (URI={})", base64Passport, request.getRequestURI());
        if (base64Passport == null) {
            throw new ErrorHandler(ErrorStatus.PASSPORT_HEADER_MISSING);
        }

        try {
            return passportValidator.validatePassport(base64Passport);
        } catch (Exception e) {
            throw new ErrorHandler(ErrorStatus.PASSPORT_INVALID_OR_TAMPERED);
        }
    }
}
