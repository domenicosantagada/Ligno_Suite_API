package uni.lignosuiteapi.controller;

// Import delle classi necessarie al funzionamento del controller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.UtenteDao;
import uni.lignosuiteapi.model.Utente;

/**
 * Controller per la gestione dell'autenticazione (Login, Registrazione) e del profilo utente.
 *
 * @RestController Indica a Spring Boot che questa classe è un controller REST.
 * Ogni metodo restituirà direttamente i dati nel corpo della risposta (solitamente in formato JSON),
 * senza dover renderizzare una vista HTML.
 * @RequestMapping("/api/auth") Definisce il prefisso URL per tutte le rotte di questo controller.
 * Tutti gli endpoint saranno quindi accessibili tramite /api/auth/...
 * @CrossOrigin Permette le richieste Cross-Origin (CORS).
 * Poiché il frontend Angular gira su http://localhost:4200 e il backend su un'altra porta,
 * il browser bloccherebbe le richieste per motivi di sicurezza.
 * Questa annotazione permette al frontend di comunicare con il backend.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    /**
     * @Autowired Permette a Spring di iniettare automaticamente l'oggetto UtenteDao.
     * In questo modo possiamo usare il DAO per accedere al database
     * senza dover creare manualmente un'istanza della classe.
     */
    @Autowired
    private UtenteDao utenteDao;

    /**
     * =========================
     * REGISTRAZIONE UTENTE
     * =========================
     * <p>
     * Endpoint per registrare un nuovo utente nel sistema.
     *
     * @PostMapping("/register") Questo metodo risponde alle richieste HTTP POST all'URL:
     * /api/auth/register
     * <p>
     * POST viene utilizzato quando si vuole creare una nuova risorsa.
     * @RequestBody Indica che il corpo della richiesta HTTP (JSON inviato dal frontend)
     * deve essere convertito automaticamente in un oggetto Java di tipo Utente.
     */
    @PostMapping("/register")
    public Utente register(@RequestBody Utente utente) {

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

        /**
         * Se l'email non esiste, il nuovo utente viene salvato nel database.
         * Il metodo save() del DAO inserisce il record e restituisce l'utente salvato.
         */
        return utenteDao.save(utente);
    }

    /**
     * =========================
     * LOGIN UTENTE
     * =========================
     * <p>
     * Endpoint per autenticare un utente già registrato.
     *
     * @PostMapping("/login") Questo metodo gestisce richieste POST all'URL:
     * /api/auth/login
     * <p>
     * Il frontend invia email e password nel corpo della richiesta.
     */
    @PostMapping("/login")
    public Utente login(@RequestBody Utente credenziali) {

        /**
         * Cerca nel database un utente con l'email fornita.
         */
        Utente utente = utenteDao.findByEmail(credenziali.getEmail());

        /**
         * Verifica due condizioni:
         *
         * 1) L'utente non esiste
         * 2) La password inserita non corrisponde a quella salvata
         *
         * Se una delle due è vera, il login fallisce.
         */
        if (utente == null || !utente.getPassword().equals(credenziali.getPassword())) {

            /**
             * HttpStatus.UNAUTHORIZED (401)
             * Indica che le credenziali fornite non sono valide.
             */
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o password errati.");
        }

        /**
         * Se email e password sono corrette,
         * restituiamo l'utente al frontend.
         */
        return utente;
    }

    /**
     * =========================
     * AGGIORNAMENTO PROFILO UTENTE
     * =========================
     * <p>
     * Endpoint per aggiornare i dati del profilo utente.
     *
     * @PutMapping("/update/{id}") Questo metodo risponde alle richieste HTTP PUT all'URL:
     * /api/auth/update/{id}
     * <p>
     * PUT viene usato per aggiornare una risorsa esistente.
     * <p>
     * {id} rappresenta l'identificativo dell'utente da aggiornare.
     */
    @PutMapping("/update/{id}")
    public Utente updateProfilo(@PathVariable Long id, @RequestBody Utente datiAggiornati) {

        /**
         * @PathVariable
         * Permette di prendere il valore dell'id direttamente dall'URL.
         *
         * @RequestBody
         * Converte il JSON inviato dal frontend nei nuovi dati dell'utente.
         */

        // Recupera dal database l'utente esistente con quell'id
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
}
