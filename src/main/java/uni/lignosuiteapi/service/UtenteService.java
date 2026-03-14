package uni.lignosuiteapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.UtenteDao;
import uni.lignosuiteapi.model.Utente;

import java.util.List;

/**
 * Service per la gestione degli utenti.
 */
@Service
public class UtenteService {

    // Il Service chiama il DAO per accedere ai dati.
    @Autowired
    private UtenteDao utenteDao;

    // Metodo per registrare un nuovo utente.
    public Utente registerUser(Utente utente) {

        // Controlla se nel database esiste già un utente con la stessa email
        if (utenteDao.findByEmail(utente.getEmail()) != null) {

            /**
             * Se l'email esiste già, viene lanciata un'eccezione HTTP.
             *
             * HttpStatus.CONFLICT (409)
             * Indica che c'è un conflitto con lo stato attuale della risorsa.
             *
             * Il messaggio verrà inviato al frontend.
             */
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email già registrata.");
        }

        // Salva l'utente nel database e restituisce l'utente appena creato.
        return utenteDao.save(utente);
    }

    // Metodo per autenticare utente (Login)
    public Utente loginUser(Utente utenteCredenziali) {

        Utente utente = utenteDao.findByEmail(utenteCredenziali.getEmail());

        /**
         * Verifica due condizioni:
         *
         * 1) L'utente non esiste
         * 2) La password inserita non corrisponde a quella salvata
         *
         * Se una delle due è vera, il login fallisce.
         */
        if (utente == null || !utente.getPassword().equals(utenteCredenziali.getPassword())) {

            /**
             * HttpStatus.UNAUTHORIZED (401)
             * Indica che le credenziali fornite non sono valide.
             */
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o password errati.");
        }

        return utente;
    }

    // Metodo per aggiornare profilo utente
    public Utente updateUser(Long id, Utente datiAggiornati) {

        Utente utenteEsistente = utenteDao.findById(id);

        // Se l'utente non esiste nel database
        if (utenteEsistente == null) {

            /**
             * HttpStatus.NOT_FOUND (404)
             * Indica che la risorsa richiesta non è stata trovata.
             */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato");
        }

        /**
         * Controllo sull'email:
         *
         * Se l'utente sta modificando la propria email,
         * bisogna verificare che non sia già utilizzata da un altro utente.
         */
        if (!utenteEsistente.getEmail().equals(datiAggiornati.getEmail())) {

            // Se l'email è già presente nel database
            if (utenteDao.findByEmail(datiAggiornati.getEmail()) != null) {

                /**
                 * HttpStatus.CONFLICT (409)
                 * Segnala che l'email è già utilizzata da un altro utente.
                 */
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Questa email è già in uso da un altro utente.");
            }
        }

        /**
         * Aggiornamento dei campi dell'utente.
         *
         * I nuovi valori ricevuti dal frontend sostituiscono quelli esistenti.
         */

        utenteEsistente.setNomeAzienda(datiAggiornati.getNomeAzienda());
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
        utenteEsistente.setEmail(datiAggiornati.getEmail());

        /**
         * Salvataggio delle modifiche nel database.
         *
         * Il metodo update() del DAO aggiorna il record esistente.
         *
         * L'utente aggiornato viene restituito al frontend.
         */
        return utenteDao.update(utenteEsistente);
    }

    // Metodo per recuperare tutti gli utenti
    public List<Utente> getAllUtenti() {
        return utenteDao.findAll();
    }
}
