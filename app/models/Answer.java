package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Answer extends Model {

	@Required
	public String label;

	public boolean isCorrect;

	@ManyToOne
	@Required
	public Question question;
	
	public Answer(String label, boolean isCorrect, Question question) {

		this.label = label;
		this.isCorrect = isCorrect;
		this.question = question;
	}
	
	public String toString(){
		return label;
	}

}
