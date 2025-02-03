package com.maiphong.insightcommerce.services.security;

public interface IPasswordService {

    String generatePassword(int length);

    String hashPassword(String plainPassword);

    String generatePasswordResetToken(String email);

    boolean validatePasswordResetToken(String token);

    String getEmailFromPasswordResetToken(String token);

    boolean deletePasswordResetToken(String token);

    boolean matchHashedPassword(String plainPassword, String hashedPassword);
}
