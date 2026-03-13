package uni.lignosuiteapi.dao;

import uni.lignosuiteapi.model.Cliente;

import java.util.List;

public interface ClienteDao {
    List<Cliente> findAllByUtenteId(Long utenteId);

    Cliente findById(Long id);

    Cliente save(Cliente cliente);

    Cliente update(Cliente cliente);

    void deleteById(Long id, Long utenteId);
}
