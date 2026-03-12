package uni.lignosuiteapi.model;

import lombok.Data;

@Data
public class Cliente {
    private Long id;
    private Long utenteId;
    private String nome;
    private String email;
    private String telefono;
    private String partitaIva;

    public void formattaDati() {
        if (this.nome != null) this.nome = capitalizzaParole(this.nome);
        if (this.email != null) this.email = this.email.trim().toLowerCase();
        if (this.partitaIva != null) this.partitaIva = this.partitaIva.trim().toUpperCase();
        if (this.telefono != null) this.telefono = this.telefono.trim();
    }

    private String capitalizzaParole(String str) {
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
