package uni.lignosuiteapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class PreventivoItem {

    @Id
    private String id; // <-- CAMBIA DA Long a String e RIMUOVI @GeneratedValue

    private String description;
    private Double quantity;
    private String unitaMisura;
    private Double rate;
    private Double amount;
}

