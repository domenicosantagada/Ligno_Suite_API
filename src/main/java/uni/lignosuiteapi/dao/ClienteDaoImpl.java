package uni.lignosuiteapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.model.Cliente;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ClienteDaoImpl implements ClienteDao {

    // Mappatura corretta con i campi effettivi della tua tabella Cliente
    private final RowMapper<Cliente> rowMapper = (rs, rowNum) -> {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setUtenteId(rs.getLong("utente_id")); // Uso corretto di utenteId
        c.setNome(rs.getString("nome"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        c.setPartitaIva(rs.getString("partita_iva"));
        return c;
    };
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Cliente> findAllByUtenteId(Long utenteId) {
        String sql = "SELECT * FROM cliente WHERE utente_id = ?";
        return jdbcTemplate.query(sql, rowMapper, utenteId);
    }

    @Override
    public Cliente findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst().orElse(null);
    }

    @Override
    public Cliente save(Cliente cliente) {
        cliente.formattaDati(); // Eseguo la formattazione a mano, visto che non c'è più @PrePersist

        String sql = "INSERT INTO cliente (nome, email, telefono, partita_iva, utente_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getPartitaIva());
            ps.setLong(5, cliente.getUtenteId()); // Uso corretto di getUtenteId()
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("id")) {
            cliente.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        }
        return cliente;
    }

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

    @Override
    public void deleteById(Long id, Long utenteId) {
        String sql = "DELETE FROM cliente WHERE id = ? AND utente_id = ?";
        jdbcTemplate.update(sql, id, utenteId);
    }
}
