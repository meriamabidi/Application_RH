package com.vermeg.back.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                response.sendRedirect("/adminhome");
                return;
            } else if (authority.getAuthority().equals("RH")) {
                response.sendRedirect("/rhhome");
                return;
            } else if (authority.getAuthority().equals("EMPLOYE")) {
                response.sendRedirect("/employehome");
                return;
            } else if (authority.getAuthority().equals("LEADER")) {
                response.sendRedirect("/leaderhome");
                return;
            }
        }

        response.sendRedirect("/login");
    }
}