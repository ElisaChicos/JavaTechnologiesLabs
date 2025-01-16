import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class HealthCheckTests {

    private static final String BASE_URL = "http://localhost:8080/Lab3_war/api/health";

    @Test
    public void testLivenessCheck() {
        given()
                .when()
                .get(BASE_URL + "/liveness")
                .then()
                .statusCode(200)
                .body(containsString("UP"));
    }

    @Test
    public void testReadinessCheck() {
        given()
                .when()
                .get(BASE_URL + "/readiness")
                .then()
                .statusCode(200)
                .body(containsString("UP"));
    }

    @Test
    public void testToggleReadiness() {
        // Simulate readiness down
        given()
                .when()
                .post(BASE_URL + "/toggle-readiness")
                .then()
                .statusCode(200);

        // Readiness should now be down
        given()
                .when()
                .get(BASE_URL + "/readiness")
                .then()
                .statusCode(503)
                .body(containsString("DOWN"));

        // Toggle readiness back to up
        given()
                .when()
                .post(BASE_URL + "/toggle-readiness")
                .then()
                .statusCode(200);

        // Readiness should now be up again
        given()
                .when()
                .get(BASE_URL + "/readiness")
                .then()
                .statusCode(200)
                .body(containsString("UP"));
    }

    @Test
    public void testToggleLiveness() {
        // Simulate liveness down
        given()
                .when()
                .post(BASE_URL + "/toggle-liveness")
                .then()
                .statusCode(200);

        // Liveness should now be down
        given()
                .when()
                .get(BASE_URL + "/liveness")
                .then()
                .statusCode(500)
                .body(containsString("DOWN"));

        // Toggle liveness back to up
        given()
                .when()
                .post(BASE_URL + "/toggle-liveness")
                .then()
                .statusCode(200);

        // Liveness should now be up again
        given()
                .when()
                .get(BASE_URL + "/liveness")
                .then()
                .statusCode(200)
                .body(containsString("UP"));
    }
}
