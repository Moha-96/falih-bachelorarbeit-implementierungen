package de.hhu.rich.warenkorb.domain.model.artikel;

import de.hhu.rich.warenkorb.domain.model.PflichtfeldFehlt;
import de.hhu.rich.warenkorb.domain.model.common.preis.Preis;

import java.math.BigDecimal;

public class Artikel {
    private final ArtikelID ID;
    private String name;
    private String beschreibung;
    private Preis preis;

    public Artikel(ArtikelID ID, String name, String beschreibung, Preis preis) {
        this.ID = ID;
        this.name = name;
        this.beschreibung = beschreibung;
        this.preis = preis;
        validiere();
    }

    public ArtikelID getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Preis getPreis() {
        return preis;
    }

    private void validiere() {
        if(ID == null) {
            throw new PflichtfeldFehlt("ArtikelID");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new PflichtfeldFehlt("Artikel Name");
        }
        if(beschreibung == null || beschreibung.trim().isEmpty()) {
            throw new PflichtfeldFehlt("Artikel Beschreibung");
        }
        if (preis == null) {
            throw new PflichtfeldFehlt("Artikel Preis");
        }
    }

    public void setzeNeuenNamen(String neuerName) {
        if (neuerName == null || neuerName.trim().isEmpty()) {
            throw new PflichtfeldFehlt("Neuer Name darf nicht null oder leer sein.");
        }
        this.name = neuerName;
    }

    public void setzeNeuenPreis(Preis neuerPreis) {
        if (neuerPreis == null || neuerPreis.betrag().compareTo(BigDecimal.ZERO) < 0) {
            throw new PflichtfeldFehlt("Neuer Preis darf nicht null sein und muss nicht-negativ sein.");
        }
        this.preis = neuerPreis;
    }

    public void setzeNeueBeschreibung(String neueBeschreibung) {
        if (neueBeschreibung == null || neueBeschreibung.trim().isEmpty()) {
            throw new PflichtfeldFehlt("Neue Beschreibung darf nicht null oder leer sein.");
        }
        this.beschreibung = neueBeschreibung;
    }

}

