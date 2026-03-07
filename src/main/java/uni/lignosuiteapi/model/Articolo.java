package uni.lignosuiteapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Articolo {

    // Identificatore univoco dell'articolo'
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Attributi dell'articolo
    private String nome;
    private String descrizione;
    private Double prezzoAcquisto;
    private String fornitore;
    private String unitaMisura;
    private LocalDate dataAcquisto;

    // Colleghiamo l'articolo all'utente loggato per privacy dei dati
    @ManyToOne
    @JoinColumn(name = "utente_id")
    @JsonIgnore // Evita loop infiniti quando Spring trasforma i dati in JSON
    private Utente utente;

    // Logica di formattazione dei dati

    @PrePersist
    @PreUpdate
    public void formattaDati() {
        if (this.nome != null) {
            this.nome = capitalizzaParole(this.nome);
        }
        if (this.fornitore != null) {
            this.fornitore = capitalizzaParole(this.fornitore);
        }

    }

    // Metodo privato di utilità per fare l'iniziale maiuscola di ogni parola
    private String capitalizzaParole(String str) {
        str = str.trim();
        if (str.isEmpty()) return str;

        String[] parole = str.split("\\s+");
        StringBuilder risultato = new StringBuilder();

        for (String parola : parole) {
            if (parola.length() > 0) {
                risultato.append(Character.toUpperCase(parola.charAt(0)))
                        .append(parola.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return risultato.toString().trim();
    }
}
