package de.hhu.rich.warenkorb.domain.model;

public class PflichtfeldFehlt extends DomainValidationException {

    public PflichtfeldFehlt(String feldname) {
        super(feldname);
    }
}
