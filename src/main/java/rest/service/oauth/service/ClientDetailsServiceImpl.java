package rest.service.oauth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * 
 * @author Veniamin
 *
 */
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private String id;
    private String secretKey;

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        List<String> authorizedGrantTypes = null;
        BaseClientDetails clientDetails = null;

        if (id.equals(clientId)) {
            authorizedGrantTypes = new ArrayList<String>();

            authorizedGrantTypes.add("password");
            authorizedGrantTypes.add("refresh_token");
            authorizedGrantTypes.add("client_credentials");

            clientDetails = new BaseClientDetails();
            clientDetails.setClientId(id);
            clientDetails.setClientSecret(secretKey);
            clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);

            return clientDetails;
        } else {
            throw new NoSuchClientException(String.format(Locale.US, "No client recognized with id: %s", clientId));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
