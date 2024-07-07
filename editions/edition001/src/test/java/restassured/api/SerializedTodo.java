package restassured.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="todo")
public class SerializedTodo {
    public String description;
    public String project_id;
    public String context_id;

    public SerializedTodo(String todoName, String projectId, String contextId){
        this.description = todoName;
        this.project_id = projectId;
        this.context_id= contextId;
    }

    public SerializedTodo(){}
}
