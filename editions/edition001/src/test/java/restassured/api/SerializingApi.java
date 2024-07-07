package restassured.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import restassured.ProjectFromXmlOrJson;

import java.util.ArrayList;
import java.util.List;


public class SerializingApi {


    private final String xmlendpoint;

    public SerializingApi(String xmlendpoint) {
        this.xmlendpoint = xmlendpoint;
    }

    public List<ProjectFromXmlOrJson> getProjects(){

        List<ProjectFromXmlOrJson> projects = new ArrayList<>();

        String xml = RestAssured.
                when().get(xmlendpoint).
                andReturn().body().asString();

        XmlPath xmlPath = new XmlPath(xml);

        projects = xmlPath.getList("projects.project",
                        ProjectFromXmlOrJson.class);

        return projects;
    }

    /*
        I initially looped over the projects individually,
        and serialized with `getObject` but it seemed easier to `getList`

        int numberOfProjects =
                    xmlPath.getInt("projects.project.size()");
        for(int projectIndex=0;
                    projectIndex<numberOfProjects;
                            projectIndex++){

            String query = String.format(
                                    "projects.project[%d]",
                                    projectIndex);

            ProjectFromXmlOrJson project;

            project = xmlPath.getObject(query,
                                        ProjectFromXmlOrJson.class);
            projects.add(project);
        }

     */


    public Response createTodo(SerializedTodo todo) {
        return RestAssured.
                given().
                    contentType(ContentType.XML).
                    body(todo).
                when().
                    post(xmlendpoint);
    }
}