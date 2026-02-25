package uni.lignosuiteapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String password;

    private String nomeAzienda;
    private String nomeTitolare;
    private String cognomeTitolare;
    private String telefono;
    private String partitaIva;
    private String codiceFiscale;
    private String indirizzo;
    private String citta;
    private String cap;
    private String provincia;

    // Il logo convertito in stringa Base64 può essere molto lungo,
    // quindi forziamo il database a usare il tipo TEXT (o LONGTEXT).
    @Column(columnDefinition = "TEXT")
    private String logoBase64;
}
