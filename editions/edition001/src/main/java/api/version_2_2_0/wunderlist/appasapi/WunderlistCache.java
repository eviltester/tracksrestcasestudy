package api.version_2_2_0.wunderlist.appasapi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Alan on 14/01/2015.
 */
public class WunderlistCache {
    private final Wunderlist wunderlist;
    private File projectsCacheFile;
    Properties projects;
    private File todosCacheFile;
    private Properties todos;

    public WunderlistCache(Wunderlist wunderlist) {
        this.wunderlist = wunderlist;
    }

    public void setProjectsCacheFile(File propertiesCacheFile) {
        this.projectsCacheFile = propertiesCacheFile;

        projects = new Properties();
        try {
            projects.load(new FileInputStream(propertiesCacheFile));
        } catch (IOException e) {
            System.out.println("Could not load Projects Cache File " + propertiesCacheFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void setTodosCacheFile(File propertiesCacheFile) {
        this.todosCacheFile = propertiesCacheFile;

        todos = new Properties();
        try {
            todos.load(new FileInputStream(propertiesCacheFile));
        } catch (IOException e) {
            System.out.println("Could not load Todos Cache File " + propertiesCacheFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public void addUncachedProjects() throws IOException {

        if(projectsCacheFile==null)
            throw new FileNotFoundException("Have not set the Projects Cache File Yet");

        List<Project> projectsList = wunderlist.getPublicProjects();

        for(Project aProject : projectsList){
            if(projects.containsKey(aProject.id())){
                // ignore it, already cached
            }else{
                projects.put(aProject.id(), aProject.name());
            }
        }

        System.out.println("Writing Project Cache to " + projectsCacheFile.getAbsolutePath());
        projects.store(new FileOutputStream(projectsCacheFile, false),null);
        System.out.println("Cache Written");
    }


    public void addUncachedTodos(String projectId) throws IOException {
        if(todosCacheFile==null)
            throw new FileNotFoundException("Have not set the Todos Cache File Yet");

        List<Todo> todoList = wunderlist.getPublicTodosForProject(projectId);

        for(Todo aTodo : todoList){
            if(todos.containsKey(aTodo.id())){
                // ignore it, already cached
            }else{
                todos.put(aTodo.id(), aTodo.name());
            }
        }

        System.out.println("Writing Todo Cache to " + todosCacheFile.getAbsolutePath());
        todos.store(new FileOutputStream(todosCacheFile, false),null);
        System.out.println("Cache Written");
    }

    public List<Project> getProjects() {

        if(projectsCacheFile==null)
            throw new RuntimeException("Have not set the Projects Cache File Yet");

        List<Project> projectsList = new ArrayList<Project>();

        for(Object projectId : projects.keySet()){
            String theID = (String) projectId;
            String theName = (String)projects.get(projectId);
            Project aProject = new Project(theName, theID);
            projectsList.add(aProject);
        }

        return projectsList;

    }


    public List<Todo> getTodos() {

        if(todosCacheFile==null)
            throw new RuntimeException("Have not set the Todos Cache File Yet");

        List<Todo> todoList = new ArrayList<Todo>();

        for(Object id : todos.keySet()){
            String theID = (String) id;
            String theName = (String)todos.get(id);
            Todo aTodo = new Todo(theName, theID);
            todoList.add(aTodo);
        }

        return todoList;
    }
}