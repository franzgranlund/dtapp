package models;

import java.util.*;
import javax.persistence.*;

import play.api.libs.Crypto;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import play.Logger;

import com.avaje.ebean.*;

@Entity
public class Contact extends Model {

	@Id
	public Long id;

	@Constraints.Required
	public String name;

	public String title;
	public String email;

	public static Model.Finder<Long,Contact> find = new Model.Finder(Long.class, Contact.class);

	public static List<Contact> findAll() {
		return find.all();
	}

	public String toString() {
		return name;
	}
}