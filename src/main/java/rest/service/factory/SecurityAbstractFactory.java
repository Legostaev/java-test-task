package rest.service.factory;

import rest.service.manager.AccountDetailsManager;
import rest.service.manager.AccountManager;

/**
 * 
 * @author Veniamin
 *
 */
public interface SecurityAbstractFactory {
    public AccountManager getAccountManager();
    
    public AccountDetailsManager getAccountDetailsManager(); 
}
