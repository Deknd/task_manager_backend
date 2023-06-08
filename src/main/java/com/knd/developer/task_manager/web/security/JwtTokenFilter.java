package com.knd.developer.task_manager.web.security;

import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@AllArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * Выдает ацетификацию пользователю по ацесс токену
     * @param request - Входящий Http запрос
     * @param response - исходящий Http запрос
     * @param chain - цепочка фильтров Spring security
     * @throws IOException - Exception из метода doFilter объекта chain
     * @throws ServletException - Exception из метода doFilter объекта chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String bearerToken = ((HttpServletRequest) request).getHeader("Authorization");

        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
            bearerToken = bearerToken.substring(7); //IndexOutOfBoundsException

        }
        if(bearerToken != null && jwtTokenProvider.validateToken(bearerToken)){  //По идее нужно совершить логаут пользователя
          try {
                Authentication authentication=jwtTokenProvider.getAuthentication(bearerToken);
              if(authentication!=null){


                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (UsernameNotFoundException ignored) {

            }
        }
        chain.doFilter(request,response);
    }


}
