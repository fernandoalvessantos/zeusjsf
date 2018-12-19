package com.fernando.zeus.utils;

public class DemandaUtil {


    public static String getDescricaoSituacao(Integer situacao) {
        if (situacao == 1) {
            return "Novo";
        }
        if (situacao == 2) {
            return "Em Planejamento";
        }
        if (situacao == 3) {
            return "Em Execução";
        }
        if (situacao == 4) {
            return "Em Homologação";
        }
        if (situacao == 5) {
            return "Entregue";
        }
        if (situacao == 6) {
            return "Cancelado";
        }
        return null;
    }

}
