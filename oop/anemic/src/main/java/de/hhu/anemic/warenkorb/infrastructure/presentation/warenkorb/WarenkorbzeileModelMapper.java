package de.hhu.anemic.warenkorb.infrastructure.presentation.warenkorb;

import de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WarenkorbzeileModelMapper {

    WarenkorbzeileModelMapper INSTANCE = Mappers.getMapper(WarenkorbzeileModelMapper.class);

    WarenkorbzeileModel vonWarenkorbzeileZuModel(Warenkorbzeile warenkorbzeile);
    Warenkorbzeile vonModelZuWarenkorbzeile(WarenkorbzeileModel warenkorbzeileModel);
}
