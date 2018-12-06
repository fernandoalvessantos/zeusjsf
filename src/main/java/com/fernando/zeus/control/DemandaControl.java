package com.fernando.zeus.control;

import com.fernando.zeus.model.Demanda;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

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
public class DemandaControl implements Serializable {

    @Inject
    private Demanda demanda;

    private String acao = "novo";

    @Inject
    private LoginControl loginControl;

    public String salvar(){
        demanda.setCliente(loginControl.getUsuario());
        try{
            Client client = ClientBuilder.newClient();
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("fernando", "1234");
            client.register(feature);
            WebTarget postDemanda = client.target("http://localhost:8080/demandas")
                    .path(loginControl.getUsuario().getId().toString());
            Invocation.Builder builder = postDemanda.request(MediaType.APPLICATION_JSON);

            Response response = builder.post(Entity.entity(demanda, MediaType.APPLICATION_JSON));

            if(response.getStatus() != 201){
                throw new Exception(response.getStatus()+" - ");
            }

        }catch (Exception e){
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro",
                            "Código "+e.getMessage()));
            return null;
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO"));
        return "lista?faces-redirect=true";
    }

    public String getTituloTela(){
        return "novo".equals(acao) ? "Nova Demanda" : "Editar Demanda";
    }

    public Demanda getDemanda() {
        return demanda;
    }

    public void setDemanda(Demanda demanda) {
        this.demanda = demanda;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
}
