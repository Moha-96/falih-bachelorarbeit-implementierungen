package de.hhu.anemic.warenkorb.domain.service;

import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PreisService {
    public Preis erhoeheUm(Preis aktuellerBetrag, Preis zuErhoehenderBetrag) {
        return new Preis(aktuellerBetrag.betrag().add(zuErhoehenderBetrag.betrag()), zuErhoehenderBetrag.waehrung());
    }

    public Preis reduziereUm(Preis aktuellerBetrag, Preis zuReduzierenderBetrag) {
        return new Preis(aktuellerBetrag.betrag().subtract(zuReduzierenderBetrag.betrag()), zuReduzierenderBetrag.waehrung());
    }

    public boolean istGroesserAls(Preis aktuellerBetrag, Preis maxEinkaufswert) {
        return aktuellerBetrag.betrag().compareTo(maxEinkaufswert.betrag()) > 0;
    }

    public Preis erstellePreis(BigDecimal betrag, String waehrung) {
        Preis preis = new Preis(betrag, waehrung);
        validierePreis(preis);
        return preis;
    }

    private void validierePreis(Preis preis) {
        if (preis.betrag().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Betrag darf nicht negativ sein");
        }
    }
}
