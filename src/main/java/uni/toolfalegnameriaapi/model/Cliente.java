package uni.toolfalegnameriaapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Spring Boot genererà l'ID in automatico

    private Long utenteId; // L'ID dell'utente proprietario

    private String nome;
    private String email;
    private String telefono;
    private String partitaIva;
}
