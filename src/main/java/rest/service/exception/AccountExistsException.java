package rest.service.exception;

/**
 * 
 * @author Veniamin
 *
 */
public class AccountExistsException extends AccountException {
    private static final long serialVersionUID = -1876384779895965900L;

    public AccountExistsException() {
        
    }
    
    public AccountExistsException(Throwable exc) {
        super(exc);
    }
}
