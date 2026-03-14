package uni.lignosuiteapi.dao;

import uni.lignosuiteapi.model.Utente;

import java.util.List;

public interface UtenteDao {

    List<Utente> findAll();

    Utente findById(Long id);

    Utente findByEmail(String email);

    Utente save(Utente utente);

    Utente update(Utente utente);
}
