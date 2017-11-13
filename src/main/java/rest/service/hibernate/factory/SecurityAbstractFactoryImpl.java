package rest.service.hibernate.factory;

import java.util.Objects;

import javax.persistence.EntityManagerFactory;

import rest.service.factory.SecurityAbstractFactory;
import rest.service.hibernate.manager.AccountManagerImpl;
import rest.service.manager.AccountDetailsManager;
import rest.service.manager.AccountManager;

/**
 * 
 * @author Veniamin
 *
 */
public class SecurityAbstractFactoryImpl implements SecurityAbstractFactory {
    private EntityManagerFactory  entityManagerFactory;
    private AccountDetailsManager accountDetailsManager;
    
    public SecurityAbstractFactoryImpl(
        EntityManagerFactory entityManagerFactory,
        AccountDetailsManager accountDetailsManager) {
        
        Objects.requireNonNull(entityManagerFactory);
        Objects.requireNonNull(accountDetailsManager);

        this.entityManagerFactory = entityManagerFactory;
        this.accountDetailsManager = accountDetailsManager;
    }

    public AccountManager getAccountManager() {
        return new AccountManagerImpl(
            this.entityManagerFactory.createEntityManager());
    }

    public AccountDetailsManager getAccountDetailsManager() {
        return this.accountDetailsManager;
    }
}
