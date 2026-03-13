package uni.lignosuiteapi.dao;

import uni.lignosuiteapi.model.PreventivoItem;

import java.util.List;

public interface PreventivoItemDao {
    List<PreventivoItem> findByPreventivoId(Long preventivoId);

    void saveAll(Long preventivoId, List<PreventivoItem> items);

    void deleteByPreventivoId(Long preventivoId);
}
