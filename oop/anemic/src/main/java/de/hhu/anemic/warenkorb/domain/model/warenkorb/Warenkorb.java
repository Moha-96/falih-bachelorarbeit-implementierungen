package de.hhu.anemic.warenkorb.domain.model.warenkorb;

import de.hhu.anemic.warenkorb.domain.model.common.preis.Preis;
import de.hhu.anemic.warenkorb.domain.model.kunde.KundeID;
import de.hhu.anemic.warenkorb.domain.model.warenkorb.warenkorbzeile.Warenkorbzeile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void setGesamtPreis(Preis gesamtPreis) {
        this.gesamtPreis = gesamtPreis;
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
}

