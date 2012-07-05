import play.*;
import play.libs.*;

import java.util.*;

import com.avaje.ebean.*;

import models.*;
import java.util.concurrent.*;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {

    /**
     * Here we load the initial data into the database
     */
    if(Ebean.find(Contact.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
      Ebean.save(all.get("contacts"));
    }
  }
}