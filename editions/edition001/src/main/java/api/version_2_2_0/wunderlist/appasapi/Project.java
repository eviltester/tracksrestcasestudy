package api.version_2_2_0.wunderlist.appasapi;

public class Project {

    private String name;
    private String id;

    Project(String aName, String anID){
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