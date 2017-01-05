package api.version_2_3_0.tracks.entity;

public class TracksContext {
    private String id;
    private String name;

    public void setId(String value) {
        this.id = value;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String id() {
        return this.id;
    }
}
