package de.hhu.anemic.warenkorb.domain.model.warenkorb;

import org.springframework.stereotype.Repository;

@Repository
public interface WarenkorbRepository {
    Warenkorb findeMit(WarenkorbID warenkorbId);
    void speichere(Warenkorb warenkorb);
    void entferne(WarenkorbID warenkorbId);
}
