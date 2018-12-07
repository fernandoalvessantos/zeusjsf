package com.fernando.zeus.control;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

public class BasicControl {


    @Inject
    private FacesContext facesContext;

    private ExternalContext getExternalContext() {
        return facesContext.getExternalContext();
    }

    public HttpSession getSessao() {
        return (HttpSession) this.getExternalContext().getSession(true);
    }

    public void setParamSession(String key, Object object) {
        this.getSessao().setAttribute(key, object);
    }

    public void setParamRequest(String nome, Object valor) {
        if (nome.startsWith("@")) {
            this.setParamSession(nome, valor);
        } else {
            this.setParamSession("@" + nome, valor);
        }

    }

    public Object getParamRequest(String nome) {
        return nome.startsWith("@") ? this.getParamSession(nome) : this.getParamSession("@" + nome);
    }

    public Object getParamSession(String key) {
        return getSessao().getAttribute(key);
    }

    public Object popParamRequest(String nome) {
        Object r = this.getParamRequest(nome);
        this.removeParamRequest(nome);
        return r;
    }

    public void removeParamRequest(String nome) {
        if (nome.startsWith("@")) {
            this.removeAttribute(nome);
        } else {
            this.removeAttribute("@" + nome);
        }
    }

    public void removeAttribute(String nome){
        this.getSessao().removeAttribute(nome);
    }

}
