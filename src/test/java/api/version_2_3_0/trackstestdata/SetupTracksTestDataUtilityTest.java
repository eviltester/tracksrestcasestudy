package api.version_2_3_0.trackstestdata;

import api.version_2_3_0.tracks.TestEnv;
import api.version_2_3_0.environmentconfig.TestEnvDefaults;
import api.version_2_3_0.testdata.*;
import api.version_2_3_0.tracks.TracksApi;
import api.version_2_3_0.tracks.TracksAppAsApi;
import api.version_2_3_0.tracks.entity.TracksContext;
import api.version_2_3_0.tracksapi.TracksResponseProcessor;
import io.restassured.response.Response;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.*;



public class SetupTracksTestDataUtilityTest {


    private final int MAX_NUMBER_OF_TODOS=20;

    // Max number of projects to create
    // -ve to use the number of projects
    // from the testdata generator
    private int numberOfProjectsLimit = 10;

    // Number of users not enabled
    // for 0 users, the default username password will be used for all
    // for more than one user, the users will be created
    // then for each project a random user will be used for each project
    private int numberOfUsersToUse = 30;

    // Username and Password patterns
    private final String newUserPattern = "nuuser%d";
    private final String newPasswordPattern = "bitnami%d";

    // Contexts to create
    private String[] theContexts = {"work", "home",
                                    "shopping", "schoolrun"};


    @Ignore("Do not run this tests automatically, " +
            "this is a utility to create a user in the system")
    @Test
    public void useTheAppAsAPIToCreateAUser(){
        TestEnv env = TestEnvDefaults.getTestEnv();
        TracksAppAsApi adminTracks = new TracksAppAsApi(env);

        adminTracks.login();
        adminTracks.createUser("bob99", "password66");
    }

    @Ignore("Do not run these tests automatically," +
            "they are not 'tests', they help me work")
    @Test
    public void createTracksDataFromTestDataRetriever()
                                        throws MalformedURLException {

        TestEnv env = TestEnvDefaults.getTestEnv();
        TestDataRetriever testData = new RandomFoodBackedDataRetriever();
        TracksAppAsApi adminTracksUserAppSession = new TracksAppAsApi(env);

        // in version 2.3.0 of tracks the basic auth does
        // not work for creating a user through the GUI
        // needed a more complicated app as API process
        adminTracksUserAppSession.login();

        // create the users name password map
        Map<String,String> usersNamePassword = new HashMap<String, String>();
        // add the admin user
        usersNamePassword.put(env.getUserName(), env.getUserPassword());

        generateUserNameAndPasswordList(usersNamePassword,
                                        newUserPattern,
                                        newPasswordPattern);

        createUsersViaAppAndApi(env, adminTracksUserAppSession,
                                theContexts, usersNamePassword);

        createProjectsForUsers(env, testData, usersNamePassword);
    }


    private void generateUserNameAndPasswordList(
                                Map<String, String> usersNamePassword,
                                String userNamePattern, String passwordPattern) {

        for(int userNumber=1; userNumber<numberOfUsersToUse; userNumber++){
            String newUsername = String.format(userNamePattern, userNumber);
            String newPassword = String.format(passwordPattern, userNumber);
            usersNamePassword.put(newUsername, newPassword);
        }
    }


    private void createUsersViaAppAndApi(TestEnv env,
                                         TracksAppAsApi adminTracksUserAppSession,
                                         String[] contexts,
                                         Map<String, String> usersNamePassword) {

        // create the users - ignores duplicates
        for(String aUserName : usersNamePassword.keySet()){

            // create a user
            String aPassword = usersNamePassword.get(aUserName);
            adminTracksUserAppSession.createUser(aUserName, aPassword);
            createContextsForUser(env, contexts, aUserName, aPassword);
        }
    }

    private void createContextsForUser(TestEnv env, String[] contexts,
                                       String aUserName, String aPassword) {
        // create the contexts for the user
        for(String aContext : contexts){

            // each user needs to create their own context
            TracksApi normalTracksUser = new TracksApi( env.getURL(),
                                                        aUserName, aPassword);

            Response response = normalTracksUser.createContext(aContext);

            if(response.getStatusCode()==201 || response.getStatusCode()==409){
                // 201 - Created
                // 409 - Already exists
            }else{
                System.out.println( "Warning: Creating Context " + aContext +
                                    " Status Code " + response.getStatusCode());
            }
        }
    }


    private void createProjectsForUsers(TestEnv env, TestDataRetriever testData,
                                        Map<String, String> usersNamePassword) {
        RandomDataGenerator wordGenerator = new RandomDataGenerator();

        List<Project> projects = testData.getProjects();

        // LOOP: create a project
        for( Project aProject : projects) {

            // Chose a random user
            String randomUserName = selectRandomUserName(usersNamePassword.keySet());
            String theRandomUsersPassword = usersNamePassword.get(randomUserName);

            TracksApi normalTracksUser = new TracksApi( env.getURL(),
                                                        randomUserName,
                                                        theRandomUsersPassword);

            List<TracksContext> contextList = normalTracksUser.getContexts();

            String projectId = createProjectViaAPIForUser(
                                            wordGenerator, aProject,
                                            normalTracksUser);

            if(projectId!=null){
                List<Todo> todos = testData.getTodosForProject(projectId);
                createRandomNumberOfTodosInProjectForUser(
                                                wordGenerator,
                                                normalTracksUser,
                                                contextList, projectId,
                                                todos);
            }

            numberOfProjectsLimit--;
            if(numberOfProjectsLimit==0){
                break;
            }
        }
    }


    /*   Original code in  createProjectsForUsers
String[] names  = usersNamePassword.keySet().toArray(
                                new String[usersNamePassword.
                                           keySet().size()]);
String randomUserName =  names[new Random().nextInt(names.length)];
 */

    private String selectRandomUserName(Set<String> userNames){
        String[] names  = userNames.toArray(new String[userNames.size()]);
        return names[new Random().nextInt(names.length)];
    }


    private String createProjectViaAPIForUser(
                                        RandomDataGenerator wordGenerator,
                                        Project aProject,
                                        TracksApi normalTracksUser) {

        String newProjectName = aProject.name() + " " +
                                wordGenerator.randomWord();
        System.out.println(newProjectName);

        Response response = normalTracksUser.createProject(newProjectName);

        String projectId = null;

        if(response.statusCode()==201) {
            projectId = new TracksResponseProcessor(response).
                                            getIdFromLocation();
        }
        return projectId;
    }

    private void createRandomNumberOfTodosInProjectForUser(
                                            RandomDataGenerator wordGenerator,
                                            TracksApi normalTracksUser,
                                            List<TracksContext> contextList,
                                            String projectId,
                                            List<Todo> todos) {
        // LOOP: generate a random number of todos
        int numberOfTodos = new Random().nextInt(MAX_NUMBER_OF_TODOS);

        while (numberOfTodos > 0) {
            // randomly choose a context
            TracksContext rndcontext = contextList.get(
                                            new Random().
                                                nextInt(contextList.size()));

            // randomly choose a todo name
            Todo rndtodo = todos.get(new Random().nextInt(todos.size()));

            // add the todo to the project and a context
            normalTracksUser.createTodo(rndtodo.name() + " " +
                                        wordGenerator.randomWord(),
                                        projectId, rndcontext.id());

            if(normalTracksUser.getLastResponse().getStatusCode()!=201){
                System.out.println("Warning: Possible error creating Todo " +
                                    normalTracksUser.getLastResponse().
                                                    getStatusCode());
            }

            numberOfTodos--;
        }
    }




}