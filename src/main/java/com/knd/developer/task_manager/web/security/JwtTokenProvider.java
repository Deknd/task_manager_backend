package com.knd.developer.task_manager.web.security;


import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.tokens.Tokens;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.TokensService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;
    private final TokensService tokenService;
    private final JwtEntityFactory entityFactory;
    private final TaskMapper taskMapper;



    /**
     * Создает Ацесс токен
     *
     * @param userId   - юзер айди
     * @param username - email(username) пользователя
     * @param roles    - роли пользователя
     * @param validity - время действия токена
     * @return - возвращает токен в виде строки
     */
    public String createAccessToken(@NotNull Long userId, @NotNull String username, @NotNull Set<Role> roles, @NotNull Instant validity) {
        Optional<Tokens> optionalToken = tokenService.getTokenById(userId.toString());
        Tokens token;
        if (optionalToken.isPresent()) {
            token=optionalToken.get();
        } else throw new AccessDeniedException();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);
        claims.put("roles", resolveRoles(roles));

        token.setAccessToken(Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(token.getSecretKey())
                .compact());

        return tokenService.addToken(token).getAccessToken();
    }

    /**
     * Маппит Set<Role> в List<String>
     *
     * @param roles - роли пользователя
     * @return - возвращает лист пользователя в виде строк
     */
    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Создает Рефреш токен
     *
     * @param userId   - айди пользователя
     * @param username - email пользователя
     * @return - возвращает Рефреш токен
     */
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

        Tokens token = new Tokens();
        token.setId(userId.toString());
        token.setSecretKey(secretKey);
        token.setRefreshToken(Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(token.getSecretKey())
                .compact());


        return tokenService.addToken(token).getRefreshToken();
    }

    public UserAndTokenResponseDto refreshUserToken(String refreshToken, Instant validity) {
        Tokens token = tokenService.getTokenForByToken(refreshToken);

        if(token == null){
            throw new AccessDeniedException();
        }
        if ( !validateToken(refreshToken, token)  ) {
            throw new AccessDeniedException();
        }
        User user = userService.getById(Long.valueOf(token.getId()));
        UserAndTokenResponseDto userAndTokenResponseDTO = new UserAndTokenResponseDto();

        userAndTokenResponseDTO.setId(user.getId());
        userAndTokenResponseDTO.setName(user.getName());
        userAndTokenResponseDTO.setExpiration(validity.toString());
        userAndTokenResponseDTO.setRefreshToken(createRefreshToken(user.getId(), user.getUsername()));
        userAndTokenResponseDTO.setAccessToken(createAccessToken(user.getId(), user.getUsername(), user.getRoles(), validity));
        if(!user.getTasks().isEmpty())
        userAndTokenResponseDTO.setTasks(taskMapper.toDto(user.getTasks()));

        return userAndTokenResponseDTO;
    }

    private boolean validateToken(String token, Tokens tokenForRedis) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(tokenForRedis.getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        }catch (
                JwtException e){
            return false;
        }




    }
    public boolean validateToken(String token){
        Tokens tokens = tokenService.getTokenForByToken(token);
        if(tokens == null){
            throw new AccessDeniedException();
        }
        return validateToken(token, tokens);
    }


//    private String getId(String token) {
//        Tokens tokens = tokenService.getTokenForByToken(token);
//        SecretKey key;
//        if (tokens != null) {
//            key = tokens.getSecretKey();
//            return Jwts
//                    .parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .get("id")
//                    .toString();
//        }
//        return null;
//    }

//    private String getUsername(String token) {
//        Tokens tokens = tokenService.getTokenForByToken(token);
//        SecretKey key;
//        if (tokens != null) {
//            key = tokens.getSecretKey();
//            return Jwts
//                    .parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
//        }
//        return null;
//    }

    public Authentication getAuthentication(String token) {
        Tokens tokens = tokenService.getTokenForByToken(token);
        if(token.equals(tokens.getAccessToken())){
            User user = userService.getById(Long.valueOf(tokens.getId()));
            UserDetails userDetails = entityFactory.create(user);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        return null;
    }

//    public void logoutUser(Long id) {
//
//    }
//    public void logoutUser(String refreshToken){
//        Tokens tokens = tokenService.getTokenForByToken(refreshToken);
//        userService.logout(Long.valueOf(tokens.getId()));
//    }


}
