package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Question extends Model {
	
	@Required
	public String label;
	
	@ManyToOne
	@Required
	public Questionnaire questionnaire;
	
	@OneToMany(mappedBy="question", cascade=CascadeType.ALL)
	public List<Answer> answers;
	
	public Question(String label, Questionnaire questionnaire){
		this.answers = new ArrayList<Answer>();
		this.label = label;
		this.questionnaire = questionnaire;
	}
	
	/**
	 * Returns the good answer for a question
	 * @return 
	 */
	public Answer getGoodAnswer(){
		return Answer.find("byIsCorrectAndQuestion", true, this).first();
	}
	
	public String toString(){
		return label;
	}
    
}
