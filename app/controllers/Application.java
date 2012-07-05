package controllers;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.*;
import views.html.*;

import java.util.*;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;


public class Application extends Controller {

  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }

  public static Result list() {
    /**
     * Get needed params
     */
    Map<String, String[]> params = request().queryString();

    Integer iTotalRecords = Contact.find.findRowCount();
    String filter = params.get("sSearch")[0];
    Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
    Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

    /**
     * Get sorting order and column
     */
    String sortBy = "name";
    String order = params.get("sSortDir_0")[0];

    switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
      case 0 :  sortBy = "name"; break;
      case 1 :  sortBy = "title"; break;
      case 2 :  sortBy = "email"; break;
    }

    /**
     * Get page to show from database
     * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
     */
    Page<Contact> contactsPage = Contact.find.where(
      Expr.or(
        Expr.ilike("name", "%"+filter+"%"),
          Expr.or(
            Expr.ilike("title", "%"+filter+"%"),
            Expr.ilike("email", "%"+filter+"%")
          )
        )
      )
      .orderBy(sortBy + " " + order + ", id " + order)
      .findPagingList(pageSize).setFetchAhead(false)
      .getPage(page);

    Integer iTotalDisplayRecords = contactsPage.getTotalRowCount();

    /**
     * Construct the JSON to return
     */
    ObjectNode result = Json.newObject();

    result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
    result.put("iTotalRecords", iTotalRecords);
    result.put("iTotalDisplayRecords", iTotalDisplayRecords);

    ArrayNode an = result.putArray("aaData");

    for(Contact c : contactsPage.getList()) {
      ObjectNode row = Json.newObject();
      row.put("0", c.name);
      row.put("1", c.title);
      row.put("2", c.email);
      an.add(row);
    }

    return ok(result);
  }

}