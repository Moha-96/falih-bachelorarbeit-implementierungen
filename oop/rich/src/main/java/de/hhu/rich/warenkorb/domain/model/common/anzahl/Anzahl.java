package de.hhu.rich.warenkorb.domain.model.common.anzahl;

public record Anzahl(int anzahl) {
    public Anzahl {
        if(anzahl < 0) {
            throw new IllegalArgumentException("Die Anzahl darf nicht negativ sein.");
        }
    }

    public Anzahl erhoeheUm(Anzahl anzahl) {
        return new Anzahl(this.anzahl + anzahl.anzahl);
    }

    public Anzahl reduziereUm(Anzahl anzahl) {
        return new Anzahl(this.anzahl - anzahl.anzahl);
    }

    public boolean istGroesserAls(Anzahl maxArtikelAnzahl){
        return anzahl > maxArtikelAnzahl.anzahl();
    }
}
