package rest.service.exception;

public class AccountException extends Exception {
    private static final long serialVersionUID = 8752253399724032275L;

    public AccountException() {
        
    }
    
    public AccountException(Throwable exc) {
        super(exc);
    }
}
