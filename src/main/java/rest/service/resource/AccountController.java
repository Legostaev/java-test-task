package rest.service.resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rest.service.entity.Account;
import rest.service.exception.AccountException;
import rest.service.factory.SecurityAbstractFactory;
import rest.service.manager.AccountDetailsManager;
import rest.service.manager.AccountManager;

/**
 * Controller for CRUD operations with accounts.
 * @author Veniamin
 *
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
    public static final class DeleteResult {
        private int count;
    	
        public DeleteResult(int count) {
            this.count = count;
        }

        public int getCount() {
            return this.count;
        }
    	
        public void setCount(int count) {
    	    this.count = count;
        }
    }
    
    @Autowired
    private SecurityAbstractFactory securityAbstractFactory;
    
    private AccountManager getAccountManager() {
        Objects.requireNonNull(this.securityAbstractFactory);
        
        return this.securityAbstractFactory.getAccountManager();
    }
    
    private AccountDetailsManager getAccountDetailsManager() {
        Objects.requireNonNull(this.securityAbstractFactory);
        
        return this.securityAbstractFactory.getAccountDetailsManager();
    }
    
    /**
     * Create new user.
     * 
     * @param login - new user account login.
     * @param password - user password.
     * @return identifier of the created account.
     * @throws AccountException 
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws InterruptedException 
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public Account createAccount(
        @RequestParam(value = "login", required = true) String login,
        @RequestParam(value = "password", required = true) String password) 
            throws 
                AccountException, 
                IOException, 
                URISyntaxException, 
                InterruptedException 
    {
        AccountManager        accountManager        = null;
        AccountDetailsManager accountDetailsManager = null;
        Account               account               = null;
        
        try {
            accountManager = this.getAccountManager();
        
            account = accountManager.createAccount(login, password);
            accountDetailsManager = this.getAccountDetailsManager();
            accountDetailsManager.getDetails(account);
            
            return account;
        }
        
        finally {
            if (accountManager != null)
                accountManager.close();
        }
    }
    
    /**
     * Update account information.
     * 
     * @param accountId - account identifier.
     * @param password - new user password.
     * @return
     * @throws AccountException
     * @throws IOException
     * @throws URISyntaxException 
     * @throws InterruptedException 
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public Account updateAccount(
        @RequestParam(value = "accountId", required = true) Long accountId,
        @RequestParam(value = "password", required = true) String password) 
            throws 
                AccountException, 
                IOException, 
                URISyntaxException, 
                InterruptedException 
    {
        AccountManager        accountManager        = null;
        AccountDetailsManager accountDetailsManager = null;
        Account               account               = null;
        
        try {
            accountManager = this.getAccountManager();
            account = accountManager.getAccount(accountId);
            account.setPassword(password);
            accountManager.updateAccount(account);
            accountDetailsManager = this.getAccountDetailsManager();
            accountDetailsManager.getDetails(account);
            
            return account;
        }
        
        finally {
            if (accountManager != null)
                accountManager.close();
        }
    }
    
    /**
     * Deletes the specified account.
     * 
     * @param accountId - account identifier.
     * @return
     * @throws AccountException
     * @throws IOException
     */
    @RequestMapping(value="/delete", method = RequestMethod.GET)
    public DeleteResult deleteAccount(
        @RequestParam(value = "accountId", required = true) List<Long> accountId) 
            throws AccountException, IOException 
    {
        AccountManager accountManager = null;
        
        try {
            accountManager = this.getAccountManager();
            
            return new DeleteResult(
            	accountManager.deleteAccount(
            		accountId));
        }
        
        finally {
            if (accountManager != null)
                accountManager.close();
        }
    }
    
    /**
     * Return exists accounts.
     * 
     * @param from  - will return account information from the specified position.
     * @param count - number of accounts to be uploaded.
     * @return
     * @throws AccountException
     * @throws IOException
     * @throws URISyntaxException 
     * @throws InterruptedException 
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public List<Account> getAccount(
        @RequestParam(value = "from", required = false) Integer from,
        @RequestParam(value = "count", required = false) Integer count) 
            throws 
                AccountException, 
                IOException, 
                URISyntaxException,
                InterruptedException 
    {
        AccountManager        accountManager        = null;
        AccountDetailsManager accountDetailsManager = null;
        List<Account>         list                  = null;
        
        try {
            accountManager = this.getAccountManager();
            accountDetailsManager = this.getAccountDetailsManager();
    
            list = accountManager.getAccount(from, count);
            
            for (Account account : list) {
                accountDetailsManager.getDetails(account);
            }
            
            return list;
        }
        
        finally {
            if (accountManager != null)
                accountManager.close();
        }
    }
}
