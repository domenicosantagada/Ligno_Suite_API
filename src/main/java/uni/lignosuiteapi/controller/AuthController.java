package uni.lignosuiteapi.controller;

// Import delle classi necessarie al funzionamento del controller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.lignosuiteapi.model.Utente;
import uni.lignosuiteapi.service.UtenteService;

import java.util.List;

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
    private UtenteService utenteService;

    /**
     * =========================
     * RECUPERO TUTTI GLI UTENTI
     * =========================
     * <p>
     * Endpoint per visualizzare la lista di tutti gli utenti registrati.
     *
     * @GetMapping("/all") Questo metodo risponde alle richieste HTTP GET all'URL:
     * /api/auth/all
     */
    @GetMapping("/all")
    public List<Utente> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

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

        // Il metodo registerUser() del Service si occupa di tutta la logica di registrazione,
        // e di chiamare il DAO per salvare l'utente nel database.
        return utenteService.registerUser(utente);
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
         * @RequestBody
         * Converte il JSON inviato dal frontend in un oggetto Utente.
         */

        return utenteService.loginUser(credenziali);
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

        return utenteService.updateUser(id, datiAggiornati);
    }
}
