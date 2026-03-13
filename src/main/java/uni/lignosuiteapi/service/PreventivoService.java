package uni.lignosuiteapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.PreventivoDao;
import uni.lignosuiteapi.model.Preventivo;

import java.util.List;

@Service
public class PreventivoService {

    /**
     * DAO per la gestione dei Preventivi.
     */
    @Autowired
    private PreventivoDao preventivoDao;

    // Metodo per verificare se esiste già un preventivo con lo stesso numero per un utente specifico
    private boolean existsInvoiceNumber(Long utenteId, Long invoiceNumber) {
        /**
         * Recupera tutti i preventivi dell'utente
         * e utilizza uno stream per verificare se
         * almeno uno ha lo stesso numero.
         */
        return preventivoDao.findAllByUtenteId(utenteId).stream()
                .anyMatch(p -> p.getInvoiceNumber().equals(invoiceNumber));
    }

    // Metodo per recuperare tutti i preventivi associati a un utente specifico
    public List<Preventivo> getAllPreventivi(Long utenteId) {
        /**
         * Il DAO recupera dal database tutti i preventivi
         * associati all'utente specificato.
         */
        return preventivoDao.findAllByUtenteId(utenteId);
    }

    // Metodo per creare un nuovo preventivo
    public Preventivo createPreventivo(Preventivo preventivo) {
        /**
         * Controllo per verificare se il numero preventivo
         * è già utilizzato da un altro preventivo dello stesso utente.
         */
        if (existsInvoiceNumber(preventivo.getUtenteId(), preventivo.getInvoiceNumber())) {

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
        return preventivoDao.save(preventivo);
    }

    // Metodo per aggiornare un preventivo esistente
    public Preventivo updatePreventivo(Long id, Preventivo preventivo) {
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
        if (!preventivoEsistente.getUtenteId().equals(preventivo.getUtenteId())) {

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
        if (!preventivoEsistente.getInvoiceNumber().equals(preventivo.getInvoiceNumber())) {

            if (existsInvoiceNumber(preventivo.getUtenteId(), preventivo.getInvoiceNumber())) {

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
        preventivo.setId(id);

        /**
         * Il DAO aggiorna il preventivo nel database.
         */
        return preventivoDao.update(preventivo);

    }

    // Metodo per eliminare un preventivo
    public void deletePreventivo(Long id, Long utenteId) {
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

    // Metodo per calcolare il prossimo numero preventivo disponibile
    public Long getNextInvoiceNumber(Long utenteId) {

        /**
         * Il DAO calcola il prossimo numero preventivo
         * disponibile per l'utente specificato.
         */
        return preventivoDao.getNextInvoiceNumber(utenteId);
    }
}
