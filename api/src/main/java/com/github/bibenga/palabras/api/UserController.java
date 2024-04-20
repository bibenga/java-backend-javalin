package com.github.bibenga.palabras.api;

import java.util.stream.Collectors;

import com.github.bibenga.palabras.entities.HibernateUtil;
import com.github.bibenga.palabras.entities.User;
import com.github.bibenga.palabras.entities.User_;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserController {

    @OpenApi(summary = "Get users count", operationId = "getUsersCount", path = "/api/users/count",
            methods = HttpMethod.GET, tags = {"User"}, responses = {@OpenApiResponse(status = "200",
                    content = {@OpenApiContent(from = Long.class)}),})
    public static void getCount(Context context) {
        var session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();

            var cb = session.getCriteriaBuilder();
            var cr = cb.createQuery(Long.class);
            var root = cr.from(User.class);
            cr.select(cb.count(root));
            var q = session.createQuery(cr);
            // var cnt = q.list().get(0);
            var cnt = q.uniqueResult();

            context.json(cnt);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @OpenApi(summary = "Get all users", operationId = "getAllUsers", path = "/api/users",
            methods = HttpMethod.GET, tags = {"User"}, responses = {@OpenApiResponse(status = "200",
                    content = {@OpenApiContent(from = UserDTO[].class)}),
            // @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
            // @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            })
    public static void getAll(Context ctx) {
        log.info("getAll");
        // NaiveRateLimit.requestPerTimeUnit(ctx, 5, TimeUnit.SECONDS); // throws if rate limit is exceeded

        var session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();

            // var q = session.createQuery("select count(*) from User", Long.class);
            // var cnt = q.list().get(0);
            // log.info("users: count={}", cnt);

            var cb = session.getCriteriaBuilder();
            var cr = cb.createQuery(User.class);
            var root = cr.from(User.class);
            cr.select(root);
            var q = session.createQuery(cr);
            // UserDTO.builder().setUid(u.getExternalId()).setUsername(u.getUsername()).build();
            var users = q.list().stream().map(u -> new UserDTO(u.getExternalId(), u.getUsername()))
                    .collect(Collectors.toList());

            ctx.json(users);

            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @OpenApi(summary = "Create new user", operationId = "createUser", path = "/api/users",
            methods = HttpMethod.POST, tags = {"User"},
            requestBody = @OpenApiRequestBody(content = {@OpenApiContent(from = UserDTO.class)}),
            responses = {@OpenApiResponse(status = "200"),})
    public static void create(Context ctx) {
        UserDTO userDto = ctx.bodyAsClass(UserDTO.class);
        log.info("create: {}", userDto);

        var session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            var user = User.builder().setExternalId(userDto.getUid())
                    .setUsername(userDto.getUsername()).build();
            session.persist(user);
            session.getTransaction().commit();
            ctx.json(userDto);
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @OpenApi(summary = "Get user", operationId = "getUser", path = "/api/users/{uid}",
            methods = HttpMethod.GET, tags = {"User"},
            pathParams = {@OpenApiParam(name = "uid", type = String.class,
                    description = "The user UID", required = true)},
            responses = {
                    @OpenApiResponse(status = "200",
                            content = {@OpenApiContent(from = UserDTO.class)}),
                    @OpenApiResponse(status = "404")})
    public static void get(Context ctx) {
        // NaiveRateLimit.requestPerTimeUnit(ctx, 5, TimeUnit.SECONDS); // throws if rate limit is exceeded

        // var uid = ctx.pathParamAsClass("uid", String.class);
        var uid = ctx.pathParamAsClass("uid", String.class).get();
        log.info("get: uid={}", uid);

        var session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();

            var cb = session.getCriteriaBuilder();
            var cr = cb.createQuery(User.class);
            var root = cr.from(User.class);
            cr.select(root);
            cr.where(cb.equal(root.get(User_.externalId), uid));
            var q = session.createQuery(cr);
            var user = q.uniqueResult();
            if (user == null) {
                throw new NotFoundResponse();
            }
            // var u = q.list().get(0);

            var userDto = new UserDTO(user.getExternalId(), user.getUsername());
            ctx.json(userDto);

            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
