package uni.lignosuiteapi.dao.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.dao.ClienteDao;
import uni.lignosuiteapi.model.Cliente;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementazione del DAO per l'entità Cliente.
 *
 * @Repository Indica a Spring che questa classe è un componente di accesso ai dati
 * e permette la gestione automatica delle eccezioni JDBC.
 */
@Repository
public class ClienteDaoImpl implements ClienteDao {

    /**
     * RowMapper
     * <p>
     * Serve a convertire ogni riga del risultato SQL
     * in un oggetto Java Cliente.
     */
    private final RowMapper<Cliente> rowMapper = (rs, rowNum) -> {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setUtenteId(rs.getLong("utente_id"));
        c.setNome(rs.getString("nome"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        c.setPartitaIva(rs.getString("partita_iva"));
        return c;
    };

    /**
     * JdbcTemplate
     * <p>
     * Classe di Spring che semplifica l'esecuzione
     * delle query SQL sul database.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Recupera tutti i clienti appartenenti ad un utente.
     */
    @Override
    public List<Cliente> findAllByUtenteId(Long utenteId) {
        String sql = "SELECT * FROM cliente WHERE utente_id = ?";
        return jdbcTemplate.query(sql, rowMapper, utenteId);
    }

    /**
     * Recupera un cliente tramite il suo ID.
     */
    @Override
    public Cliente findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Inserisce un nuovo cliente nel database.
     */
    @Override
    public Cliente save(Cliente cliente) {

        // Formattazione dei dati prima del salvataggio
        cliente.formattaDati();

        String sql = "INSERT INTO cliente (nome, email, telefono, partita_iva, utente_id) VALUES (?, ?, ?, ?, ?)";

        // Permette di recuperare la chiave primaria generata dal database
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getPartitaIva());
            ps.setLong(5, cliente.getUtenteId());
            return ps;
        }, keyHolder);

        // Recupera l'id generato dal database
        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("id")) {
            cliente.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        }

        return cliente;
    }

    /**
     * Aggiorna i dati di un cliente esistente.
     */
    @Override
    public Cliente update(Cliente cliente) {

        cliente.formattaDati();

        String sql = "UPDATE cliente SET nome = ?, email = ?, telefono = ?, partita_iva = ? WHERE id = ? AND utente_id = ?";

        jdbcTemplate.update(sql,
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getPartitaIva(),
                cliente.getId(),
                cliente.getUtenteId());

        return cliente;
    }

    /**
     * Elimina un cliente dal database.
     */
    @Override
    public void deleteById(Long id, Long utenteId) {
        String sql = "DELETE FROM cliente WHERE id = ? AND utente_id = ?";
        jdbcTemplate.update(sql, id, utenteId);
    }
}
