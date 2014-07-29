package tests.speechRecognition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Enums.ByTypes;
import Enums.SRWordLevel;
import Objects.Course;
import Objects.Recording;
import pageObjects.EdoHomePage;
import pageObjects.RecordPanel;
import pageObjects.RecordPanel;
import tests.misc.EdusoftWebTest;

public class RecoredYourself extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();

	}

	@Test
	public void testEecoredYourSelf() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);
		String[] words = new String[] { "Hi", "I'm", "Tom", "Smith." };
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(3);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		startStep("Click on record and send audio file to microphone");

		for (int i = 0; i < numOfRecordingsInTest; i++) {
			recordPanel.clickOnRecordAndStop(10);
			String[] wordsScoring = textService.splitStringToArray(recordPanel
					.getWordsScoring());
			recWordLevel.add(wordsScoring);
			int sentenceLevel = recordPanel.getSentenceLevel();
			sentenceLevels.add(sentenceLevel);
			recordPanel.checkSentenceScoreRatingText(sentenceLevel);
			recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
			// to do: check SL for added entry in the list
			recordPanel.checkAddedRecordingToList(sentenceLevel, i);

			recordPanel.checkWordsLevel(words, wordsScoring, textService);
		}

		startStep("Select each recording from the list and check Sentence level annd word level");

		for (int i = 0; i < numOfRecordingsInTest; i++) {
			int index = i + 1;
			recordPanel.selectRecording(String.valueOf(index));
			recordPanel
					.checkWordsLevel(words, recWordLevel.get(i), textService);
			recordPanel.checkSentenceScoreRatingText(sentenceLevels.get(i));

		}

		startStep("Send to Teacher");
		// recordPanel.clickOnPlayButton();
		sleep(10);
		recordPanel.clickOnSendToTeacher();
	}

	@Test
	public void testRecoredMoreThenEightTimes() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);
		String[] words = new String[] { "Hi", "I'm", "Tom", "Smith." };
		int numOfRecordingsInTest = 8;
		List<String[]> wordsScoreList = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(3);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		startStep("Click on record ");

		for (int i = 0; i < numOfRecordingsInTest; i++) {
			recordPanel.clickOnRecordAndStop(10);
			String[] wordsScoring = textService.splitStringToArray(recordPanel
					.getWordsScoring());
			wordsScoreList.add(wordsScoring);
			int sentenceLevel = recordPanel.getSentenceLevel();
			sentenceLevels.add(sentenceLevel);
			recordPanel.checkAddedRecordingToList(sentenceLevel, i);
		}
		startStep("try to add the 9th recording");
		recordPanel.clickOnRecordButton();
		recordPanel.checkSendStutusMessage();
		recordPanel.clickOnSendStatusCancelButton();
		recordPanel.clickOnRecordButton();
		recordPanel.clickOnSendStatusRecordButton();

		startStep("Record another time");
		recordPanel.clickOnRecordAndStop(5);
		wordsScoreList.remove(0);
		String[] wordsScoring = textService.splitStringToArray(recordPanel
				.getWordsScoring());
		wordsScoreList.add(wordsScoring);

		int sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevels.remove(0);
		sentenceLevels.add(sentenceLevel);

		startStep("Check the recording was added and that the 1st recording was removed");

		for (int i = 0; i < numOfRecordingsInTest; i++) {
			
			int index = i + 1;
			System.out.println("Index is: "+index);
			recordPanel.selectRecording(String.valueOf(index));
			sleep(1);
			recordPanel.checkWordsLevel(words, wordsScoreList.get(i),
					textService);
			recordPanel.checkSentenceScoreRatingText(sentenceLevels.get(i));

		}

	}

	// Test case 13439
	@Test
	public void testRecordPanelFromVacabulary() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(9);
		String[] words = new String[] { "twenty-one" };

		List<String[]> wordsScoreList = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();

		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");
		edoHomePage.clickOnVocabText("W0");
		startStep("Click on recored yourself");

		RecordPanel recordPanel = edoHomePage
				.clickOnRecordYourselfInVocabulary();
		edoHomePage.switchToFrameByClassName("cboxIframe");

		startStep("Check that the record panel opens");
		recordPanel.clickOnRecordAndStop(5);
		String[] wordsScoring = textService.splitStringToArray(recordPanel
				.getWordsScoring());
		wordsScoreList.add(wordsScoring);
		int sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevels.add(sentenceLevel);
		recordPanel.checkSentenceScoreRatingText(sentenceLevel);
		recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
		recordPanel.checkAddedRecordingToList(sentenceLevel, 0);
		recordPanel.checkWordsLevel(words, wordsScoring, textService);
	}

	// Test case 13440

	public void testRecordYourselfPanelOpenFromIdioms() throws Exception {
		startStep("Init test data");

		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		
		startStep("Go to Talking Idioms");
		edoHomePage.clickOnCommunity();
		edoHomePage.clickOnTalkingIdioms();

		startStep("Select a section and click record");
		sleep(3);
		edoHomePage.selectIdiomFromList("1");
		edoHomePage.clickOnRecordFromIdioms();

	}
	
	//Test case 13438
	@Test
	public void testRecoredYourselfPanelOpensFromPronunciation()throws Exception{
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);
		String[] words = new String[] { "Hi" };
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on pronunciation");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		RecordPanel recordPanel = edoHomePage.clickOnPronunciation();
		sleep(3);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		
		startStep("Click on record and send audio file to microphone");
		recordPanel.clickOnRecordAndStop(5);
		String[] wordsScoring = textService.splitStringToArray(recordPanel
				.getWordsScoring());
		
		int sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevels.add(sentenceLevel);
		recordPanel.checkSentenceScoreRatingText(sentenceLevel);
		recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
		recordPanel.checkAddedRecordingToList(sentenceLevel, 0);
		recordPanel.checkWordsLevel(words, wordsScoring, textService);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
