package endpoint;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import util.TokenGenerator;
import util.WebContext;

import java.util.logging.Logger;

class CustomerIT {

    private static final String ENDPOINT = String.join("/", WebContext.API_BASE_URL, "entity", "customer");
    private static final Logger LOG = Logger.getLogger(CustomerIT.class.getName());
    CustomerIT() {
    }

    @Test
    void testEndpointIsOnline() {

        RestAssured
                .when()
                .options(ENDPOINT)
                .then()
                .assertThat()
                .header(HttpHeaders.ALLOW, Matchers.is("OPTIONS, GET, POST"))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testFind() {
        var path = String.join("/", ENDPOINT, DataSet.JOHN_DOE_ID);
        var jwt = TokenGenerator.generateNewToken();


        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .when()
                .get(path)
                .then()
                .body("givenName", Matchers.is("John"))
                .body("familyName", Matchers.is("DOE"))
                .body("email", Matchers.is("john.doe@example.com"))
                .body("phoneNumber", Matchers.is("0091077640"))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testCreate() {
        var jwt = TokenGenerator.generateNewToken();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(DataSet.SAMPLE_JSON_DATA)
                .when()
                .post(ENDPOINT)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void testUpdate() {
        var updatedData = DataSet.SAMPLE_JSON_DATA
                .replace("0000000000", "0123456789")
                .replace("Johnny", "Johnathan")
                .replace("johnny.doe@example.com", "johnathan.doe@example.com");

        var path = String.join("/", ENDPOINT, DataSet.JOHN_DOE_ID);
        var jwt = TokenGenerator.generateNewToken();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(updatedData)
                .when()
                .put(path)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void testDelete() {
        var path = String.join("/", ENDPOINT, DataSet.JANE_DOE_ID);
        var jwt = TokenGenerator.generateNewToken();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .when()
                .delete(path)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void testBadCreate() {
        var jwt = TokenGenerator.generateNewToken();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(DataSet.BAD_JSON_DATA)
                .when()
                .post(ENDPOINT)
                .then()
                .body("violations", Matchers.hasSize(3))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    private static final class DataSet {

        private static final String JOHN_DOE_ID = "0d5be73c-55f4-4379-accb-a7dcef0e9f2d";

        private static final String JANE_DOE_ID = "6eb7cd4a-dbb4-4a7e-9bce-e3c959739c53";

        private static final String SAMPLE_JSON_DATA = """
                {
                    "givenName": "Johnny",
                    "familyName": "DOE",
                    "email": "johnny.doe@example.com",
                    "phoneNumber": "0000000000",
                    "gender": {
                        "id": "09ee5d9d-bf9b-4b5d-aad0-19117eb8da34",
                        "name": "Male",
                        "code": "M",
                        "description": "A male humain"
                    }
                }
                """;

        private static final String BAD_JSON_DATA = """
                {
                    "givenName": "Johnny",
                    "familyName": "",
                    "email": "@bad.email.org",
                    "phoneNumber": "0000000000"
                }
                """;
    }
}
