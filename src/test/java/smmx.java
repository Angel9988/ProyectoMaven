import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import java.net.URLEncoder;
import java.util.Base64;


public class smmx {

    @Test
    public void getTokenStatusFailTest(){
        given().queryParam("lang","es").when()
                .log().all()
                .post("https://webapi.segundamano.mx/nga/api/v1.1/private/accounts")
                .then().statusCode(400);
    }

    @Test
    public void testTokenFail() {
        //Request an account token without authorization header
        RestAssured.baseURI = String.format("https://webapi.segundamano.mx/nga/api/v1.1/private/accounts");
        Response response = given().log().all()
                .post();
        //validations
        System.out.println("Status expected: 400");
        System.out.println("Result: " + response.getStatusCode());
        assertEquals(400, response.getStatusCode());
        String errorCode = response.jsonPath().getString("error.code");
        System.out.println("Error Code expected: VALIDATION FAILED \nResult: " + errorCode);
        assertEquals("VALIDATION_FAILED", errorCode);
    }

    @Test
    public void getToken(){

        String email = "apitest@mailinator.com";
        String pass = "12345";
        String ToEncode = email + ":" + pass;
        String Basic_encoded = Base64.getEncoder().encodeToString(ToEncode.getBytes());

        RestAssured.baseURI = String.format("https://webapi.segundamano.mx/nga/api/v1.1/private/accounts");
        Response response = given().queryParam("lang","es").log().all()
                            .header("Authorization", "Basic " + Basic_encoded)
                            .post();

        String body = response.getBody().asString();
        System.out.println("Response Body: " + response.getBody().asString());

        assertEquals(200, response.getStatusCode());
        assertNotNull(body);
        assertTrue(body.contains("access_token"));

    }
}
