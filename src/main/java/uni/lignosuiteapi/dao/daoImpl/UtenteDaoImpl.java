package uni.lignosuiteapi.dao.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.dao.UtenteDao;
import uni.lignosuiteapi.model.Utente;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementazione del DAO per la gestione degli utenti.
 *
 * @Repository Indica a Spring che questa classe gestisce
 * l'accesso ai dati della tabella utente.
 */
@Repository
public class UtenteDaoImpl implements UtenteDao {

    /**
     * RowMapper
     * <p>
     * Converte una riga del risultato SQL
     * in un oggetto Java di tipo Utente.
     */
    private final RowMapper<Utente> rowMapper = (rs, rowNum) -> {
        Utente u = new Utente();
        u.setId(rs.getLong("id"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setNome(rs.getString("nome"));
        u.setNomeAzienda(rs.getString("nome_azienda"));
        u.setNomeTitolare(rs.getString("nome_titolare"));
        u.setCognomeTitolare(rs.getString("cognome_titolare"));
        u.setTelefono(rs.getString("telefono"));
        u.setPartitaIva(rs.getString("partita_iva"));
        u.setCodiceFiscale(rs.getString("codice_fiscale"));
        u.setIndirizzo(rs.getString("indirizzo"));
        u.setCitta(rs.getString("citta"));
        u.setCap(rs.getString("cap"));
        u.setProvincia(rs.getString("provincia"));
        u.setLogoBase64(rs.getString("logo_base64"));
        return u;
    };

    /**
     * JdbcTemplate
     * utilizzato per eseguire le query SQL.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Utente> findAll() {
        String sql = "SELECT * FROM utente";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * Recupera un utente tramite ID.
     */
    @Override
    public Utente findById(Long id) {
        String sql = "SELECT * FROM utente WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Recupera un utente tramite email.
     * Utilizzato principalmente nel login.
     */
    @Override
    public Utente findByEmail(String email) {
        String sql = "SELECT * FROM utente WHERE email = ?";
        return jdbcTemplate.query(sql, rowMapper, email)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Inserisce un nuovo utente nel database.
     */
    @Override
    public Utente save(Utente utente) {

        // Applica eventuali formattazioni o regole di business
        utente.formattaDati();

        String sql = "INSERT INTO utente (email, password, nome, nome_azienda, nome_titolare, cognome_titolare, telefono, partita_iva, codice_fiscale, indirizzo, citta, cap, provincia, logo_base64) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Serve per recuperare l'id generato automaticamente
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getPassword());
            ps.setString(3, utente.getNome());
            ps.setString(4, utente.getNomeAzienda());
            ps.setString(5, utente.getNomeTitolare());
            ps.setString(6, utente.getCognomeTitolare());
            ps.setString(7, utente.getTelefono());
            ps.setString(8, utente.getPartitaIva());
            ps.setString(9, utente.getCodiceFiscale());
            ps.setString(10, utente.getIndirizzo());
            ps.setString(11, utente.getCitta());
            ps.setString(12, utente.getCap());
            ps.setString(13, utente.getProvincia());
            ps.setString(14, utente.getLogoBase64());

            return ps;
        }, keyHolder);

        // Recupero dell'id generato dal database
        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("id")) {
            utente.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        }

        return utente;
    }

    /**
     * Aggiorna i dati di un utente esistente.
     */
    @Override
    public Utente update(Utente utente) {

        utente.formattaDati();

        String sql = "UPDATE utente SET email=?, password=?, nome=?, nome_azienda=?, nome_titolare=?, cognome_titolare=?, telefono=?, partita_iva=?, codice_fiscale=?, indirizzo=?, citta=?, cap=?, provincia=?, logo_base64=? WHERE id=?";

        jdbcTemplate.update(sql,
                utente.getEmail(),
                utente.getPassword(),
                utente.getNome(),
                utente.getNomeAzienda(),
                utente.getNomeTitolare(),
                utente.getCognomeTitolare(),
                utente.getTelefono(),
                utente.getPartitaIva(),
                utente.getCodiceFiscale(),
                utente.getIndirizzo(),
                utente.getCitta(),
                utente.getCap(),
                utente.getProvincia(),
                utente.getLogoBase64(),
                utente.getId());

        return utente;
    }
}
