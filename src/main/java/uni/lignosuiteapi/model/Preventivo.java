package uni.lignosuiteapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * @Entity: Indica a Spring Data JPA (Hibernate) che questa classe Java corrisponde
 * a una tabella nel database relazionale. Il nome della tabella sarà, di default, "preventivo".
 * * @Data: È un'annotazione della libreria "Lombok". Durante la compilazione, genera
 * automaticamente tutti i metodi Getter, Setter, toString(), equals() e hashCode(),
 * mantenendo il codice pulito e compatto.
 */
@Entity
@Data
public class Preventivo {

    /*
     * @Id: Segnala che questo campo è la Chiave Primaria (Primary Key) della tabella.
     * @GeneratedValue: Specifica come viene generato l'ID. L'opzione "IDENTITY" indica
     * che deleghiamo la generazione al database (corrisponde all'AUTO_INCREMENT in SQL).
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceNumber;
    private Long utenteId;  // ID dell'utente che ha creato il preventivo'
    private String date;

    // Dati del falegname
    private String fromName;
    private String fromEmail;
    private String fromPiva;

    // Dati del cliente
    private String toName;
    private String toEmail;
    private String toPiva;

//    /**
//     * @OneToMany: Indica una relazione Uno-A-Molti. UN preventivo contiene MOLTI articoli (items).
//     * cascade = CascadeType.ALL: Questo comando è potentissimo. Dice a JPA:
//     * "Se io salvo, aggiorno o elimino il Preventivo, applica la stessa operazione a tutti
//     * i PreventivoItem collegati ad esso".
//     * Ad esempio, se elimino il preventivo col DELETE, verranno cancellate automaticamente dal DB
//     * anche tutte le righe di spesa associate, mantenendo il database pulito (integrità referenziale).
//     */
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<PreventivoItem> items; // Le righe del preventivo


    /**
     * @OneToMany: Indica una relazione Uno-A-Molti. UN preventivo contiene MOLTI articoli (items).
     * cascade = CascadeType.ALL: Applica salvataggio/eliminazione a cascata.
     * orphanRemoval = true: Elimina fisicamente dal DB le righe che vengono rimosse dalla lista.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "preventivo_id") // Evita la creazione della terza tabella di appoggio
    private List<PreventivoItem> items;

    private Double taxRate;
    private Double subtotal;
    private Double taxAmount;
    private Double discount;
    private Double total;
}
