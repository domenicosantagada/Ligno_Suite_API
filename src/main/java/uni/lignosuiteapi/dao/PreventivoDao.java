package uni.lignosuiteapi.dao;

import uni.lignosuiteapi.model.Preventivo;

import java.util.List;

public interface PreventivoDao {
    List<Preventivo> findAllByUtenteId(Long utenteId);

    Preventivo findById(Long id);

    Preventivo save(Preventivo preventivo);

    Preventivo update(Preventivo preventivo);

    void deleteById(Long id, Long utenteId);

    Long getNextInvoiceNumber(Long utenteId);
}
