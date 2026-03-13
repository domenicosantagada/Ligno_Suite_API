package uni.lignosuiteapi.controller;

// Import delle classi necessarie al funzionamento del controller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.service.PreventivoService;

import java.util.List;

/**
 * Controller REST per la gestione dei preventivi.
 * <p>
 * Questo controller espone le API che permettono al frontend
 * di eseguire operazioni CRUD sui preventivi:
 * <p>
 * - Recuperare tutti i preventivi
 * - Creare un nuovo preventivo
 * - Aggiornare un preventivo
 * - Eliminare un preventivo
 * - Ottenere il prossimo numero preventivo disponibile
 *
 * @RestController Indica a Spring Boot che questa classe è un controller REST.
 * I metodi restituiscono direttamente dati JSON come risposta HTTP.
 * @RequestMapping("/api/preventivi") Definisce il prefisso di tutte le rotte di questo controller.
 * Tutti gli endpoint saranno accessibili tramite /api/preventivi.
 * @CrossOrigin Permette al frontend Angular (che gira su localhost:4200)
 * di effettuare richieste HTTP verso questo backend.
 */
@RestController
@RequestMapping("/api/preventivi")
@CrossOrigin(origins = "http://localhost:4200")
public class PreventiviController {

    /**
     * @Autowired Permette a Spring di iniettare automaticamente l'oggetto PreventivoDao.
     * In questo modo possiamo usare il DAO per accedere al database
     */
    @Autowired
    private PreventivoService preventivoService;

    /**
     * =========================
     * OTTENERE TUTTI I PREVENTIVI
     * =========================
     * <p>
     * Endpoint per recuperare tutti i preventivi
     * appartenenti ad uno specifico utente.
     *
     * @GetMapping Gestisce richieste HTTP GET all'URL:
     * /api/preventivi
     * @RequestParam Permette di leggere un parametro dalla query dell'URL.
     * <p>
     * Esempio:
     * GET /api/preventivi?utenteId=1
     */
    @GetMapping
    public List<Preventivo> getAllPreventivi(@RequestParam Long utenteId) {

        /**
         * Il Service chiama il DAO per recuperare i preventivi dal database.
         */
        return preventivoService.getAllPreventivi(utenteId);
    }

    /**
     * =========================
     * CREARE UN NUOVO PREVENTIVO
     * =========================
     * <p>
     * Endpoint per creare un nuovo preventivo.
     *
     * @PostMapping Gestisce richieste HTTP POST all'URL:
     * /api/preventivi
     * @RequestBody Il JSON inviato dal frontend viene convertito
     * automaticamente in un oggetto Java Preventivo.
     */
    @PostMapping
    public Preventivo createPreventivo(@RequestBody Preventivo preventivo) {

        /**
         * Il Service chiama il DAO per salvare il nuovo preventivo nel database.
         */
        return preventivoService.createPreventivo(preventivo);
    }

    /**
     * =========================
     * AGGIORNARE UN PREVENTIVO
     * =========================
     * <p>
     * Endpoint per aggiornare un preventivo esistente.
     *
     * @PutMapping("/{id}") Gestisce richieste HTTP PUT all'URL:
     * /api/preventivi/{id}
     * <p>
     * {id} rappresenta l'identificativo del preventivo.
     */
    @PutMapping("/{id}")
    public Preventivo updatePreventivo(@PathVariable Long id, @RequestBody Preventivo preventivo) {

        /**
         * Il Service chiama il DAO per aggiornare il preventivo nel database.
         */
        return preventivoService.updatePreventivo(id, preventivo);
    }

    /**
     * =========================
     * ELIMINARE UN PREVENTIVO
     * =========================
     * <p>
     * Endpoint per eliminare un preventivo dal database.
     *
     * @DeleteMapping("/{id}") Gestisce richieste HTTP DELETE all'URL:
     * /api/preventivi/{id}
     */
    @DeleteMapping("/{id}")
    public void deletePreventivo(@PathVariable Long id, @RequestParam Long utenteId) {

        /**
         * Il Service chiama il DAO per eliminare il preventivo dal database.
         */
        preventivoService.deletePreventivo(id, utenteId);
    }

    /**
     * =========================
     * OTTENERE IL PROSSIMO NUMERO PREVENTIVO
     * =========================
     * <p>
     * Endpoint che restituisce il prossimo numero
     * di preventivo disponibile per un utente.
     *
     * @GetMapping("/next-number") Esempio chiamata:
     * GET /api/preventivi/next-number?utenteId=1
     */
    @GetMapping("/next-number")
    public Long getNextInvoiceNumber(@RequestParam Long utenteId) {
        /**
         * Il Service chiama il DAO per ottenere il prossimo numero preventivo.
         */
        return preventivoService.getNextInvoiceNumber(utenteId);
    }
}
