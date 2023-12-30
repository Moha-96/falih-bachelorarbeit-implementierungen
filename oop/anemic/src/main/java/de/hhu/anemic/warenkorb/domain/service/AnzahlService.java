package de.hhu.anemic.warenkorb.domain.service;

import de.hhu.anemic.warenkorb.domain.model.common.anzahl.Anzahl;
import org.springframework.stereotype.Service;

@Service
public class AnzahlService {
    public Anzahl erhoeheUm(Anzahl aktuelleAnzahl, Anzahl zuErhoehendeAnzahl) {
        return new Anzahl(aktuelleAnzahl.anzahl() + zuErhoehendeAnzahl.anzahl());
    }

    public Anzahl reduziereUm(Anzahl aktuelleAnzahl, Anzahl zuReduzierendeAnzahl) {
        if (aktuelleAnzahl.anzahl() < zuReduzierendeAnzahl.anzahl()) {
            throw new IllegalArgumentException("Die zu reduzierende Anzahl darf nicht größer als die aktuelle Anzahl sein.");
        }
        return new Anzahl(aktuelleAnzahl.anzahl() - zuReduzierendeAnzahl.anzahl());
    }

    public boolean istGroesserAls(Anzahl aktuelleAnzahl, Anzahl maxArtikelAnzahl) {
        return aktuelleAnzahl.anzahl() > maxArtikelAnzahl.anzahl();
    }

    public Anzahl erstelleAnzahl(Integer menge) {
        Anzahl anzahl = new Anzahl(menge);
        validiereAnzahl(anzahl);
        return anzahl;
    }

    private void validiereAnzahl(Anzahl anzahl) {
        if(anzahl.anzahl() < 0) {
            throw new IllegalArgumentException("Die Anzahl darf nicht negativ sein.");
        }
    }
}
