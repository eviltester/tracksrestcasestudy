package version_2_3_0.testdata;

import api.version_2_3_0.testdata.Project;
import api.version_2_3_0.testdata.RandomFoodBackedDataRetriever;
import api.version_2_3_0.testdata.TestDataRetriever;
import api.version_2_3_0.testdata.Todo;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestDataRetrieverTest {

    @Test
    public void weCanCreateProjectNameTestData(){

        TestDataRetriever testData = new RandomFoodBackedDataRetriever();

        List<Project> projects = testData.getProjects();

        Assert.assertTrue("Projects List should not be null", projects != null);
        Assert.assertTrue("Should be more than 0 projects", projects.size() > 0);

        for(Project project : projects){
            Assert.assertTrue("Project name should not be null", project.name() != null);
            Assert.assertTrue("Trimmed project name should have chars in it", project.name().trim().length() >0);
            Assert.assertTrue("Project name should not contain null", project.name().contains("null") == false);
        }

    }

    @Test
    public void weCanCreateProjectTodoTestData(){

        TestDataRetriever testData = new RandomFoodBackedDataRetriever();

        List<Todo> todos = testData.getTodosForProject("project");

        Assert.assertTrue("TODO List should not be null", todos != null);
        Assert.assertTrue("Should be more than 0 todos", todos.size() > 0);

        for(Todo todo : todos){
            Assert.assertTrue("Todo name should not be null", todo.name() != null);
            Assert.assertTrue("Trimmed todo name should have chars in it", todo.name().trim().length() >0);
            Assert.assertTrue("Todo name should not contain null", todo.name().contains("null") == false);
        }
    }
}