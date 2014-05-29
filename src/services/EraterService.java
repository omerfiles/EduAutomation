package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.system.SystemObjectImpl;
import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EraterService extends SystemObjectImpl {

	private static final String FEEDBACK_CODES_CSV = "files/csvFiles/EraterFeedbackCodes.csv";

	@Autowired
	private TextService textService;

	@Autowired
	private DbService dbService;
	@Autowired
	private NetService netService;

	List<String[]> feedbackList = new ArrayList<String[]>();

	public EraterService() {

	}

	public List<String[]> removeHiddenCodesAndConvertToFeedbackCodes(
			List<String[]> list) throws Exception {
		// this method will get the Array list created from the XML
		loadList();
		// loop on the list
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);
			str[0] = str[0].replace("code=", "");
			str[0] = str[0].replace("\"", "");
			// if the code is hidden, remove it
			if (checkIfCodeValid(str[0]) == false) {
				System.out.println("removing unused code=" + str[0]);
				list.remove(i);
				i = i - 1;
				continue;
			}
			// convert the code to feedback
			// str[3] is the suggestion id from ETS
			str[0] = getErateFeedbackByCode(str[0], str[3]);
			list.set(i, str);
		}
		return list;
	}

	public void loadList() throws Exception {
		List<String[]> list = textService
				.getStr2dimArrFromCsv(FEEDBACK_CODES_CSV);
		feedbackList = list;
	}

	public String getErateFeedbackByCode(String code, String suggestionId)
			throws Exception {
		String feedback = null;

		for (int i = 0; i < feedbackList.size(); i++) {
			String[] arr = feedbackList.get(i);
			if (suggestionId.isEmpty()
					|| suggestionId.matches(".*\\d.*") == false) {
				if (arr[0].equals(code) && arr[3].equals("1")) {
					feedback = arr[1];
					break;
				}
			} else {
				int sugggestionid=Integer.valueOf(suggestionId);
				sugggestionid=sugggestionid+1;
				if (arr[0].equals(code) && arr[2].equals(String.valueOf(sugggestionid))) {
					feedback = arr[1];
					break;
				}
			}
		}
		return feedback;
	}

	public boolean checkIfCodeValid(String code) throws Exception {
		boolean isValid = true;
		for (int i = 0; i < feedbackList.size(); i++) {
			String[] arr = feedbackList.get(i);
			if (arr[0].equals(code) && arr[3].equals("0")) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	public void printArrayList(List<String[]> list) {
		for (int i = 0; i < list.size(); i++) {
			String[] arr = list.get(i);
			System.out.println("printing the array");
			for (int j = 0; j < arr.length; j++) {

				System.out.println(arr[j]);

			}
			report.report(arr[0] + "," + arr[1] + "," + arr[2]);
		}
	}

	public List<String[]> sortArrayList(List<String[]> list) {
		Collections.sort(list, new Comparator<String[]>() {

			@Override
			public int compare(String[] o1, String[] o2) {
				int compareInt = o1[0].compareTo(o2[0]);
				if (compareInt == 0) {

					for (int j = 1; j < o1.length; j++) {
						compareInt = o1[j].compareTo(o2[j]);
						if (compareInt == 0) {
							break;
						}
					}
				}
				System.out.println();
				return compareInt;
			}
		});
		return list;
	}

	public boolean compareJsonAndXmlByWritingId(String writingId) throws Exception {
		String sqlxml = dbService
				.getStringFromQuery("select EraterXML from Erater where writingId="
						+ writingId,10);
		report.report("RAW XML: " + sqlxml);
		List<String[]> xmlList = netService.getListFromXmlNode(
				netService.getXmlFromString(sqlxml), "/WAT:DetailInfo");
		String jsonStr = dbService
				.getStringFromQuery("select EraterJson from Erater where writingId="
						+ writingId,10);
		report.report("RAW JSON: " + jsonStr);
		List<String[]> jsonList = netService.getListFromJson(jsonStr,
				"sections", "details", new String[] { "feedback", "length",
						"offset" });
		report.startLevel("Printing json list", EnumReportLevel.CurrentPlace);
		printArrayList(jsonList);
		report.stopLevel();
		// jsonList = sortArrayList(jsonList);
		xmlList = removeHiddenCodesAndConvertToFeedbackCodes(xmlList);
		// xmlList = sortArrayList(xmlList);

		report.startLevel("Printing xml list", EnumReportLevel.CurrentPlace);
		printArrayList(xmlList);
		report.stopLevel();

		report.report("json list length is: " + jsonList.size());
		report.report("xml list length is: " + xmlList.size());
		return AssertJsonAndXmlLists(xmlList, jsonList);
//		Assert.assertTrue("Some unmatches found",
//				AssertJsonAndXmlLists(xmlList, jsonList));
		// Assert.assertEquals("Json list and xml list are not in the same size",
		// jsonList.size(), xmlList.size());
	}
	
	

	public boolean AssertJsonAndXmlLists(List<String[]> xmlList,
			List<String[]> jsonList) {
		boolean unmatchFound = true;
		outerloop: for (int i = 0; i < xmlList.size(); i++) {
			String[] xmlStrArr = xmlList.get(i);
			boolean found = false;
			innerloop: for (int j = 0; j < jsonList.size(); j++) {
				String[] jsonStrArr = jsonList.get(j);
				if (jsonStrArr[0].equals(xmlStrArr[0])
						&& jsonStrArr[1].equals(xmlStrArr[1])
						&& jsonStrArr[2].equals(xmlStrArr[2])) {
					report.report("Match found for entry: " + xmlStrArr[0]
							+ " " + xmlStrArr[1] + " " + xmlStrArr[2],
							Reporter.PASS);
					found = true;
					break innerloop;
				}

			}
			if (found == false) {
				unmatchFound = false;
				report.report("entry not found: " + xmlStrArr[0] + " "
						+ xmlStrArr[1] + " " + xmlStrArr[2],
						Reporter.WARNING);
			}
		}
		return unmatchFound;
	}

	public String getWritingIdByUserIdAndTextStart(String userId,
			String textStart) throws Exception {
		String sql = "  select * from writing where UserId='" + userId
				+ "' and EssayText like '" + textStart + "%'";
		String result = dbService.getStringFromQuery(sql);
		return result;
	}
	public void checkWritingIdsByCSV()throws Exception{
		 List<String[]>writingIds=textService.getStr2dimArrFromCsv("files/csvFiles/writingIds.csv");
		 for(int i=0;i<writingIds.size();i++){
			 
			 compareJsonAndXmlByWritingId(writingIds.get(i)[0]);
		 }
	}
	

}
