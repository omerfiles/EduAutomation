package tests.speechRecognition;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tests.misc.EdusoftWebTest;

public class GrammersTests extends EdusoftWebTest {

	private static final String SPEAKING_FOLDER = "\\\\frontqa3\\EDO_HTML_SR\\Runtime\\Content\\speaking";
	private final String grammerFilesPath = "\\\\NEWSTORAGE\\Sendhere\\_EDOHTML\\EduSpeak\\sample-grammars\\English\\baseEDOCources.grammar";

	// private final String grammerFilesPath="C:\\automation\\testFile.txt";
	@Before
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void compareAllGrammers() throws Exception {
		startStep("Iterate on all Speakeing folders");

		// \\frontqa3\EDO_HTML_SR\Runtime\Content\speaking
		List<String> folders = getSubFolders(SPEAKING_FOLDER);

		startStep("For all folder, search for js file, get its contend and compare texts to grammer file according the grammer id");

		try {
			outerloop: for (int i = 0; i < folders.size(); i++) {
				System.out.println("Checking folder:" + folders.get(i));

				startStep("check if js file exist");

				boolean fileExist = textService
						.checkIfFileExist(SPEAKING_FOLDER + "\\"
								+ folders.get(i) + "\\" + folders.get(i)
								+ ".js");

				testResultService.assertEquals(true, fileExist, "File: "
						+ folders.get(i) + " was not found");
				if (fileExist == false) {
					continue;
				}

				String fileContent = textService.getTextFromFile(
						SPEAKING_FOLDER + "\\" + folders.get(i) + "\\"
								+ folders.get(i) + ".js",
						Charset.defaultCharset());
				String[] segments = textService.getHtmlElementFromHtmlFile(
						"//span[@class='segment']", fileContent);
				segments = textService.trimLowerCaseAndRemoveChars(segments);

				startStep("Compare each segment with the text from grammer file");
				String[] textFromGrammer = new String[segments.length];
				for (int j = 0; j < segments.length; j++) {
					int index = j + 1;
					String grammerText = getGrammerTextFromGrammerFiles("."
							+ folders.get(i).toUpperCase() + "_" + index);
					if (grammerText == null) {
						report.report("Grammer text not found in grammer file:"
								+ folders.get(i).toUpperCase() + "_" + index);
						continue outerloop;
					}
					textFromGrammer[j] = grammerText;

				}

				startStep("split sentence to string arrays and compare");
				for (int k = 0; k < segments.length; k++) {
					String[] segmentsWords = textService.splitStringToArray(
							segments[k], "\\s+");
					String[] grammerWords = textService.splitStringToArray(
							textFromGrammer[k], "\\s+");
					if (testResultService.assertEquals(true,
							segmentsWords.length == grammerWords.length,
							"number of words is not the same for grammer: "
									+ folders.get(i) + "_" + k) == false) {
						System.out.println("segment words:");
						report.report("segment words:");
						textService.printStringArray(segmentsWords);
						System.out.println("Grammer words:");
						report.report("grammer words:");
						textService.printStringArray(grammerWords);
						
						continue outerloop;
						
					}
					for (int h = 0; h < segmentsWords.length; h++) {
						testResultService.assertEquals(segmentsWords[h],
								grammerWords[h], "problem in grammer: "
										+ folders.get(i) + "_" + k);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void findGrammer() throws Exception {
		String text = getGrammerTextFromGrammerFiles(".A3VEADE_886");
		System.out.println(text);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public String getGrammerTextFromGrammerFiles(String grammerID)
			throws IOException {
		String text = null;
		try {
			text = textService.getLineFromTextFile(new File(grammerFilesPath),
					grammerID);
			int begin = text.indexOf("(");
			begin++;
			text = text.substring(begin, text.lastIndexOf(")"));
			text.trim();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Problem getting text for grammer in grammer file:"
					+ grammerID);

		}
		return text;
	}

	public List<String> getSubFolders(String path) {
		File file = new File(path);
		String[] names = file.list();
		List<String> folders = new ArrayList<String>();

		for (String name : names) {
			if (new File(path + "\\" + name).isDirectory()) {
				System.out.println(name);
				if (name.length() <= 6) {
					folders.add(name);
				}
			}
		}
		return folders;

	}

}
