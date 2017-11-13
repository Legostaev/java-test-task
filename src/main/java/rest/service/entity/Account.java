package rest.service.entity;

/**
 * 
 * @author Veniamin
 */
public class Account {
    private Long   id;
    private String login;
    private String password;
    private String details;

    public Account() {
        this.id        = null;
        this.login     = null;
        this.password  = null;
        this.details   = null;
    }
    
    /**
     * Return account id.
     * 
     * @return
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Set a new account id.
     * @param id - new account id.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Return account login.
     * @return
     */
    public String getLogin() {
        return this.login;
    }
    
    /**
     * Set new account login.
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }
    
    /**
     * Return account password.
     * @return
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Set new account password.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Return account details.
     * @return
     */
    public String getDetails() {
        return this.details;
    }
    
    /**
     * Set account details.
     * @param details
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
