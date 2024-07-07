package api.version_2_2_0.trackstestdata;

import api.version_2_2_0.tracks.TracksApi_v2_2;
import api.version_2_2_0.tracks.entity.TracksContext_v2_2;
import api.version_2_2_0.wunderlist.appasapi.Project;
import api.version_2_2_0.wunderlist.appasapi.Todo;
import api.version_2_2_0.wunderlist.appasapi.Wunderlist;
import api.version_2_2_0.wunderlist.appasapi.WunderlistCache;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SetupTracksTestDataTest {

    // You probably want to change this to your user and your setup
    // Mac alan
    //public static final String MyDataFolder = "/Users/alan/Downloads/";
    // Windows alan
    public static final String MyDataFolder = "d:/temp/";


    public static final String WUNDERLIST_PROJECTS = MyDataFolder + "wunderlistProjects.properties";
    public static final String WUNDERLIST_TODOS = MyDataFolder + "wunderlistTodos.properties";
    public static final int MAX_NUMBER_OF_TODOS=20;
    public int numberOfProjectsLimit = 10; // -ve to use the number of projects from wunderlist

    // Number of users not enabled
    public int numberOfUsersToUse = 30; // for 0 users, the default username password will be used for all
                                       // for more than one user, fir the users will be created
                                       // then for each project a random user will be used for each project


    public static final String url = "192.168.58.128"; // API VM
    public static final String username = "user";
    public static final String password = "bitnami"; // Bitnami Default

    // cloud VM for data
    //public static final String url = "tracks-with-data.bitnamiapp.com/tracks";
    //public static final String password = "bitnami01tracks";

    //public static final String username = "mrlogout";
    //public static final String password = "logmeout";

    //public static final String username = "mrlong";
    //public static final String password = "longpasswordforme";


    public static final String newUserPattern = "user%d";
    public static final String newPasswordPattern = "bitnami%d";

    // Run this test to update the data in the properties files from Wunderlist

    @Ignore // Do not run these tests automatically, they are not 'tests', they help me work
    @Test
    public void updateWunderlistCache() throws IOException {

        File projectsListCache = new File(WUNDERLIST_PROJECTS);
        WunderlistCache cache = new WunderlistCache(new Wunderlist());
        cache.setProjectsCacheFile(projectsListCache);
        cache.addUncachedProjects();

        File todoListCache = new File(WUNDERLIST_TODOS);
        cache.setTodosCacheFile(todoListCache);

        List<Project> projects = cache.getProjects();
        for(Project aProject : projects){
            cache.addUncachedTodos(aProject.id());
        }
    }

    private String randomUser(Set<String> userNames){
        String[] names  = userNames.toArray(new String[userNames.size()]);
        return names[new Random().nextInt(names.length)];
    }

    @Ignore // Do not run these tests automatically, they are not 'tests', they help me work
    @Test
    public void createTracksDataFromWunderlistCache(){

        // If you are not using a proxy then comment this out.
        // If you are then make sure details are correct
        RestAssured.proxy("localhost", 8080);

        // Setup Wunderlist cache for test data
        WunderlistCache cache = new WunderlistCache(new Wunderlist());
        cache.setProjectsCacheFile( new File(WUNDERLIST_PROJECTS));
        cache.setTodosCacheFile(new File(WUNDERLIST_TODOS));

        TracksApi_v2_2 tracks = new TracksApi_v2_2(url, username, password);

        // create the contexts
        String[] contexts = {"work", "home", "shopping", "schoolrun"};

        // create the users name password map
        Map<String,String> usersNamePassword = new HashMap<String, String>();
        usersNamePassword.put(username, password);

        for(int userNumber=1; userNumber<numberOfUsersToUse; userNumber++){
            String newUsername = String.format(newUserPattern, userNumber);
            String newPassword = String.format(newPasswordPattern, userNumber);
            usersNamePassword.put(newUsername, newPassword);
        }


        // create the users - ignores duplicates
        for(String aUserName : usersNamePassword.keySet()){

            // create users with the default user
            tracks.basicAuth(username,password);

            // create a user
            String aPassword = usersNamePassword.get(aUserName);
            tracks.createUser(aUserName, aPassword);

            // create the contexts for the user
            for(String aContext : contexts){

                // each user needs ot create their own context
                tracks.basicAuth(aUserName,aPassword);

                Response response = tracks.createContext(aContext);
                if(response.getStatusCode()==201 || response.getStatusCode()==409){
                    // 201 - Created
                    // 409 - Already exists
                }else{
                    System.out.println("Warning: Creating Context " + aContext + " Status Code " + response.getStatusCode());
                }
            }
        }



        // read projects from cache
        List<Project> projects = cache.getProjects();

        // read todos from cache
        List<Todo> todos = cache.getTodos();

        // LOOP: create a project
        for( Project aProject : projects) {

            // have not enabled multiple users - run for each user you want to create data for
            String randomUserName = randomUser(usersNamePassword.keySet());
            tracks.basicAuth(randomUserName, usersNamePassword.get(randomUserName));


            // get all the context ids for the user
            List<TracksContext_v2_2> contextList = tracks.getContexts();

            String newProjectName = aProject.name() + " " + randomWord();

            System.out.println(newProjectName);
            Response response = tracks.createProject(newProjectName);

            // Only add todos if we created one
            if(response.statusCode()==201) {
                // get the project id you just added
                String location = response.header("Location");

                //Location: [\S]+?/projects/(\d+)
                String findLocation = "[\\S]+?/projects/(\\d+)";

                Pattern p = Pattern.compile(findLocation);

                Matcher m = p.matcher(location);

                String projectId = "";
                if (m.find()) {
                    projectId = m.group(1);
                }

                // LOOP: generate a random number of todos
                int numberOfTodos = new Random().nextInt(MAX_NUMBER_OF_TODOS);

                while (numberOfTodos > 0) {
                    // randomly choose a context
                    TracksContext_v2_2 rndcontext = contextList.get(new Random().nextInt(contextList.size()));

                    // randomly choose a todo name
                    Todo rndtodo = todos.get(new Random().nextInt(todos.size()));

                    // add the todo to the project and a context
                    tracks.createTodo(rndtodo.name() + " " + randomWord(), projectId, rndcontext.id());

                    numberOfTodos--;
                }
            }

        // next project
            numberOfProjectsLimit--;
            if(numberOfProjectsLimit==0){
                break;
            }
        }

    }

    // Quick hack random word
    private String randomWord() {
        return String.valueOf(System.currentTimeMillis()).
                                replaceAll("1", "a").
                                replaceAll("2", "d").
                                replaceAll("3", "o").
                                replaceAll("4", "n").
                                replaceAll("5", "e").
                                replaceAll("6", "r").
                                replaceAll("7", "h").
                                replaceAll("8", "i").
                                replaceAll("9", "t").
                                replaceAll("0", "s");
    }
}