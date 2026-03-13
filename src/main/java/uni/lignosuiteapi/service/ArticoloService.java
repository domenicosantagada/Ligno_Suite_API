package uni.lignosuiteapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.ArticoloDao;
import uni.lignosuiteapi.model.Articolo;

import java.util.List;

@Service
public class ArticoloService {

    @Autowired
    private ArticoloDao articoloDao;

    // Metodo per ottenere tutti gli articoli di un utente.
    public List<Articolo> getArticoliByUtenteId(Long utenteId) {
        return articoloDao.findByUtenteId(utenteId);
    }

    // Metodo per creare un nuovo articolo.
    public Articolo createArticolo(Long utenteId, Articolo articolo) {
        articolo.setUtenteId(utenteId);
        return articoloDao.save(articolo);
    }

    // Metodo per aggiornare un articolo esistente.
    public Articolo updateArticolo(Long id, Articolo dettagli) {
        // Creiamo un articolo temporaneo con i dettagli aggiornati
        // In caso non troviamo l'articolo, lanciamo un'eccezione
        Articolo articoloEsistente = articoloDao.findById(id);
        if (articoloEsistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Articolo non trovato");
        }

        // Aggiorniamo i campi desiderati (Logica di Business)
        articoloEsistente.setNome(dettagli.getNome());
        articoloEsistente.setDescrizione(dettagli.getDescrizione());
        articoloEsistente.setPrezzoAcquisto(dettagli.getPrezzoAcquisto());
        articoloEsistente.setFornitore(dettagli.getFornitore());
        articoloEsistente.setDataAcquisto(dettagli.getDataAcquisto());
        articoloEsistente.setUnitaMisura(dettagli.getUnitaMisura());

        return articoloDao.update(articoloEsistente);
    }

    // Metodo per eliminare un articolo.
    public void deleteArticolo(Long id) {
        articoloDao.deleteById(id);
    }
}
