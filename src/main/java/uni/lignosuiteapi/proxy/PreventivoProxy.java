package uni.lignosuiteapi.proxy;

import uni.lignosuiteapi.dao.PreventivoItemDao;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.model.PreventivoItem;

import java.util.List;

/**
 * IMPLEMENTAZIONE DEL PATTERN PROXY (Requisito d'esame)
 * Questa classe "finge" di essere un Preventivo (estende Preventivo),
 * ma intercetta la chiamata a getItems() per effettuare il Lazy Loading
 * dal database solo quando è strettamente necessario.
 */
public class PreventivoProxy extends Preventivo {

    private final PreventivoItemDao itemDao;
    private boolean isItemsLoaded = false;

    public PreventivoProxy(PreventivoItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public List<PreventivoItem> getItems() {
        // Se le righe non sono ancora state caricate dal DB...
        if (!isItemsLoaded) {
            System.out.println("PROXY: Lazy loading attivato! Recupero le righe del preventivo ID: " + this.getId());
            // Carica le righe tramite il DAO e le salva nell'oggetto genitore
            List<PreventivoItem> righe = itemDao.findByPreventivoId(this.getId());
            super.setItems(righe);
            isItemsLoaded = true; // Segna come caricate per non ripetere la query
        }
        return super.getItems();
    }
}
