package controllers;

import java.util.List;

import models.Answer;
import models.Question;
import models.Result;
import play.Play;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;

public class AskQuestion extends Controller {

	@Before
	static void addDefaults() {

		Welcome.addDefaults();
	}

	/**
	 * Save a user's answer in the result
	 * 
	 * @param resultId
	 * @param answerId
	 */
	public static void saveAnswer(Long answerId, Long resultId) {

		Result result = Result.findById(resultId);
		if (answerId != null) {
			result.answer = Answer.findById(answerId);
		} else {
			result.answer = null;
		}
		result.save();

		Result nextResult = getNextResult(resultId);
		if (nextResult != null && result.genQuestionnaireId == nextResult.genQuestionnaireId) {
			flash.put("orderId", Long.toString(nextResult.orderId));
			show(result.genQuestionnaireId);
		} else {
			DisplayResults.showResults(Crypto.encryptAES(Long.toString(result.genQuestionnaireId)));
		}
	}

	/**
	 * Displays a question and the possible answers
	 * 
	 * @param genQuestionnaireId
	 * @param resultId
	 */
	public static void show(Long genQuestionnaireId) {

		if (flash.contains("orderId")) {
			Result result = Result.find("byGenQuestionnaireIdAndOrderId", genQuestionnaireId, Long.parseLong(flash.get("orderId"))).first();
			if (result != null) {
				Question question = result.question;
				// Answers in a random order
				List<Answer> answers = Answer.find("question.id = ? order by rand()", question.id).fetch();
				render(question, answers, result);
			}
		}
		render();
	}

	/**
	 * Returns the next result
	 * 
	 * @param resultId
	 * @return Result
	 */
	public static Result getNextResult(Long resultId) {

		return Result.find("id > ? order by id asc", resultId).first();
	}

}