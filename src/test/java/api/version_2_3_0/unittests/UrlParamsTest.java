package api.version_2_3_0.unittests;

import api.version_2_3_0.tracks.http.UrlParams;
import org.junit.Assert;
import org.junit.Test;

public class UrlParamsTest {

    @Test
    public void createURlParam(){

        String username = "bob";
        String password = "dobbs";
        String authenticityToken = "this token";


        UrlParams params = new UrlParams();
        params.add("utf8","%E2%9C%93");
        params.addEncoded("authenticity_token", authenticityToken);
        params.add("user%5Blogin%5D", username);
        params.add("user%5Bpassword%5D", password);
        params.add("user%5Bpassword_confirmation%5D", password);

        String utf8EncodedAuthenticityToken = "this+token";

        String expected = String.format("utf8=%%E2%%9C%%93&authenticity_token=%s&user%%5Blogin%%5D=%s&user%%5Bpassword%%5D=%s&user%%5Bpassword_confirmation%%5D=%s",
                utf8EncodedAuthenticityToken,
                username,
                password, password);

        Assert.assertEquals(expected, params.toString());
    }
}