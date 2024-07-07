package api.version_2_3_0.tracks.http;

import java.net.MalformedURLException;
import java.net.URL;

public class URLifier {

    public static URL getURLfromString(String aString){
        try {
            return new URL(aString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                            String.format(
                            "URL %s is not correctly formatted",
                            aString));
        }
    }
}
