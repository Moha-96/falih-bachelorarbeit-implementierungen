package de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile;

import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.anemic.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;

public class Warenkorbzeile {
    private final WarenkorbzeileID ID;
    private final ArtikelID artikelID;
    private Anzahl anzahl;
    private final Preis preis;
    private final Anzahl maxArtikelAnzahl;

    public Warenkorbzeile(WarenkorbzeileID ID, ArtikelID artikelID, Anzahl anzahl, Preis preis, Anzahl maxArtikelAnzahl) {
        this.ID = ID;
        this.artikelID = artikelID;
        this.anzahl = anzahl;
        this.preis = preis;
        this.maxArtikelAnzahl = maxArtikelAnzahl;
    }

    public WarenkorbzeileID getID() {
        return ID;
    }

    public ArtikelID getArtikelID() {
        return artikelID;
    }

    public Anzahl getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(Anzahl anzahl) {
        this.anzahl = anzahl;
    }

    public Preis getPreis() {
        return preis;
    }

    public Anzahl getMaxArtikelAnzahl() {
        return maxArtikelAnzahl;
    }

}


