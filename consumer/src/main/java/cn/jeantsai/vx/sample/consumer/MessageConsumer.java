package cn.jeantsai.vx.sample.consumer;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.http.HttpServerRequest;
import rx.Single;


public class MessageConsumer extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(this::getResultsFromProducer)
                .listen(9998);

    }

    private void getResultsFromProducer(HttpServerRequest req) {
        EventBus bus = vertx.eventBus();

        Single<JsonObject> s1 = bus
                .<JsonObject>rxSend("hello", "jean")
                .map(Message::body);
        Single<JsonObject> s2 = bus
                .<JsonObject>rxSend("hello", "louis")
                .map(Message::body);

        Single
                .zip(s1, s2, (jean, louis) ->
                        "<html><header><title>Sample Message Consumer</title></header>" +
                        "<body><p>From: " + jean.getString("served-by") + " - " +
                        jean.getString("message") +
                        "</p><p>From: " + louis.getString("served-by") + " - " +
                        louis.getString("message") + "</p></body></html>"
                )
                .subscribe(
                        x -> {
                            req.response().end(x);
                        },
                        t -> {
                            t.printStackTrace();
                            req.response()
                                    .setStatusCode(500)
                                    .end(t.getMessage());
                        }
                );
    }
}
