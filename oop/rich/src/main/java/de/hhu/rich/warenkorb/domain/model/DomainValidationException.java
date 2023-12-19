package de.hhu.rich.warenkorb.domain.model;

public class DomainValidationException extends RuntimeException{

    public DomainValidationException() {
        super();
    }

    public DomainValidationException(String message) {
        super(message);
    }
}