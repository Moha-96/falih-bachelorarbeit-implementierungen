package de.hhu.rich.warenkorb.domain.model.common.preis;

import java.math.BigDecimal;

public record Preis(BigDecimal betrag, String waehrung) {
    public Preis {
        if (betrag.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Betrag darf nicht negativ sein");
        }
    }

    public Preis erhoeheUm(Preis betrag) {
        return new Preis(this.betrag.add(betrag.betrag), betrag.waehrung());
    }

    public Preis reduziereUm(Preis betrag) {
        return new Preis(this.betrag.subtract(betrag.betrag), betrag.waehrung());
    }

    public boolean istGroesserAls(Preis maxEinkaufswert) {
        return betrag.compareTo(maxEinkaufswert.betrag) > 0;
    }
}
