package uni.toolfalegnameriaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.toolfalegnameriaapi.model.Cliente;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Trova tutti i clienti di un utente specifico
    List<Cliente> findByUtenteId(Long utenteId);
}
