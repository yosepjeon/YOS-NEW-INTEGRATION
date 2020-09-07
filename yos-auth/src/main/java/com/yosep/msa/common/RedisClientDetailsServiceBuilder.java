package com.yosep.msa.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

public class RedisClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<RedisClientDetailsServiceBuilder> {

    private Map<String, ClientDetails> clientDetails = new HashMap<String, ClientDetails>();

    private PasswordEncoder passwordEncoder; // for writing client secrets

    public RedisClientDetailsServiceBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    @Override
    protected void addClient(
            final String clientId,
            final ClientDetails build) {

        clientDetails.put(clientId, build);
    }

    @Override
    protected ClientDetailsService performBuild() {
        final RedisClientDetailsService redisClientDetailsService = new RedisClientDetailsService();
        if (passwordEncoder != null) {
            // This is used to encode secrets as they are added to the database (if it isn't set then the user has top
            // pass in pre-encoded secrets)
            redisClientDetailsService.setPasswordEncoder(passwordEncoder);
        }

        redisClientDetailsService.setClientDetailsStore(clientDetails);
        return redisClientDetailsService;
    }

}
