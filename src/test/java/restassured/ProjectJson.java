package restassured;

import java.util.Date;

/**
 * class for deserialising JSON into object with JSONPath
   https://github.com/rest-assured/rest-assured/wiki/Usage#deserialization
 */

public class ProjectJson {
    public int id;
    public String name;
    public int position;
    public String description;
    public String state;
    public Date created_at;
    public Date updated_at;
    public int default_context;
    public Date completed_at;
    public String default_tags;
    public Date last_reviewed;
}
