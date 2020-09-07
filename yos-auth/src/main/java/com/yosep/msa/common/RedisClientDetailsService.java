package com.yosep.msa.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

@SuppressWarnings("deprecation")
public class RedisClientDetailsService implements ClientDetailsService {

    @SuppressWarnings({ "unused" })
	private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();

    public ClientDetails loadClientByClientId(final String clientId) throws ClientRegistrationException {
        final ClientDetails details = clientDetailsStore.get(clientId);
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
        return details;
    }

    public void setClientDetailsStore(final Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap<String, ClientDetails>(clientDetailsStore);
    }

    /**
     * @param passwordEncoder the password encoder to set
     */
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
