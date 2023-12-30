package de.hhu.rich.warenkorb.infrastructure.persistence.artikel;

import de.hhu.rich.warenkorb.domain.model.artikel.Artikel;
import de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.rich.warenkorb.domain.model.artikel.ArtikelRepository;

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
        ArtikelDTO artikelDTO = ArtikelDTOMapper.INSTANCE.vonArtikelZuDTO(artikel);
        artikelDAO.update(artikelDTO);

    }

    @Override
    public void entferne(ArtikelID artikelID) {
        artikelDAO.delete(artikelID.ID());
    }
}
