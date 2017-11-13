package rest.service.manager;

import java.io.Closeable;
import java.util.List;

import rest.service.entity.Account;
import rest.service.exception.AccountException;

/**
 * 
 * @author Veniamin
 *
 */
public interface AccountManager extends Closeable {
    public Account createAccount(
        String login, 
        String password) 
            throws AccountException;
    
    public Account getAccount(
        Long accountId)
            throws AccountException;
    
    public Account getAccount(
        String login)
            throws AccountException;
    
    public List<Account> getAccount(
        Integer from, 
        Integer count)
            throws AccountException;
    
    public int deleteAccount(
        List<Long> accountId) 
            throws AccountException;
    
    public void updateAccount(
        Account account)
            throws AccountException;
}
