package uni.lignosuiteapi.proxy;

import uni.lignosuiteapi.dao.PreventivoItemDao;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.model.PreventivoItem;

import java.util.List;

/**
 * Implementazione del design pattern Proxy.
 * <p>
 * Questa classe estende la classe Preventivo e funge da "sostituto intelligente"
 * dell'oggetto reale. Il suo compito è intercettare l'accesso alle righe
 * del preventivo (items) e caricarle dal database solo quando necessario.
 * <p>
 * Questo approccio implementa il concetto di Lazy Loading:
 * i dati non vengono caricati subito, ma solo nel momento in cui vengono richiesti.
 */
public class PreventivoProxy extends Preventivo {

    // DAO utilizzato per recuperare le righe del preventivo dal database
    private final PreventivoItemDao itemDao;

    // Flag che indica se le righe sono già state caricate
    private boolean isItemsLoaded = false;

    /**
     * Costruttore della classe proxy.
     * Riceve il DAO necessario per recuperare le righe del preventivo.
     */
    public PreventivoProxy(PreventivoItemDao itemDao) {

        this.itemDao = itemDao;
    }

    /**
     * Override del metodo getItems() della classe Preventivo.
     * <p>
     * Qui viene implementata la logica del Lazy Loading:
     * le righe del preventivo vengono caricate dal database
     * solo alla prima richiesta.
     */
    @Override
    public List<PreventivoItem> getItems() {

        // Se gli items non sono ancora stati caricati
        if (!isItemsLoaded) {

            System.out.println(
                    "PROXY attivato per il preventivo con ID: "
                            + this.getId()
            );

            // Recupera le righe dal database tramite il DAO
            List<PreventivoItem> righe = itemDao.findByPreventivoId(this.getId());

            // Salva le righe nell'oggetto padre (Preventivo)
            super.setItems(righe);

            // Segna che i dati sono stati caricati per evitare altre query
            isItemsLoaded = true;
        }

        // Restituisce le righe del preventivo
        return super.getItems();
    }
}
