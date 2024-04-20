package com.github.bibenga.palabras.api;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import com.github.bibenga.palabras.entities.HibernateUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.vue.VueComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebApiApplication {
    // https://javalin.io/tutorials/openapi-example

    static { //runs when the main class is loaded.
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        // var session = HibernateUtil.getSessionFactory().getCurrentSession();
        // session.close();

        var app = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.http.asyncTimeout = 10_000L;
            // config.staticFiles.add("/public");
            config.staticFiles.enableWebjars();

            config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withInfo(info -> info.setTitle("Javalin OpenAPI example"));
                });
            }));
            config.registerPlugin(new SwaggerPlugin());
            config.registerPlugin(new ReDocPlugin());

            config.vue.vueInstanceNameInJs = "app";
            // config.vue.rootDirectory("classpath:vue");
            config.vue.isDevFunction = ctx -> false;
            // config.vue.rootDirectory(config.classpath("/vue"));

            config.router.mount(router -> {
                router.beforeMatched(WebApiApplication::handleAccess);
            }).apiBuilder(() -> {
                // frontend routes
                get("/", ctx -> ctx.redirect("/olala"));
                get("/olala", new VueComponent("olala"));
                get("/purum", new VueComponent("purum"));
            }).apiBuilder(() -> {
                // api routes
                path("/api/users", () -> {
                    path("/count", () -> {
                        get(UserController::getCount);
                    });
                    get(UserController::getAll);
                    post(UserController::create);
                    path("/{uid}", () -> {
                        get(UserController::get);
                        // patch(UserController::update);
                        // delete(UserController::delete);
                    });
                    // ws("/events", userController::webSocketEvents);
                });
            });
        });
        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        //     app.stop();
        // }));
        app.start(8000);
    }

    private static void handleAccess(Context ctx) {
        log.info("handleAccess: {}", ctx.req().getPathInfo());
    }

}
