package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Questionnaire extends Model {
	
	@Required
	public String title;
	
	@OneToMany(mappedBy="questionnaire", cascade=CascadeType.ALL)
	public List<Question> questions;
	
	public Questionnaire (String title){
		this.questions = new ArrayList<Question>();
		this.title = title;
	}
	
	public String toString() {
		return title;
	}
    
}
