package controllers;

import java.util.Iterator;
import java.util.List;

import models.Result;
import play.exceptions.UnexpectedException;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;

public class DisplayResults extends Controller {

	@Before
	static void addDefaults() {

		Welcome.addDefaults();
	}

	/**
	 * Display the result page
	 * 
	 * @param genQuestionnaireId
	 */
	public static void showResults(String cryptGenQuestionnaireId) {

		// Decrypt the URL parameter
		try {
			Long genQuestionnaireId = Long.parseLong(Crypto.decryptAES(cryptGenQuestionnaireId));
			// Results of genQuestionnaireId
			List<Result> results = Result.find("byGenQuestionnaireId", genQuestionnaireId).fetch();
			// Calculate score
			int score = calculateScore(results);
			render(results, score);
		} catch (UnexpectedException e) {
			// Catch the URL parameter that can't be decrypted
			Welcome.index();
		}
	}

	/**
	 * Calculate the number of good answers in one generated questionnaire
	 * 
	 * @param results
	 * @return score
	 */
	public static int calculateScore(List<Result> results) {

		int score = 0;
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			Result result = (Result) iterator.next();
			if (result.answer != null && result.answer.id == result.question.getGoodAnswer().id) {
				score++;
			}
		}
		return score;
	}
}