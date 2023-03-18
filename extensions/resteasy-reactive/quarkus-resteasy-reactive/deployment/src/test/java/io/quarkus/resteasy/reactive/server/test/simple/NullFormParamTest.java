package io.quarkus.resteasy.reactive.server.test.simple;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.jboss.resteasy.reactive.RestForm;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.QuarkusUnitTest;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

public class NullFormParamTest {

    @RegisterExtension
    static QuarkusUnitTest test = new QuarkusUnitTest()
            .withApplicationRoot(jar -> jar.addClass(NullResource.class));

    @ParameterizedTest
    @ValueSource(strings = { "bean", "direct" })
    void emptyRequest(String endpoint) {
        given()
                .post("/null/" + endpoint)
                .then()
                .statusCode(200)
                .body(Matchers.equalTo("null,null"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "bean", "direct" })
    void presentInRequestButNoValue(String endpoint) {
        given()
                .formParam("formString", new Object[] {})
                .formParam("formInteger", new Object[] {})
                .post("/null/" + endpoint)
                .then()
                .statusCode(400);
    }

    @ParameterizedTest
    @ValueSource(strings = { "bean", "direct" })
    void stringEmptyIntegerNoValue(String endpoint) {
        given()
                .formParam("formString", "")
                .formParam("formInteger", new Object[] {})
                .post("/null/" + endpoint)
                .then()
                .statusCode(400);
    }

    @ParameterizedTest
    @ValueSource(strings = { "bean", "direct" })
    void bothEmpty(String endpoint) {
        given()
                .formParam("formString", "")
                .formParam("formInteger", "")
                .post("/null/" + endpoint)
                .then()
                .statusCode(400);
    }

    @Path("null")
    public static class NullResource {

        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Path("bean")
        public String bean(@BeanParam Bean bean) {
            return String.format("%s,%s", bean.formString, bean.formInteger);
        }

        @POST
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Path("direct")
        public String direct(@RestForm String formString, @RestForm Integer formInteger) {
            return String.format("%s,%s", formString, formInteger);
        }
    }

    public static class Bean {
        @RestForm
        String formString;

        @RestForm
        Integer formInteger;
    }
}
