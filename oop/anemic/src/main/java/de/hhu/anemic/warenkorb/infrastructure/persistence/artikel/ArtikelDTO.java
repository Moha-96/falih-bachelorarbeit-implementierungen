package de.hhu.anemic.warenkorb.infrastructure.persistence.artikel;

import java.math.BigDecimal;
import java.util.UUID;

public record ArtikelDTO(UUID ID,
                         String name,
                         String beschreibung,
                         BigDecimal preis) {
}
