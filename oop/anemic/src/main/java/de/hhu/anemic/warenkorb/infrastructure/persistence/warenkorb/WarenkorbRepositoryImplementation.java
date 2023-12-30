package de.hhu.anemic.warenkorb.infrastructure.persistence.warenkorb;

import de.hhu.anemic.warenkorb.domain.model.warenkorb.Warenkorb;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbID;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbRepository;

public class WarenkorbRepositoryImplementation implements WarenkorbRepository {
    private final WarenkorbDAO warenkorbDAO;

    public WarenkorbRepositoryImplementation(WarenkorbDAO warenkorbDAO) {
        this.warenkorbDAO = warenkorbDAO;
    }

    @Override
    public Warenkorb findeMit(WarenkorbID warenkorbID) {
        WarenkorbDTO warenkorbDTO = warenkorbDAO.read(warenkorbID.ID());
        return WarenkorbDTOMapper.INSTANCE.vonDTOZuWarenkorb(warenkorbDTO);
    }

    @Override
    public void speichere(Warenkorb warenkorb) {
        WarenkorbDTO warenkorbDTO = WarenkorbDTOMapper.INSTANCE.vonWarenkorbZuDTO(warenkorb);
        warenkorbDAO.update(warenkorbDTO);

    }

    @Override
    public void entferne(WarenkorbID warenkorbID) {
        warenkorbDAO.delete(warenkorbID.ID());
    }
}
