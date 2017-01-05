package api.version_2_3_0.testdata;

public class Project{

    private String name;
    private String id;

    public Project(String aName, String anID){
        this.name = aName;
        this.id = anID;
    }

    public String id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

}