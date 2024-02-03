package de.hhu.rich.warenkorb.domain.model.warenkorb.warenkorbzeile;

import de.hhu.rich.warenkorb.domain.model.PflichtfeldFehlt;
import de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.rich.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.rich.warenkorb.domain.model.common.preis.Preis;

import java.math.BigDecimal;

public class Warenkorbzeile {
    private final WarenkorbzeileID ID;
    private final ArtikelID artikelID;
    private Anzahl anzahl;
    private final Preis preis;
    private final Anzahl maxArtikelAnzahl;

    public WarenkorbzeileID getID() {
        return ID;
    }

    public ArtikelID getArtikelID() {
        return artikelID;
    }

    public Anzahl getAnzahl() {
        return anzahl;
    }

    public Preis getPreis() {
        return preis;
    }

    public Anzahl getMaxArtikelAnzahl() {
        return maxArtikelAnzahl;
    }

    public Warenkorbzeile(WarenkorbzeileID ID, ArtikelID artikelID, Anzahl anzahl, Preis preis, Anzahl maxArtikelAnzahl) {
        this.ID = ID;
        this.artikelID = artikelID;
        this.anzahl = anzahl;
        this.preis = preis;
        this.maxArtikelAnzahl = maxArtikelAnzahl;
    }

    public void erhoeheUm(Anzahl anzahl) {
        this.anzahl = this.anzahl.erhoeheUm(anzahl);
        validiere();
    }

    public void reduziereUm(Anzahl anzahl) {
        this.anzahl = this.anzahl.reduziereUm(anzahl);
        validiere();
    }

    public Preis berechneGesamtpreis() {
        BigDecimal gesamtBetrag = getPreis().betrag().multiply(new BigDecimal(getAnzahl().anzahl()));
        return new Preis(gesamtBetrag, getPreis().waehrung());
    }

    private void validiere(){
        if (ID == null) {
            throw new PflichtfeldFehlt("WarenkorbzeileID darf nicht null sein!");
        }
        if (artikelID == null) {
            throw new PflichtfeldFehlt("ArtikelID darf nicht null sein!");
        }
        if (anzahl == null || anzahl.anzahl() < 0) {
            throw new PflichtfeldFehlt("Anzahl darf nicht null oder kleiner als 0 sein!");
        }
        if (preis == null) {
            throw new PflichtfeldFehlt("Preis darf nicht null sein!");
        }
        if (maxArtikelAnzahl == null) {
            throw new PflichtfeldFehlt("Maximale Artikel Anzahl darf nicht null sein!");
        }
        if(anzahl.istGroesserAls(maxArtikelAnzahl)) {
            throw new IllegalStateException("Maximale Anzahl von Artikel ist überschritten.");
        }
    }
}

