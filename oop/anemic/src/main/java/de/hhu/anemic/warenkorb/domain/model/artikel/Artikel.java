package de.hhu.anemic.warenkorb.domain.model.artikel;

import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setPreis(Preis preis) {
        this.preis = preis;
    }

}

