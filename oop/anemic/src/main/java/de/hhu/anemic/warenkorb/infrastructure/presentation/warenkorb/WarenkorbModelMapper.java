package de.hhu.anemic.warenkorb.infrastructure.presentation.warenkorb;

import de.hhu.anemic.warenkorb.domain.model.warenkorb.Warenkorb;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WarenkorbModelMapper {

    WarenkorbModelMapper INSTANCE = Mappers.getMapper(WarenkorbModelMapper.class);

    WarenkorbModel vonWarenkorbZuModel(Warenkorb warenkorb);
    Warenkorb vonModelZuWarenkorb(WarenkorbModel warenkorbModel);
}
