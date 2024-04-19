package com.github.bibenga.palabras.api;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import org.jetbrains.annotations.NotNull;

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

    // static {
    //     var log4j2File = new File("log4j2.xml");
    //     // System.out.println(log4j2File.toURI().toString());
    //     System.setProperty("log4j2.configurationFile", log4j2File.toURI().toString());
    // }

    public static void main(String[] args) {
        // System.out.println("olala");
        // try {
        //     var inputStream = new FileInputStream("log4j2.xml");
        //     var source = new ConfigurationSource(inputStream);
        //     Configurator.initialize(null, source);
        // } catch (Exception ex) {
        //     ex.printStackTrace();
        // }
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
                get("/olala", new VueComponent("olala"));
                get("/purum", new VueComponent("purum"));
            }).apiBuilder(() -> {
                // api routes
                // get("/", ctx -> ctx.redirect("/users"));
                path("/api/users", () -> {
                    get(UserController::getAll);
                    // get();
                    // post(UserController::create);
                    // path("/{userId}", () -> {
                    //     get(UserController::getOne);
                    //     patch(UserController::update);
                    //     delete(UserController::delete);
                    // });
                    // ws("/events", userController::webSocketEvents);
                });
            });
        });
        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        //     app.stop();
        // }));
        app.start(8000);
    }

    private static void handleAccess(@NotNull Context context) {
        log.info("handleAccess: {}", context.req().getPathInfo());
    }

}
