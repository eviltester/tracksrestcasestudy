package api.version_2_3_0.testdata;

import java.util.List;

public interface TestDataRetriever {
    List<Project> getProjects();

    List<Todo> getTodosForProject(String anID);
}
