package uni.lignosuiteapi.controller;

// Import delle classi necessarie al funzionamento del controller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.PreventivoDao;
import uni.lignosuiteapi.model.Preventivo;

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
     * <p>
     * Il DAO (Data Access Object) è la classe che si occupa
     * dell'accesso al database per l'entità Preventivo.
     */
    @Autowired
    private PreventivoDao preventivoDao;

    /**
     * =========================
     * METODO DI UTILITÀ
     * =========================
     * <p>
     * Questo metodo controlla se esiste già un preventivo
     * con lo stesso numero per uno specifico utente.
     * <p>
     * Serve per evitare duplicati nel numero dei preventivi.
     *
     * @param utenteId      ID dell'utente proprietario dei preventivi
     * @param invoiceNumber numero del preventivo da verificare
     * @return true se esiste già, false se è disponibile
     */
    private boolean existsInvoiceNumber(Long utenteId, Long invoiceNumber) {

        /**
         * Recupera tutti i preventivi dell'utente
         * e utilizza uno stream per verificare se
         * almeno uno ha lo stesso numero.
         */
        return preventivoDao.findAllByUtenteId(utenteId).stream()
                .anyMatch(p -> p.getInvoiceNumber().equals(invoiceNumber));
    }

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
         * Il DAO recupera dal database tutti i preventivi
         * associati all'utente specificato.
         */
        return preventivoDao.findAllByUtenteId(utenteId);
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
    public Preventivo createPreventivo(@RequestBody Preventivo invoice) {

        /**
         * Controllo per verificare se il numero preventivo
         * è già utilizzato da un altro preventivo dello stesso utente.
         */
        if (existsInvoiceNumber(invoice.getUtenteId(), invoice.getInvoiceNumber())) {

            /**
             * HttpStatus.CONFLICT (409)
             * Indica un conflitto perché il numero preventivo è già utilizzato.
             */
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Numero preventivo già in uso.");
        }

        /**
         * Se il numero è disponibile,
         * il preventivo viene salvato nel database.
         */
        return preventivoDao.save(invoice);
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
    public Preventivo updatePreventivo(@PathVariable Long id, @RequestBody Preventivo invoice) {

        /**
         * Recupera il preventivo esistente dal database.
         */
        Preventivo preventivoEsistente = preventivoDao.findById(id);

        // Se il preventivo non esiste
        if (preventivoEsistente == null) {

            /**
             * HttpStatus.NOT_FOUND (404)
             * Il preventivo richiesto non è stato trovato.
             */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile aggiornare: preventivo non trovato.");
        }

        /**
         * Controllo di sicurezza:
         *
         * Verifica che il preventivo appartenga
         * all'utente che sta tentando di modificarlo.
         */
        if (!preventivoEsistente.getUtenteId().equals(invoice.getUtenteId())) {

            /**
             * HttpStatus.FORBIDDEN (403)
             * L'utente non ha il permesso di modificare questo preventivo.
             */
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato.");
        }

        /**
         * Se il numero del preventivo è stato modificato,
         * bisogna verificare che non sia già utilizzato
         * da un altro preventivo dello stesso utente.
         */
        if (!preventivoEsistente.getInvoiceNumber().equals(invoice.getInvoiceNumber())) {

            if (existsInvoiceNumber(invoice.getUtenteId(), invoice.getInvoiceNumber())) {

                /**
                 * HttpStatus.CONFLICT (409)
                 * Il numero preventivo è già utilizzato.
                 */
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Numero preventivo già in uso.");
            }
        }

        /**
         * Impostiamo manualmente l'id del preventivo
         * per assicurarci che venga aggiornato il record corretto.
         */
        invoice.setId(id);

        /**
         * Il DAO aggiorna il preventivo nel database.
         */
        return preventivoDao.update(invoice);
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
         * Recupera il preventivo dal database.
         */
        Preventivo preventivoEsistente = preventivoDao.findById(id);

        // Se il preventivo non esiste
        if (preventivoEsistente == null) {

            /**
             * HttpStatus.NOT_FOUND (404)
             * Il preventivo richiesto non esiste.
             */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Preventivo non trovato.");
        }

        /**
         * Controllo di sicurezza:
         *
         * Verifica che il preventivo appartenga
         * all'utente che sta tentando di eliminarlo.
         */
        if (!preventivoEsistente.getUtenteId().equals(utenteId)) {

            /**
             * HttpStatus.FORBIDDEN (403)
             * L'utente non ha i permessi per eliminare questo preventivo.
             */
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato.");
        }

        /**
         * Se tutti i controlli sono superati,
         * il preventivo viene eliminato dal database.
         */
        preventivoDao.deleteById(id, utenteId);
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
         * Il DAO calcola automaticamente
         * il valore massimo dei numeri preventivo esistenti
         * e restituisce il successivo (MAX + 1).
         */
        return preventivoDao.getNextInvoiceNumber(utenteId);
    }
}
