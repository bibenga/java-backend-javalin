package com.github.bibenga.palabras.api;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserController {

    @OpenApi(summary = "Get all users", operationId = "getAllUsers", path = "/users", methods = HttpMethod.GET, tags = {
            "User" }, responses = {
                    @OpenApiResponse(status = "200", content = { @OpenApiContent(from = User[].class) }),
            // @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
            // @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            })
    public static void getAll(@NotNull Context context) {
        log.info("getAll");
        // NaiveRateLimit.requestPerTimeUnit(context, 5, TimeUnit.SECONDS); // throws if rate limit is exceeded
        var users = new ArrayList<User>();
        users.add(new User("uid1", "admin"));
        users.add(new User("uid2", "tester"));
        context.json(users);
    }
}
