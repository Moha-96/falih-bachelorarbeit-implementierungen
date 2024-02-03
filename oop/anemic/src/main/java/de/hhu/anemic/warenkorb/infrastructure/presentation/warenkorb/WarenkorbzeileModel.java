package de.hhu.anemic.warenkorb.infrastructure.presentation.warenkorb;

import java.math.BigDecimal;

public record WarenkorbzeileModel(String ID,
                                  String artikel,
                                  int anzahl,
                                  BigDecimal preis,
                                  int maxArtikelAnzahl) {
}
