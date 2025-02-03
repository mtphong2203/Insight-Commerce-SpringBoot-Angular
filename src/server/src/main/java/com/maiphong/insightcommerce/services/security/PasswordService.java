package com.maiphong.insightcommerce.services.security;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.insightcommerce.entities.security.PasswordResetToken;
import com.maiphong.insightcommerce.repositories.security.IPasswordResetTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@Transactional
public class PasswordService implements IPasswordService {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    private static final SecureRandom random = new SecureRandom();
    private final PasswordEncoder passwordEncoder;
    private final IPasswordResetTokenRepository tokenRepository;

    @Value("${app.security.access-token-secret-key}")
    private String secretKey;

    @Value("${app.security.reset-password-token-expired-in-second}")
    private int expireTime;

    public PasswordService(PasswordEncoder passwordEncoder, IPasswordResetTokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String generatePassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters.");
        }

        StringBuilder password = new StringBuilder();

        // Ensure password contains at least one of each required character type
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the rest of the password with random characters
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Shuffle the password for better randomness
        return shuffleString(password.toString());
    }

    /**
     * Helper method
     * 
     * @param input
     * @return
     */
    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    @Override
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public String generatePasswordResetToken(String email) {
        String jti = UUID.randomUUID().toString(); // Unique token ID
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expireTime);
        Date expiriation = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        String jwt = Jwts.builder()
                .subject(email)
                .id(jti)
                .expiration(expiriation)
                .signWith(key)
                .compact();

        // Hash the `jti`
        String hashedTokenId = passwordEncoder.encode(jti);

        // Save token details to database
        PasswordResetToken token = new PasswordResetToken(
                hashedTokenId,
                email,
                ZonedDateTime.now().plusMinutes(15));

        tokenRepository.save(token);

        return jwt; // Return the token
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        boolean isValid = true;
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        // Parse the JWT
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String jti = claims.getId(); // Extract `jti`

        // Check database for hashed token
        PasswordResetToken resetToken = tokenRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new IllegalArgumentException("Token not found."));

        // Validate hashed token
        if (!passwordEncoder.matches(jti, resetToken.getHashedTokenId())) {
            throw new IllegalArgumentException("Invalid token.");
        }

        // Validate expiration
        if (resetToken.getExpiration().isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Token expired.");
        }

        return isValid;
    }

    @Override
    public String getEmailFromPasswordResetToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        // Parse the JWT
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean deletePasswordResetToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        // Parse the JWT
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Check database for hashed token
        PasswordResetToken resetToken = tokenRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new IllegalArgumentException("Token not found."));

        tokenRepository.deleteByHashedTokenId(resetToken.getHashedTokenId());

        var result = tokenRepository.existsById(resetToken.getId());

        return !result;
    }

    @Override
    public boolean matchHashedPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

}
