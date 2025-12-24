package ma.emsi.ebank.services;

public interface MailService {
    void sendCredentials(String toEmail, String username, String rawPassword);
    void sendPasswordReset(String toEmail, String resetLink);
}
