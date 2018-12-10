package com.fernando.zeus.control;

import com.fernando.zeus.model.Demanda;
import com.fernando.zeus.model.DemandaGerente;
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
public class DemandaNaoRelacionadaControl extends BasicControl implements Serializable {


    @Inject
    private DemandaGerente demandaGerente;

    private List<Demanda> lista;

    @Inject
    private LoginControl loginControl;

    @PostConstruct
    public void init() {
        this.listar();
    }

    public void listar() {
        WebTarget targetNaoRelacionadas = ClientUtil.criaConexao("http://localhost:8080/demandas/naorelacionados");
        Invocation.Builder builder = targetNaoRelacionadas.request(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        if (response.getStatus() != 200) {
            MessagesUtil.addErrorMessage("Erro ao listar Demandas Cód. " + response.getStatus());
        }
        lista = response.readEntity(new GenericType<List<Demanda>>() {
        });
    }

    public String limparPesquisa() {
        demandaGerente = new DemandaGerente();
        this.listar();
        return null;
    }

    public String pesquisar() {
        if (demandaGerente.getIdDemanda() == null
                && demandaGerente.getNome() == null
                && demandaGerente.getDescricao() == null
                && demandaGerente.getDescricao() == null
                && demandaGerente.getNomeCliente() == null) {
            MessagesUtil.addErrorMessage("Informe um dos campos para pesquisa");
            return null;
        }
        WebTarget pesquisaDemanda = ClientUtil.criaConexao("http://localhost:8080/demandas/naorelacionados");
        Invocation.Builder builder = pesquisaDemanda.request(MediaType.APPLICATION_JSON);
        Response response = builder.post(Entity.entity(demandaGerente, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
            MessagesUtil.addErrorMessage("Código " + response.getStatus());
            return null;
        }
        lista = response.readEntity(new GenericType<List<Demanda>>() {
        });
        return null;
    }

    public String relacionarDemanda(Long idDemanda){
        DemandaGerente relacao = new DemandaGerente();
        relacao.setIdDemanda(idDemanda);
        relacao.setIdGerente(loginControl.getUsuario().getId());
        WebTarget targetRelacao = ClientUtil.criaConexao("http://localhost:8080/demandas/relacionar");
        Invocation.Builder builder = targetRelacao.request(MediaType.APPLICATION_JSON);
        Response response = builder.post(Entity.entity(relacao, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
            MessagesUtil.addErrorMessage(response.getStatus() +" - ");
            return null;
        }
        return "listaporgerente?faces-redirect=true";
    }

    public DemandaGerente getDemandaGerente() {
        return demandaGerente;
    }

    public void setDemandaGerente(DemandaGerente demandaGerente) {
        this.demandaGerente = demandaGerente;
    }

    public List<Demanda> getLista() {
        return lista;
    }

    public void setLista(List<Demanda> lista) {
        this.lista = lista;
    }
}
