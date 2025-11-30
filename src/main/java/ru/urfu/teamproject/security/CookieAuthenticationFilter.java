package ru.urfu.teamproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.urfu.teamproject.service.UserService;


import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Arrays.stream(cookies)
                        .filter(c -> "user_id".equals(c.getName()))
                        .findFirst()
                        .ifPresent(cookie -> {
                            try {
                                Long userId = Long.valueOf(cookie.getValue());
                                userService.findById(userId).ifPresent(user -> {
                                    UsernamePasswordAuthenticationToken auth =
                                            new UsernamePasswordAuthenticationToken(
                                                    user.getLogin(), null, null);
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                });
                            } catch (NumberFormatException ignored) {
                            }
                        });
            }
        }

        filterChain.doFilter(request, response);
    }
}