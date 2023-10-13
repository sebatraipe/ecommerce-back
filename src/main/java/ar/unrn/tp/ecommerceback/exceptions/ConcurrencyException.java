package ar.unrn.tp.ecommerceback.exceptions;

public class ConcurrencyException extends RuntimeException {

    public ConcurrencyException(String message) {
        super(message);
    }
}
