package uni.lignosuiteapi.dao.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.dao.PreventivoDao;
import uni.lignosuiteapi.dao.PreventivoItemDao;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.proxy.PreventivoProxy;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementazione DAO per la gestione dei Preventivi.
 *
 * @Repository Indica a Spring che questa classe è un componente
 * responsabile dell'accesso al database.
 */
@Repository
public class PreventivoDaoImpl implements PreventivoDao {

    /**
     * JdbcTemplate semplifica l'esecuzione delle query SQL
     * e la gestione delle connessioni al database.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * DAO utilizzato per gestire le righe (items)
     * associate ad ogni preventivo.
     */
    @Autowired
    private PreventivoItemDao itemDao;

    /**
     * RowMapper
     * <p>
     * Converte ogni riga del risultato SQL
     * in un oggetto Preventivo.
     * <p>
     * Qui viene utilizzato il PATTERN PROXY:
     * invece di creare direttamente Preventivo,
     * viene creato un PreventivoProxy che caricherà
     * gli items solo quando necessario (lazy loading).
     */
    private final RowMapper<Preventivo> rowMapper = (rs, rowNum) -> {

        System.out.println("Mapping del preventivo con ID: " + rs.getLong("id"));
        // Creazione del proxy
        PreventivoProxy p = new PreventivoProxy(itemDao);

        p.setId(rs.getLong("id"));
        p.setInvoiceNumber(rs.getLong("invoice_number"));
        p.setUtenteId(rs.getLong("utente_id"));
        p.setDate(rs.getString("date"));
        p.setFromName(rs.getString("from_name"));
        p.setFromEmail(rs.getString("from_email"));
        p.setFromPiva(rs.getString("from_piva"));
        p.setToName(rs.getString("to_name"));
        p.setToEmail(rs.getString("to_email"));
        p.setToPiva(rs.getString("to_piva"));
        p.setTaxRate(rs.getDouble("tax_rate"));
        p.setSubtotal(rs.getDouble("subtotal"));
        p.setTaxAmount(rs.getDouble("tax_amount"));
        p.setDiscount(rs.getDouble("discount"));
        p.setTotal(rs.getDouble("total"));
        return p;
    };

    /**
     * Recupera tutti i preventivi di un utente.
     */
    @Override
    public List<Preventivo> findAllByUtenteId(Long utenteId) {
        String sql = "SELECT * FROM preventivo WHERE utente_id = ?";
        return jdbcTemplate.query(sql, rowMapper, utenteId);
    }

    /**
     * Recupera un preventivo tramite ID.
     */
    @Override
    public Preventivo findById(Long id) {
        String sql = "SELECT * FROM preventivo WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Inserisce un nuovo preventivo nel database.
     */
    @Override
    public Preventivo save(Preventivo p) {

        String sql = "INSERT INTO preventivo (invoice_number, utente_id, date, from_name, from_email, from_piva, to_name, to_email, to_piva, tax_rate, subtotal, tax_amount, discount, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Serve per recuperare la chiave primaria generata
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, p.getInvoiceNumber());
            ps.setLong(2, p.getUtenteId());
            ps.setString(3, p.getDate());
            ps.setString(4, p.getFromName());
            ps.setString(5, p.getFromEmail());
            ps.setString(6, p.getFromPiva());
            ps.setString(7, p.getToName());
            ps.setString(8, p.getToEmail());
            ps.setString(9, p.getToPiva());

            // Se i valori sono null vengono salvati come 0
            ps.setDouble(10, p.getTaxRate() != null ? p.getTaxRate() : 0.0);
            ps.setDouble(11, p.getSubtotal() != null ? p.getSubtotal() : 0.0);
            ps.setDouble(12, p.getTaxAmount() != null ? p.getTaxAmount() : 0.0);
            ps.setDouble(13, p.getDiscount() != null ? p.getDiscount() : 0.0);
            ps.setDouble(14, p.getTotal() != null ? p.getTotal() : 0.0);

            return ps;
        }, keyHolder);

        // Recupero dell'id generato dal database
        if (keyHolder.getKeys() != null) {
            p.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        }

        /**
         * Salvataggio a cascata degli items del preventivo.
         */
        if (p.getItems() != null && !p.getItems().isEmpty()) {
            itemDao.saveAll(p.getId(), p.getItems());
        }

        return p;
    }

    /**
     * Aggiorna un preventivo esistente.
     */
    @Override
    public Preventivo update(Preventivo p) {

        String sql = "UPDATE preventivo SET invoice_number=?, date=?, from_name=?, from_email=?, from_piva=?, to_name=?, to_email=?, to_piva=?, tax_rate=?, subtotal=?, tax_amount=?, discount=?, total=? WHERE id=? AND utente_id=?";

        jdbcTemplate.update(sql,
                p.getInvoiceNumber(),
                p.getDate(),
                p.getFromName(),
                p.getFromEmail(),
                p.getFromPiva(),
                p.getToName(),
                p.getToEmail(),
                p.getToPiva(),
                p.getTaxRate(),
                p.getSubtotal(),
                p.getTaxAmount(),
                p.getDiscount(),
                p.getTotal(),
                p.getId(),
                p.getUtenteId());

        /**
         * Strategia semplice per aggiornare gli items:
         * 1. eliminare le righe esistenti
         * 2. inserire nuovamente quelle aggiornate
         */
        itemDao.deleteByPreventivoId(p.getId());

        if (p.getItems() != null && !p.getItems().isEmpty()) {
            itemDao.saveAll(p.getId(), p.getItems());
        }

        return p;
    }

    /**
     * Elimina un preventivo.
     * Prima vengono eliminati gli items per mantenere
     * l'integrità referenziale.
     */
    @Override
    public void deleteById(Long id, Long utenteId) {

        itemDao.deleteByPreventivoId(id);

        String sql = "DELETE FROM preventivo WHERE id = ? AND utente_id = ?";
        jdbcTemplate.update(sql, id, utenteId);
    }

    /**
     * Calcola il prossimo numero preventivo disponibile.
     * Recupera il massimo numero esistente e aggiunge 1.
     */
    @Override
    public Long getNextInvoiceNumber(Long utenteId) {

        String sql = "SELECT MAX(invoice_number) FROM preventivo WHERE utente_id = ?";

        Long max = jdbcTemplate.queryForObject(sql, Long.class, utenteId);

        return (max != null) ? max + 1 : 1L;
    }
}
