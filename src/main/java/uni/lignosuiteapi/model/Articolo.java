package uni.lignosuiteapi.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Articolo {

    // Identificativo univoco dell'articolo
    private Long id;
    // Attributi dell'articolo
    private String nome;
    private String descrizione;
    private Double prezzoAcquisto;
    private String fornitore;
    private String unitaMisura;
    private LocalDate dataAcquisto;

    // Invece dell'intero oggetto Utente, salviamo solo il suo ID
    private Long utenteId;

    // Utente che ha creato l'articolo
    private Utente utente;

    // Logica di formattazione dei dati

    // Metodo richiamato manualmente dal DAO
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
