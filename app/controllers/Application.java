package controllers;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.Contact;
import views.html.*;

import java.util.*;


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
        String filter = params.get("search[value]")[0];


        Integer pageSize = Integer.valueOf(params.get("length")[0]);
        Integer page = Integer.valueOf(params.get("start")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "name";
        String order = params.get("order[0][dir]")[0];

        switch (Integer.valueOf(params.get("order[0][column]")[0])) {
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
                        Expr.ilike("name", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("title", "%" + filter + "%"),
                                Expr.ilike("email", "%" + filter + "%")
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

        result.put("draw", Integer.valueOf(params.get("draw")[0]));
        result.put("recordsTotal", iTotalRecords);
        result.put("recordsFilter", iTotalDisplayRecords);

        ArrayNode an = result.putArray("data");

        for (Contact c : contactsPage.getList()) {
            ObjectNode row = Json.newObject();
            row.put("0", c.name);
            row.put("1", c.title);
            row.put("2", c.email);
            an.add(row);
        }

        return ok(result);
    }

}