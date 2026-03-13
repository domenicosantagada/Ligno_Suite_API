package uni.lignosuiteapi.model;

import lombok.Data;

/**
 * Classe modello che rappresenta un utente del sistema.
 * <p>
 * Questa classe corrisponde alla tabella "utente" nel database
 * e contiene tutti i campi relativi al profilo dell'utente.
 *
 * @Data (Lombok)
 * Genera automaticamente:
 * - getter e setter
 * - toString()
 * - equals() e hashCode()
 * <p>
 * In questo modo evitiamo di scrivere manualmente molto codice.
 */
@Data
public class Utente {

    // Identificativo univoco dell'utente (chiave primaria nel database)
    private Long id;

    // Credenziali di accesso
    private String email;
    private String password;

    // Informazioni personali / aziendali
    private String nome;
    private String nomeAzienda;
    private String nomeTitolare;
    private String cognomeTitolare;

    // Contatti
    private String telefono;

    // Dati fiscali
    private String partitaIva;
    private String codiceFiscale;

    // Indirizzo
    private String indirizzo;
    private String citta;
    private String cap;
    private String provincia;

    // Logo aziendale salvato come stringa Base64
    private String logoBase64;


    /**
     * Metodo di formattazione dei dati.
     * <p>
     * Viene richiamato manualmente dal DAO prima di salvare o aggiornare
     * un utente nel database.
     * <p>
     * Serve per normalizzare i dati inseriti dall'utente
     * (es. rimuovere spazi, sistemare maiuscole/minuscole).
     */
    public void formattaDati() {

        // Email in minuscolo senza spazi
        if (this.email != null)
            this.email = this.email.trim().toLowerCase();

        // Capitalizzazione delle parole (Nome Cognome ecc.)
        if (this.nome != null)
            this.nome = capitalizzaParole(this.nome);

        if (this.nomeAzienda != null)
            this.nomeAzienda = capitalizzaParole(this.nomeAzienda);

        if (this.nomeTitolare != null)
            this.nomeTitolare = capitalizzaParole(this.nomeTitolare);

        if (this.cognomeTitolare != null)
            this.cognomeTitolare = capitalizzaParole(this.cognomeTitolare);

        if (this.indirizzo != null)
            this.indirizzo = capitalizzaParole(this.indirizzo);

        if (this.citta != null)
            this.citta = capitalizzaParole(this.citta);

        // Partita IVA e Codice Fiscale in maiuscolo
        if (this.partitaIva != null)
            this.partitaIva = this.partitaIva.trim().toUpperCase();

        if (this.codiceFiscale != null)
            this.codiceFiscale = this.codiceFiscale.trim().toUpperCase();

        // Provincia in maiuscolo
        if (this.provincia != null)
            this.provincia = this.provincia.trim().toUpperCase();

        // Pulizia di altri campi
        if (this.telefono != null)
            this.telefono = this.telefono.trim();

        if (this.cap != null)
            this.cap = this.cap.trim();
    }


    /**
     * Metodo di utilità che capitalizza le parole di una stringa.
     * <p>
     * Esempio:
     * "mario rossi" -> "Mario Rossi"
     */
    private String capitalizzaParole(String str) {

        str = str.trim();

        // Se la stringa è vuota ritorna direttamente
        if (str.isEmpty())
            return str;

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
