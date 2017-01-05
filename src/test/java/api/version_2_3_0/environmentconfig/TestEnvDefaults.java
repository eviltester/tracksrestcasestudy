package api.version_2_3_0.environmentconfig;

import api.version_2_3_0.tracks.TestEnv;
import api.version_2_3_0.tracks.http.URLifier;

import java.net.URL;

public class TestEnvDefaults {
    public static final String theURL = "http://192.168.126.129";
    public static final String adminUserName="user";
    public static final String adminUserPassword="bitnami";
    public static final String proxyUrl="localhost";
    public static final int proxyPort=8000;

    public static URL getURL() {
        return URLifier.getURLfromString(theURL);
    }

    public static String getAdminUserName() {
        return adminUserName;
    }

    public static String getAdminUserPassword() {
        return adminUserPassword;
    }

    public static String getProxyUrl() {
        return proxyUrl;
    }

    public static int getProxyPort() {
        return proxyPort;
    }

    public static TestEnv getTestEnv(){
        TestEnv env = new TestEnv(theURL, adminUserName, adminUserPassword);
        env.configureProxy(proxyUrl, proxyPort);
        env.setUseProxy(false);
        return env;
    }
}
