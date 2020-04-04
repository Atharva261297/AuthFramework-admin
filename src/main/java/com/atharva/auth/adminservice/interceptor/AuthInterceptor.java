package com.atharva.auth.adminservice.interceptor;

import com.atharva.auth.adminservice.service.AuthService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Verify key from cookies
        ErrorCodes code = verifyAuthKeyFromCookies(request.getCookies());

        // if key not valid and auth pass is provided
        String auth = request.getHeader("admin_auth");
        if ((Objects.isNull(code) || code == ErrorCodes.AUTH_KEY_NOT_VALID) && Objects.nonNull(auth)) {
            code = verifyHash(response, auth);
        }

        if (code != ErrorCodes.SUCCESS) {
            response.getWriter().write(code.toString());
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private ErrorCodes verifyHash(HttpServletResponse response, String auth) {
        ErrorCodes code;
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        code = authService.login(auth);
        if (code == ErrorCodes.SUCCESS) {
            Cookie cookieId = new Cookie("auth.userId", id);
            Cookie cookiePass = new Cookie("auth.key", authService.generateKey(id));
            response.addCookie(cookieId);
            response.addCookie(cookiePass);
        }
        return code;
    }

    public ErrorCodes verifyAuthKeyFromCookies(Cookie[] cookies) {
        String cookieUserIdValue = StringUtils.EMPTY;
        String cookieKeyValue = StringUtils.EMPTY;
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if ("auth.userId".equals(cookie.getName())) {
                    cookieUserIdValue = cookie.getValue();
                } else if ("auth.key".equals(cookie.getName())) {
                    cookieKeyValue = cookie.getValue();
                }
            }
            if (!cookieUserIdValue.isEmpty() && !cookieKeyValue.isEmpty()) {
                return authService.verify(cookieUserIdValue, cookieKeyValue);
            }
        }
        return ErrorCodes.AUTH_KEY_NOT_VALID;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
