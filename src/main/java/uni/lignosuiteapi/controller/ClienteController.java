package uni.lignosuiteapi.controller;

// Import delle classi necessarie per il controller REST

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.ClienteDao;
import uni.lignosuiteapi.model.Cliente;

import java.util.List;

/**
 * Controller REST per la gestione dei clienti.
 * <p>
 * Questo controller espone gli endpoint API che permettono al frontend
 * (Angular) di effettuare operazioni CRUD sui clienti:
 * <p>
 * - Recuperare la lista dei clienti
 * - Creare un nuovo cliente
 * - Aggiornare un cliente
 * - Eliminare un cliente
 *
 * @RestController Indica a Spring Boot che questa classe è un controller REST.
 * I metodi restituiscono direttamente dati JSON nella risposta HTTP.
 * @RequestMapping("/api/clienti") Definisce il prefisso dell'URL per tutti gli endpoint di questo controller.
 * Tutte le rotte inizieranno quindi con /api/clienti.
 * @CrossOrigin Permette al frontend Angular (che gira su localhost:4200)
 * di effettuare richieste HTTP verso questo backend.
 */
@RestController
@RequestMapping("/api/clienti")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    /**
     * @Autowired Permette a Spring di iniettare automaticamente l'oggetto ClienteDao.
     * <p>
     * Il DAO (Data Access Object) è la classe che gestisce
     * l'accesso al database per l'entità Cliente.
     */
    @Autowired
    private ClienteDao clienteDao;

    /**
     * =========================
     * OTTENERE TUTTI I CLIENTI
     * =========================
     * <p>
     * Endpoint per recuperare tutti i clienti appartenenti
     * ad uno specifico utente.
     *
     * @GetMapping Questo metodo gestisce richieste HTTP GET all'URL:
     * /api/clienti
     * @RequestParam Permette di leggere un parametro dalla query dell'URL.
     * <p>
     * Esempio di chiamata:
     * GET /api/clienti?utenteId=1
     * <p>
     * In questo modo ogni utente vede solo i propri clienti.
     */
    @GetMapping
    public List<Cliente> getAllClienti(@RequestParam Long utenteId) {

        /**
         * Il DAO recupera dal database tutti i clienti
         * associati all'utente specificato.
         */
        return clienteDao.findAllByUtenteId(utenteId);
    }

    /**
     * =========================
     * CREARE UN NUOVO CLIENTE
     * =========================
     * <p>
     * Endpoint per inserire un nuovo cliente nel database.
     *
     * @PostMapping Gestisce richieste HTTP POST all'URL:
     * /api/clienti
     * <p>
     * POST viene utilizzato per creare una nuova risorsa.
     * @RequestBody Il JSON inviato dal frontend viene convertito automaticamente
     * in un oggetto Java di tipo Cliente.
     */
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {

        /**
         * Il DAO salva il nuovo cliente nel database.
         * Il metodo save() restituisce l'oggetto salvato.
         */
        return clienteDao.save(cliente);
    }

    /**
     * =========================
     * AGGIORNARE UN CLIENTE
     * =========================
     * <p>
     * Endpoint per aggiornare i dati di un cliente esistente.
     *
     * @PutMapping("/{id}") Gestisce richieste HTTP PUT all'URL:
     * /api/clienti/{id}
     * <p>
     * PUT viene utilizzato per aggiornare una risorsa esistente.
     * <p>
     * {id} rappresenta l'identificativo del cliente da aggiornare.
     */
    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {

        /**
         * @PathVariable
         * Permette di ottenere il valore dell'id direttamente dall'URL.
         *
         * @RequestBody
         * Converte il JSON ricevuto dal frontend in un oggetto Cliente.
         */

        // Recupera il cliente esistente dal database
        Cliente clienteEsistente = clienteDao.findById(id);

        // Se il cliente non esiste
        if (clienteEsistente == null) {

            /**
             * HttpStatus.NOT_FOUND (404)
             * Indica che il cliente richiesto non è stato trovato.
             */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile aggiornare: cliente non trovato.");
        }

        /**
         * Controllo di sicurezza:
         *
         * Verifica che il cliente appartenga allo stesso utente
         * che sta tentando di modificarlo.
         *
         * Questo impedisce ad un utente di modificare
         * i clienti di un altro utente.
         */
        if (!clienteEsistente.getUtenteId().equals(cliente.getUtenteId())) {

            /**
             * HttpStatus.FORBIDDEN (403)
             * Indica che l'utente non ha i permessi per effettuare l'operazione.
             */
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato.");
        }

        /**
         * Impostiamo manualmente l'id del cliente,
         * per assicurarci che venga aggiornato il record corretto.
         */
        cliente.setId(id);

        /**
         * Il DAO aggiorna il cliente nel database.
         */
        return clienteDao.update(cliente);
    }

    /**
     * =========================
     * ELIMINARE UN CLIENTE
     * =========================
     * <p>
     * Endpoint per cancellare un cliente dal database.
     *
     * @DeleteMapping("/{id}") Gestisce richieste HTTP DELETE all'URL:
     * /api/clienti/{id}
     * <p>
     * DELETE viene utilizzato per eliminare una risorsa.
     */
    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable Long id, @RequestParam Long utenteId) {

        /**
         * @PathVariable
         * Recupera l'id del cliente dall'URL.
         *
         * @RequestParam
         * Recupera l'id dell'utente dalla query dell'URL.
         */

        // Recupera il cliente dal database
        Cliente clienteEsistente = clienteDao.findById(id);

        // Se il cliente non esiste
        if (clienteEsistente == null) {

            /**
             * HttpStatus.NOT_FOUND (404)
             * Il cliente richiesto non esiste.
             */
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente non trovato.");
        }

        /**
         * Controllo di sicurezza:
         *
         * Verifica che il cliente appartenga all'utente
         * che sta tentando di eliminarlo.
         */
        if (!clienteEsistente.getUtenteId().equals(utenteId)) {

            /**
             * HttpStatus.FORBIDDEN (403)
             * L'utente non ha il permesso di eliminare questo cliente.
             */
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato.");
        }

        /**
         * Se tutti i controlli sono superati,
         * il cliente viene eliminato dal database.
         */
        clienteDao.deleteById(id, utenteId);
    }
}
