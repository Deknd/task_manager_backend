package com.knd.developer.task_manager.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @InjectMocks
    private JwtTokenFilter tokenFilter;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Test
    void doFilter_ShouldCheckValidateAccessToken(){
        ServletResponse response = mock(ServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Authentication authentication = mock(Authentication.class);


        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer your_token_here");
        when(tokenProvider.validateToken("your_token_here")).thenReturn(true);
        when(tokenProvider.getAuthentication("your_token_here")).thenReturn(authentication);

        try {
            tokenFilter.doFilter(httpRequest, response, chain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        verify(tokenProvider).getAuthentication(eq("your_token_here"));
        try {
            verify(chain).doFilter(httpRequest,response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void doFilter_ShouldCheckValidateNoUserLoad(){
        ServletResponse response = mock(ServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);


        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer your_token_here");
        when(tokenProvider.validateToken("your_token_here")).thenReturn(true);
        when(tokenProvider.getAuthentication("your_token_here")).thenThrow(new UsernameNotFoundException("authentication"));

        try {
            tokenFilter.doFilter(httpRequest, response, chain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        verify(tokenProvider).getAuthentication(eq("your_token_here"));

        try {
            verify(chain).doFilter(httpRequest,response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }


    }
    @Test
    void doFilter_ShouldContinueTheFilterChain(){
        ServletResponse response = mock(ServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(httpRequest.getHeader("Authorization")).thenReturn(null);



        try {
            tokenFilter.doFilter(httpRequest, response, chain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        verify(tokenProvider, never()).getAuthentication(eq("your_token_here"));

        try {
            verify(chain).doFilter(httpRequest,response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

}