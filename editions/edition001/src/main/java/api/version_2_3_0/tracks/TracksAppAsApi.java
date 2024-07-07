package api.version_2_3_0.tracks;

import api.version_2_3_0.tracks.http.HttpMessageSender;
import api.version_2_3_0.tracks.http.UrlParams;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.net.URL;
import java.util.Map;


public class TracksAppAsApi {

    private final URL url;
    private final HttpMessageSender httpMessageSender;
    private final String username;
    private final String password;
    private Map<String, String> loggedInCookieJar;

    /*
    public TracksAppAsApi(URL url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        // construct and pass this in outside of the TracksApi
        httpMessageSender = new HttpMessageSender(url);
        httpMessageSender.basicAuth(username, password);
    }
    */

    // TODO: construct and pass HttpMessageSender in outside of the TracksApi

    public TracksAppAsApi(TestEnv env) {
        this.url = env.getURL();
        this.username = env.getUserName();
        this.password = env.getUserPassword();

        httpMessageSender = new HttpMessageSender(url);
        httpMessageSender.basicAuth(username, password);
        if(env.useProxy()){
            httpMessageSender.proxy(env.getProxyURL(), env.getProxyPort());
        }
    }


    // Useful information about RestAssured XmlPath
    // https://github.com/rest-assured/rest-assured/wiki/usage#xml-using-xmlpath
    // https://blog.jayway.com/2013/04/12/whats-new-in-rest-assured-1-8/
    // http://stackoverflow.com/questions/2117739/what-is-the-full-syntax-of-groovys-gpath-expressions
    // https://rest-assured.googlecode.com/svn/tags/1.6/apidocs/com/jayway/restassured/path/xml/XmlPath.html
    // http://stackoverflow.com/questions/28298851/finding-all-xml-nodes-with-rest-assureds-xmlpath
    // http://www.hascode.com/2011/10/testing-restful-web-services-made-easy-using-the-rest-assured-framework/
    // http://stackoverflow.com/questions/33167541/checking-html-document-with-rest-assured

    // constructs tried
    // "html.body.div.div.find {it.@id=='database_auth_form'}.form"
    //Node theForm = htmlParser.get("**.find {it.name() =='form'}");
    //Node authenticityToken = theForm.get("input@name='authenticity_token'");


    public void login() {

        Response response = httpMessageSender.getResponseFrom("/login");

        String authenticity_token =
                        getAuthenticityTokenFromResponse(response);

        // post the login form and get the cookies
        response = loginUserPost(username, password, authenticity_token);

        if(response.getStatusCode()==302) {
            //get the cookies
            loggedInCookieJar = response.getCookies();
        }else{
            System.out.println(response.asString());
            new RuntimeException("Could not login");
        }
    }

    private String getAuthenticityTokenFromResponse(
                                            Response response){
        // get the authenticity_token from the response
        XmlPath htmlParser = response.body().htmlPath();
        String auth_token_path =
                    "**.find {it.@name =='authenticity_token'}.@value";
        String authenticity_token = htmlParser.get(auth_token_path);
        return authenticity_token;
    }

    private Response loginUserPost(String username, String password,
                                   String authenticityToken) {

        UrlParams params = new UrlParams();
        params.add("utf8","%E2%9C%93");
        params.addEncoded("authenticity_token",
                            authenticityToken);
        params.add("user_login", username);
        params.add("user_password", password);
        params.add("user_noexpiry", "on");
        params.add("login", "Sign+in");

        Response response = httpMessageSender.postFormMessageTo(
                                                    params.toString(),
                                            "/login");
        return response;
    }

    public Response createUser(String username, String password){

        Response response= httpMessageSender.getResponseFrom(
                                                    "/signup",
                                                    loggedInCookieJar);

        String authenticity_token =
                            getAuthenticityTokenFromResponse(response);

        // cookies seem to change after signup
        // - if I don't use these then the request fails
        loggedInCookieJar = response.getCookies();

        response =  createUserPost(username, password,
                                    authenticity_token,
                                    loggedInCookieJar);

        if(response.getStatusCode()!=302) {
            //get the cookies
            loggedInCookieJar = response.getCookies();
        }else{
            System.out.println(response.asString());
            new RuntimeException(
                    String.format("Could not create user %s %s",
                                        username, password));
        }

        return response;
    }

    // Had to use the form submission to create a user to bypass the API issues
    private Response createUserPost(String username, String password,
                                    String authenticityToken,
                                    Map<String, String> loggedInCookieJar) {

        UrlParams params = new UrlParams();
        params.add("utf8","%E2%9C%93");
        params.addEncoded("authenticity_token", authenticityToken);
        params.add("user%5Blogin%5D", username);
        params.add("user%5Bpassword%5D", password);
        params.add("user%5Bpassword_confirmation%5D", password);

        Response response = httpMessageSender.postFormMessageTo(
                                                    params.toString(),
                                                    "/users",
                                                    loggedInCookieJar);

        return response;
    }
}