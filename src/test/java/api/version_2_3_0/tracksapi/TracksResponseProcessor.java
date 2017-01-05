package api.version_2_3_0.tracksapi;


import io.restassured.response.Response;

public class TracksResponseProcessor {
    private final Response response;

    public TracksResponseProcessor(Response response) {
        this.response = response;
    }

    public String getIdFromLocation() {
        String location = response.getHeader("location");
        String[] urlChunks = location.split("/");
        String id = urlChunks[urlChunks.length-1];
        return id;
    }
}
