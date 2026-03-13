package uni.lignosuiteapi.model;

import lombok.Data;

import java.util.List;

@Data
public class Preventivo {
    private Long id;
    private Long invoiceNumber;
    private Long utenteId;
    private String date;

    private String fromName;
    private String fromEmail;
    private String fromPiva;

    private String toName;
    private String toEmail;
    private String toPiva;

    private List<PreventivoItem> items;

    private Double taxRate;
    private Double subtotal;
    private Double taxAmount;
    private Double discount;
    private Double total;
}
