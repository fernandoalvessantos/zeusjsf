package com.fernando.zeus.control;

import com.fernando.zeus.model.Login;
import com.fernando.zeus.model.Usuario;
import com.fernando.zeus.utils.MessagesUtil;
import com.fernando.zeus.ws.ClientUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;

@Named
@SessionScoped
public class LoginControl implements Serializable {

    @Inject
    private Login login;

    private Usuario usuario;

    @PostConstruct
    public void init() {
    }

    public String logar() {
        try {
            WebTarget targetLogin = ClientUtil.criaConexao("http://localhost:8080/usuarios/login");
            Invocation.Builder builder = targetLogin.request(MediaType.APPLICATION_JSON);
            Response response = builder.post(Entity.entity(login, MediaType.APPLICATION_JSON));
            if (response.getStatus() != 200) {
                if (response.getStatus() == 409) {
                    MessagesUtil.addErrorMessage("Usuário e/ou senha inválidos");
                    return null;
                }else{
                    throw new Exception();
                }
            }

            usuario = response.readEntity(new GenericType<Usuario>(){});
        } catch (ConnectException connexc){
            MessagesUtil.addErrorMessage("Serviço de login indisponível");
        }

        catch (Exception e) {
            MessagesUtil.addErrorMessage("Erro ao efetuar Login!!");
            return null;
        }

        return "/dashboard/index.xhtml?faces-redirect=true";
    }

    public String logOut() throws IOException {
        usuario = null;
        return "/login?faces-redirect=true";
    }

    public boolean isCliente(){
        if(usuario != null){
            return "CLIENTE".equalsIgnoreCase(usuario.getPerfil());
        }
        return false;
    }

    public boolean isGerente(){
        if(usuario != null){
            return "GERENTE".equalsIgnoreCase(usuario.getPerfil());
        }
        return false;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

