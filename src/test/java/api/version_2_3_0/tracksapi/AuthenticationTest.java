package api.version_2_3_0.tracksapi;

import api.version_2_3_0.environmentconfig.TestEnvDefaults;
import api.version_2_3_0.testdata.RandomDataGenerator;
import api.version_2_3_0.tracks.TracksApiEndPoints;
import api.version_2_3_0.tracks.http.HttpMessageSender;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AuthenticationTest {

    @Test
    public void aUserCanNotAccessIfNotAuthenticatedWithBasicAuth(){

        RandomDataGenerator wordGenerator = new RandomDataGenerator();

        HttpMessageSender http = new HttpMessageSender(
                                        TestEnvDefaults.getURL());

        // add basic auth but with wrong details
        http.basicAuth( TestEnvDefaults.getAdminUserName()
                                + wordGenerator.randomWord(),
                        TestEnvDefaults.getAdminUserPassword()
                                + wordGenerator.randomWord());

        Response response = http.getResponseFrom(
                                    TracksApiEndPoints.todos);
        Assert.assertEquals(401, response.getStatusCode());
    }

    @Test
    public void aUserCanNotAccessIfNoBasicAuthHeader(){

        given().
                contentType("text/html,application/xhtml+xml," +
                            "application/xml;q=0.9,*/*;q=0.8").
        expect().
                statusCode(401).
        when().
                get(TestEnvDefaults.getURL().toExternalForm() +
                        TracksApiEndPoints.todos);
    }

    @Test
    public void aUserCanAuthenticateAndUseAPIWithBasicAuth(){

        HttpMessageSender http = new HttpMessageSender(
                                            TestEnvDefaults.getURL());
        // setup the sender to use a proxy
        //http.proxy(TestEnvDefaults.getProxyUrl(),
        //          TestEnvDefaults.getProxyPort());

        http.basicAuth( TestEnvDefaults.getAdminUserName(),
                        TestEnvDefaults.getAdminUserPassword());

        Response response = http.getResponseFrom(
                                    TracksApiEndPoints.todos);

        Assert.assertEquals(200, response.getStatusCode());
    }

    @Test
    public void aUserCanAccessWithBasicAuthHeader(){

        given().
                contentType("text/xml").
                auth().preemptive().basic(
                            TestEnvDefaults.getAdminUserName(),
                            TestEnvDefaults.getAdminUserPassword()).
        expect().
                statusCode(200).
        when().
                get(TestEnvDefaults.getURL().toExternalForm() +
                        TracksApiEndPoints.todos);
    }

}