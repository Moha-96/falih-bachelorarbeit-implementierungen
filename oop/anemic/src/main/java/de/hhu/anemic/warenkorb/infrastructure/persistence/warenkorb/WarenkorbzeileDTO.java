package de.hhu.anemic.warenkorb.infrastructure.persistence.warenkorb;

import java.math.BigDecimal;
import java.util.UUID;

public record WarenkorbzeileDTO(UUID ID,
                                UUID artikelID,
                                int anzahl,
                                BigDecimal preis,
                                int maxArtikelAnzahl ) {
}
