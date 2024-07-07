package restassured;

import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

// class for deserializing XML into object with XmlPath
// https://github.com/rest-assured/rest-assured/wiki/Usage#deserialization
// note the use of JAXB @XmlElement to rename the fields from `-` in XML to `_` in Java
// made public static otherwise JAXB couldn't handle it - could
public class ProjectFromXmlOrJson {
    public int id;
    public String name;
    public int position;
    public String description;
    public String state;
    @XmlElement(name="created-at")
    public Date created_at;
    @XmlElement(name="updated-at")
    public Date updated_at;
    @XmlElement(name="default-context")
    public int default_context;
    @XmlElement(name="completed-at")
    public Date completed_at;
    @XmlElement(name="default-tags")
    public String default_tags;
    @XmlElement(name="last-reviewed")
    public Date last_reviewed;
}
