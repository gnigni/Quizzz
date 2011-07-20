package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Result extends Model {

	// Id of generated questionnaire
	public Long genQuestionnaireId;

	// Number of the question
	public Long orderId;

	@ManyToOne
	public Question question;

	@ManyToOne
	public Answer answer;

	public Result(Long genQuestionnaireId, Long orderId, Question question, Answer answer) {

		this.genQuestionnaireId = genQuestionnaireId;
		this.orderId = orderId;
		this.question = question;
		this.answer = answer;
	}

	public Result(Long genQuestionnaireId, Long orderId, Question question) {

		this.genQuestionnaireId = genQuestionnaireId;
		this.orderId = orderId;
		this.question = question;
	}
}
