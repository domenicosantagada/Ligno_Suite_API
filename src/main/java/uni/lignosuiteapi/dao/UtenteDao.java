package uni.lignosuiteapi.dao;

import uni.lignosuiteapi.model.Utente;

public interface UtenteDao {
    Utente findById(Long id);

    Utente findByEmail(String email);

    Utente save(Utente utente);

    Utente update(Utente utente);
}
