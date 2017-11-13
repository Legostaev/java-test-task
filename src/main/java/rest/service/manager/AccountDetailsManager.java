package rest.service.manager;

import java.io.IOException;
import java.net.URISyntaxException;

import rest.service.entity.Account;

/**
 * 
 * @author Veniamin
 *
 */
public interface AccountDetailsManager {
    public void getDetails(
        Account account) 
            throws 
                IOException,
                InterruptedException,
                URISyntaxException;
}
