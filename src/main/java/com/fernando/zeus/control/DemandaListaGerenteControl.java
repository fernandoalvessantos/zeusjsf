package com.fernando.zeus.control;

import com.fernando.zeus.model.Demanda;
import com.fernando.zeus.model.DemandaGerente;
import com.fernando.zeus.model.Usuario;
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
public class DemandaListaGerenteControl extends BasicControl implements Serializable {

    @Inject
    private DemandaGerente demandaGerente;

    private List<Demanda> lista;
    private List<Usuario> listaUsuarios;

    @Inject
    private LoginControl loginControl;

    @PostConstruct
    public void init() {
        this.listar();
        demandaGerente.setIdGerente(loginControl.getUsuario().getId());
        this.carregarListaUsuario();
    }

    private void carregarListaUsuario() {
        WebTarget targetListaUsuarios = ClientUtil.criaConexao("http://localhost:8080/usuarios/gerentes");
        Invocation.Builder builder = targetListaUsuarios.request(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        if (response.getStatus() != 200) {
            MessagesUtil.addErrorMessage("Erro ao listar Usuarios" + response.getStatus());
        }
        listaUsuarios = response.readEntity(new GenericType<List<Usuario>>() {
        });
    }

    public String limparPesquisa() {
        demandaGerente = new DemandaGerente();
        demandaGerente.setIdGerente(loginControl.getUsuario().getId());
        this.listar();
        return null;
    }

    public void listar() {
        WebTarget targetDemandas = ClientUtil.criaConexao("http://localhost:8080/demandas");
        targetDemandas = targetDemandas.path("gerente").path(loginControl.getUsuario().getId().toString());
        Invocation.Builder builder = targetDemandas.request(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        if (response.getStatus() != 200) {
            MessagesUtil.addErrorMessage("Erro ao listar Demandas " + response.getStatus());
        }
        lista = response.readEntity(new GenericType<List<Demanda>>() {
        });
    }

    public String pesquisar() {
        WebTarget targetPesquisa = ClientUtil.criaConexao("http://localhost:8080/demandas");
        targetPesquisa = targetPesquisa.path("gerente").path(demandaGerente.getIdGerente().toString());
        Invocation.Builder builder = targetPesquisa.request(MediaType.APPLICATION_JSON);
        Response response = builder.post(Entity.entity(demandaGerente, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
            MessagesUtil.addErrorMessage("Erro ao pesquisar - " + response.getStatus());
            return null;
        }
        lista = response.readEntity(new GenericType<List<Demanda>>() {
        });
        return null;
    }

    public String editarDemanda(Long idDemanda){
        super.setParamRequest(DemandaListaControl.PARAM_DEMANDA, idDemanda);
        return "/dashboard/demanda/formulario?faces-redirect=true";
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
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
