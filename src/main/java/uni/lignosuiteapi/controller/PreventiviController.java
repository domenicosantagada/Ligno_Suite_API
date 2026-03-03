package uni.lignosuiteapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.repository.PreventivoRepository;

import java.util.List;

/**
 * Controller REST per la gestione dei preventivi (o fatture/invoice).
 * * @RestController: Specifica che la classe gestirà richieste HTTP e restituirà risposte in formato JSON.
 * * @RequestMapping("/api/preventivi"): Definisce la rotta base. Tutti gli endpoint qui dentro
 * inizieranno con http://localhost:8080/api/preventivi.
 * * @CrossOrigin: Consente ad Angular (sulla porta 4200) di fare richieste a questo backend
 */
@RestController
@RequestMapping("/api/preventivi")
@CrossOrigin(origins = "http://localhost:4200")
public class PreventiviController {

    /**
     * @Autowired: Inietta l'istanza del repository, senza dover scrivere query sql manuali.
     */
    @Autowired
    private PreventivoRepository preventivoRepository;

    /**
     * LETTURA (GET) di tutti i preventivi di un utente.
     * * @RequestParam Long utenteId: Legge l'ID dell'utente loggato dall'URL (es. /api/preventivi?utenteId=1).
     * Fondamentale per far sì che ogni artigiano veda ESCLUSIVAMENTE i propri preventivi.
     */
    @GetMapping
    public List<Preventivo> getAllPreventivi(@RequestParam Long utenteId) {
        // Usa un metodo personalizzato del repository per filtrare dal DB solo i preventivi di quell'utente
        return preventivoRepository.findByUtenteId(utenteId);
    }

    /**
     * 1. CREAZIONE NUOVO PREVENTIVO (POST)
     * * @RequestBody: Converte il preventivo (incluso l'elenco degli articoli) in arrivo dal frontend
     * in un oggetto Java.
     */
    @PostMapping
    public Preventivo createPreventivo(@RequestBody Preventivo invoice) {
        return preventivoRepository.save(invoice);
    }

    /**
     * 2. AGGIORNAMENTO PREVENTIVO ESISTENTE (PUT)
     * * @PathVariable Long id: Prende l'ID del preventivo direttamente dall'URL (es. /api/preventivi/5).
     * * @RequestBody Preventivo invoice: Contiene i nuovi dati del preventivo modificati dall'utente nel frontend.
     */
    @PutMapping("/{id}")
    public Preventivo updatePreventivo(@PathVariable Long id, @RequestBody Preventivo invoice) {

        // 1. Recupera il preventivo originale dal DB
        Preventivo preventivoEsistente = preventivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile aggiornare: preventivo non trovato."));

        // 2. Controllo di sicurezza: chi tenta di modificarlo è il vero proprietario?
        if (!preventivoEsistente.getUtenteId().equals(invoice.getUtenteId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato: non sei autorizzato a modificare questo preventivo.");
        }

        // 3. Assicuriamoci che l'ID del preventivo non venga sovrascritto
        invoice.setId(id);

        // 4. Salva la modifica
        return preventivoRepository.save(invoice);
    }

    /**
     * ELIMINAZIONE PREVENTIVO (DELETE)
     * * @PathVariable Long id: ID del preventivo da cancellare.
     * * @RequestParam Long utenteId: ID dell'utente che ha creato il preventivo.
     */
    @DeleteMapping("/{id}")
    public void deletePreventivo(@PathVariable Long id, @RequestParam Long utenteId) {
        // 1. Cerca il preventivo nel database
        Preventivo preventivoEsistente = preventivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Preventivo non trovato."));

        // 2. Controllo di sicurezza
        // Verifica che l'utente che fa la richiesta sia il vero proprietario del preventivo
        if (!preventivoEsistente.getUtenteId().equals(utenteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato: non sei autorizzato a eliminare questo preventivo.");
        }


        // 3. Se il controllo è superato, procedi con l'eliminazione sicura
        preventivoRepository.delete(preventivoEsistente);
    }

    /**
     * ENDPOINT PERSONALIZZATO: Ottiene il prossimo numero di preventivo progressivo.
     * Rotta: GET /api/preventivi/next-number?utenteId=X
     * Questo è molto utile dal lato business: quando l'utente crea un nuovo preventivo,
     * il sistema sa già suggerirgli il numero progressivo corretto (es. Preventivo n° 6).
     */
    @GetMapping("/next-number")
    public Long getNextInvoiceNumber(@RequestParam Long utenteId) {
        // Chiama una query personalizzata (scritta da te nel PreventivoRepository) che cerca
        // il numero (o ID) massimo di preventivo per quello specifico utente.
        Long maxId = preventivoRepository.findMaxInvoiceNumberByUtenteId(utenteId);

        // Aggiunge 1 al massimo trovato per suggerire il prossimo numero.
        // Se l'utente non ha ancora preventivi (maxId = 0), il primo sarà il numero 1.
        return maxId + 1;
    }
}
