package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.services.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmtpMailService implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendCredentials(String toEmail, String username, String rawPassword) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("eBank - Vos identifiants de connexion");
        msg.setText(
                "Bonjour,\n\n" +
                        "Votre compte eBank a été créé.\n\n" +
                        "Username : " + username + "\n" +
                        "Mot de passe : " + rawPassword + "\n\n" +
                        "Merci."
        );

        mailSender.send(msg);
    }
    @Override
    public void sendPasswordReset(String toEmail, String resetLink) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("eBank - Réinitialisation du mot de passe");
        msg.setText(
                "Bonjour,\n\n" +
                        "Vous avez demandé la réinitialisation de votre mot de passe.\n" +
                        "Cliquez sur ce lien :\n" + resetLink + "\n\n" +
                        "Ce lien expire dans 15 minutes.\n\n" +
                        "Si ce n'est pas vous, ignorez cet email."
        );
        mailSender.send(msg);
    }

}
