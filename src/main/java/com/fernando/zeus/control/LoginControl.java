package com.fernando.zeus.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.zeus.model.Usuario;
import com.fernando.zeus.ws.ClientWS;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Named
@ViewScoped
public class LoginControl implements Serializable {

    @Inject
    private Usuario usuario;

    @PostConstruct
    public void init(){
        usuario = new Usuario();
    }

    public String logar(){
        ObjectMapper mapper = new ObjectMapper();
        String mensagem = null;
        HttpURLConnection connection = null;
        //get lista;
        List<Usuario> listaaa = ClientWS.getListaObjeto("http://localhost:8080/usuarios/clientes", Usuario.class);
        try {
            mensagem = mapper.writeValueAsString(usuario);
            URL url = new URL("http://localhost:8080/usuarios/clientes");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setRequestProperty("Authorization", "Basic ZmVybmFuZG86MTIzNA==");
            connection.connect();

            String responseJson = ClientWS.inputStreamToString(connection.getInputStream());
            connection.disconnect();
            System.out.println(responseJson);
            List<Usuario> lista = mapper.readValue(responseJson, new TypeReference<List<Usuario>>(){});
            System.out.println(lista.toArray().toString());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        System.out.println(mensagem);
        return null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
