package tests.speechRecognition;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jsystem.framework.report.Reporter.EnumReportLevel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import tests.misc.EdusoftWebTest;

public class GrammarsTests extends EdusoftWebTest {

	private static final String SPEAKING_FOLDER = "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\speaking";
	private static final String LISTENING_FOLDER = "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\listening";
	private static final String GRAMMER_FOLDER = "\\\\frontq_a3\\EDO_HTML_SR\\Runtime\\Content\\grammar";
	// private static final String Vocabulary_FOLDER =
	// "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\Vocabulary";
	private static final String Vocabulary_FOLDER = "\\\\newstorage\\sendhere\\_EDOHTML\\EDO_GRAMMAR_CHANGES\\BaseCourses\\Alphabet";
	private static final String tempTestFile = "C:\\automation\\vocabulary.grammar";

	// private final String grammerFilesPath =
	// "\\\\NEWSTORAGE\\Sendhere\\_EDOHTML\\EduSpeak\\sample-grammars\\English\\baseEDOCources.grammar";
	private final String grammerFilesPath = "\\\\NEWSTORAGE\\Sendhere\\_EDOHTML\\SR_grammars\\OnlyExcel\\UnitedBaseEDO\\speakingBaseEDO.grammar";
	// private final String grammerFilesPath="C:\\automation\\testFile.txt";

	List<String[]> results = new ArrayList<>();

	@Before
	public void setup() throws Exception {
		super.setup();
		setPrintResults(false);
	}

	@Test
	public void testSpeaking() throws Exception {

		compareAllGrammers(
				SPEAKING_FOLDER,
				"speaking_AlteredPedagogy.csv",
				"\\\\newstorage\\Sendhere\\_EDOHTML\\SR_grammars\\OnlyExcel\\UnitedBaseEDO\\speakingBaseEDO.grammar");
	}

	@Test
	public void testListening() throws Exception {
		compareAllGrammers(
				LISTENING_FOLDER,
				"listening_AlteredPedagogy.csv",
				"\\\\newstorage\\Sendhere\\_EDOHTML\\SR_grammars\\OnlyExcel\\UnitedBaseEDO\\listeningBaseEDO.grammar");
	}

	@Test
	public void testGrammer() throws Exception {
		// compareAllGrammers(GRAMMER_FOLDER);
	}

