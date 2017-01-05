package api.version_2_3_0.unittests;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUrlParseExampleTest {

    @Test
    public void regexExample(){

        String location = "http://localhost/projects/12.xml";

        String findLocation = "[\\S]+?/projects/(\\d+)\\.xml";

        Pattern p = Pattern.compile(findLocation);

        Matcher m = p.matcher(location);

        String projectId = "";
        if (m.find()) {
            projectId = m.group(1);
        }

        Assert.assertEquals("12", projectId);
    }
}