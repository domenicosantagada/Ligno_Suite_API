package uni.lignosuiteapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void inviaPreventivoConAllegato(String destinatario, String oggetto, String testo, MultipartFile allegatoPdf) throws MessagingException {
        MimeMessage messaggio = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messaggio, true);

        helper.setTo(destinatario);
        helper.setSubject(oggetto);
        helper.setText(testo);

        // Aggiunge il PDF come allegato
        helper.addAttachment(allegatoPdf.getOriginalFilename(), allegatoPdf);

        mailSender.send(messaggio);
    }
}
