package tests.edo;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.PLTCycleType;
import Enums.PLTStartLevel;
import Enums.TestQuestionType;
import Objects.PLTCycle;
import Objects.TestQuestion;
import pageObjects.EdoHomePage;
import pageObjects.edo.edoPlacementTestPage;
import tests.misc.EdusoftWebTest;

public class EdoPLTTests extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void basePLTTest() throws Exception {
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoPlacementTestPage edoPlacementTestPage = edoHomePage
				.clickOnPLTTests();
		edoPlacementTestPage.selectStartLevel(PLTStartLevel.Intermediate);

		TestQuestion question = new TestQuestion(
				 new String[] {
						"New Town has joined an ecology program.",
						"in cities and towns throughout the U.S." },null, null,
				new int[] { 2 },false,TestQuestionType.RadioMultiple);

		List<TestQuestion> cycle1questions = new ArrayList<TestQuestion>();

		PLTCycle cycleOne = new PLTCycle(PLTStartLevel.Intermediate, 1, null,
				cycle1questions, PLTCycleType.Reading);
		edoPlacementTestPage.performTest(cycleOne, null);

	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
