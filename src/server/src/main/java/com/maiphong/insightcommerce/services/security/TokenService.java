package com.maiphong.insightcommerce.services.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maiphong.insightcommerce.dtos.security.user.UserInformationDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService implements ITokenService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.security.access-token-secret-key}")
    private String secretKey;

    @Value("${app.security.access-token-expired-in-second}")
    private Integer expireTime;

    @Override
    public String generateToken(Authentication authentication, UserInformationDTO userInformationDTO) {
        String roles = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(","));
        return generateAccessToken(userInformationDTO, roles);
    }

    private String generateAccessToken(UserInformationDTO userInformationDTO, String roles) {
        // Now + 3600s from application.properties => expiredAt = Now + 1h
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expireTime);

        // Decode secretKey from Base64 to SecretKey
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        // Convert LocalDateTime to Date
        Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        try {
            // Serialize using objectMapper
            String userInfoJson = objectMapper.writeValueAsString(userInformationDTO);
            return Jwts.builder()
                    .subject(userInformationDTO.getUsername())
                    .claim("userInformation", userInfoJson)
                    .claim("roles", roles)
                    .expiration(expiration)
                    .signWith(key)
                    .compact();
        } catch (JsonProcessingException e) {
            // Handle exception
            return null;
        }
    }

    @Override
    public Authentication getAuthentication(String jwtToken) {
        if (jwtToken == null) {
            return null;
        }

        // Decode secretKey from Base64 to SecretKey
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            String roles = claims.get("roles").toString();

            Set<GrantedAuthority> authorities = Set.of(roles.split(",")).stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

            User principle = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principle, jwtToken, authorities);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserInformationDTO getUserInformation(String jwtToken) {
        if (jwtToken == null) {
            return null;
        }
        // Decode secretKey from Base64 to SecretKey
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            String userInfoJson = claims.get("userInformation").toString();
            // Deserialize using objectMapper
            return objectMapper.readValue(userInfoJson, UserInformationDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

}
