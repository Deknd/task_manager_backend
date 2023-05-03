package com.knd.developer.task_manager.web.security;

import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String bearerToken = ((HttpServletRequest) request).getHeader("Authorization");
        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
            bearerToken = bearerToken.substring(7);
        }
        if(bearerToken != null && jwtTokenProvider.validateToken(bearerToken)){
          try {
                Authentication authentication=jwtTokenProvider.getAuthentication(bearerToken);
                if(authentication!=null){

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ResourceNotFoundException ignored) {

              throw new AccessDeniedException("что то плохое в авторизации");
            }
        }
        chain.doFilter(request,response);
    }


}
