package uni.lignosuiteapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Recupera l'email globale di sistema dal file application.properties
    @Value("${spring.mail.username}")
    private String emailDiSistema;

    public void inviaPreventivoConAllegato(String destinatario, String oggetto, String testo, MultipartFile allegatoPdf, String nomeArtigiano, String emailArtigiano) throws MessagingException, UnsupportedEncodingException {
        MimeMessage messaggio = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messaggio, true);

        helper.setTo(destinatario);
        helper.setSubject(oggetto);
        helper.setText(testo);

        // 1. ALIAS: Il cliente vede arrivare l'email da "Falegnameria Rossi <tuamail@gmail.com>"
        helper.setFrom(emailDiSistema, nomeArtigiano);

        // 2. REPLY-TO: Se il cliente clicca "Rispondi", la mail va all'artigiano!
        helper.setReplyTo(emailArtigiano, nomeArtigiano);

        // Aggiunge il PDF come allegato
        helper.addAttachment(allegatoPdf.getOriginalFilename(), allegatoPdf);

        mailSender.send(messaggio);
    }
}
