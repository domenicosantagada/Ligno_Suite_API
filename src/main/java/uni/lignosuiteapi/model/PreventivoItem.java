package uni.lignosuiteapi.model;

import lombok.Data;

@Data
public class PreventivoItem {
    private String id;
    private String description;
    private Double quantity;
    private String unitaMisura;
    private Double rate;
    private Double amount;
}
