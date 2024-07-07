package api.version_2_3_0.tracks.http;


import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpMessageSender {


    private final URL url;
    private String authUser;
    private String authPassword;
    private Response lastResponse;


    public HttpMessageSender(URL aUrl) {
        this.url = aUrl;
    }

    public void proxy(String proxyURL, int proxyPort) {
        if(proxyURL!=null) {
            RestAssured.proxy(proxyURL, proxyPort);
        }
    }


    /*
        Authentication is set using

    given().
        auth().preemptive().basic(authUser, authPassword).

     */
    public void basicAuth(String user, String password) {
        this.authUser = user;
        this.authPassword = password;
    }

    public Response postFormMessageTo(String msg, String endpoint){
        return postFormMessageTo(msg, endpoint, anEmptyCookieJar());
    }

    public Response postFormMessageTo(String msg, String endpoint,
                                      Map<String, String> cookieJar){
        return postMessageTo(msg, endpoint,
                "application/x-www-form-urlencoded", cookieJar);
    }

    public Response postXmlMessageTo(String msg, String endpoint){
        return postMessageTo(msg, endpoint,
                            "text/xml", anEmptyCookieJar());
    }

     /*
     if I .auth().basic(authUser, authPassword).
     then it sends a request without authentication headers to the serve
     to see what authentication the server asks for
     before sending the request with the authentication header
     this effectively sends two messages
    auth().basic(authUser, authPassword).
     If I add the authorisation header myself then...
     Only one message is sent, but if the authentication scheme changes
     Then I have to handle it, and I have to encode the username/password myself
        header(new Header("Authorization", "Basic dXNlcjpiaXRuYW1p")).
     Or if I use preemptive mode rather than 'challenge' mode
     It works the way I 'expected'
     */

    private Response postMessageTo(String msg, String endpoint,
                                   String contentType,
                                   Map<String, String> cookieJar){

        URL theEndPointUrl = createEndPointURL(url, endpoint);

        Response ret =
                given().
                        auth().preemptive().
                            basic(authUser, authPassword).
                        body(msg).
                        contentType(contentType).
                        cookies(cookieJar).
                when().
                        post(theEndPointUrl.toExternalForm()).
                andReturn();

        // ignore CREATED UNAUTHORIZED CONFLICT
        if( ret.statusCode()!=201 && ret.statusCode()!=401 &&
            ret.statusCode()!=409 ){
            System.out.println("POTENTIAL BUG - " +
                                ret.statusCode() + " FOR " +
                                endpoint + "\n" + msg );
        }

        return setLastResponse(ret);
    }



    public Response putXmlMessageTo(String endpoint, String msg) {
        return putXmlMessageTo(endpoint, msg, anEmptyCookieJar());
    }

    public Response putXmlMessageTo(String endpoint, String msg,
                                    Map<String, String> cookieJar) {

        URL theEndPointUrl = createEndPointURL(url, endpoint);

        Response ret =
                given().
                        auth().preemptive().
                                    basic(authUser, authPassword).
                        body(msg).contentType("text/xml").
                        cookies(cookieJar).
                when().
                        put(theEndPointUrl.toExternalForm()).
                andReturn();

        return setLastResponse(ret);
    }


    public Response getXMLResponseFrom(String endpoint) {

        URL theEndPointUrl = createEndPointURL(url, endpoint);

        Response ret =
                given().
                        auth().preemptive().
                                basic(authUser, authPassword).
                        contentType("text/xml").
                get(theEndPointUrl.toExternalForm()).
                andReturn();

        return setLastResponse(ret);
    }

    public Response getResponseFrom(String endpoint){
        return getResponseFrom(endpoint, anEmptyCookieJar() );
    }

    public Response getResponseFrom(String endpoint,
                                    Map<String, String> cookieJar) {

        URL theEndPointUrl = createEndPointURL(url, endpoint);

        String ct = "text/html,application/xhtml+xml," +
                    "application/xml;q=0.9,*/*;q=0.8";

        Response ret =
                given().
                    auth().preemptive().
                                basic(authUser, authPassword).
                    cookies(cookieJar).
                    contentType(ct).
                get(theEndPointUrl.toExternalForm()).
                andReturn();

        return setLastResponse(ret);
    }

    public Response deleteMessage(String endpoint){
        return deleteMessage(endpoint, anEmptyCookieJar() );
    }

    public Response deleteMessage(String endpoint,
                                  Map<String, String> cookieJar) {
        URL theEndPointUrl = createEndPointURL(url, endpoint);

        String ct= "text/html,application/xhtml+xml,"+
                   "application/xml;q=0.9,*/*;q=0.8";

        Response ret =
                given().
                    auth().preemptive().
                                basic(authUser, authPassword).
                    cookies(cookieJar).
                    contentType(ct).
                delete(theEndPointUrl.toExternalForm()).
                andReturn();

        return setLastResponse(ret);
    }

    public Response getLastResponse(){
        return lastResponse;
    }

    private Response setLastResponse(Response ret) {
        lastResponse = ret;
        return ret;
    }

    private HashMap<String,String> anEmptyCookieJar(){
        return new HashMap<String,String>();
    }

    // avoid users of HttpMessageSender having to catch MalformedURLExceptions
    private URL createEndPointURL(URL url, String endpoint) {

        try {

            return new URL(url, endpoint);

        } catch (MalformedURLException e) {

            e.printStackTrace();
            new RuntimeException(
                    String.format("Could not construct %s endpoint using %s",
                                endpoint, url.toExternalForm()));
            return null;
        }

    }


}