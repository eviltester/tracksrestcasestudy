package api.version_2_3_0.restassuredexamples;

import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredAuthenticationExamplesTest {


    @Ignore("Hardcoded to illustrate use of RestAssured")
    @Test
    public void aUserCanNotAccessIfNoBasicAuthHeaderUsingRestAssured(){

        given().
                contentType("text/xml").
        expect().
                statusCode(401).
        when().
                get("http://192.168.17.129/todos.xml");

    }

    @Ignore("Hardcoded to illustrate use of RestAssured Given When Then")
    @Test
    public void aUserCanNotAccessIfNoBasicAuthHeaderUsingGivenWhenThen(){

        given().
                contentType("text/xml").
        when().
                get("http://192.168.17.129/todos.xml").
        then().
                statusCode(401);
    }

    @Ignore("Hardcoded to illustrate use of RestAssured")
    @Test
    public void aUserCanAccessWithBasicAuthHeaderUsingRestAssured(){

        given().
                contentType("text/xml").
                auth().preemptive().basic("user", "bitnami").
        expect().
                statusCode(200).
        when().
                get("http://192.168.17.129/todos.xml");
    }
}