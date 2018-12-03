package com.fernando.zeus.ws;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ClientUtil {

    public static WebTarget criaConexao(String target){
        Client client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("fernando", "1234");
        client.register(feature);
        return client.target(target);
    }
}
