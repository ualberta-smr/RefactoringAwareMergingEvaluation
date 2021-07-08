package ca.ualberta.cs.smr.evaluation.database;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("project")
public class Project extends Model {

    public Project() {
    }

    public Project(String url, String name) {
        set("url", url, "name", name);
    }

}
