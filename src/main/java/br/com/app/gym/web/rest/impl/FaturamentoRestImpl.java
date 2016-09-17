package br.com.app.gym.web.rest.impl;

import br.com.app.gym.web.model.Faturamento;
import br.com.app.gym.web.parameter.FaturamentoParameter;
import br.com.app.gym.web.parameter.PeriodoParameter;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import br.com.app.gym.web.rest.FaturamentoRest;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import javax.ws.rs.core.GenericType;

public class FaturamentoRestImpl implements FaturamentoRest, Serializable {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/mylocalgym/resources";

    public FaturamentoRestImpl() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("transacao");
    }

    @Override
    public List<Faturamento> listarTransacoesPorPeriodo(Integer academiaId, String periodo) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("listar/{0}/periodo/{1}", new Object[]{academiaId, periodo}));
        Response response = resource.request(MediaType.APPLICATION_JSON).get();

        List<FaturamentoParameter> parameters = response.readEntity(new GenericType<List<FaturamentoParameter>>() {
        });

        List<Faturamento> faturamentos = new ArrayList<>();
        for (FaturamentoParameter parameter : parameters) {
            faturamentos.add(parameter.convert());
        }

        return faturamentos;
    }

    @Override
    public PeriodoParameter listarFaturamento(String academiaId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("periodos/{0}", new Object[]{academiaId}));
        Response response = resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get();

        PeriodoParameter periodoParameter = response.readEntity(PeriodoParameter.class);

        return periodoParameter;
    }

    public void close() {
        client.close();
    }

}
