package tests.speechRecognition;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Objects.Course;
import Objects.Recording;
import pageObjects.EdoHomePage;
import pageObjects.RecordPanel;
import tests.misc.EdusoftWebTest;

public class RecoredYourself extends EdusoftWebTest {

	@Before
	public void setup() throws Exception {
		super.setup();

	}

	@Test
	public void testRecordPanel1() throws Exception {
		testRecordYourselfIntegrated(13, 1, 1, 0);
	}

	@Test
	public void testRecordPanel2() throws Exception {
		testRecordYourselfIntegrated(15, 6, 3, 8000.0F);
	}

	public void testRecordYourselfIntegrated(int courseId, int scriptSection,
			int recordingId, float sampleRate) throws Exception {
		startStep("Init test data");
		// int courseId = 13;
		// int scriptSection = 1;
		Recording recording = pageHelper.getRecordings().get(recordingId);
		Course course = pageHelper.initCouse(courseId);
		// Recording recording=pageHelper.getRecordings().get(1);

		String[] expectedWordLevels = null;
		int sentenceLevel = 0;
		boolean SRDebug = false;
		String[] words = null;
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(3);
		edoHomePage.clickOnCourses();
		sleep(2);
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		edoHomePage.selectTextFromContainer(scriptSection);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(3);

		// if QA, allow mic
		startStep("Check if running in QA");
		if (pageHelper.getSutAndSubDomain().contains("qa")) {
			recordPanel.allowMicFirefox();
		}

		edoHomePage.switchToFrameByClassName("cboxIframe");
		startStep("Click on record and send audio file to microphone");

		words = recordPanel.getSentenceText(textService);

		sleep(3);
		recordPanel.clickOnRecordButton();
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		// recordPanel.waitForSpeakStatus();
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
				sampleRate);

		startStep("Check that recording ended");
		sleep(2);
		recordPanel.waitForRecordingToEnd(1);
		webDriver.printScreen("After recording ended");
		// audioService.sendSoundToVirtualMic(new File(
		// "files/audioFiles/TheBeatMe16000_16.wav"), 16000.0F);
		int debugSentenceLevel = recordPanel.getSentenceLevel();

		sentenceLevel = recording.getSL().get(0);

		System.out.println("Sentence level is: " + sentenceLevel);
		String[] debugWordLevels = textService.splitStringToArray(recordPanel
		 .getWordsScoring("wl"));
		expectedWordLevels = recording.getWL().get(0);
		System.out.println("Word level is: " + expectedWordLevels.toString());

