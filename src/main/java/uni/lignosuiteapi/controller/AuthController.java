package uni.lignosuiteapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.model.Utente;
import uni.lignosuiteapi.repository.UtenteRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UtenteRepository utenteRepository;

    @PostMapping("/register")
    public Utente register(@RequestBody Utente utente) {
        if (utenteRepository.existsByEmail(utente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già registrata.");
        }
        return utenteRepository.save(utente);
    }

    @PostMapping("/login")
    public Utente login(@RequestBody Utente credenziali) {
        Utente utente = utenteRepository.findByEmailAndPassword(credenziali.getEmail(), credenziali.getPassword());
        if (utente == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o password errati.");
        }
        return utente; // Restituisce i dati dell'utente se il login ha successo
    }

    @PutMapping("/update/{id}")
    public Utente updateProfilo(@PathVariable Long id, @RequestBody Utente datiAggiornati) {
        // Cerca l'utente nel DB
        Utente utenteEsistente = utenteRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato")
        );

        // Aggiorna solo i campi del profilo aziendale (evitiamo di toccare la password qui per sicurezza)
        utenteEsistente.setNomeAzienda(datiAggiornati.getNomeAzienda());
        // Se l'utente cambia il nome azienda, aggiorniamo anche il nome generico usato nel login
        utenteEsistente.setNome(datiAggiornati.getNomeAzienda());

        utenteEsistente.setNomeTitolare(datiAggiornati.getNomeTitolare());
        utenteEsistente.setCognomeTitolare(datiAggiornati.getCognomeTitolare());
        utenteEsistente.setTelefono(datiAggiornati.getTelefono());
        utenteEsistente.setPartitaIva(datiAggiornati.getPartitaIva());
        utenteEsistente.setCodiceFiscale(datiAggiornati.getCodiceFiscale());
        utenteEsistente.setIndirizzo(datiAggiornati.getIndirizzo());
        utenteEsistente.setCitta(datiAggiornati.getCitta());
        utenteEsistente.setCap(datiAggiornati.getCap());
        utenteEsistente.setProvincia(datiAggiornati.getProvincia());
        utenteEsistente.setLogoBase64(datiAggiornati.getLogoBase64());
        utenteEsistente.setEmail(datiAggiornati.getEmail()); // Permettiamo di aggiornare l'email di contatto

        // Salva e restituisce l'utente aggiornato
        return utenteRepository.save(utenteEsistente);
    }
}
