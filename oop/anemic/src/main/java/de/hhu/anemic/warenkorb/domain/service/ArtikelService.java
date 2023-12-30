package de.hhu.anemic.warenkorb.domain.service;

import de.hhu.anemic.warenkorb.domain.model.PflichtfeldFehlt;
import de.hhu.anemic.warenkorb.domain.model.artikel.Artikel;
import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ArtikelService {
    public static void setzeNeuenNamen(Artikel artikel, String neuerName) {
        if (neuerName == null || neuerName.trim().isEmpty()) {
            throw new PflichtfeldFehlt("Neuer Name darf nicht null oder leer sein.");
        }
        artikel.setName(neuerName);
        validiereArtikel(artikel);
    }

    public static void setzeNeuenPreis(Artikel artikel, Preis neuerPreis) {
        if (neuerPreis == null || neuerPreis.betrag().compareTo(BigDecimal.ZERO) < 0) {
            throw new PflichtfeldFehlt("Neuer Preis darf nicht null sein und muss nicht-negativ sein.");
        }
        artikel.setPreis(neuerPreis);
        validiereArtikel(artikel);
    }

    public static void setzeNeueBeschreibung(Artikel artikel, String neueBeschreibung) {
        if (neueBeschreibung == null || neueBeschreibung.trim().isEmpty()) {
            throw new PflichtfeldFehlt("Neue Beschreibung darf nicht null oder leer sein.");
        }
        artikel.setBeschreibung(neueBeschreibung);
        validiereArtikel(artikel);
    }

    private static void validiereArtikel(Artikel artikel) {
        if(artikel.getID() == null) {
            throw new PflichtfeldFehlt("ArtikelID");
        }
        if (artikel.getName() == null || artikel.getName().trim().isEmpty()) {
            throw new PflichtfeldFehlt("Artikel Name");
        }
        if(artikel.getBeschreibung() == null || artikel.getBeschreibung().trim().isEmpty()) {
            throw new PflichtfeldFehlt("Artikel Beschreibung");
        }
        if (artikel.getPreis() == null) {
            throw new PflichtfeldFehlt("Artikel Preis");
        }
    }
}
