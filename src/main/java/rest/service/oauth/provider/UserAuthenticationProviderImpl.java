package rest.service.oauth.provider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import rest.service.entity.Account;
import rest.service.exception.AccountException;
import rest.service.factory.SecurityAbstractFactory;
import rest.service.manager.AccountManager;

/**
 * 
 * @author Veniamin
 *
 */
public class UserAuthenticationProviderImpl implements AuthenticationProvider {

    private String login;
    private String password;
    private SecurityAbstractFactory securityAbstractFactory;

    private boolean authenticate(String login, String password) {
        boolean res = false;
        SecurityAbstractFactory factory = null;
        AccountManager accountManager = null;
        Account account = null;

        try {
            if ((factory = this.getSecurityAbstractFactory()) != null) {
                accountManager = factory.getAccountManager();

                account = accountManager.getAccount(login);

                res = password.equals(account.getPassword());
            }
        }

        catch (AccountException exc) {

        }

        return res;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String user = authentication.getPrincipal().toString();
        String pwd = authentication.getCredentials().toString();

        boolean result = false;

        if ((user != null) && (pwd != null)) {
            result = user.equals(this.login) && pwd.equals(this.password);

            if (!result) {
                result = this.authenticate(user, pwd);
            }
        }

        if (result) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorities);

            return auth;
        } else {
            throw new BadCredentialsException("Bad User Credentials.");
        }
    }

    public boolean supports(Class<?> authentication) {
        return true;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SecurityAbstractFactory getSecurityAbstractFactory() {
        return this.securityAbstractFactory;
    }

    public void setSecurityAbstractFactory(SecurityAbstractFactory securityAbstractFactory) {

        this.securityAbstractFactory = securityAbstractFactory;
    }
}
