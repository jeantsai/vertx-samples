package cn.jeantsai.vx.sample.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MessageProducer extends AbstractVerticle {

    @Override
    public void start() {
        vertx.eventBus().<String>consumer("hello", message -> {
            JsonObject json = new JsonObject();
            json.put("served-by", this.toString());

            if (message.body().isEmpty()) {
                message.reply(json.put("message", "Hello"));
            } else {
                message.reply(json.put("message", "Hello " + message.body()));
            }
        });
    }

}
