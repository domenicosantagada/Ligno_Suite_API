package uni.lignosuiteapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.lignosuiteapi.model.Articolo;
import uni.lignosuiteapi.service.ArticoloService;

import java.util.List;


/**
 * Controller per la gestione degli articoli.
 */

@RestController
@RequestMapping("/api/articoli")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticoloController {

    // @Autowired: Inietta l'istanza del repository per interagire con il database senza dover scrivere query SQL manuali.
    @Autowired
    private ArticoloService articoloService;


    /**
     * Metodo HTTP GET per recuperare tutti gli articoli creati da un utente specifico.
     *
     * @param utenteId: ID dell'utente che ha creato l'articolo.
     * @return: Restituisce tutti gli articoli creati da un utente specifico.
     */
    @GetMapping("/utente/{utenteId}")
    public List<Articolo> getArticoliByUtenteId(@PathVariable Long utenteId) {

        /**
         * Il Service chiama il DAO per recuperare tutti gli articoli associati all'utente specificato.
         */
        return articoloService.getArticoliByUtenteId(utenteId);
    }

    /**
     * Metodo HTTP POST per creare un nuovo articolo associato a un utente specifico.
     *
     * @param utenteId: ID dell'utente che ha creato l'articolo.
     * @param articolo: Dettagli del nuovo articolo da creare.
     */
    @PostMapping("/utente/{utenteId}")
    public Articolo createArticolo(@PathVariable Long utenteId, @RequestBody Articolo articolo) {
        /**
         * Il Service chiama il DAO per salvare il nuovo articolo associato all'utente specificato.
         */
        return articoloService.createArticolo(utenteId, articolo);
    }

    /**
     * Metodo HTTP PUT per aggiornare i dettagli di un articolo esistente.
     *
     * @param id:       ID dell'articolo da aggiornare.
     * @param dettagli: Nuovi dettagli dell'articolo.
     * @return: Restituisce l'articolo aggiornato.
     */
    @PutMapping("/{id}")
    public Articolo updateArticolo(@PathVariable Long id, @RequestBody Articolo dettagli) {

        /**
         * Il Service chiama il DAO per aggiornare i dettagli dell'articolo specificato.
         */
        return articoloService.updateArticolo(id, dettagli);
    }

    /**
     * Metodo HTTP DELETE per eliminare un articolo specifico.
     *
     * @param id: ID dell'articolo da eliminare.
     */
    @DeleteMapping("/{id}")
    public void deleteArticolo(@PathVariable Long id) {

        /**
         * Il Service chiama il DAO per eliminare l'articolo specificato.
         */
        articoloService.deleteArticolo(id);
    }
}
