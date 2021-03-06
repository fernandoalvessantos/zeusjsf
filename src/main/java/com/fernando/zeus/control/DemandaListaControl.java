package com.fernando.zeus.control;

import com.fernando.zeus.model.Demanda;
import com.fernando.zeus.utils.DemandaUtil;
import com.fernando.zeus.utils.MessagesUtil;
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
public class DemandaListaControl extends BasicControl implements Serializable {

    public static final String PARAM_DEMANDA ="idDemanda";

    @Inject
    private Demanda demanda;

    private List<Demanda> lista;

    @Inject
    private LoginControl loginControl;

    @PostConstruct
    public void init()  {
        this.listar();
    }

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
            MessagesUtil.addErrorMessage("Erro ao listar Demandas Cód. "+response.getStatus());
        }

        lista = response.readEntity(new GenericType<List<Demanda>>(){});
    }

    public String editarDemanda(Long idDemanda){
        super.setParamRequest(DemandaListaControl.PARAM_DEMANDA, idDemanda);
        return "/dashboard/demanda/formulario?faces-redirect=true";
    }

    public String deletar(Long idDemanda){
        WebTarget consultaDemandas = ClientUtil.criaConexao("http://localhost:8080/demandas");
        consultaDemandas = consultaDemandas.path(idDemanda.toString());
        Invocation.Builder builder = consultaDemandas.request();
        Response response = builder.delete();
        if(response.getStatus() != 204){
            MessagesUtil.addErrorMessage("Erro ao Cancelar. Cód. "+response.getStatus());
            return null;
        }
        this.listar();
        return null;
    }

    public String getDescricaoSit(Integer sit){
        return DemandaUtil.getDescricaoSituacao(sit);
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
