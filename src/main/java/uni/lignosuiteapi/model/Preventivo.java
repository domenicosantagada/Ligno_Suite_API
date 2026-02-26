package uni.lignosuiteapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity // <-- QUESTO E' IL PEZZO CHE MANCA O NON E' RICONOSCIUTO
@Data
public class Preventivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Vera chiave primaria del DB

    private Long invoiceNumber; // Collegato a invoiceNumber in Angular
    private Long utenteId;
    private String date;

    private String fromName;
    private String fromEmail;
    private String fromPiva;

    private String toName;
    private String toEmail;
    private String toPiva;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PreventivoItem> items; // Le righe del preventivo

    private Double taxRate;
    private Double subtotal;
    private Double taxAmount;
    private Double total;
}
