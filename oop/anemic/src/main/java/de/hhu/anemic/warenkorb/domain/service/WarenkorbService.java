package de.hhu.anemic.warenkorb.domain.service;

import de.hhu.anemic.warenkorb.domain.model.PflichtfeldFehlt;
import de.hhu.anemic.warenkorb.domain.model.artikel.Artikel;
import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.anemic.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;
import de.hhu.anemic.warenkorb.domain.model.kunde.KundeID;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.Warenkorb;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbID;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.WarenkorbzeileID;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarenkorbService {
    private final AnzahlService anzahlService;
    private final PreisService preisService;
    private final WarenkorbzeileService warenkorbzeileService;

    public WarenkorbService(AnzahlService anzahlService, PreisService preisService, WarenkorbzeileService warenkorbzeileService) {
        this.anzahlService = anzahlService;
        this.preisService = preisService;
        this.warenkorbzeileService = warenkorbzeileService;
    }

    public Warenkorb erstelleWarenkorb(WarenkorbID ID, KundeID kundeID, Preis maxEinkaufswert) {
        Warenkorb warenkorb = new Warenkorb(ID, kundeID, maxEinkaufswert);
        validiereWarenkorb(warenkorb);
        return warenkorb;
    }

    public void fuegeArtikelHinzu(Warenkorb warenkorb, Artikel artikel, Anzahl anzahl) {
        Warenkorbzeile zeileMitArtikel = findeZeileZu(warenkorb.getWarenkorbzeilen(), artikel.getID());

        if (zeileMitArtikel != null) {
            warenkorbzeileService.erhoeheAnzahl(zeileMitArtikel, anzahl);
        } else {
            Warenkorbzeile neueZeile = warenkorbzeileService.erstelleWarenkorbzeile(
                    new WarenkorbzeileID(UUID.randomUUID()),
                    artikel.getID(),
                    anzahl,
                    artikel.getPreis(),
                    anzahlService.erstelleAnzahl(10)
            );
            warenkorb.getWarenkorbzeilen().add(neueZeile);
        }

        Preis gesamtpreisVonArtikel = warenkorbzeileService.berechneGesamtpreis(findeZeileZu(warenkorb.getWarenkorbzeilen(), artikel.getID()));
        warenkorb.setGesamtPreis(preisService.erhoeheUm(warenkorb.getGesamtPreis(), gesamtpreisVonArtikel));
        validiereWarenkorb(warenkorb);
    }

    public void reduziereArtikel(Warenkorb warenkorb, Artikel artikel, Anzahl anzahl) {
        Warenkorbzeile zeileMitArtikel = findeZeileZu(warenkorb.getWarenkorbzeilen(), artikel.getID());

        if (zeileMitArtikel != null) {
            warenkorbzeileService.reduziereAnzahl(zeileMitArtikel, anzahl);
            if (zeileMitArtikel.getAnzahl().anzahl() <= 0) {
                warenkorb.getWarenkorbzeilen().remove(zeileMitArtikel);
            }
            Preis gesamtpreisVonArtikel = warenkorbzeileService.berechneGesamtpreis(zeileMitArtikel);
            warenkorb.setGesamtPreis(preisService.reduziereUm(warenkorb.getGesamtPreis(), gesamtpreisVonArtikel));
        }
        validiereWarenkorb(warenkorb);
    }

    public void entferneArtikel(Warenkorb warenkorb, Artikel artikel) {
        Warenkorbzeile zeileMitArtikel = findeZeileZu(warenkorb.getWarenkorbzeilen(), artikel.getID());

        if (zeileMitArtikel != null) {
            Preis gesamtpreisVonArtikel = warenkorbzeileService.berechneGesamtpreis(zeileMitArtikel);
            warenkorb.getWarenkorbzeilen().remove(zeileMitArtikel);
            warenkorb.setGesamtPreis(preisService.reduziereUm(warenkorb.getGesamtPreis(), gesamtpreisVonArtikel));
        }
        validiereWarenkorb(warenkorb);
    }

    public Warenkorbzeile findeZeileZu(List<Warenkorbzeile> warenkorbzeilen, ArtikelID artikelId) {
        return warenkorbzeilen.stream()
                .filter(a -> a.getArtikelID().equals(artikelId))
                .findFirst()
                .orElse(null);
    }

    private void validiereWarenkorb(Warenkorb warenkorb) {
        if (warenkorb == null) {
            throw new IllegalArgumentException("Warenkorb darf nicht null sein!");
        }
        if (warenkorb.getWarenkorbID() == null) {
            throw new PflichtfeldFehlt("WarenkorbID darf nicht null sein!");
        }
        if (warenkorb.getKundeID() == null) {
            throw new PflichtfeldFehlt("KundeID darf nicht null sein!");
        }
        if (warenkorb.getGesamtPreis() == null) {
            throw new PflichtfeldFehlt("Gesamt darf nicht null sein!");
        }
        if (warenkorb.getMaxEinkaufswert() == null) {
            throw new PflichtfeldFehlt("Maximaler Einkaufswert darf nicht null sein!");
        }
        if (preisService.istGroesserAls(warenkorb.getGesamtPreis(), warenkorb.getMaxEinkaufswert())) {
            throw new IllegalStateException("Maximaler Einkaufswert ist überschritten.");
        }
    }
}
