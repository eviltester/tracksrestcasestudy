package api.version_2_3_0.tracks;

public class TracksApiEndPoints {

    public static final String todos= "/todos.xml";
    public static final String todoId ="/todos/%s.xml";
    public static final String users="/users.xml";
    public static final String projects="/projects.xml";
    public static final String projectId="/projects/%s.xml";
    public static final String contexts="/contexts.xml";
    public static final String projectIdTodos="/projects/%s/todos.xml";

    public static String project(String id) {
        return String.format(projectId, id);
    }

    public static String projectsTodos(String id) {
        return String.format(projectIdTodos, id);
    }

    public static String todo(String id) {
        return String.format(todoId, id);
    }
}
