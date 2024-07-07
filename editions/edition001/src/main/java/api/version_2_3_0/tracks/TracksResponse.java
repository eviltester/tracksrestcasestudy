package api.version_2_3_0.tracks;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/*  This class was not used in the case study, it was created
    to demonstrate the separation of RestAssured from TracksApi
 */

public class TracksResponse {

    private final int statusCode;
    private final String body;
    private final Map<String,String> responseHeaders;

    public TracksResponse(Response response) {
        this.statusCode = response.getStatusCode();

        this.responseHeaders = new HashMap<>();
        Headers headers = response.getHeaders();
        for(Header header: headers){
            responseHeaders.put(header.getName(), header.getValue());
        }

        this.body = response.body().asString();
    }

    public int getStatusCode(){
        return statusCode;
    }

    public String body(){
        return body;
    }

    public Map<String,String> getHeaders(){
        return responseHeaders;
    }
}
