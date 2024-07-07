package api.version_2_3_0.tracksapi;

import api.version_2_3_0.environmentconfig.TestEnvDefaults;
import api.version_2_3_0.testdata.RandomDataGenerator;
import api.version_2_3_0.tracks.TracksApi;
import api.version_2_3_0.tracks.entity.TracksTodo;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TasksActionsTest {

   /*
    * Action
    * Create `action` uses same data validation as GUI
    * `description` is 100 length max
    * must have a context
    * List `Action`s in a `Context`
            * 'Star' an 'Action'
            * Retrieve list of 'starred' `Action`s
    * Edit an `action`
            * Show `actions` for a project
   */


    /**
     * If this test had been written differently it might not have checked for the known bug
     */
    // the exploratory testing has revealed that trying to  http://{{url}}/projects/189/todos.xml
    // returns all todos, not those limited by the project
    // TODO: this is either a bug or should be clarified in the documentation
/*
        List<TracksTodo> tasks = api.getProjectTasks(createdProjectId);
        Assert.assertEquals(0, tasks.size());
*/
    // if the above assertion is placed after the `getProjectTasks`
    // immediately after creating a project then teh test fails

    @Test
    public void weCanAddTasksToAProject(){

        TracksApi api = new TracksApi(TestEnvDefaults.getTestEnv());

        // create a new project
        api.createProject("A New Project" +
                            new RandomDataGenerator().randomWord());
        Assert.assertEquals(201, getLastStatusCode(api));

        // get the id of the created project
        String createdProjectId = getIdFromApiResponse(api);

        // create a new Context
        api.createContext("context" +
                            new RandomDataGenerator().randomWord());
        Assert.assertEquals(201, getLastStatusCode(api));

        // get the id of the created context
        String createdContextId = getIdFromApiResponse(api);

        // check task numbers for project
        List<TracksTodo> tasks = api.getProjectTasks(createdProjectId);
        int currentTodos = tasks.size();

        api.createTodo("todo " + new RandomDataGenerator().randomWord(),
                        createdProjectId, createdContextId);
        Assert.assertEquals(201, getLastStatusCode(api));

        // check task numbers increased
        List<TracksTodo> newTasks = api.getProjectTasks(createdProjectId);
        Assert.assertTrue(newTasks.size() > currentTodos);
        Assert.assertTrue(newTasks.size() == currentTodos+1);
    }

    @Test
    public void whenWeDeleteAProjectTheTasksAreDeletedAsWell(){

        TracksApi api = new TracksApi(TestEnvDefaults.getTestEnv());

        api.createProject("A New Project" +
                            new RandomDataGenerator().randomWord());
        Assert.assertEquals(201, getLastStatusCode(api));
        String createdProjectId = getIdFromApiResponse(api);

        api.createContext("context" +
                            new RandomDataGenerator().randomWord());
        Assert.assertEquals(201, getLastStatusCode(api));
        String createdContextId = getIdFromApiResponse(api);

        api.createTodo("todo " + new RandomDataGenerator().randomWord(),
                        createdProjectId, createdContextId);
        Assert.assertEquals(201, getLastStatusCode(api));
        String createdTodoId = getIdFromApiResponse(api);

        api.getTodo(createdTodoId);
        Assert.assertEquals(200, getLastStatusCode(api));

        api.deleteProject(createdProjectId);

        api.getProject(createdProjectId);
        Assert.assertEquals(404, getLastStatusCode(api));

        // check the todo was deleted as well
        api.getTodo(createdTodoId);
        Assert.assertEquals(404, getLastStatusCode(api));
    }


    private int getLastStatusCode(TracksApi api) {
        return api.getLastResponse().getStatusCode();
    }

    private String getIdFromApiResponse(TracksApi api) {
        return new TracksResponseProcessor(
                api.getLastResponse()).getIdFromLocation();
    }

}