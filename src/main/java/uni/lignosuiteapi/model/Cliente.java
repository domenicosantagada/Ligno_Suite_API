package uni.lignosuiteapi.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entità che rappresenta un Cliente all'interno della rubrica di un falegname (utente).
 */
@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chiave primaria generata dal DB (Auto Increment)

    private Long utenteId; // ID dell'utente (falegname) a cui questo cliente è associato
    private String nome;
    private String email;
    private String telefono;
    private String partitaIva;

    @PrePersist // Eseguito in automatico prima di una INSERT nel DB
    @PreUpdate  // Eseguito in automatico prima di un UPDATE nel DB
    public void formattaDati() {
        // 1. Il nome (o ragione sociale) avrà sempre le iniziali maiuscole
        if (this.nome != null) {
            this.nome = capitalizzaParole(this.nome);
        }
        // 2. L'email sarà sempre tutta minuscola e senza spazi accidentali
        if (this.email != null) {
            this.email = this.email.trim().toLowerCase();
        }
        // 3. La Partita IVA (o Codice Fiscale) sarà sempre tutta maiuscola e senza spazi
        if (this.partitaIva != null) {
            this.partitaIva = this.partitaIva.trim().toUpperCase();
        }
        // 4. Tolgo eventuali spazi messi per sbaglio prima o dopo il telefono
        if (this.telefono != null) {
            this.telefono = this.telefono.trim();
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