		startStep("Check word level and sentence level");
		recordPanel.checkWordsLevels(words, debugWordLevels, textService);
		recordPanel.checckSentenceLevelLightBulbs(debugSentenceLevel);
		recordPanel.checkSentenceScoreRatingText(debugSentenceLevel);
		// recordPanel.checkSentenceScoreText(sentenceLevel);
		recordPanel.checkThatWlIsCloseToExpectedWL(expectedWordLevels, textService
				.splitStringToArray(recordPanel.getWordsScoring("wl")));

	}

	public void testRecordYourselfIntegrated2() throws Exception {
		startStep("Init test data");
		int courseId = 15;
		int scriptSection = 6;
		Recording recording = pageHelper.getRecordings().get(3);
		Course course = pageHelper.initCouse(courseId);
		// Recording recording=pageHelper.getRecordings().get(1);

		String[] wordLevels = null;
		int sentenceLevel = 0;
		boolean SRDebug = false;
		String[] words = null;
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(3);
		edoHomePage.clickOnCourses();
		sleep(2);
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		edoHomePage.selectTextFromContainer(scriptSection);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(3);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		startStep("Click on record and send audio file to microphone");

		words = recordPanel.getSentenceText(textService);

		sleep(3);
		recordPanel.clickOnRecordButton();
		sleep(3);
		// recordPanel.waitForSpeakStatus();
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0), 0);

		startStep("Check that recording ended");
		sleep(2);
		recordPanel.waitForRecordingToEnd(1);
		// audioService.sendSoundToVirtualMic(new File(
		// "files/audioFiles/TheBeatMe16000_16.wav"), 16000.0F);
		// sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevel = recording.getSL().get(0);
		System.out.println("Sentence level is: " + sentenceLevel);
		wordLevels = textService.splitStringToArray(recordPanel
				.getWordsScoring("wl"));
		// wordLevels=recording.getWL();
		System.out.println("Word level is: " + wordLevels.toString());

		startStep("Check word level and sentence level");
		recordPanel.checkWordsLevels(words, wordLevels, textService);
		recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
		recordPanel.checkSentenceScoreRatingText(sentenceLevel);
		// recordPanel.checkSentenceScoreText(sentenceLevel);
		recordPanel.checkThatWlIsCloseToExpectedWL(wordLevels, recording
				.getWL().get(0));

	}

	public void testRecordPanelScore4() throws Exception {
		// they beat me up and stole my wife's car
		// Advanced 1,Life In The City,How Awful!,6
		// 191301.wav
		// Script section 1
		testRecoredPanel(13, pageHelper.getRecordings().get(1), 1);
	}

	public void testRecordPanelScore5() throws Exception {
		// text: what happened
		// Advanced 1,Dangerous Sports,What Happened?,6
		// 225059.wav
		// Script section 2
		testRecoredPanel(14, pageHelper.getRecordings().get(2), 2);
	}

	public void testRecoredPanel(int courseId, Recording recording,
			int scriptSection) throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(courseId);
		// Recording recording=pageHelper.getRecordings().get(1);

		String[] wordLevels = null;
		int sentenceLevel = 0;
		boolean SRDebug = false;
		String[] words = null;
		int numOfRecordingsInTest = 6;
		List<String[]> recWordLevel = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(3);
		edoHomePage.clickOnCourses();
		sleep(2);
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		edoHomePage.selectTextFromContainer(scriptSection);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(3);
		edoHomePage.switchToFrameByClassName("cboxIframe");
		startStep("Click on record and send audio file to microphone");

		words = recordPanel.getSentenceText(textService);

		for (int i = 0; i < numOfRecordingsInTest; i++) {

			if (SRDebug == true) {
				recordPanel.clickOnRecordAndStop(10);
				sentenceLevel = recordPanel.getSentenceLevel();
				sentenceLevels.add(sentenceLevel);
				wordLevels = textService.splitStringToArray(recordPanel
						.getWordsScoring("wl"));
				recWordLevel.add(wordLevels);
			} else if (SRDebug == false) {
				sleep(3);
				recordPanel.clickOnRecordButton();
				audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
						0);
				sleep(2);
				// audioService.sendSoundToVirtualMic(new File(
				// "files/audioFiles/TheBeatMe16000_16.wav"), 16000.0F);
				sentenceLevel = recording.getSL().get(0);
				sentenceLevels.add(sentenceLevel);
				wordLevels = recording.getWL().get(0);
				recWordLevel.add(wordLevels);
			}

			// recordPanel.checkSentenceScoreRatingText(sentenceLevel);
			// recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
			// to do: check SL for added entry in the list
			// recordPanel.checkAddedRecordingToList(sentenceLevel, i);

			// recordPanel.checkWordsLevels(words, wordLevels, textService);
		}

		startStep("Select each recording from the list and check Sentence level annd word level");

		// for (int i = 0; i < numOfRecordingsInTest; i++) {
		// int index = i + 1;
		// recordPanel.selectRecording(String.valueOf(index));
		// recordPanel
		// .checkWordsLevels(words, recWordLevel.get(i), textService);
		// recordPanel.checkSentenceScoreRatingText(sentenceLevels.get(i));
		//
		// }

		startStep("Send to Teacher");
		// recordPanel.clickOnPlayButton();
		sleep(10);
		recordPanel.clickOnSendToTeacher();
		// recordPanel.checkSendToTeacherText();
	}

	@Test
	public void testRecoredMoreThenEightTimes() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(8);
		Recording recording = pageHelper.getRecordings().get(5);
		float sampleRate = 0;
		String[] words = new String[] { "Hi", "I'm", "Tom", "Smith" };
		int numOfRecordingsInTest = 8;
		List<String[]> wordsScoreList = new ArrayList<String[]>();
		List<Integer> sentenceLevels = new ArrayList<Integer>();
		startStep("Login to EDO as student");
		EdoHomePage edoHomePage = pageHelper.loginAsStudent();
		sleep(3);
		edoHomePage.clickOnCourses();
		edoHomePage.clickOnCourseByName(course.getName());
		edoHomePage.clickOnCourseUnit(course.getCourseUnit());
		edoHomePage.clickOntUnitComponent(course.getUnitComponent(), "Explore");

		startStep("Click on recored yourself");
		edoHomePage.clickOnSeeScript();
		sleep(3);
		RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
		sleep(5);
		startStep("Check if running in QA");
		if (pageHelper.getSutAndSubDomain().contains("qa")) {
			System.out.println("In QA. allowing firefox");
					
			recordPanel.allowMicFirefox();
			sleep(3);
		}
		edoHomePage.switchToFrameByClassName("cboxIframe");

		startStep("Click on record ");
		sleep(2);
		for (int i = 0; i < numOfRecordingsInTest; i++) {
			recordPanel.clickOnRecordButton();
			String status = recordPanel.getRecordPanelStatus();
			testResultService.assertEquals("SPEAK", status);
			// recordPanel.waitForSpeakStatus();
			audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
					sampleRate);

			startStep("Check that recording ended");
			sleep(2);
			recordPanel.waitForRecordingToEnd(1);
			
			sleep(5);
			webDriver.printScreen("After recording ended_"+i);
			String[] wordsScoring = textService.splitStringToArray(recordPanel
					.getWordsScoring("wl"));
			wordsScoreList.add(wordsScoring);
			int sentenceLevel = recordPanel.getSentenceLevel();
			sentenceLevels.add(sentenceLevel);
			recordPanel.checkAddedRecordingToList(sentenceLevel, i);
			startStep("Check SL according to word levels");
			pageHelper.calculateSLbyWL(wordsScoring,String.valueOf(sentenceLevel) );
		}
		startStep("try to add the 9th recording");
		recordPanel.clickOnRecordButton();
		recordPanel.checkSendStatusMessage();
		recordPanel.clickOnSendStatusCancelButton();
		recordPanel.clickOnRecordButton();
		recordPanel.clickOnSendStatusRecordButton();
		sleep(1);
		startStep("Record another time");
