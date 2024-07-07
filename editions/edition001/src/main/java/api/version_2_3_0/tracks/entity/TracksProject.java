package api.version_2_3_0.tracks.entity;


import java.util.HashMap;

public class TracksProject {
    private final HashMap<String, String> kvp;

    public TracksProject(){
        this.kvp = new HashMap<String,String>();
    }

    public void setKeyValuePair(String name, String value) {
        kvp.put(name, value);
    }

    public String getName() {
        return kvp.get("name");
    }

    public String getUpdatedAt() {
        return kvp.get("updated-at");
    }

    public String getCreatedAt() {
        return kvp.get("created-at");
    }
}