package api.version_2_3_0.tracks;

import api.version_2_3_0.tracks.entity.TracksContext;
import api.version_2_3_0.tracks.entity.TracksProject;
import api.version_2_3_0.tracks.entity.TracksTodo;
import api.version_2_3_0.tracks.http.HttpMessageSender;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.path.xml.element.NodeChildren;
import io.restassured.response.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TracksApi {
    private final URL url;
    private final HttpMessageSender httpMessageSender;


    // todo - allow pass in HttpMessageSender during construct

    /**
     *  Create API instance for a given user to access the URL
     *
     * @param tracksUrl - main URL for the tracks system
     * @param username
     * @param password
     */
    public TracksApi(URL tracksUrl, String username,
                                    String password){
        this.url = tracksUrl;

        httpMessageSender = new HttpMessageSender(tracksUrl);
        httpMessageSender.basicAuth(username, password);
    }

    public TracksApi(TestEnv testEnv) {

        this(   testEnv.getURL(),
                testEnv.getUserName(),
                testEnv.getUserPassword());

        if(testEnv.useProxy()){
            proxy(testEnv.getProxyURL(), testEnv.getProxyPort());
        }
    }

    public void proxy(String proxyURL, int proxyPort) {
        httpMessageSender.proxy(proxyURL, proxyPort);
    }

    public Response createContext(String aContext) {

        String tmplt = "<context><name>%s</name></context>";

        String msg = String.format(tmplt, aContext);

        Response response = httpMessageSender.postXmlMessageTo(msg,
                                            TracksApiEndPoints.contexts);

        return response;
    }


    /*
    Could refactor to a TracksResponse instead of returning Response

    public TracksResponse createContext(String aContext) {

        String tmplt = "<context><name>%s</name></context>";

        String msg = String.format(tmplt, aContext);

        Response response = httpMessageSender.postXmlMessageTo(msg,
                TracksApiEndPoints.contexts);

        return new TracksResponse(response);
    }
    */

    public Response createProject(String newProjectName) {

        String tmplt = "<project><name>%s</name></project>";

        String msg = String.format(tmplt, newProjectName);

        Response response= httpMessageSender.postXmlMessageTo(msg,
                                            TracksApiEndPoints.projects);

        return response;
    }


    // Warning - creating a user with the API does not work
    // The user is created but doesn't work from the API and attempting to use the
    // user from the GUI fails - this may be a bug or unfinished feature of the app
    // This is documented on the API for v 2.2 and in the code for v 2.3 but does not work
    /**
     * @deprecated  Api call to create user does not work. Use the AppAsAPI functionality TracksAppAsApi to create users
     */
    @Deprecated
    public Response createUserAPI(String username, String password) {
        String tmplt = "<user><login>%s</login><password>%s</password></user>";
        String msg = String.format(tmplt, username, password);

        Response response = httpMessageSender.postXmlMessageTo(msg,
                                                    TracksApiEndPoints.users);

        return response;
    }

    public Response createTodo(String todoName, String projectId, String contextId) {
        String msg = String.format("<todo>\n<description>%s</description>\n" +
                        "  <project_id>%s</project_id>\n" +
                        "  <context_id>%s</context_id>\n" +
                        "</todo>",
                todoName, projectId, contextId);

        Response response= httpMessageSender.postXmlMessageTo(msg,
                                                TracksApiEndPoints.todos);
        return response;
    }


    /** get all the context ids for the user using the API
     *
     * @return  List<TracksContext> - all the contexts for a user
     */
    public List<TracksContext> getContexts() {

        List<TracksContext> contexts = new ArrayList<TracksContext>();

        Response contextsListResponse=
                        httpMessageSender.getXMLResponseFrom(
                                            TracksApiEndPoints.contexts);

        String xml = contextsListResponse.body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        NodeChildren contextNodes =
                        xmlPath.getNodeChildren(
                                    "contexts.context");

        for(Node contextNode : contextNodes.list()){
            TracksContext context = new TracksContext();
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


    // rather than code a bunch of 'set' and fields
    //if(element.name().contentEquals("id")){
    //    project.setId(element.value());
    //}
    // - just add them to a hash
    // e.g. project.setKeyValuePair(element.name(), element.value());

    public List<TracksProject> getProjects() {

        List<TracksProject> projects = new ArrayList<>();

        Response projectsListResponse=
                        httpMessageSender.getXMLResponseFrom(
                                            TracksApiEndPoints.projects);

        String xml = projectsListResponse.body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        NodeChildren nodes = xmlPath.getNodeChildren("projects.project");

        for(Node contextNode : nodes.list()){

            TracksProject project = new TracksProject();

            for(Node element : contextNode.children().list()){

                project.setKeyValuePair(element.name(), element.value());
            }
            projects.add(project);
        }

        return projects;
    }

    public TracksProject getProject(String projectId) {

        Response response=
                    httpMessageSender.getXMLResponseFrom(
                                        TracksApiEndPoints.project(projectId));

        TracksProject project = new TracksProject();

        // if project does not exist then return an empty project
        if(response.getStatusCode()==404){
            return project;
        }

        String xml = response.body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        Node contextNode = xmlPath.getNode("project");

        for(Node element : contextNode.children().list()){
            project.setKeyValuePair(element.name(), element.value());
        }

        return project;
    }


    public List<TracksTodo> getProjectTasks(String projectId) {

        List<TracksTodo> todos = new ArrayList<>();

        Response response =
                    httpMessageSender.getXMLResponseFrom(
                                        TracksApiEndPoints.projectsTodos(projectId));

        String xml = response.body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        NodeChildren nodes = xmlPath.getNodeChildren("todos.todo");

        for(Node node : nodes.list()){

            TracksTodo todo = new TracksTodo();

            for(Node element : node.children().list()){

                todo.setKeyValuePair(element.name(), element.value());

            }
            todos.add(todo);
        }

        return todos;
    }

    public TracksTodo getTodo(String todoId) {

        Response response=
                    httpMessageSender.getXMLResponseFrom(
                                        TracksApiEndPoints.todo(todoId));

        TracksTodo todo = new TracksTodo();

        // if it does not exist then return an empty todo
        if(response.getStatusCode()==404){
            return todo;
        }

        String xml = response.body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        Node contextNode = xmlPath.getNode("todo");

        for(Node element : contextNode.children().list()){
            todo.setKeyValuePair(element.name(), element.value());
        }

        return todo;
    }

    public Response amendProject(String projectId,
                                 Map<String, String> fieldsToAmend) {

        StringBuilder messageBody = new StringBuilder();

        messageBody.append("<project>");

        for(String key: fieldsToAmend.keySet()){
            messageBody.append(String.format("<%s>", key));
            String value = fieldsToAmend.get(key);
            if(value!=null){
                messageBody.append(value);
            }
            messageBody.append(String.format("</%s>", key));
        }

        messageBody.append("</project>");

        return httpMessageSender.putXmlMessageTo(
                                    TracksApiEndPoints.project(projectId),
                                    messageBody.toString());
    }

    public Response deleteProject(String projectId) {
        return httpMessageSender.deleteMessage(
                        TracksApiEndPoints.project(projectId));
    }

    public Response getLastResponse() {
        return httpMessageSender.getLastResponse();
    }

}