package de.hhu.anemic.warenkorb.domain.model.artikel;

import org.springframework.stereotype.Repository;

@Repository
public interface ArtikelRepository {
    Artikel findeMit(ArtikelID artikelId);
    void speichere(Artikel artikel);
    void entferne(ArtikelID artikelID);
}
