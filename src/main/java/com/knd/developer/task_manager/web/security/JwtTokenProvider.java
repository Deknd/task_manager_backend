package com.knd.developer.task_manager.web.security;


import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.RefreshTokenService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.ResponseAuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final RefreshTokenService tokenService;



    public String createAccessToken(Long userId, String username, Set<Role> roles, Instant validity) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));
        SecretKey key = tokenService.getTokenById(userId.toString()).get().getSecretKey();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public String createRefreshToken(Long userId, String username) {

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKey secretKey = keyGenerator.generateKey();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);

        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.HOURS);

        RefreshToken token = new RefreshToken();
        token.setId(userId.toString());
        token.setSecretKey(secretKey);
        token.setRefreshToken(Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(token.getSecretKey())
                .compact());

        RefreshToken result = tokenService.addToken(token);

        return result.getRefreshToken();
    }

    public ResponseAuthUser refreshUserToken(String refreshToken, Instant validity) {
        ResponseAuthUser responseAuthUser = new ResponseAuthUser();
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        if (!validateRefreshToken(refreshToken, userId)) {
            throw new AccessDeniedException();
        }
        responseAuthUser.setId(userId);
        responseAuthUser.setName(user.getName());
        responseAuthUser.setExpiration(validity.toString());
        String newRefresh = createRefreshToken(userId, user.getUsername());
        responseAuthUser.setRefreshToken(newRefresh);
        responseAuthUser.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles(), validity));

        return responseAuthUser;
    }

    public boolean validateToken(String token) {
        Long userId;
        try {
            userId = Long.valueOf(getId(token));
        } catch (SignatureException e) {

            return false;
        }


        Optional<RefreshToken> tokenForRedis = tokenService.getTokenById(userId.toString());
        if (tokenForRedis.isEmpty()) return false;


        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(tokenForRedis.get().getSecretKey())
                .build()
                .parseClaimsJws(token);


        return claims.getBody().getExpiration().after(new Date());
    }

    public boolean validateRefreshToken(String token, Long id) {

        Optional<RefreshToken> refreshToken = tokenService.getTokenById(id.toString());

        if (!refreshToken.isEmpty()) {
           return token.equals(refreshToken.get().getRefreshToken());

        } else return false;
    }

    private String getId(String token) {
        RefreshToken refreshToken = tokenService.getTokenForByToken(token);
        SecretKey key;
        if (refreshToken != null) {
            key = refreshToken.getSecretKey();
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("id")
                    .toString();
        }
        return null;
    }

    private String getUsername(String token) {
        RefreshToken refreshToken = tokenService.getTokenForByToken(token);
        SecretKey key;
        if (refreshToken != null) {
            key = refreshToken.getSecretKey();
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public void logoutUser(String refreshToken) {

        Long userId = Long.valueOf(getId(refreshToken));

        tokenService.deleteToken(userId.toString());

    }


}
