package com.example;

import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("")
public class ExampleResource {

    private final Vertx vertx;

    public ExampleResource(Vertx vertx) {
        this.vertx = vertx;
    }

    @GET
    @Path("/run")
    public void testRunner() {

        WebClientOptions webClientOptions = new WebClientOptions().setDefaultHost("0.0.0.0").setDefaultPort(8181);
        WebClient client = WebClient.create(vertx, webClientOptions);

        client.get("/string")
                .send()
                .onFailure().invoke(resp -> log.error("error: " + resp))
                .onItem().invoke(resp -> log.info("result: " + resp.statusCode()))
                .toMulti()
                .subscribe().with(r -> log.info(String.format("Subscribe: code:%d body:%s",r.statusCode(), r.bodyAsString())));

    }
}