package uni.lignosuiteapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.model.PreventivoItem;

import java.util.List;

@Repository
public class PreventivoItemDaoImpl implements PreventivoItemDao {

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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PreventivoItem> findByPreventivoId(Long preventivoId) {
        String sql = "SELECT * FROM preventivo_item WHERE preventivo_id = ?";
        return jdbcTemplate.query(sql, rowMapper, preventivoId);
    }

    @Override
    public void saveAll(Long preventivoId, List<PreventivoItem> items) {
        String sql = "INSERT INTO preventivo_item (id, description, quantity, unita_misura, rate, amount, preventivo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        for (PreventivoItem item : items) {
            jdbcTemplate.update(sql, item.getId(), item.getDescription(), item.getQuantity(), item.getUnitaMisura(), item.getRate(), item.getAmount(), preventivoId);
        }
    }

    @Override
    public void deleteByPreventivoId(Long preventivoId) {
        String sql = "DELETE FROM preventivo_item WHERE preventivo_id = ?";
        jdbcTemplate.update(sql, preventivoId);
    }
}
