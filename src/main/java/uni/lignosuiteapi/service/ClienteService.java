package uni.lignosuiteapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uni.lignosuiteapi.dao.ClienteDao;
import uni.lignosuiteapi.model.Cliente;

import java.util.List;

/**
 * Service per la gestione dei clienti.
 */
@Service
public class ClienteService {

    // Il Service chiama il DAO per accedere ai dati.
    @Autowired
    private ClienteDao clienteDao;

    public List<Cliente> getAllClienti(Long utenteId) {
        return clienteDao.findAllByUtenteId(utenteId);
    }

    public Cliente createCliente(Cliente cliente) {
        // Qui in futuro potresti aggiungere logiche, es:
        // "Se il cliente non ha la Partita IVA, metti 'N/D'"
        return clienteDao.save(cliente);
    }

    public Cliente updateCliente(Long id, Cliente cliente) {
        // LOGICA DI BUSINESS: Verifichiamo che l'utente stia modificando un SUO cliente
        Cliente clienteEsistente = clienteDao.findById(id);

        if (clienteEsistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile aggiornare: cliente non trovato.");
        }
        if (!clienteEsistente.getUtenteId().equals(cliente.getUtenteId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato.");
        }

        cliente.setId(id);
        return clienteDao.update(cliente);
    }

    public void deleteCliente(Long id, Long utenteId) {
        // LOGICA DI BUSINESS: Verifichiamo prima di eliminare
        Cliente clienteEsistente = clienteDao.findById(id);

        if (clienteEsistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente non trovato.");
        }
        if (!clienteEsistente.getUtenteId().equals(utenteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato.");
        }

        clienteDao.deleteById(id, utenteId);
    }
}
