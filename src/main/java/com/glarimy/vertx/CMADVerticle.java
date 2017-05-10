package com.glarimy.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class CMADVerticle extends AbstractVerticle {
	@Override
	public void start(Future<Void> future) throws Exception {
		Router router = Router.router(vertx);
		router.route("/static/*").handler(StaticHandler.create("web"));
		router.get("/library/book/:isbn").handler(rctx -> {
			String isbn = rctx.request().getParam("isbn");
			JsonObject config = new JsonObject();
			config.put("db_name", "cmad");
			config.put("connection_string", "mongodb://localhost:27017");
			MongoClient client = MongoClient.createShared(vertx, config);
			client.find("books", new JsonObject().put("isbn", Integer.parseInt(isbn)), res -> {
				if (res.succeeded()) {
					if (res.result().size() == 0)
						rctx.response().setStatusCode(404).end();
					else {
						JsonObject book = res.result().get(0);
						String json = Json.encodePrettily(book);
						rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
								.end(json);
					}
				} else {
					res.cause().printStackTrace();
					rctx.response().setStatusCode(500).end();
				}
			});
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 8080),
				result -> {
					if (result.succeeded()) {
						future.complete();
					} else {
						future.fail(result.cause());
					}
				});
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}
}