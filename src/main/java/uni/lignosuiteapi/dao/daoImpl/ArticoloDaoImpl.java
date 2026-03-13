package uni.lignosuiteapi.dao.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.dao.ArticoloDao;
import uni.lignosuiteapi.model.Articolo;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ArticoloDaoImpl implements ArticoloDao {

    private final RowMapper<Articolo> rowMapper = (rs, rowNum) -> {
        Articolo a = new Articolo();
        a.setId(rs.getLong("id"));
        a.setNome(rs.getString("nome"));
        a.setDescrizione(rs.getString("descrizione"));
        // Uso getObject per gestire correttamente i valori Double nullable
        a.setPrezzoAcquisto(rs.getObject("prezzo_acquisto", Double.class));
        a.setFornitore(rs.getString("fornitore"));
        a.setUnitaMisura(rs.getString("unita_misura"));

        // Conversione sicura per le date (da java.sql.Date a LocalDate)
        java.sql.Date sqlDate = rs.getDate("data_acquisto");
        if (sqlDate != null) {
            a.setDataAcquisto(sqlDate.toLocalDate());
        }

        a.setUtenteId(rs.getLong("utente_id"));
        return a;
    };
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Articolo> findByUtenteId(Long utenteId) {
        String sql = "SELECT * FROM articolo WHERE utente_id = ?";
        return jdbcTemplate.query(sql, rowMapper, utenteId);
    }

    @Override
    public Articolo findById(Long id) {
        String sql = "SELECT * FROM articolo WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst().orElse(null);
    }

    @Override
    public Articolo save(Articolo articolo) {
        articolo.formattaDati();
        String sql = "INSERT INTO articolo (nome, descrizione, prezzo_acquisto, fornitore, unita_misura, data_acquisto, utente_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, articolo.getNome());
            ps.setString(2, articolo.getDescrizione());
            ps.setObject(3, articolo.getPrezzoAcquisto());
            ps.setString(4, articolo.getFornitore());
            ps.setString(5, articolo.getUnitaMisura());
            ps.setObject(6, articolo.getDataAcquisto() != null ? java.sql.Date.valueOf(articolo.getDataAcquisto()) : null);
            ps.setLong(7, articolo.getUtenteId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("id")) {
            articolo.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        }
        return articolo;
    }

    @Override
    public Articolo update(Articolo articolo) {
        articolo.formattaDati();
        String sql = "UPDATE articolo SET nome=?, descrizione=?, prezzo_acquisto=?, fornitore=?, unita_misura=?, data_acquisto=? WHERE id=? AND utente_id=?";
        jdbcTemplate.update(sql,
                articolo.getNome(),
                articolo.getDescrizione(),
                articolo.getPrezzoAcquisto(),
                articolo.getFornitore(),
                articolo.getUnitaMisura(),
                articolo.getDataAcquisto() != null ? java.sql.Date.valueOf(articolo.getDataAcquisto()) : null,
                articolo.getId(),
                articolo.getUtenteId());
        return articolo;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM articolo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
