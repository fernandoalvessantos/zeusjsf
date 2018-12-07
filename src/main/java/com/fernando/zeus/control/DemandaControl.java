package com.fernando.zeus.control;

import com.fernando.zeus.model.Demanda;
import com.fernando.zeus.utils.MessagesUtil;
import com.fernando.zeus.ws.ClientUtil;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@ViewScoped
public class DemandaControl extends BasicControl implements Serializable {

    @Inject
    private Demanda demanda;

    private boolean novo = true;

    @Inject
    private LoginControl loginControl;

    @PostConstruct
    public void init(){
        Long id = null;
        id = (Long) super.popParamRequest(DemandaListaControl.PARAM_DEMANDA);
        if(id != null){
            novo = false;
            this.buscaDemanda(id);
        }else{
            novo = true;
        }
    }

    public void buscaDemanda(Long id){
        WebTarget buscaDemandaTarget = ClientUtil.criaConexao("http://localhost:8080/demandas");
        buscaDemandaTarget = buscaDemandaTarget.path(loginControl.getUsuario().getId().toString()).path(id.toString());
        Invocation.Builder builder = buscaDemandaTarget.request();
        Response response = builder.get();
        if(response.getStatus() != 200){
            MessagesUtil.addErrorMessage("Erro ao buscar Demanda");
        }
        demanda = response.readEntity(Demanda.class);
    }


    public String salvar(){
        if(isNovo()) {
            demanda.setCliente(loginControl.getUsuario());
            try {
                WebTarget postDemanda = ClientUtil.criaConexao("http://localhost:8080/demandas");
                postDemanda = postDemanda.path(loginControl.getUsuario().getId().toString());
                Invocation.Builder builder = postDemanda.request(MediaType.APPLICATION_JSON);
                Response response = builder.post(Entity.entity(demanda, MediaType.APPLICATION_JSON));

                if (response.getStatus() != 201) {
                    throw new Exception(response.getStatus() + " - ");
                }

            } catch (Exception e) {
                e.printStackTrace();
                MessagesUtil.addErrorMessage("Erro ao salvar Demanda. Cód. " + e.getMessage());
                return null;
            }

            MessagesUtil.addInfoMessage("Demanda salva com sucesso");
            return "lista?faces-redirect=true";
        }else{
            try {
                WebTarget targetAtualiza = ClientUtil.criaConexao("http://localhost:8080/demandas");
                targetAtualiza.path(demanda.getId().toString());
                Invocation.Builder builder = targetAtualiza.request(MediaType.APPLICATION_JSON);
                Response response = builder.put(Entity.entity(demanda, MediaType.APPLICATION_JSON));
                if (response.getStatus() != 204) {
                    throw new Exception(response.getStatus() + " - ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MessagesUtil.addErrorMessage("Erro ao salvar Demanda. Cód. " + e.getMessage());
                return null;
            }
            MessagesUtil.addInfoMessage("Demanda salva com sucesso");
            return "lista?faces-redirect=true";
        }
    }

    public String getTituloTela(){
        return novo ? "Nova Demanda" : "Editar Demanda";
    }

    public Demanda getDemanda() {
        return demanda;
    }

    public void setDemanda(Demanda demanda) {
        this.demanda = demanda;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }
}
