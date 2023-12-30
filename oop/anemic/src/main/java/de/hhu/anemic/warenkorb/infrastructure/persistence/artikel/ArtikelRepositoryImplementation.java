package de.hhu.anemic.warenkorb.infrastructure.persistence.artikel;

import de.hhu.anemic.warenkorb.domain.model.artikel.Artikel;
import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelRepository;

public class ArtikelRepositoryImplementation implements ArtikelRepository {

    private final ArtikelDAO artikelDAO;

    public ArtikelRepositoryImplementation(ArtikelDAO artikelDAO) {
        this.artikelDAO = artikelDAO;
    }

    @Override
    public Artikel findeMit(ArtikelID artikelID) {
        ArtikelDTO artikelDTO = artikelDAO.read(artikelID.ID());
        return ArtikelDTOMapper.INSTANCE.vonDTOZuArtikel(artikelDTO);
    }

    @Override
    public void speichere(Artikel artikel) {

    }

    @Override
    public void entferne(ArtikelID artikelID) {

    }
}