//		recordPanel.clickOnRecordButton();
		String status = recordPanel.getRecordPanelStatus();
		testResultService.assertEquals("SPEAK", status);
		// recordPanel.waitForSpeakStatus();
		audioService.sendSoundToVirtualMic(recording.getFiles().get(0),
				sampleRate);

		startStep("Check that recording ended");
//		sleep(2);
		recordPanel.waitForRecordingToEnd(1);
		
		sleep(4);
		wordsScoreList.remove(0);
		String[] wordsScoring = textService.splitStringToArray(recordPanel
				.getWordsScoring("wl"));
		System.out.println("printing word score of last recording:");
		textService.printStringArray(wordsScoring);
		wordsScoreList.add(wordsScoring);

		int sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevels.remove(0);
		sentenceLevels.add(sentenceLevel);
		sleep(3);
		startStep("Check the recording was added and that the 1st recording was removed");
		sleep(5);
		for (int i = 0; i < numOfRecordingsInTest; i++) {

			int index = i + 1;
			System.out.println("Index is: " + index);
			recordPanel.selectRecording(String.valueOf(index));
			Thread.sleep(2000);
			System.out.println("printing expected scores");
			if(index==8){
				System.out.println("index 8");
			}
			textService.printStringArray( wordsScoreList.get(i));
			recordPanel.checkWordsLevels(words, wordsScoreList.get(i),
					textService);
			recordPanel.checkSentenceScoreRatingText(sentenceLevels.get(i));

		}

	}

	// Test case 13439

	public void testRecordPanelFromVacabulary() throws Exception {
		startStep("Init test data");
		Course course = pageHelper.initCouse(9);
		int recordingId=7;
		float sampleRate=8000.0F;
		Recording recording=pageHelper.getRecordings().get(recordingId); 
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
		sleep(3);
//		recordPanel.clickOnRecordAndStop(5);
		recordPanel.clickOnRecordButtonAndSendRecording(recording, sampleRate, audioService);
		String[] wordsScoring = textService.splitStringToArray(recordPanel
				.getWordsScoring("wl"));
		wordsScoreList.add(wordsScoring);
		int sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevels.add(sentenceLevel);
		recordPanel.checkSentenceScoreRatingText(sentenceLevel);
		recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
		recordPanel.checkAddedRecordingToList(sentenceLevel, 0);
		recordPanel.checkWordsLevels(words, wordsScoring, textService);
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

	// Test case 13438

	public void testRecoredYourselfPanelOpensFromPronunciation()
			throws Exception {
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
				.getWordsScoring("wl"));

		int sentenceLevel = recordPanel.getSentenceLevel();
		sentenceLevels.add(sentenceLevel);
		recordPanel.checkSentenceScoreRatingText(sentenceLevel);
		recordPanel.checckSentenceLevelLightBulbs(sentenceLevel);
		recordPanel.checkAddedRecordingToList(sentenceLevel, 0);
		recordPanel.checkWordsLevels(words, wordsScoring, textService);
	}
	
	public void calculateSLbyWL(String[]WL,int SL){
		double wordLevels=0;
		for(int i=0;i<WL.length;i++){
			wordLevels+=Integer.valueOf(WL[i]);
		}
		wordLevels=wordLevels/WL.length;
		wordLevels=Math.ceil(wordLevels);
		
	}
	
	

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

}
