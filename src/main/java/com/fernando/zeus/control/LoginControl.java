package com.fernando.zeus.control;

import com.fernando.zeus.model.Login;
import com.fernando.zeus.model.Usuario;
import com.fernando.zeus.ws.ClientWS;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginControl implements Serializable {

    @Inject
    private Login login;

    private Usuario usuario;

    @PostConstruct
    public void init(){
        //List<Usuario> listaaa = ClientWS.getListaObjeto("http://localhost:8080/usuarios/clientes", Usuario.class);
    }

    public String logar(){
        try {
            usuario = (Usuario) ClientWS.postObjeto(login, Usuario.class, ClientWS.URL_LOGIN);
        }catch (Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro",
                            "Usuário e/ou senha inválidos"));
            return null;
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO"));
        return "/dashboard/index.xhtml?faces-redirect=true";
    }

    public String logOut(){
        usuario = null;
        return "/index.xhtml";
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

