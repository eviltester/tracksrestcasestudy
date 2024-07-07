package restassured;


import api.version_2_3_0.testdata.RandomDataGenerator;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.NodeChildren;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import restassured.api.SerializedTodo;
import restassured.api.SerializingApi;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;

public class RestAssuredXMLExamplesTest {

    /*

    see JSON test for all useful links

    http://compendiumdev.co.uk/apps/mocktracks/projectsxml.php

    */

    String xmlendpoint =
            "http://compendiumdev.co.uk/apps/mocktracks/projectsxml.php";

    @Test
    public void simpleXmlRestAssuredExample(){
        RestAssured.when().get(xmlendpoint).
            then().assertThat().
                body("projects.project[0].name",
                     equalTo("A New Projectaniheeiadtatd"));
    }


    @Test
    public void aXmlRestAssuredExample() {


        // json parsing is more permissive
        // e.g. could use `.size` instead of `.size()`
        // and did not need to `toInteger()` the it.id in JSON
        RestAssured.
                when().
                get(xmlendpoint).
                then().assertThat().
                body("projects.project.size()",
                        equalTo(6)).and().
                body("projects.project[1].name",
                        equalTo("the new name aniheeiaosono")).and().
                body("projects.project[1].id",
                        equalTo("3")).and().
                body("projects.project.find {it.id == 3}.name",
                        equalTo("the new name aniheeiaosono")).and().
                body("projects.project.findAll {it.id.toInteger() <= 6}.size()",
                        equalTo(4));

    }

    @Test
    public void canGetXMLAttributes(){

        File xmlExample = new File(System.getProperty("user.dir"),"src/test/resources/jsonxml/xmlexample.xml");

        XmlPath xmlPath = new XmlPath(xmlExample);
        // xml get attributes
        Assert.assertEquals("integer",
                            xmlPath.get("projects.project[1].id.@type"));
    }


    @Test
    public void aSetOfXmlPathExamples(){

        File xmlExample = new File(System.getProperty("user.dir"),"src/test/resources/jsonxml/xmlexample.xml");

        XmlPath xmlPath = new XmlPath(xmlExample);

        // multiple matches returned in an ArrayList
        List ret = xmlPath.getList("projects.project");
        Assert.assertEquals(6, ret.size());

        // can index on multiple matches with array indexing notation [1]
        Assert.assertEquals("the new name aniheeiaosono",xmlPath.get("projects.project[1].name"));
        Assert.assertEquals(3,xmlPath.getInt("projects.project[1].id"));

        // filters and finding stuff
        // find the project with id == '3' and return the name
        Assert.assertEquals("the new name aniheeiaosono",xmlPath.get("projects.project.find {it.id == 3}.name"));

        // use `findAll` to find all the items that match a condition
        // conditions can use Groovy so convert the id to an integer in the filter if it was a string with  `{it.id.toInteger() <= 6}`

        // use `it` in the filter condition to refer to currently matched item
        ArrayList projectsWithIdLessThanSix = xmlPath.get("projects.project.findAll {it.id.toInteger() <= 6}");
        Assert.assertEquals(4,projectsWithIdLessThanSix.size());

        // get an 'object' as a NodeChildren collection from the list
        NodeChildren children = xmlPath.getNode("projects.project[1]").children();
        Assert.assertEquals("active", children.get("state").toString());


        // set root to an element in the Json to make accessing it easier
        XmlPath project2 = new XmlPath(xmlExample).setRoot("projects.project[2]");
        Assert.assertEquals(5 ,project2.getInt("id"));
        Assert.assertEquals("active" ,project2.get("state"));


        // no additional dependencies are required to deserialise XML with Rest Assured
        ProjectFromXmlOrJson projectFromXML = xmlPath.getObject("projects.project[1]", ProjectFromXmlOrJson.class);
        Assert.assertEquals(3, projectFromXML.id);

    }

    @Test
    public void anXMLPathExampleFromResponse(){


        Response response = RestAssured.
                when().
                    get(xmlendpoint).
                andReturn();

        XmlPath xmlPath = new XmlPath(response.body().asString());

        List ret = xmlPath.getList("projects.project");
        Assert.assertEquals(6, ret.size());

        Assert.assertEquals("the new name aniheeiaosono",xmlPath.get("projects.project[1].name"));
        Assert.assertEquals(3,xmlPath.getInt("projects.project[1].id"));

        Assert.assertEquals("the new name aniheeiaosono",xmlPath.get("projects.project.find {it.id == 3}.name"));

        ArrayList projectsWithIdLessThanSix = xmlPath.get("projects.project.findAll {it.id.toInteger() <= 6}");
        Assert.assertEquals(4,projectsWithIdLessThanSix.size());

    }


    @Test
    public void anXmlDeserializationExample(){
        // if I was to deserialize the whole message into an object
        // then I could just use `get("...").as(ProjectXml.class)`
        ProjectFromXmlOrJson projectFromXml = RestAssured.
                when().
                    get(xmlendpoint).
                xmlPath().
                    getObject("projects.project[1]",
                                ProjectFromXmlOrJson.class);

        Assert.assertEquals(3, projectFromXml.id);
    }

    @Test
    public void anXmlDeserializationExampleFromUrl() throws URISyntaxException {

        URI endpoint = new URI(xmlendpoint);

        ProjectFromXmlOrJson projectFromXml;
        projectFromXml = new XmlPath(endpoint).
                                getObject("projects.project[1]",
                                          ProjectFromXmlOrJson.class);

        Assert.assertEquals(3, projectFromXml.id);
    }

    @Test
    public void anXmlAPIDeserializationExample(){

        List<ProjectFromXmlOrJson> projects;
        
        projects = new SerializingApi(xmlendpoint).getProjects();

        Assert.assertEquals(6, projects.size());
        Assert.assertEquals(1, projects.get(0).id);
        Assert.assertEquals("A New Projectaniheeiadtatd",
                            projects.get(0).name);
    }


    @Test
    public void anXmlAPISerializationExample(){

        String reflectEndPoint =
                    "http://compendiumdev.co.uk/apps/mocktracks/reflect.php";

        String todoName = "todo " + new RandomDataGenerator().randomWord();

        SerializedTodo todo;
        todo =  new SerializedTodo(todoName, "12", "13");

        SerializingApi api = new SerializingApi(reflectEndPoint);
        Response response = api.createTodo(todo);

        System.out.println(response.body().asString());
    }
}