package com.dawm.sonara.services;

public interface PasswordResetService {
    void requestPasswordReset(String email, String requestIp, String userAgent);
    void resetPassword(String rawToken, String newPassword);
}
