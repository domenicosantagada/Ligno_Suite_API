package uni.lignosuiteapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uni.lignosuiteapi.model.Preventivo;

import java.util.List;

@Repository
public interface PreventivoRepository extends JpaRepository<Preventivo, Long> {

    // Query Method automatico: Trova tutti i preventivi associati a un utente
    List<Preventivo> findByUtenteId(Long utenteId);

    // Restituisce true se esiste già una combinazione di quell'utente con quel numero fattura
    boolean existsByUtenteIdAndInvoiceNumber(Long utenteId, Long invoiceNumber);

    /**
     * QUERY PERSONALIZZATA (JPQL - Java Persistence Query Language)
     * Quando il nome del metodo non basta a generare una query complessa,
     * possiamo scriverla noi usando @Query.
     * * ATTENZIONE: Questa non è SQL standard, è JPQL! Parla con le classi Java, non con le tabelle.
     * "Preventivo" è il nome della classe, "p.invoiceNumber" è la proprietà della classe.
     * * Spiegazione della query:
     * 1. MAX(p.invoiceNumber): Trova il numero di preventivo più alto.
     * 2. COALESCE(..., 0): Funzione di sicurezza. Se l'utente non ha ancora preventivi,
     * MAX restituirebbe 'null'. COALESCE converte quel 'null' in '0'.
     * 3. WHERE p.utenteId = :utenteId: Filtra solo per l'utente specifico.
     * * @Param("utenteId"): Associa il parametro del metodo Java al segnaposto ":utenteId" nella query.
     */
    @Query("SELECT COALESCE(MAX(p.invoiceNumber), 0) FROM Preventivo p WHERE p.utenteId = :utenteId")
    Long findMaxInvoiceNumberByUtenteId(@Param("utenteId") Long utenteId);
}
