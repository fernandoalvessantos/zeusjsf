package com.fernando.zeus.control;

import com.fernando.zeus.model.Demanda;
import com.fernando.zeus.ws.ClientUtil;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class DemandaListaControl implements Serializable {

    @Inject
    private Demanda demanda;

    private List<Demanda> lista;

    @Inject
    private LoginControl loginControl;

    @PostConstruct
    public void init()  {
        this.listar();
    }

    /**
     * @Inject
     *     private FacesContext facesContext;
     *
     * public HttpSession getSessao() {
     *         return (HttpSession)this.getExternalContext().getSession(true);
     *     }
     *     public void setParamSession(String key, Object object) {
     *         this.getSessao().setAttribute(key, object);
     *     }
     *     public void setParamRequest(String nome, Object valor) {
     *         if (nome.startsWith("@")) {
     *             this.setParamSession(nome, valor);
     *         } else {
     *             this.setParamSession("@" + nome, valor);
     *         }
     *
     *     }
     */

    /**
     * public Object getParamRequest(String nome) {
     *         return nome.startsWith("@") ? this.getParamSession(nome) : this.getParamSession("@" + nome);
     *     }
     *     public Object popParamRequest(String nome) {
     *         Object r = this.getParamRequest(nome);
     *         this.removeParamRequest(nome);
     *         return r;
     *     }
     *
     *     public void removeParamRequest(String nome) {
     *         if (nome.startsWith("@")) {
     *             this.removeAttribute(nome);
     *         } else {
     *             this.removeAttribute("@" + nome);
     *         }
     *
     *     }
     */



    public String pesquisar() {
        if (demanda.getId() == null
                && demanda.getNome() == null
                && demanda.getDescricao() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro",
                            "Informe um dos campos de pesquisa"));
            return null;
        }
        WebTarget pesquisaDemanda = ClientUtil.criaConexao("http://localhost:8080/demandas");
        pesquisaDemanda = pesquisaDemanda.path(loginControl.getUsuario().getId().toString());
        pesquisaDemanda = pesquisaDemanda.path("pesquisa");
        Invocation.Builder builder = pesquisaDemanda.request(MediaType.APPLICATION_JSON);
        Response response = builder.post(Entity.entity(demanda, MediaType.APPLICATION_JSON));
        if(response.getStatus() != 200){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro",
                            "Código "+response.getStatus()));
            return null;
        }
        lista = response.readEntity(new GenericType<List<Demanda>>(){});
        return null;
    }

    public String limparPesquisa(){
        demanda = new Demanda();
        this.listar();
        return null;
    }

    public void listar(){
        WebTarget consultaDemandas = ClientUtil.criaConexao("http://localhost:8080/demandas");
        consultaDemandas = consultaDemandas.path(loginControl.getUsuario().getId().toString());
        Invocation.Builder builder = consultaDemandas.request(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        if(response.getStatus() != 200){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro",
                            "Código "+response.getStatus()));
        }

        lista = response.readEntity(new GenericType<List<Demanda>>(){});
    }

    public String deletar(Long idDemanda){
        WebTarget consultaDemandas = ClientUtil.criaConexao("http://localhost:8080/demandas");
        consultaDemandas = consultaDemandas.path(idDemanda.toString());
        Invocation.Builder builder = consultaDemandas.request();
        Response response = builder.delete();
        if(response.getStatus() != 204){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro",
                            "Código "+response.getStatus()));
            return null;
        }
        this.listar();
        return null;
    }


    public Demanda getDemanda() {
        return demanda;
    }

    public void setDemanda(Demanda demanda) {
        this.demanda = demanda;
    }

    public List<Demanda> getLista() {
        return lista;
    }

    public void setLista(List<Demanda> lista) {
        this.lista = lista;
    }
}
