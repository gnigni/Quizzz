package controllers;

import java.util.Iterator;
import java.util.List;

import models.Answer;
import models.Question;
import models.Questionnaire;
import models.Result;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class Welcome extends Controller {

	@Before
	static void addDefaults() {

		renderArgs.put("quizTitle", Play.configuration.getProperty("quiz.title"));
		renderArgs.put("quizHeader", Play.configuration.getProperty("quiz.header"));
		renderArgs.put("quizBaseline", Play.configuration.getProperty("quiz.baseline"));
	}

	/**
	 * List of questionnaires
	 */
	public static void index() {

		List<Questionnaire> questionnaires = Questionnaire.find("order by id").fetch();
		render(questionnaires);
	}

	/**
	 * Generate in the database the random questionnaire from the id of the
	 * selected questionnaire
	 * 
	 * @param questionnaireId
	 */
	public static void generate(Long questionnaireId) {

		int i = 1;
		Long genQuestionnaireId = generateQuestionnaireId();
		// Number of questions for the questionnaire
		long quizSize = Long.parseLong(Play.configuration.getProperty("quiz.size"));

		// List of questions of the questionnaire in a random order
		List<Question> questions = Question.find("questionnaire.id = ? order by rand()", questionnaireId).fetch();

		for (Iterator iterator = questions.iterator(); iterator.hasNext();) {

			if (i <= quizSize) {
				Question question = (Question) iterator.next();
				if (isValidQuestion(question)) {
					new Result(genQuestionnaireId, new Long(i), question).save();
					i++;
				}
			} else {
				break;
			}
		}
		flash.put("orderId", "1");
		AskQuestion.show(genQuestionnaireId);
	}

	/**
	 * Find the new id for the generated questionnaire
	 * 
	 * @return Long
	 */
	protected static Long generateQuestionnaireId() {

		Result result = Result.find("order by genQuestionnaireId desc").first();
		if (result == null) {
			return new Long(1);
		}
		return result.genQuestionnaireId + 1;

	}

	/**
	 * Check that a question has one good answer and 3 bad answers
	 * 
	 * @return boolean
	 */
	protected static boolean isValidQuestion(Question question) {

		long countAnswers = Answer.count("question.id = ?", question.id);
		long answersSize = Long.parseLong(Play.configuration.getProperty("quiz.answers.size"));
		return (hasUniqueGoodAnswer(question) && countAnswers == answersSize);
	}

	/**
	 * Check if the question has only one good answer
	 * 
	 * @param question
	 * @return boolean
	 */
	protected static boolean hasUniqueGoodAnswer(Question question) {

		long countGoodAnswers = Answer.count("question.id = ? and isCorrect is true", question.id);
		return (countGoodAnswers == 1);
	}

}