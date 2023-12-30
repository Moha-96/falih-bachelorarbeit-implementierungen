package de.hhu.rich.warenkorb.application;

import de.hhu.rich.warenkorb.domain.model.artikel.Artikel;
import de.hhu.rich.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.rich.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.rich.warenkorb.domain.model.common.preis.Preis;
import de.hhu.rich.warenkorb.domain.model.kunde.KundeID;
import de.hhu.rich.warenkorb.domain.model.warenkorb.Warenkorb;
import de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbID;
import de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbRepository;
import de.hhu.rich.warenkorb.domain.model.artikel.ArtikelRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ApplicationService {
    private final WarenkorbRepository warenkorbRepository;
    private final ArtikelRepository artikelRepository;


    public ApplicationService(WarenkorbRepository warenkorbRepository,
                              ArtikelRepository artikelRepository) {
        this.warenkorbRepository = warenkorbRepository;
        this.artikelRepository = artikelRepository;
    }

    public void erstelleWarenkorbFuerKunde(UUID warenkorbID, UUID kundeID, BigDecimal preis) {
        Preis maxEinkaufswert = new Preis(preis, "EUR");
        Warenkorb warenkorb = new Warenkorb(
            new WarenkorbID(warenkorbID),
            new KundeID(kundeID),
            maxEinkaufswert);
        warenkorbRepository.speichere(warenkorb);
    }

    public Warenkorb getWarenkorb(UUID warenkorb) {
        WarenkorbID warenkorbID = new WarenkorbID(warenkorb);
        return warenkorbRepository.findeMit(warenkorbID);
    }

    public Artikel getArtikel(UUID artikel) {
        ArtikelID artikelID = new ArtikelID(artikel);
        return artikelRepository.findeMit(artikelID);
    }

    public void fuegeArtikelZumWarenkorbHinzu(UUID artikelId, int anzahl, UUID warenkorbID) {
        Warenkorb warenkorb = getWarenkorb(warenkorbID);
        Artikel artikel = getArtikel(artikelId);

        warenkorb.fuegeHinzu(artikel, new Anzahl(anzahl));

        warenkorbRepository.speichere(warenkorb);
    }

    public void entferneArtikelAusWarenkorb(UUID artikelId, UUID warenkorbID) {
        Warenkorb warenkorb = getWarenkorb(warenkorbID);
        Artikel artikel = getArtikel(artikelId);

        warenkorb.entferne(artikel);

        warenkorbRepository.speichere(warenkorb);
    }


    public void reduziereAnzahlVonArtikelImWarenkorb(UUID artikelId, int anzahl, UUID warenkorbID) {
        Warenkorb warenkorb = getWarenkorb(warenkorbID);
        Artikel artikel = getArtikel(artikelId);

        warenkorb.reduziere(artikel, new Anzahl(anzahl));

        warenkorbRepository.speichere(warenkorb);
    }
}

