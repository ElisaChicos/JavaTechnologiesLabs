import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SecureRestServiceTests {

    private static final String BASE_URL = "http://localhost:8080/Lab3_war/api/test";

    @Test
    public void testAdminFallback() {
        given()
                .when()
                .get(BASE_URL + "/admin")
                .then()
                .statusCode(200) // Fallback should still return HTTP 200
                .body(containsString("Fallback: Timeout occurred"));
    }

    @Test
    public void testTeacherRetries() {
        given()
                .when()
                .get(BASE_URL + "/teacher")
                .then()
                .statusCode(200)
                .body(containsString("Fallback: Retry attempts failed"));
    }

    @Test
    public void testStudentCircuitBreaker() {
        // Simulate multiple failures to trigger CircuitBreaker
        for (int i = 0; i < 3; i++) {
            given()
                    .when()
                    .get(BASE_URL + "/student")
                    .then()
                    .statusCode(200)
                    .body(containsString("Fallback: Operation failed"));
        }

        // Next request should return CircuitBreaker fallback
        given()
                .when()
                .get(BASE_URL + "/student")
                .then()
                .statusCode(200)
                .body(containsString("Fallback: Circuit breaker open"));
    }

    @Test
    public void testBulkheadThreadPool() {
        given()
                .when()
                .get(BASE_URL + "/bulkhead-thread")
                .then()
                .statusCode(200)
                .body(containsString("Bulkhead thread-pool test success"));
    }

    @Test
    public void testBulkheadSemaphore() {
        given()
                .when()
                .get(BASE_URL + "/bulkhead-semaphore")
                .then()
                .statusCode(200)
                .body(containsString("Bulkhead semaphore test success"));
    }
}
