package uni.lignosuiteapi.model;

import lombok.Data;

/**
 * Classe modello che rappresenta un cliente.
 * <p>
 * Questa classe corrisponde alla tabella "cliente" nel database
 * e contiene le informazioni principali dei clienti associati
 * ad un utente del sistema.
 *
 * @Data (Lombok)
 * Genera automaticamente:
 * - getter
 * - setter
 * - toString()
 * - equals() e hashCode()
 */
@Data
public class Cliente {

    // Identificativo univoco del cliente
    private Long id;

    // ID dell'utente proprietario del cliente
    private Long utenteId;

    // Dati principali del cliente
    private String nome;
    private String email;
    private String telefono;
    private String partitaIva;

    /**
     * Metodo che normalizza i dati prima del salvataggio nel database.
     * Viene richiamato dal DAO prima di eseguire INSERT o UPDATE.
     */
    public void formattaDati() {

        // Capitalizza il nome (es. "mario rossi" -> "Mario Rossi")
        if (this.nome != null)
            this.nome = capitalizzaParole(this.nome);

        // Email in minuscolo senza spazi
        if (this.email != null)
            this.email = this.email.trim().toLowerCase();

        // Partita IVA in maiuscolo
        if (this.partitaIva != null)
            this.partitaIva = this.partitaIva.trim().toUpperCase();

        // Rimozione spazi nel telefono
        if (this.telefono != null)
            this.telefono = this.telefono.trim();
    }

    /**
     * Metodo di utilità che capitalizza le parole di una stringa.
     * <p>
     * Esempio:
     * "azienda alfa srl" -> "Azienda Alfa Srl"
     */
    private String capitalizzaParole(String str) {

        str = str.trim();

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
