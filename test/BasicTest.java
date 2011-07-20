import java.util.List;

import models.Answer;
import models.Question;
import models.Questionnaire;
import models.Result;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

	/**
	 * Delete data before each test
	 */
	@Before
	public void setup() {

		Fixtures.deleteAllModels();
	}

	@Test
	public void createAndRetrieveQuestionnaire() {

		// Create a new quiz and save it
		new Questionnaire("Premier quiz test").save();

		// Retrieve the quiz with the title
		Questionnaire firstQuestionnaire = Questionnaire.find("byTitle",
				"Premier quiz test").first();

		// Test
		assertNotNull(firstQuestionnaire);
		assertEquals("Premier quiz test", firstQuestionnaire.title);
	}

	@Test
	public void createQuestion() {

		// Create a new quiz and save it
		Questionnaire firstQuestionnaire = new Questionnaire(
				"Premier quiz test").save();

		// Create a new question
		new Question("Première question", firstQuestionnaire).save();

		// Test that the question has been created
		assertEquals(1, Question.count());

		// Retrieve all questions from firstQuestionnaire
		List<Question> firstQuestionnaireQuestions = Question.find(
				"byQuestionnaire", firstQuestionnaire).fetch();

		// Tests
		assertEquals(1, firstQuestionnaireQuestions.size());
		Question firstQuestion = firstQuestionnaireQuestions.get(0);
		assertNotNull(firstQuestion);
		assertEquals("Première question", firstQuestion.label);
		assertEquals(firstQuestionnaire, firstQuestion.questionnaire);
	}

	@Test
	public void createAnswer() {

		// Create a new quiz and save it
		Questionnaire firstQuestionnaire = new Questionnaire(
				"Premier quiz test").save();

		// Create a new question
		Question firstQuestion = new Question("Combien font 1+1?",
				firstQuestionnaire).save();

		// Create 2 new answers and save them
		new Answer("2", true, firstQuestion).save();
		new Answer("18", false, firstQuestion).save();

		// Retrieve all answers of the first question
		List<Answer> firstQuestionAnswers = Answer.find("byQuestion",
				firstQuestion).fetch();

		// Tests
		assertEquals(2, firstQuestionAnswers.size());

		Answer firstAnswer = firstQuestionAnswers.get(0);
		assertNotNull(firstAnswer);
		assertEquals("2", firstAnswer.label);
		assertEquals(true, firstAnswer.isCorrect);

		Answer secondAnswer = firstQuestionAnswers.get(1);
		assertNotNull(secondAnswer);
		assertEquals("18", secondAnswer.label);
		assertEquals(false, secondAnswer.isCorrect);

	}

	@Test
	public void createResult() {
		// Create a new quiz and save it
		Questionnaire firstQuestionnaire = new Questionnaire(
				"Premier quiz test").save();

		// Create a new question
		Question firstQuestion = new Question("Combien font 1+1?",
				firstQuestionnaire).save();

		// Create 2 new answers and save them
		Answer firstAnswer = new Answer("2", true, firstQuestion).save();

		// Create new result
		new Result(new Long(1), new Long(1), firstQuestion, firstAnswer).save();

		// Retrieve the result of the firstQuestion
		List<Result> results = Result.find("byQuestion", firstQuestion).fetch();

		// Tests
		assertEquals(1, results.size());

		Result firstResult = results.get(0);
		assertNotNull(firstResult);
		assertEquals(new Long(1), firstResult.genQuestionnaireId);
		assertEquals(firstQuestion, firstResult.question);
		assertEquals(firstAnswer, firstResult.answer);
	}

	@Test
	public void fullTest() {

		Fixtures.loadModels("initial-data.yml");

		// Count things
		assertEquals(3, Questionnaire.count());
		assertEquals(19, Question.count());
		assertEquals(76, Answer.count());

		// Find questionnaires
		Questionnaire botaniqueQuestionnaire = Questionnaire.find("byTitle",
				"Questionnaire sur la botanique").first();
		assertNotNull(botaniqueQuestionnaire);

		Questionnaire vacheQuestionnaire = Questionnaire.find("byTitle",
				"Questionnaire sur les vaches").first();
		assertNotNull(vacheQuestionnaire);

		Questionnaire normandeQuestionnaire = Questionnaire.find("byTitle",
				"Questionnaire sur la Normande").first();
		assertNotNull(normandeQuestionnaire);

		// Find all questions
		List<Question> botaniqueQuestionnaireQuestions = Question.find(
				"byQuestionnaire", botaniqueQuestionnaire).fetch();
		assertEquals(14, botaniqueQuestionnaireQuestions.size());

		List<Question> vacheQuestionnaireQuestions = Question.find(
				"byQuestionnaire", vacheQuestionnaire).fetch();
		assertEquals(4, vacheQuestionnaireQuestions.size());

		List<Question> normandeQuestionnaireQuestions = Question.find(
				"byQuestionnaire", normandeQuestionnaire).fetch();
		assertEquals(1, normandeQuestionnaireQuestions.size());

		// Find all answers related to botaniqueQuestionnaire
		List<Answer> botaniqueAnswers = Answer.find(
				"question.questionnaire.title",
				"Questionnaire sur la botanique").fetch();
		assertEquals(56, botaniqueAnswers.size());

		// Find all answers related to vacheQuestionnaire
		List<Answer> vacheAnswers = Answer.find("question.questionnaire.title",
				"Questionnaire sur les vaches").fetch();
		assertEquals(16, vacheAnswers.size());

		// Find all answers related to vacheQuestionnaire
		List<Answer> normandeAnswers = Answer
				.find("question.questionnaire.title",
						"Questionnaire sur la Normande").fetch();
		assertEquals(4, normandeAnswers.size());
	}

	@Test
	public void createAndRetrieveUser() {
		// Create a new user and save it
		new User("paul@gmail.com", "admin").save();

		// Retrieve the user with email address paul@gmail.com
		User paul = User.find("byEmail", "paul@gmail.com").first();

		// Test
		assertNotNull(paul);
		assertEquals("admin", paul.password);
	}

	@Test
	public void tryConnectAsUser() {
		// Create a new user and save it
		new User("paul@gmail.com", "admin").save();

		// Test
		assertNotNull(User.connect("paul@gmail.com", "admin"));
		assertNull(User.connect("paul@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));
	}

}
