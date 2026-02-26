package uni.lignosuiteapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.repository.PreventivoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/preventivi")
@CrossOrigin(origins = "http://localhost:4200")
public class PreventiviController {

    @Autowired
    private PreventivoRepository preventivoRepository;

    @GetMapping
    public List<Preventivo> getAllPreventivi(@RequestParam Long utenteId) {
        // Ora usa il nuovo metodo del repository!
        return preventivoRepository.findByUtenteId(utenteId);
    }

    // 1. CREAZIONE NUOVO PREVENTIVO (POST)
    @PostMapping
    public Preventivo createPreventivo(@RequestBody Preventivo invoice) {
//        // Blocca se si sta cercando di creare un preventivo con un ID già esistente
//        if (preventivoRepository.existsById(invoice.getInvoiceNumber())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Il numero preventivo esiste già.");
//        }
        return preventivoRepository.save(invoice);
    }

    // 2. AGGIORNAMENTO PREVENTIVO ESISTENTE (PUT)
    @PutMapping("/{id}")
    public Preventivo updatePreventivo(@PathVariable Long id, @RequestBody Preventivo invoice) {
        // Assicuriamoci che il preventivo da modificare esista davvero
        if (!preventivoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile aggiornare: preventivo non trovato.");
        }
        // Il save di Spring Data JPA esegue in automatico un UPDATE se l'ID esiste già
        return preventivoRepository.save(invoice);
    }

    @DeleteMapping("/{id}")
    public void deletePreventivo(@PathVariable Long id) {
        preventivoRepository.deleteById(id);
    }

    @GetMapping("/next-number")
    public Long getNextInvoiceNumber(@RequestParam Long utenteId) {
        Long maxId = preventivoRepository.findMaxInvoiceNumberByUtenteId(utenteId);
        return maxId + 1; // Se il max è 0, restituirà 1. Se il max è 5, restituirà 6.
    }
}
