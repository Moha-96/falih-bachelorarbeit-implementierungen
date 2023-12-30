package de.hhu.anemic.warenkorb.domain.model.common.preis;

import java.math.BigDecimal;

public record Preis(BigDecimal betrag, String waehrung) {
}
