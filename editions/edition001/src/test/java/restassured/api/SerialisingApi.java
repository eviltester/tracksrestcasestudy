package restassured.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import restassured.ProjectFromXmlOrJson;

import java.util.ArrayList;
import java.util.List;


public class SerialisingApi {


    private final String xmlendpoint;

    public SerialisingApi(String xmlendpoint) {
        this.xmlendpoint = xmlendpoint;
    }

    public List<ProjectFromXmlOrJson> getProjects(){

        List<ProjectFromXmlOrJson> projects = new ArrayList<>();

        String xml = RestAssured.
                when().get(xmlendpoint).
                andReturn().body().asString();

        XmlPath xmlPath = new XmlPath(xml);
        int numberOfProjects = xmlPath.getInt("projects.project.size()");
        for(int projectIndex=0; projectIndex<numberOfProjects; projectIndex++){

            String query = String.format("projects.project[%d]",projectIndex);
            ProjectFromXmlOrJson project = xmlPath.getObject(query,ProjectFromXmlOrJson.class);

            projects.add(project);
        }

        return projects;
    }


    public Response createTodo(SerialisedTodo todo) {
        return RestAssured.
                given().
                contentType(ContentType.XML).
                body(todo).
                when().
                post(xmlendpoint);
    }
}