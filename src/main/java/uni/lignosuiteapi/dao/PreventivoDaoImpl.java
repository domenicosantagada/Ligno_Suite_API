package uni.lignosuiteapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.model.Preventivo;
import uni.lignosuiteapi.proxy.PreventivoProxy;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PreventivoDaoImpl implements PreventivoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PreventivoItemDao itemDao;

    // ATTENZIONE: Il RowMapper crea un PreventivoProxy, NON un Preventivo standard!
    private final RowMapper<Preventivo> rowMapper = (rs, rowNum) -> {
        PreventivoProxy p = new PreventivoProxy(itemDao); // <-- PATTERN PROXY QUI!
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

    @Override
    public List<Preventivo> findAllByUtenteId(Long utenteId) {
        String sql = "SELECT * FROM preventivo WHERE utente_id = ?";
        return jdbcTemplate.query(sql, rowMapper, utenteId);
    }

    @Override
    public Preventivo findById(Long id) {
        String sql = "SELECT * FROM preventivo WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst().orElse(null);
    }

    @Override
    public Preventivo save(Preventivo p) {
        String sql = "INSERT INTO preventivo (invoice_number, utente_id, date, from_name, from_email, from_piva, to_name, to_email, to_piva, tax_rate, subtotal, tax_amount, discount, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.setDouble(10, p.getTaxRate() != null ? p.getTaxRate() : 0.0);
            ps.setDouble(11, p.getSubtotal() != null ? p.getSubtotal() : 0.0);
            ps.setDouble(12, p.getTaxAmount() != null ? p.getTaxAmount() : 0.0);
            ps.setDouble(13, p.getDiscount() != null ? p.getDiscount() : 0.0);
            ps.setDouble(14, p.getTotal() != null ? p.getTotal() : 0.0);
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null) {
            p.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        }

        // Salvo anche gli items a cascata
        if (p.getItems() != null && !p.getItems().isEmpty()) {
            itemDao.saveAll(p.getId(), p.getItems());
        }
        return p;
    }

    @Override
    public Preventivo update(Preventivo p) {
        String sql = "UPDATE preventivo SET invoice_number=?, date=?, from_name=?, from_email=?, from_piva=?, to_name=?, to_email=?, to_piva=?, tax_rate=?, subtotal=?, tax_amount=?, discount=?, total=? WHERE id=? AND utente_id=?";
        jdbcTemplate.update(sql, p.getInvoiceNumber(), p.getDate(), p.getFromName(), p.getFromEmail(), p.getFromPiva(), p.getToName(), p.getToEmail(), p.getToPiva(), p.getTaxRate(), p.getSubtotal(), p.getTaxAmount(), p.getDiscount(), p.getTotal(), p.getId(), p.getUtenteId());

        // Per aggiornare le righe in modo pulito: eliminiamo le vecchie e inseriamo le nuove
        itemDao.deleteByPreventivoId(p.getId());
        if (p.getItems() != null && !p.getItems().isEmpty()) {
            itemDao.saveAll(p.getId(), p.getItems());
        }
        return p;
    }

    @Override
    public void deleteById(Long id, Long utenteId) {
        // Elimino prima i figli (items) per l'integrità referenziale, poi il padre
        itemDao.deleteByPreventivoId(id);
        String sql = "DELETE FROM preventivo WHERE id = ? AND utente_id = ?";
        jdbcTemplate.update(sql, id, utenteId);
    }

    @Override
    public Long getNextInvoiceNumber(Long utenteId) {
        String sql = "SELECT MAX(invoice_number) FROM preventivo WHERE utente_id = ?";
        Long max = jdbcTemplate.queryForObject(sql, Long.class, utenteId);
        return (max != null) ? max + 1 : 1L;
    }
}
