package uni.lignosuiteapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uni.lignosuiteapi.service.EmailService;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // versione senza form di Angular per modificare e visualizzare la mail prima dell'invio
    //    @PostMapping("/invia-preventivo")
//    public ResponseEntity<String> inviaPreventivoEmail(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("destinatario") String destinatario,
//            @RequestParam("nomeCliente") String nomeCliente) {
//        try {
//            String oggetto = "Preventivo LignoSuite - " + nomeCliente;
//            String testo = "Gentile " + nomeCliente + ",\n\nIn allegato trova il preventivo richiesto.\n\nCordiali saluti.";
//
//            emailService.inviaPreventivoConAllegato(destinatario, oggetto, testo, file);
//            return ResponseEntity.ok("{\"messaggio\": \"Email inviata con successo\"}");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"errore\": \"Impossibile inviare l'email\"}");
//        }
//    }

//    // versione con form di Angular per modificare e visualizzare la mail prima dell'invio'
//    @PostMapping("/invia-preventivo")
//    public ResponseEntity<String> inviaPreventivoEmail(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("destinatario") String destinatario,
//            @RequestParam("oggetto") String oggetto,
//            @RequestParam("testo") String testo) {
//        try {
//            // Ora usiamo i dati presi direttamente dal form di Angular!
//            emailService.inviaPreventivoConAllegato(destinatario, oggetto, testo, file);
//            return ResponseEntity.ok("{\"messaggio\": \"Email inviata con successo\"}");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"errore\": \"Impossibile inviare l'email\"}");
//        }
//    }


    // versione per applicare la metodologia di replay-to
    @PostMapping("/invia-preventivo")
    public ResponseEntity<String> inviaPreventivoEmail(
            @RequestParam("file") MultipartFile file,
            @RequestParam("destinatario") String destinatario,
            @RequestParam("oggetto") String oggetto,
            @RequestParam("testo") String testo,
            @RequestParam("nomeMittente") String nomeMittente, // Aggiunto
            @RequestParam("emailMittente") String emailMittente) { // Aggiunto
        try {
            emailService.inviaPreventivoConAllegato(destinatario, oggetto, testo, file, nomeMittente, emailMittente);
            return ResponseEntity.ok("{\"messaggio\": \"Email inviata con successo\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"errore\": \"Impossibile inviare l'email\"}");
        }
    }

}
