package de.hhu.anemic.warenkorb.domain.model;

public class PflichtfeldFehlt extends DomainValidationException {

    public PflichtfeldFehlt(String feldname) {
        super(feldname);
    }
}
