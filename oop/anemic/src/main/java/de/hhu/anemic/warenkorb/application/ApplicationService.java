package de.hhu.anemic.warenkorb.application;

import de.hhu.anemic.warenkorb.domain.model.artikel.Artikel;
import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelID;
import de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelRepository;
import de.hhu.anemic.warenkorb.domain.model.common.anzahl.Anzahl;
import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;
import de.hhu.anemic.warenkorb.domain.model.kunde.KundeID;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.Warenkorb;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbID;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.WarenkorbRepository;
import de.hhu.anemic.warenkorb.domain.service.AnzahlService;
import de.hhu.anemic.warenkorb.domain.service.PreisService;
import de.hhu.anemic.warenkorb.domain.service.WarenkorbService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ApplicationService {
    private final WarenkorbService warenkorbService;
    private final PreisService preisService;
    private final AnzahlService anzahlService;
    private final WarenkorbRepository warenkorbRepository;
    private final ArtikelRepository artikelRepository;


    public ApplicationService(WarenkorbService warenkorbService,
                              PreisService preisService,
                              AnzahlService anzahlService, WarenkorbRepository warenkorbRepository,
                              ArtikelRepository artikelRepository) {
        this.warenkorbService = warenkorbService;
        this.preisService = preisService;
        this.anzahlService = anzahlService;
        this.warenkorbRepository = warenkorbRepository;
        this.artikelRepository = artikelRepository;
    }

    public void erstelleWarenkorbFuerKunde(UUID warenkorbID, UUID kundeID, BigDecimal preis) {
        Preis maxEinkaufswert = preisService.erstellePreis(preis, "EUR");
        Warenkorb warenkorb = warenkorbService.erstelleWarenkorb(
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

    public void legeArtikelInDenWarenkorb(UUID artikelId, int anzahl, UUID warenkorbID) {
        Warenkorb warenkorb = getWarenkorb(warenkorbID);
        Artikel artikel = getArtikel(artikelId);

        warenkorbService.fuegeArtikelHinzu(warenkorb, artikel, anzahlService.erstelleAnzahl(anzahl));

        warenkorbRepository.speichere(warenkorb);
    }

    public void entferneArtikelAusDemWarenkorb(UUID artikelId, UUID warenkorbID) {
        Warenkorb warenkorb = getWarenkorb(warenkorbID);
        Artikel artikel = getArtikel(artikelId);

        warenkorbService.entferneArtikel(warenkorb, artikel);

        warenkorbRepository.speichere(warenkorb);
    }


    public void reduziereAnzahlVonArtikelInDemWarenkorb(UUID artikelId, int anzahl, UUID warenkorbID) {
        Warenkorb warenkorb = getWarenkorb(warenkorbID);
        Artikel artikel = getArtikel(artikelId);

        warenkorbService.reduziereArtikel(warenkorb, artikel, new Anzahl(anzahl));

        warenkorbRepository.speichere(warenkorb);
    }
}

