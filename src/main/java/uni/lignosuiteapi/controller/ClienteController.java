package uni.lignosuiteapi.controller;

// Import delle classi necessarie per il controller REST

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.lignosuiteapi.model.Cliente;
import uni.lignosuiteapi.service.ClienteService;

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
@CrossOrigin(origins = "*")
public class ClienteController {

    /**
     * @Autowired Permette a Spring di iniettare automaticamente l'oggetto ClienteService.
     * ClienteService si occupa di gestire le operazioni CRUD sui clienti.
     */
    @Autowired
    private ClienteService clienteService;


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
         * Il Service chiama il DAO per recuperare i clienti dal database.
         */
        return clienteService.getAllClienti(utenteId);
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
         * Il Service chiama il DAO per salvare il nuovo cliente nel database.
         */
        return clienteService.createCliente(cliente);
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

        // Il Service chiama il DAO per aggiornare il cliente nel database.
        return clienteService.updateCliente(id, cliente);
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

        // Il Service chiama il DAO per eliminare il cliente dal database.
        clienteService.deleteCliente(id, utenteId);
    }
}
