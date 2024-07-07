package api.version_2_3_0.tracksapi;

import api.version_2_3_0.environmentconfig.TestEnvDefaults;
import api.version_2_3_0.synchronisation.Wait;
import api.version_2_3_0.testdata.RandomDataGenerator;
import api.version_2_3_0.tracks.TracksApi;
import api.version_2_3_0.tracks.entity.TracksProject;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectTest {

    /*
    * Project
        * Create Project
            * Add `action` to existing project
        * Amend Project
        * Delete Project
     */


    // TODO: Raise this as a bug against the documentation because the location does not include the .xml
    // so it points to the GUI version of the created item, not the API version
    // The documentation says that the location header should tell us where it is e.g.
    //>> HTTP/1.1 201 Created
    //Location: http://192.168.17.129/projects/65.xml
    //String locationHeader = response.getHeader("location");
    //Assert.assertTrue("location does not end with .xml" + locationHeader, locationHeader.endsWith(".xml"));


    @Test
    public void aUserCanCreateAProject(){

        TracksApi api = new TracksApi(TestEnvDefaults.getTestEnv());

        // get the current set of projects
        int totalProjectsForUser = api.getProjects().size();

        // create a new project
        String newProjectName = "A New Project" +
                                new RandomDataGenerator().randomWord();
        Response response = api.createProject(newProjectName);
        Assert.assertEquals(201, response.getStatusCode());

        // get projects again and check the new project is in the list
        List<TracksProject> theProjects = api.getProjects();
        int newTotalProjectsForUser = theProjects.size();
        Assert.assertTrue(newTotalProjectsForUser >
                            totalProjectsForUser);

        Boolean foundProject = false;
        for(TracksProject project : theProjects){
            if(project.getName().contentEquals(newProjectName)){
                foundProject = true;
            }
        }
        Assert.assertTrue("Could not find project named " +
                            newProjectName, foundProject);
    }

    @Test
    public void aUserCanAmendAProjectName(){

        TracksApi api = new TracksApi(TestEnvDefaults.getTestEnv());

        // create a new project
        String newProjectName =
                        "A New Project" +
                        new RandomDataGenerator().randomWord();

        api.createProject(newProjectName);
        Assert.assertEquals(201, api.getLastResponse().
                                                getStatusCode());

        String projectId = new TracksResponseProcessor(
                                    api.getLastResponse())
                                        .getIdFromLocation();
        TracksProject createdProject = api.getProject(projectId);
        Assert.assertEquals(newProjectName, createdProject.getName());

        Wait.aFewSeconds(2);
        // so that when we compare update times they are different

        // amend the project

        Map<String,String> fieldsToAmend =
                                    new HashMap<String,String>();
        fieldsToAmend.put("name", "the new name " +
                            new RandomDataGenerator().randomWord());

        api.amendProject(projectId, fieldsToAmend );

        TracksProject amendedProject = api.getProject(projectId);
        // check amended date has changed
        Assert.assertFalse(amendedProject.getUpdatedAt().
                            contentEquals(
                                    createdProject.getUpdatedAt()));
        // check created is the same
        Assert.assertTrue(amendedProject.getCreatedAt().
                            contentEquals(
                                    createdProject.getCreatedAt()));

        // check name changed
        Assert.assertEquals(fieldsToAmend.get("name"),
                                amendedProject.getName());

    }


    @Test
    public void aUserCanDeleteAProject(){

        TracksApi api = new TracksApi(TestEnvDefaults.getTestEnv());

        api.createProject("A New Project" +
                             new RandomDataGenerator().randomWord());
        Assert.assertEquals(201,
                            api.getLastResponse().getStatusCode());
        String projectId = new TracksResponseProcessor(
                                    api.getLastResponse())
                                        .getIdFromLocation();

        // check we can get it
        api.getProject(projectId);
        Assert.assertEquals(200,
                            api.getLastResponse().getStatusCode());

        // check we can delete it
        api.deleteProject(projectId);
        Assert.assertEquals(200,
                            api.getLastResponse().getStatusCode());

        // check it has been deleted
        api.getProject(projectId);
        Assert.assertEquals(404,
                            api.getLastResponse().getStatusCode());
    }
}