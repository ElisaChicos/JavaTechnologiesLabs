import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MetricsTests {

    private static final String BASE_URL = "http://localhost:8080/api";

    @Test
    public void testMonitoredEndpoint() {
        given()
                .when()
                .get(BASE_URL + "/metrics/monitored-endpoint")
                .then()
                .statusCode(200)
                .body(containsString("Monitored endpoint accessed"));
    }

    @Test
    public void testMetricsCollected() {
        // Call the endpoint a few times to generate metrics
        for (int i = 0; i < 5; i++) {
            given().when().get(BASE_URL + "/metrics/monitored-endpoint").then().statusCode(200);
        }

        // Check the metrics
        given()
                .when()
                .get(BASE_URL + "/metrics")
                .then()
                .statusCode(200)
                .body(containsString("monitoredEndpointInvocations"))
                .body(containsString("monitoredEndpointResponseTime"));
    }
}
