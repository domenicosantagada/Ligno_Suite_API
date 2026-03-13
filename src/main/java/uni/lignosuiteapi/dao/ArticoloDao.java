package uni.lignosuiteapi.dao;

import uni.lignosuiteapi.model.Articolo;

import java.util.List;

public interface ArticoloDao {
    List<Articolo> findByUtenteId(Long utenteId);

    Articolo findById(Long id);

    Articolo save(Articolo articolo);

    Articolo update(Articolo articolo);

    void deleteById(Long id);
}
