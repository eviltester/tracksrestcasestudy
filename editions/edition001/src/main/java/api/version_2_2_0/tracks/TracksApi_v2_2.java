package api.version_2_2_0.tracks;

import api.version_2_2_0.tracks.entity.TracksContext_v2_2;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.path.xml.element.NodeChildren;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;


public class TracksApi_v2_2 {
    private final String url;
    private String authUser;
    private String authPassword;

    public TracksApi_v2_2(String tracksUrl) {
        this(tracksUrl, "user", "password");
    }

    public TracksApi_v2_2(String tracksUrl, String username, String password) {
        this.url = tracksUrl;
        basicAuth(username, password);
    }

    public Response createContext(String aContext) {

        String msg = String.format("<context><name>%s</name></context>", aContext);

        return postXmlMessageTo(msg,"/contexts.xml");
    }


    public Response createProject(String newProjectName) {
        String msg = String.format("<project><name>%s</name></project>", newProjectName);

        return postXmlMessageTo(msg,"/projects.xml");
    }


    public Response createUser(String username, String password) {

        return createUserAppAsAPI(username, password);
    }

    // Warning - creating a user with the API does not work very well
    // The user is created but doesn't always work from the API and attempting to use the
    // user from the GUI fails - this may be a bug or unfinished feature of the app
    public Response createUserAPI(String username, String password) {
        String msg = String.format("<user><login>%s</login><password>%s</password></user>", username, password);
        return postXmlMessageTo(msg,"/users.xml");
    }


    // Had to use the form submission to create a user to bypass the API issues
    public Response createUserAppAsAPI(String username, String password) {

        String msg = String.format("utf8=%%E2%%9C%%93&user%%5Blogin%%5D=%s&user%%5Bpassword%%5D=%s&user%%5Bpassword_confirmation%%5D=%s", username, password,password);

        return postFormMessageTo(msg,"/users");
    }

    public Response createTodo(String todoName, String projectId, String contextId) {
        String msg = String.format("<todo>\n<description>%s</description>\n" +
                        "  <project_id>%s</project_id>\n" +
                        "  <context_id>%s</context_id>\n" +
                        "</todo>",
                todoName, projectId, contextId);

        return postXmlMessageTo(msg,"/todos.xml");
    }

    public Response postFormMessageTo(String msg, String endpoint) {
        return postMessageTo(msg, endpoint, "application/x-www-form-urlencoded");
    }

    public Response postXmlMessageTo(String msg, String endpoint) {
        return postMessageTo(msg, endpoint, "text/xml");
    }


    private Response postMessageTo(String msg, String endpoint, String contentType) {


        Response ret =
                given().
                        // if I .auth().basic(authUser, authPassword).
                        // then it sends a request without authentication headers to the serve
                        // to see what authentication the server asks for
                        // before sending the request with the authentication header
                        // this effectively sends two messages
                //auth().basic(authUser, authPassword).
                        // If I add the authorisation header myself then...
                        // Only one message is sent, but if the authentication scheme changes
                        // Then I have to handle it, and I have to encode the username/password myself
                //header(new Header("Authorization", "Basic dXNlcjpiaXRuYW1p")).
                        // Or if I use preemptive mode rather than 'challenge' mode
                        // It works the way I 'expected'
                auth().preemptive().basic(authUser, authPassword).
                content(msg).contentType(contentType).
                when().
                post("http://" + url + endpoint).
                        andReturn();

        // ignore CREATED UNAUTHORIZED CONFLICT
        if(ret.statusCode()!=201 && ret.statusCode()!=401 && ret.statusCode()!=409 ){
            System.out.println("POTENTIAL BUG - " +
                                    ret.statusCode() + " FOR " + endpoint + "\n" + msg );
        }

        return ret;
    }



    public void basicAuth(String user, String password) {
        this.authUser = user;
        this.authPassword = password;
    }

    public List<TracksContext_v2_2> getContexts() {

        List<TracksContext_v2_2> contexts = new ArrayList<TracksContext_v2_2>();

        String xml = given().
                        auth().preemptive().basic(authUser,authPassword).
                        contentType("text/xml").
                     get("http://" + url + "/contexts.xml").
                     andReturn().body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        NodeChildren contextNodes = xmlPath.getNodeChildren("contexts.context");

        contextNodes.size();

        for(Node contextNode : contextNodes.list()){
            System.out.println(contextNode.name());

            TracksContext_v2_2 context = new TracksContext_v2_2();

            for(Node contextElement : contextNode.children().list()){

                if(contextElement.name().contentEquals("id")){
                    context.setId(contextElement.value());
                }
                if(contextElement.name().contentEquals("name")){
                    context.setName(contextElement.value());
                }
            }
            contexts.add(context);
        }

        return contexts;
    }


}