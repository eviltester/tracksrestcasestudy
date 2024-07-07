package api.version_2_3_0.tracks.entity;

import java.util.HashMap;

public class TracksTodo {

    private final HashMap<String, String> kvp;

    public TracksTodo(){
        this.kvp = new HashMap<String,String>();
    }

    public void setKeyValuePair(String name, String value) {
        kvp.put(name, value);
    }
}