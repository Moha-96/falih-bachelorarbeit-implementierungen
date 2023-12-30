package de.hhu.anemic.warenkorb.infrastructure.persistence.warenkorb;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record WarenkorbDTO(UUID ID,
                           UUID kundeID,
                           List<WarenkorbzeileDTO> warenkorbzeilen,
                           BigDecimal gesamtPreis,
                           BigDecimal maxEinkaufswert) {
}
