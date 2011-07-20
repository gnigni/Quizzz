package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Email
	@Required
	public String email;
	
	@Required
	public String password;
	
	public User(String email, String password){
		this.email =email;
		this.password = password;
	}
	
	public static User connect(String email, String password){
		return find("byEmailAndPassword",email, password).first();
	}
	
	public String toString() {
	    return email;
	}

    
}
