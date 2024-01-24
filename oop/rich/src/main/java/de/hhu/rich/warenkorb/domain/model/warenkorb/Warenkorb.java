package de.hhu.rich.warenkorb.domain.model.warenkorb;

import de.hhu.rich.warenkorb.domain.model.PflichtfeldFehlt;
import de.hhu.rich.warenkorb.domain.model.artikel.Artikel;
import de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.rich.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.rich.warenkorb.domain.model.common.preis.Preis;
import de.hhu.rich.warenkorb.domain.model.kunde.KundeID;
import de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile;
import de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile.WarenkorbzeileID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Warenkorb {

    private final WarenkorbID ID;
    private final KundeID kundeID;
    private final List<Warenkorbzeile> warenkorbzeilen;
    private Preis gesamtPreis;
    private final Preis maxEinkaufswert;

    public Warenkorb(WarenkorbID ID, KundeID kundeID, Preis maxEinkaufswert) {
        this.ID = ID;
        this.kundeID = kundeID;
        this.warenkorbzeilen = new ArrayList<>();
        this.gesamtPreis = new Preis(new BigDecimal(0), "EUR");
        this.maxEinkaufswert = maxEinkaufswert;
        this.validiere();
    }

    public Warenkorb(WarenkorbID ID,
                     KundeID kundeID,
                     List<Warenkorbzeile> warenkorbzeilen,
                     Preis gesamtPreis,
                     Preis maxEinkaufswert) {
        this.ID = ID;
        this.kundeID = kundeID;
        this.warenkorbzeilen = new ArrayList<>(warenkorbzeilen);
        this.gesamtPreis = gesamtPreis;
        this.maxEinkaufswert = maxEinkaufswert;
        this.validiere();
    }

    public WarenkorbID getWarenkorbID() {
        return ID;
    }

    public KundeID getKundeID() {
        return kundeID;
    }

    public List<Warenkorbzeile> getWarenkorbzeilen() {
        return warenkorbzeilen;
    }

    public Preis getGesamtPreis() {
        return gesamtPreis;
    }

    public Preis getMaxEinkaufswert() {
        return maxEinkaufswert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warenkorb warenkorb = (Warenkorb) o;
        return ID.equals(warenkorb.ID);
    }

    @Override
    public int hashCode() {
        if(ID != null) return ID.hashCode();
        else return super.hashCode();
    }

    @Override
    public String toString() {
        return "Warenkorb{" +
                "id=" + ID +
                ", kundeID=" + kundeID +
                ", warenkorbzeilen=" + warenkorbzeilen +
                ", gesamtPreis=" + gesamtPreis +
                ", maxEinkaufswert=" + maxEinkaufswert +
                '}';
    }

    public Warenkorbzeile findeZeileZu(ArtikelID artikelId) {
        return warenkorbzeilen.stream()
                .filter(a -> a.getArtikelID().equals(artikelId))
                .findFirst()
                .orElse(null);
    }

    public void fuegeHinzu(Artikel artikel, Anzahl anzahl) {
        Warenkorbzeile zeileMitArtikel = findeZeileZu(artikel.getID());

        if(zeileMitArtikel != null) {
            zeileMitArtikel.erhoeheUm(anzahl);
        } else {
            warenkorbzeilen.add(
                    new Warenkorbzeile(
                            new WarenkorbzeileID(UUID.randomUUID()),
                            artikel.getID(),
                            anzahl,
                            artikel.getPreis(),
                            new Anzahl(10)));
        }
        Preis gesamtpreisVonArtikel = findeZeileZu(artikel.getID()).berechneGesamtpreis();
        this.gesamtPreis = gesamtPreis.erhoeheUm(gesamtpreisVonArtikel);
        this.validiere();
    }

    public void reduziere(Artikel artikel, Anzahl anzahl) {
        Warenkorbzeile zeileMitArtikel = findeZeileZu(artikel.getID());

        if (zeileMitArtikel != null) {
            zeileMitArtikel.reduziereUm(anzahl);
            if (zeileMitArtikel.getAnzahl() == null) {
                warenkorbzeilen.remove(zeileMitArtikel);
            }
            Preis gesamtpreisVonArtikel = zeileMitArtikel.berechneGesamtpreis();
            this.gesamtPreis = gesamtPreis.reduziereUm(gesamtpreisVonArtikel);
        }
        validiere();
    }

    public void entferne(Artikel artikel) {
        Warenkorbzeile zeileMitArtikel = findeZeileZu(artikel.getID());

        if (zeileMitArtikel != null) {
            Preis gesamtpreisVonArtikel = zeileMitArtikel.berechneGesamtpreis();
            warenkorbzeilen.remove(zeileMitArtikel);
            this.gesamtPreis = gesamtPreis.reduziereUm(gesamtpreisVonArtikel);
        }
        validiere();
    }

    private void validiere() {
        if (ID == null) {
            throw new PflichtfeldFehlt("WarenkorbzeileID darf nicht null sein!");
        }
        if (kundeID == null) {
            throw new PflichtfeldFehlt("KundeId darf nicht null sein!");
        }
        if (gesamtPreis == null) {
            throw new PflichtfeldFehlt("Gesamt darf nicht null sein!");
        }
        if (maxEinkaufswert == null) {
            throw new PflichtfeldFehlt("Maximaler Einkaufswert darf nicht null sein!");
        }
        if (getGesamtPreis().istGroesserAls(maxEinkaufswert)) {
            throw new IllegalStateException("Maximaler Einkaufswert ist überschritten.");
        }
    }
}