	@Test
	public void checkVocabaalaryGrammers() throws Exception {
		int failed = 0;
		int passed = 0;
		startStep("Get folders list");
		List<String> folders = getSubFolders(Vocabulary_FOLDER);
		try {
			outerloop: for (int i = 0; i < folders.size(); i++) {
				startStep("For all folder, search for xml file, get its contend and compare texts to grammer file according the grammer id");
				String filePath = Vocabulary_FOLDER + "\\" + folders.get(i)
						+ "\\" + folders.get(i) + "e.xml";
				startStep("Check that file exist");
				boolean fileExist = textService.checkIfFileExist(filePath);
				boolean fileFound = testResultService
						.assertEquals(true, fileExist,
								"File: " + folders.get(i) + " was not found");
				if (fileFound == false) {
					String[] str = new String[] { "File not found",
							folders.get(i), "File " + filePath + " Not Found" };
					results.add(str);
				}
				List<String[]> list = getTextSegmensFromXmlFile(filePath,
						folders.get(i));
				if (list == null) {
					System.out.println("file " + folders.get(i)
							+ " should not be tested. move on");
					break outerloop;
				}
				String[] subGrammerIds = new String[list.size()];
				String[] segments = new String[list.size()];
				String[] textFromGrammer = new String[list.size()];
				for (int j = 0; j < list.size(); j++) {
					String textFromXml = list.get(j)[1];
					// String subGrammerId = list.get(j)[0];
					subGrammerIds[j] = list.get(j)[0];
					subGrammerIds[j] = subGrammerIds[j].toUpperCase();
					segments[j] = textFromXml;
					textFromGrammer[j] = getGrammerTextFromGrammerFiles("."
							+ subGrammerIds[j], grammerFilesPath);
					int index = j + 1;
					if (textFromGrammer[j] == null) {
						report.report("Grammer text not found in grammer file:"
								+ folders.get(i).toUpperCase() + "_" + index);

						String[] str = new String[] {
								"Missing grammer",
								folders.get(i),
								"Grammer missing "
										+ folders.get(i).toUpperCase() + "_"
										+ index };
						results.add(str);
						failed++;
						continue outerloop;
					}

				}
				for (int k = 0; k < segments.length; k++) {
					int index = k + 1;

					startStep("Compare grammer texts and segements texts");

					if (compareSegmentTextAndGrammerTexsts(segments,
							textFromGrammer, folders.get(i)) == false) {

						failed++;
						continue outerloop;
					}

				}
				passed++;
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block

		}

		textService.writeArrayistToCSVFile(
				"files/csvFIles/resultOutput_vocab.csv", results);

	}

	public void compareAllGrammers(String testFolder, String csvFile,
			String grammarFile) throws Exception {
		startStep("Iterate on all Speakeing folders");
		
		
		// testFolder = testFolder;
		int passed = 0;
		int failed = 0;
		// \\frontqa3\EDO_HTML_SR\Runtime\Content\speaking
		List<String> folders = getSubFolders(testFolder, "files/csvFIles/"
				+ csvFile);

		startStep("For all folder, search for js file, get its contend and compare texts to grammer file according the grammer id");
		String filePath = null;
		try {
			outerloop: for (int i = 0; i < folders.size(); i++) {
				System.out.println("Checking folder:" + folders.get(i));

				startStep("check if js file exist");
				// if testing vocabalary files
				if (testFolder == Vocabulary_FOLDER) {
					filePath = testFolder + "\\" + folders.get(i) + "\\"
							+ folders.get(i) + ".xml";
				} else {
					filePath = testFolder + "\\" + folders.get(i) + "\\"
							+ folders.get(i) + ".js";
				}
				boolean fileExist = textService.checkIfFileExist(filePath);

				testResultService.assertEquals(true, fileExist, "File: "
						+ folders.get(i) + " was not found");
				if (fileExist == false) {
					String[] str = new String[] { "File not found",
							folders.get(i), "File " + filePath + " Not Found" };
					results.add(str);
					failed++;
					continue;
				}
				String fileContent = textService.getTextFromFile(filePath,
						Charset.defaultCharset());
				String[] segments = textService.getHtmlElementFromHtmlFile(
						"//span[@class='segment']", fileContent);
				segments = textService.trimLowerCaseAndRemoveChars(segments);

				startStep("Compare each segment with the text from grammer file");
				String[] textFromGrammer = new String[segments.length];
				innerloop: for (int j = 0; j < segments.length; j++) {
					int index = j + 1;
					String grammerText = getGrammerTextFromGrammerFiles("."
							+ folders.get(i).toUpperCase() + "_" + index,
							grammarFile);
					if (grammerText == null) {
						report.report("Grammer text not found in grammer file:"
								+ folders.get(i).toUpperCase() + "_" + index);

						failed++;
						continue innerloop;
					} else {
						textFromGrammer[j] = grammerText;
					}

				}

				startStep("split sentence to string arrays and compare");
				for (int k = 0; k < segments.length; k++) {
					String[] segmentsWords = textService.splitStringToArray(
							segments[k], "\\s+");
					if (textFromGrammer[k] != null) {
						String[] grammerWords = textService.splitStringToArray(
								textFromGrammer[k], "\\s+");

						if (testResultService
								.assertEquals(
										true,
										segmentsWords.length == grammerWords.length,
										"number of words is not the same for grammer: "
												+ folders.get(i)
												+ "_"
												+ k
												+ " segment words: "
												+ textService
														.printStringArray(segmentsWords)
												+ " grammer words:"
												+ textService
														.printStringArray(grammerWords)) == false) {
							System.out.println("segment words:");
							report.report("segment words:");

							textService.printStringArray(segmentsWords);
							System.out.println("Grammer words:");
							report.report("grammer words:");
							textService.printStringArray(grammerWords);
							String[] str = new String[] {
									"Error",
									folders.get(i),
									"Missing word count",
									"Grammer words:"
											+ textService
													.printStringArray(grammerWords),
									"Segment words:"
											+ textService
													.printStringArray(segmentsWords) };
							results.add(str);
							failed++;
							continue outerloop;

						}
						for (int h = 0; h < segmentsWords.length; h++) {
							boolean wordMatch = testResultService.assertEquals(
									segmentsWords[h], grammerWords[h],
									"problem in grammer: " + folders.get(i)
											+ "_" + k);
							if (wordMatch == false) {
								String[] str = new String[] { "Word mismatch",
										folders.get(i), segmentsWords[h],
										grammerWords[h] };
								results.add(str);
							}
						}
					}
				}
				System.out.println(i + "Finished checking folder: "
						+ folders.get(0) + ". folder is OK");
				report.startLevel(
						i + "Finished checking folder: " + folders.get(0)
								+ ". folder is OK", EnumReportLevel.MainFrame);
				passed++;
			}
			System.out.println("Passed:" + passed);
			System.out.println("Failed:" + failed);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String csvFilepath = "D:\\automationLogs\\csvFiles\\grammerTests\\resultOutput"
				+ testFolder.substring(testFolder.length() - 6,
						testFolder.length()) + "_" + dbService.sig(8) + ".csv";
		textService.writeArrayistToCSVFile(csvFilepath, results);
		System.out.println("csv file path:" + "file:///" + csvFilepath);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public String getGrammerTextFromGrammerFiles(String grammerID,
			String grammarFilePath) throws IOException {
		String text = null;
		try {
			text = textService.getLineFromTextFile(new File(grammarFilePath),
					grammerID);
			int begin = text.indexOf("(");
			begin++;
			text = text.substring(begin, text.lastIndexOf(")"));
			text.trim();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService
					.addFailTest("Problem getting text for grammer in grammer file:"
							+ grammerID);
			String[] str = new String[] { "Missing grammer",

			"Grammer missing: " + grammerID };
			results.add(str);

		}
		return text;
	}

	public List<String> getSubFolders(String path) throws Exception {
		return getSubFolders(path, null);
	}

	public List<String> getSubFolders(String path, String csvFilePath)
			throws Exception {
		List<String> folders = null;

		List<String> grammarsFromExcel = null;
		if (csvFilePath != null) {
			grammarsFromExcel = textService.getStrListFromCsv(csvFilePath, 0);
		}
		try {
			File file = new File(path);
			String[] names = file.list();
			if (names == null) {
				System.out.println("Folder is empty");
				Assert.fail("Test faolder is empty");
			}
			folders = new ArrayList<String>();

			for (String name : names) {

				if (new File(path + "\\" + name).isDirectory()
						&& grammarsFromExcel.contains(name.toUpperCase())) {
					System.out.println(name);
					if (name.length() <= 6) {
						folders.add(name);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Path is: " + path);
			System.out.println(" Failed in getSubFolders:" + e.getMessage());
			testResultService.addFailTest("Failed on getSybFolder "
					+ e.getMessage());
			e.printStackTrace();
		}
		return folders;

	}

	private List<String[]> getTextSegmensFromXmlFile(String xmlPath,
			String grammerId) throws Exception {
		boolean VocabData = false;
		List<String[]> list = new ArrayList<String[]>();

		Document document = netService.getXmlFromFile(xmlPath);

		NodeList idNodeList = netService.getNodesFromXml("//Word", document);
		if (idNodeList.item(0).getAttributes().getNamedItem("Id") == null) {
			idNodeList = netService.getNodesFromXml("//Word//Id", document);
			VocabData = true;
		}
		NodeList segmentNodeList = netService.getNodesFromXml(
				"//Word//Example", document);
		// check if segments are not null. if null try //Word//Sentence
		if (segmentNodeList.getLength() == 0) {
			segmentNodeList = netService.getNodesFromXml("//Word//Sentence",
					document);
		}
		if (segmentNodeList.getLength() == 0) {
			// file does not contains valid data
			return list;
		}
		String id = null;
		for (int i = 0; i < idNodeList.getLength(); i++) {
			try {
				id = null;
				id = grammerId
						+ "E_"
						+ idNodeList.item(i).getAttributes().getNamedItem("Id")
								.getNodeValue();
			} catch (Exception e) {

			}
			// check other location of id

			// if id=-1, add failure
			if (id == "-1") {
				String[] errorStr = new String[] { "Error", grammerId,
						"SubGrammar id in XML=-1", };
				results.add(errorStr);
				continue;
			}
			if (id == null && VocabData == true) {
				id = grammerId + "_";
				id = id + idNodeList.item(i).getTextContent();

				// id = grammerId
				// + "E_"
				// + idNodeList.item(i).getChildNodes().item(2)
				// .getTextContent();
				System.out.println(idNodeList.item(i).getTextContent());

			}
			String[] str = new String[] { id,
					segmentNodeList.item(i).getTextContent() };
			list.add(str);
		}

		return list;

	}

	public boolean compareSegmentTextAndGrammerTexsts(String[] segmentTexts,
			String[] grammerTexts, String folder) throws Exception {
		segmentTexts = textService.trimLowerCaseAndRemoveChars(segmentTexts);
		for (int k = 0; k < segmentTexts.length; k++) {
			String[] segmentsWords = textService.splitStringToArray(
					segmentTexts[k], "\\s+");
			String[] grammerWords = textService.splitStringToArray(
					grammerTexts[k], "\\s+");
			if (testResultService.assertEquals(
					true,
					segmentsWords.length == grammerWords.length,
					"number of words is not the same for grammer: " + folder
							+ "_" + k + " segment words: "
							+ textService.printStringArray(segmentsWords)
							+ " grammer words:"
							+ textService.printStringArray(grammerWords)) == false) {
				System.out.println("segment words:");
				report.report("segment words:");

				textService.printStringArray(segmentsWords);
				System.out.println("Grammer words:");
				report.report("grammer words:");
				textService.printStringArray(grammerWords);

				String[] str = new String[] {
						"Word count mismatch",
						folder,

						"Grammer words:"
								+ textService.printStringArray(grammerWords),
						"Segment words:"
								+ textService.printStringArray(segmentsWords) };
				results.add(str);

				return false;
			}
			for (int h = 0; h < segmentsWords.length; h++) {
				if (testResultService.assertEquals(segmentsWords[h],
						grammerWords[h], "problem in grammer: " + folder + "_"
								+ k) == false) {
					// /////
					String[] str = new String[] { "Word mismatch", folder,
							segmentsWords[h], grammerWords[h] };
					results.add(str);
					return false;
				}
			}
		}
		return true;
	}

}
