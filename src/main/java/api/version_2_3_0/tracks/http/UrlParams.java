package api.version_2_3_0.tracks.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UrlParams {


    private List<String> params = new ArrayList<>();

    public void add(String paramName, String paramValue) {
        params.add(paramName + "=" + paramValue);
    }

    public void addEncoded(String paramName, String paramValue) {
        params.add(paramName + "=" + utf8EncodedString(paramValue));
    }

    @Override
    public String toString() {
        StringBuilder paramString = new StringBuilder();

        for(String aParam : params){
            if(paramString.length()!=0){
                paramString.append("&");
            }
            paramString.append(aParam);
        }

        return paramString.toString();
    }

    private String utf8EncodedString(String authenticityToken) {

        // since the encoding is 'hard coded' to UTF-8 I will take
        // the risk that an UnsupportedEncodingException is thrown and swallow it
        try {
            return URLEncoder.encode(authenticityToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("utf8EncodedString threw an UnsupportedEncodingException");
            e.printStackTrace();
            return "";
        }
    }


}