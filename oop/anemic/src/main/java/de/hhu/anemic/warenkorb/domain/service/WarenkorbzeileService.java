package de.hhu.anemic.warenkorb.domain.service;

import de.hhu.anemic.warenkorb.domain.model.PflichtfeldFehlt;
import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.anemic.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.WarenkorbzeileID;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WarenkorbzeileService {
    private final AnzahlService anzahlService;
    private final PreisService preisService;

    public WarenkorbzeileService(AnzahlService anzahlService, PreisService preisService) {
        this.anzahlService = anzahlService;
        this.preisService = preisService;
    }

    public Warenkorbzeile erstelleWarenkorbzeile(WarenkorbzeileID ID, ArtikelID artikelID, Anzahl anzahl, Preis preis, Anzahl maxArtikelAnzahl) {
        Warenkorbzeile warenkorbzeile = new Warenkorbzeile(ID, artikelID, anzahl, preis, maxArtikelAnzahl);
        validiereWarenkorbzeile(warenkorbzeile);
        return warenkorbzeile;
    }

    public void erhoeheAnzahl(Warenkorbzeile warenkorbzeile, Anzahl anzahl) {
        Anzahl neueAnzahl = anzahlService.erhoeheUm(warenkorbzeile.getAnzahl(), anzahl);
        warenkorbzeile.setAnzahl(neueAnzahl);
        validiereWarenkorbzeile(warenkorbzeile);
    }

    public void reduziereAnzahl(Warenkorbzeile warenkorbzeile, Anzahl anzahl) {
        Anzahl neueAnzahl = anzahlService.reduziereUm(warenkorbzeile.getAnzahl(), anzahl);
        warenkorbzeile.setAnzahl(neueAnzahl);
        validiereWarenkorbzeile(warenkorbzeile);
    }

    public Preis berechneGesamtpreis(Warenkorbzeile warenkorbzeile) {
        BigDecimal gesamtBetrag = warenkorbzeile.getPreis().betrag().multiply(new BigDecimal(warenkorbzeile.getAnzahl().anzahl()));
        return preisService.erstellePreis(gesamtBetrag, warenkorbzeile.getPreis().waehrung());
    }

    private void validiereWarenkorbzeile(Warenkorbzeile warenkorbzeile) {
        if (warenkorbzeile.getID() == null) {
            throw new PflichtfeldFehlt("WarenkorbzeileID darf nicht null sein!");
        }
        if (warenkorbzeile.getArtikelID() == null) {
            throw new PflichtfeldFehlt("ArtikelID darf nicht null sein!");
        }
        if (warenkorbzeile.getAnzahl() == null || warenkorbzeile.getAnzahl().anzahl() < 0) {
            throw new PflichtfeldFehlt("Anzahl darf nicht null oder kleiner als 0 sein!");
        }
        if (warenkorbzeile.getPreis() == null) {
            throw new PflichtfeldFehlt("Preis darf nicht null sein!");
        }
        if (warenkorbzeile.getMaxArtikelAnzahl() == null) {
            throw new PflichtfeldFehlt("Maximale Artikel Anzahl darf nicht null sein!");
        }
        if (anzahlService.istGroesserAls(warenkorbzeile.getAnzahl(), warenkorbzeile.getMaxArtikelAnzahl())) {
            throw new IllegalStateException("Maximale Anzahl von Artikel ist überschritten.");
        }
    }
}
