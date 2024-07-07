package api.version_2_2_0.appasapi.wunderlist;

import api.version_2_2_0.wunderlist.appasapi.Project;
import api.version_2_2_0.wunderlist.appasapi.Todo;
import api.version_2_2_0.wunderlist.appasapi.Wunderlist;
import api.version_2_2_0.wunderlist.appasapi.WunderlistCache;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Ignore("Wunderlist has changed so these no longer work")
public class WunderListAppAsApiTest {

    @Test
    public void canRetrievePublicProjectList(){
        Wunderlist wunderlist = new Wunderlist();

        List<Project> projects = wunderlist.getPublicProjects();

        Assert.assertTrue(projects.size() > 0);
    }

    @Test
    public void canRetrieveTodoForProjectList(){

        Wunderlist wunderlist = new Wunderlist();
        List<Project> projects = wunderlist.getPublicProjects();

        List<Todo> todos = wunderlist.getPublicTodosForProject(projects.get(0).id());

        Assert.assertTrue(todos.size()>0);
    }

    @Test
    public void canCachePublicProjectList() throws IOException {
        File propertiesCacheFile = null;

        try {
            propertiesCacheFile = File.createTempFile("projectList","properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WunderlistCache cache = new WunderlistCache(new Wunderlist());

        cache.setProjectsCacheFile(propertiesCacheFile);
        cache.addUncachedProjects();

        List<Project> projects = cache.getProjects();
        Assert.assertTrue(projects.size() > 0);
    }

    @Test
    public void canCachePublicProjectTodoList() throws IOException {
        File propertiesCacheFile = null;

        try {
            propertiesCacheFile = File.createTempFile("projectTodoList","properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Wunderlist wunderlist = new Wunderlist();
        WunderlistCache cache = new WunderlistCache(wunderlist);

        cache.setTodosCacheFile(propertiesCacheFile);
        cache.addUncachedTodos(wunderlist.getPublicProjects().get(0).id());

        List<Todo> todos = cache.getTodos();
        int aSize = todos.size();
        Assert.assertTrue(todos.size() > 0);

        cache.addUncachedTodos(wunderlist.getPublicProjects().get(1).id());
        todos = cache.getTodos();
        Assert.assertTrue(todos.size() > aSize);
    }


}