package com.github.bibenga.palabras.api;

import io.javalin.http.Context;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;

public class UserControllerTest {
    private final Context ctx = mock(Context.class);

    @Test
    public void testGetCount() {
        // when(ctx.queryParam("username")).thenReturn("Roland");
        UserController.getCount(ctx); // the handler we're testing
        verify(ctx).status(200);
    }

}
