package org.ziegelbauer.homepage.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.ziegelbauer.homepage.models.authentication.ExternalOAuth2User;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExternalOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        ExternalOAuth2User oAuthUser = (ExternalOAuth2User) authentication.getPrincipal();

        var loggedInUser = userService.processOAuth2PostLogin(oAuthUser);

        response.sendRedirect("/");
    }
}
