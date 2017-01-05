package api.version_2_3_0.tracks;

import api.version_2_3_0.tracks.http.URLifier;

import java.net.URL;

public class TestEnv {

    private final String theURL;
    private final String userName;
    private final String userPassword;
    private String proxyUrl;
    private int proxyPort;
    private boolean useProxy;

    public TestEnv(String theURL, String userName,
                                  String userPassword){
        this.theURL = theURL;
        this.userName = userName;
        this.userPassword = userPassword;
        this.useProxy = false;
    }


    public void configureProxy(String url, int port){
        this.proxyUrl = url;
        this.proxyPort = port;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public URL getURL() {
        return URLifier.getURLfromString(theURL);
    }


    public boolean useProxy(){
        return (useProxy && proxyUrl!=null);
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getProxyURL() {
        return proxyUrl;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

}