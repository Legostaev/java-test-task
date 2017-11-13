package rest.service.hibernate.manager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;

import rest.service.entity.Account;
import rest.service.exception.AccountException;
import rest.service.exception.AccountExistsException;
import rest.service.exception.AccountNotFoundException;
import rest.service.manager.AccountManager;

/**
 * 
 * @author Veniamin
 *
 */
public class AccountManagerImpl implements AccountManager {

    private EntityManager entityManager;
    
    public AccountManagerImpl(EntityManager entityManager) {
        Objects.requireNonNull(entityManager);

        this.entityManager = entityManager;
    }

    public Account createAccount(
        String login,
        String password) 
            throws AccountException {
        Objects.requireNonNull(login);
        
        Account account               = null;
        EntityTransaction transaction = null;
        
        try {
            transaction = this.entityManager.getTransaction();
            transaction.begin();
            
            account = new Account();
            account.setLogin(login);
            account.setPassword(password);
        
            this.entityManager.persist(account);
            transaction.commit();
            transaction = null;
        } 
        
        catch (RollbackException exc) {
            Throwable e = exc;
            transaction = null;
            
            while ((e = e.getCause()) != null) {
                if (e instanceof ConstraintViolationException) {
                    throw new AccountExistsException();
                }
            }
            
            throw exc;
        }
        
        finally {
            if (transaction != null)
                transaction.rollback();
        }
        
        return account;
    }

    public Account getAccount(
        Long accountId) 
            throws AccountException {
        Objects.requireNonNull(accountId);
        
        TypedQuery<Account>            typedQuery = null;
        
        typedQuery = this.entityManager.createQuery(
            String.format(
                Locale.US, 
                "select c from %s c where c.id=:accountId",
                Account.class.getName()), 
            Account.class);
        
        typedQuery = typedQuery.setParameter(
            "accountId", 
            accountId);
        
        try {
            return typedQuery.getSingleResult();
        }
        
        catch (NoResultException exc) {
            throw new AccountNotFoundException();
        }
    }

    public int deleteAccount(List<Long> accountId) {
        Objects.requireNonNull(accountId);
        
        int                         res         = 0;
        Query                       query       = null;
        EntityTransaction           transaction = null;
        
        try {
            transaction = this.entityManager.getTransaction();
            
            transaction.begin();
            
            query = this.entityManager.createQuery(
                String.format(
                    Locale.US, 
                    "delete from %s c where c.id IN :accountId",
                    Account.class.getName()));
        
            query = query.setParameter(
                "accountId", 
                accountId);
        
            res = query.executeUpdate();
            transaction.commit();
            transaction = null;
            
            return res;
        }
        
        finally {
            if (transaction != null)
                transaction.rollback();
        }
    }

    public void updateAccount(Account account) {
        Objects.requireNonNull(account);
        
        EntityTransaction           transaction = null;
        
        try {
            transaction = this.entityManager.getTransaction();
            
            transaction.begin();
            this.entityManager.merge(account);
            transaction.commit();
            transaction = null;
        }
        
        finally {
            if (transaction != null)
                transaction.rollback();
        }
    }

    public List<Account> getAccount(
        Integer from, 
        Integer count) 
            throws AccountException {
        TypedQuery<Account>            typedQuery = null;
        
        typedQuery = this.entityManager.createQuery(
            String.format(
                Locale.US, 
                "select c from %s c",
                Account.class.getName()), 
            Account.class);
        
        if (from != null)
            typedQuery.setFirstResult(from);
        
        if (count != null)
            typedQuery.setMaxResults(count);
        
        return typedQuery.getResultList();
    }

    public void close() throws IOException {
        this.entityManager.close();
    }

    public Account getAccount(
        String login) 
            throws AccountException {
        Objects.requireNonNull(login);
        
        TypedQuery<Account>            typedQuery = null;
        
        typedQuery = this.entityManager.createQuery(
            String.format(
                Locale.US, 
                "select c from %s c where c.login=:login",
                Account.class.getName()), 
            Account.class);
        
        typedQuery = typedQuery.setParameter(
            "login", 
            login);
        
        try {
            return typedQuery.getSingleResult();
        }
        
        catch (NoResultException exc) {
            throw new AccountNotFoundException();
        }
    }
}
