package api.version_2_2_0.wunderlist.appasapi;


import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.when;


public class Wunderlist {

    public List<Project> getPublicProjects(){

        String aUrl = "https://wunderlist.com/discover";
        Response projectsHtml = when().get(aUrl).
                                then().extract().response();

        String projectListHtml = projectsHtml.body().asString();

        String titlepattern = "<div class=\"editors-card\" title=\"([^\"]*)\">";
        String urlpattern = "<a href=\"/list/(\\d*)\">";
        Pattern p = Pattern.compile(titlepattern + "\\s*" + urlpattern);
        Matcher m = p.matcher(projectListHtml);

        List<Project> projects = new ArrayList<Project>();

        System.out.println("Projects");
        System.out.println("======");

        while( m.find()){

            String titlegroupy = m.group(1);
            String urlgroupy = m.group(2);
            System.out.println("- " + titlegroupy + "(" + urlgroupy +")");
            Project aProject = new Project(titlegroupy, urlgroupy);
            projects.add(aProject);
        }


        return projects;
    }

    public List<Todo> getPublicTodosForProject(String anID){

        Response todosHtml = when().get("https://wunderlist.com/list/" + anID).then().extract().response();

        String todosListHtml = todosHtml.body().asString();

        String titlepattern = "<li class=taskItem aria-label=\"([^\"]*)\" id=(\\d*)";

        Pattern p = Pattern.compile(titlepattern);

        Matcher m = p.matcher(todosListHtml);

        List<Todo> todos = new ArrayList<Todo>();


        System.out.println("ToDos");
        System.out.println("======");

        while( m.find()){

            String titlegroupy = m.group(1);
            String urlgroupy = m.group(2);
            System.out.println("- " + titlegroupy + "(" + urlgroupy +")");
            Todo aTodo = new Todo(titlegroupy, urlgroupy);
            todos.add(aTodo);
        }

        return todos;
    }


}