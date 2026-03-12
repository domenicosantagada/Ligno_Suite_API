package uni.lignosuiteapi.model;

import lombok.Data;

@Data
public class Utente {
    private Long id;
    private String email;
    private String password;
    private String nome;
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
    private String logoBase64;

    // Richiamato manualmente dal DAO prima di salvare
    public void formattaDati() {
        if (this.email != null) this.email = this.email.trim().toLowerCase();
        // ... (lascia pure qui il resto della tua logica di formattazione che avevi già scritto)
        if (this.nome != null) this.nome = capitalizzaParole(this.nome);
        if (this.nomeAzienda != null) this.nomeAzienda = capitalizzaParole(this.nomeAzienda);
        if (this.nomeTitolare != null) this.nomeTitolare = capitalizzaParole(this.nomeTitolare);
        if (this.cognomeTitolare != null) this.cognomeTitolare = capitalizzaParole(this.cognomeTitolare);
        if (this.indirizzo != null) this.indirizzo = capitalizzaParole(this.indirizzo);
        if (this.citta != null) this.citta = capitalizzaParole(this.citta);
        if (this.partitaIva != null) this.partitaIva = this.partitaIva.trim().toUpperCase();
        if (this.codiceFiscale != null) this.codiceFiscale = this.codiceFiscale.trim().toUpperCase();
        if (this.provincia != null) this.provincia = this.provincia.trim().toUpperCase();
        if (this.telefono != null) this.telefono = this.telefono.trim();
        if (this.cap != null) this.cap = this.cap.trim();
    }

    private String capitalizzaParole(String str) {
        // ... (il tuo codice per capitalizzare)
        str = str.trim();
        if (str.isEmpty()) return str;
        String[] parole = str.split("\\s+");
        StringBuilder risultato = new StringBuilder();
        for (String parola : parole) {
            if (parola.length() > 0) {
                risultato.append(Character.toUpperCase(parola.charAt(0)))
                        .append(parola.substring(1).toLowerCase()).append(" ");
            }
        }
        return risultato.toString().trim();
    }
}
