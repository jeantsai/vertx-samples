package cn.jeantsai.vx.sample.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

public class HttpConsumer extends AbstractVerticle {

    private WebClient client;

    @Override
    public void start() {
        client = WebClient.create(vertx);
        Router router = Router.router(vertx);

        router.get("/").handler(this::getResultFromProducer);

        vertx.createHttpServer().requestHandler(router::accept).listen(9998);
    }

    private void getResultFromProducer(RoutingContext rc) {
        HttpRequest<Buffer> request = client
                .get(9999, "localhost", "/louis");
        request.send(ar -> {
            if (ar.failed()) {
                rc.fail(ar.cause());
            } else {
                rc.response().end(ar.result().bodyAsString());
            }
        });
    }
}
