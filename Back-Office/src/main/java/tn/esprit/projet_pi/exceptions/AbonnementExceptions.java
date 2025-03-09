package tn.esprit.projet_pi.exceptions;

public class AbonnementExceptions extends RuntimeException {

    public AbonnementExceptions(String message) {
        super(message);
    }
    public AbonnementExceptions UserAlreadyHasAbonnementException(String message) {
        return new AbonnementExceptions(message);
    }
}
