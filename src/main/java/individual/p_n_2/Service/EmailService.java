package individual.p_n_2.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetEmail(String to, String resetLink) {
        String subject = "Resetowanie hasła";
        String content = "<p>Witaj,</p>"
                + "<p>Kliknij w poniższy link, aby zresetować swoje hasło:</p>"
                + "<p><a href=\"" + resetLink + "\">Zresetuj hasło</a></p>"
                + "<br><p>Jeśli to nie Ty inicjowałeś reset, zignoruj tę wiadomość.</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Błąd podczas wysyłania e-maila", e);
        }
    }
}