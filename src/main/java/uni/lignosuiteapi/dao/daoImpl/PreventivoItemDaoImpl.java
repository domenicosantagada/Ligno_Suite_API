package uni.lignosuiteapi.dao.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.dao.PreventivoItemDao;
import uni.lignosuiteapi.model.PreventivoItem;

import java.util.List;

/**
 * Implementazione DAO per la gestione delle righe (items) di un preventivo.
 *
 * @Repository Indica a Spring che questa classe è responsabile
 * dell'accesso ai dati nel database.
 */
@Repository
public class PreventivoItemDaoImpl implements PreventivoItemDao {

    /**
     * RowMapper
     * <p>
     * Converte ogni riga della tabella preventivo_item
     * in un oggetto Java PreventivoItem.
     */
    private final RowMapper<PreventivoItem> rowMapper = (rs, rowNum) -> {
        PreventivoItem item = new PreventivoItem();
        item.setId(rs.getString("id"));
        item.setDescription(rs.getString("description"));
        item.setQuantity(rs.getDouble("quantity"));
        item.setUnitaMisura(rs.getString("unita_misura"));
        item.setRate(rs.getDouble("rate"));
        item.setAmount(rs.getDouble("amount"));
        return item;
    };

    /**
     * JdbcTemplate
     * utilizzato per eseguire le query SQL.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Recupera tutte le righe associate ad un preventivo.
     */
    @Override
    public List<PreventivoItem> findByPreventivoId(Long preventivoId) {
        String sql = "SELECT * FROM preventivo_item WHERE preventivo_id = ?";
        return jdbcTemplate.query(sql, rowMapper, preventivoId);
    }

    /**
     * Salva tutte le righe di un preventivo.
     * <p>
     * Viene usato quando si crea o si aggiorna un preventivo.
     */
    @Override
    public void saveAll(Long preventivoId, List<PreventivoItem> items) {

        String sql = "INSERT INTO preventivo_item (id, description, quantity, unita_misura, rate, amount, preventivo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Inserimento di ogni riga del preventivo
        for (PreventivoItem item : items) {
            jdbcTemplate.update(sql,
                    item.getId(),
                    item.getDescription(),
                    item.getQuantity(),
                    item.getUnitaMisura(),
                    item.getRate(),
                    item.getAmount(),
                    preventivoId);
        }
    }

    /**
     * Elimina tutte le righe associate ad un preventivo.
     * <p>
     * Usato quando:
     * - si elimina un preventivo
     * - si aggiornano completamente le sue righe
     */
    @Override
    public void deleteByPreventivoId(Long preventivoId) {
        String sql = "DELETE FROM preventivo_item WHERE preventivo_id = ?";
        jdbcTemplate.update(sql, preventivoId);
    }
}
