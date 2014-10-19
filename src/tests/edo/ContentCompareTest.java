package tests.edo;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tests.BasicTests.ContentCompareBasicTest;

public class ContentCompareTest extends ContentCompareBasicTest {

	private final String oldContentBaseFolder = "\\\\frontqa3\\EDO_HTML_QA\\Runtime\\Content\\reading";
	private final String newContentBaseFolder = "\\\\frontdev2003\\EDOPedagogical\\EDO\\Runtime\\Content\\reading";
	private int numberOfSegmentsNotTheSame;
	private int wordsMisMatch;
	private int numberOfWordsInSubSegmentsNotTheSame;
	private int passedTests;

	@Test
	public void compareOldAndNewConent() throws Exception {
		// get old content subfolders into arrayList
		List<String> oldContentFolders = getSubFolders(oldContentBaseFolder,
				false);
		// get new content subfolsers into arrayList
		List<String> newContentFolders = getSubFolders(newContentBaseFolder,
				false);

		int foldersPassed = 0;
		int foldersFailed = 0;

		List<String[]> testResults = new ArrayList<String[]>();// folder
																// id,segment
																// id,status,explanation

		for (int i = 0; i < newContentFolders.size(); i++) {
			try {
				report.startLevel("Checking folder: "
						+ newContentFolders.get(i));
				String newContenetfilePath = newContentBaseFolder + "\\"
						+ newContentFolders.get(i) + "\\"
						+ newContentFolders.get(i) + ".js";

				String currentContenetfilePath = oldContentBaseFolder + "\\"
						+ newContentFolders.get(i) + "\\"
						+ newContentFolders.get(i) + ".js";

				boolean fileExist = textService
						.checkIfFileExist(newContenetfilePath);
				String newfileContent = textService.getTextFromFile(
						newContenetfilePath, Charset.defaultCharset());

				String currentFileContent = textService.getTextFromFile(
						currentContenetfilePath, Charset.defaultCharset());

				String[] newContentsegments = textService
						.getHtmlElementFromHtmlFile("//span[@class='segment']",
								newfileContent);
				newContentsegments = textService
						.trimLowerCaseAndRemoveChars(newContentsegments);

				String[] currentContentsegments = textService
						.getHtmlElementFromHtmlFile("//span[@class='segment']",
								currentFileContent);
				currentContentsegments = textService
						.trimLowerCaseAndRemoveChars(currentContentsegments);

				// check number of segments
				if (testResultService.assertEquals(newContentsegments.length,
						currentContentsegments.length,
						"Length of segments is not the same")) {
					// compare content of segments
					for (int j = 0; j < newContentsegments[j].length(); j++) {
						// split segment into array
						String[] newSegmentWords = textService
								.splitStringToArray(newContentsegments[j],
										"\\s+");
						String[] currentSegmentWords = textService
								.splitStringToArray(currentContentsegments[j],
										"\\s+");

						if (testResultService.assertEquals(
								newSegmentWords.length,
								currentSegmentWords.length,
								"Number of words in segment text mismatch. ")) {
							// compare text of sub-segments
							boolean wordsMatch = true;
							String newWord = null;
							String currentWord = null;
							for (int k = 0; k < currentSegmentWords.length; k++) {

								if (!currentSegmentWords[k]
										.equals(newSegmentWords[k])) {
									wordsMatch = false;
									newWord = newSegmentWords[k];
									currentWord = currentSegmentWords[k];
								}

							}
							if (wordsMatch == true) {
								// add folder and sub segment to results list
								String[] str = new String[] {
										newContentFolders.get(i),
										String.valueOf(j), "Passed" };
								passedTests++;
								testResults.add(str);
							} else {
								String[] str = new String[] {
										newContentFolders.get(i),
										String.valueOf(j),
										"Failed",
										"Words are not the same: word in new content is: "
												+ newWord
												+ " while word in current content is: "
												+ currentWord };
								wordsMisMatch++;
								testResults.add(str);
							}

						} else {
							// add folder name, and sub segment number to test
							// results
							String[] str = new String[] {
									newContentFolders.get(i),
									String.valueOf(j),
									"Failed",
									"Number of words in new content sub-segment("
											+ newSegmentWords.length
											+ ") is not the same as number of words in current text("
											+ currentSegmentWords.length
											+ ").: "
											+ textService.printStringArray(
													newSegmentWords, " ")
											+ " while text in current segment is: "
											+ textService.printStringArray(
													currentSegmentWords, " ") };
							numberOfWordsInSubSegmentsNotTheSame++;
							testResults.add(str);

						}
						;

					}
				} else {
					String[] str = new String[] {
							newContentFolders.get(i),
							null,
							"Failed",
							"Number of segments is not the same. Number of segments in new content is: "
									+ newContentsegments.length
									+ " while number of segment in current content is: "
									+ currentContentsegments.length };
					numberOfSegmentsNotTheSame++;
					testResults.add(str);

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Problem found while checking folder: "
						+ newContentFolders.get(i));
				testResultService
						.addFailTest("Problem found while checking folder: "
								+ newContentFolders.get(i) + " " + e.toString());
			}

		}
		testResults.add(new String[] { "Folder scanned:"
				+ newContentFolders.size() });
		testResults.add(new String[] { "Passed sub-segments:"
				+ passedTests });
		testResults.add(new String[] { "Segments with diffrent number of words:"
				+ numberOfSegmentsNotTheSame });
		testResults.add(new String[] { "Sub-segment with diffrent number of words:"
				+ numberOfWordsInSubSegmentsNotTheSame });
		testResults.add(new String[] { "Words missmatch:"
				+ wordsMisMatch });
		
		
		
		textService.writeArrayistToCSVFile("files/csvFiles/compareResults.csv",
				testResults);
	}
}
