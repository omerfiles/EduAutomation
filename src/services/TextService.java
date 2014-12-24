package services;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jcifs.ntlmssp.NtlmMessage;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.io.FileUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import au.com.bytecode.opencsv.CSVWriter;
import jsystem.framework.system.SystemObjectImpl;

@Service
public class TextService extends SystemObjectImpl {

	@Autowired
	protected NetService netService;

	Scanner scanner;

	public TextService() {
	}

	public String getTextFromFile(String filePath, Charset encoding)
			throws Exception {
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(filePath));

		} catch (IOException e) {
			report.report(e.toString());

		} finally {

		}
		return new String(encoded, encoding);
	}

	public String getFirstCharsFromCsv(int charsNumber, String filePath)
			throws Exception {
		String str = getTextFromFile(filePath, Charset.defaultCharset());
		str = str.substring(0, charsNumber);
		return str;
	}

	public List<String[]> getStr2dimArrFromCsv(String filePath)
			throws Exception {
		List<String[]> list = new ArrayList<String[]>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] str = null;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				str = line.split(cvsSplitBy);
				list.add(str);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			return list;
		}

	}

	public List<String> getStrListFromCsv(String filePath, int colIndex)
			throws Exception {
		List<String> list = new ArrayList<String>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] str = null;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				str = line.split(cvsSplitBy);
				list.add(str[colIndex]);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {

		}
		br.close();
		return list;

	}

	public String[] getStrArrayFromCsv(String filePath, int index)
			throws Exception {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] strArr = new String[700];
		String[] str = null;
		int i = 0;
		ArrayList<String> list = null;
		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				str = line.split(cvsSplitBy);
				strArr[i] = str[index];
				// System.out.println(strArr[i]);
				i++;

			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			return strArr;
		}
	}

	public void setClipboardText(String text) throws Exception {
		StringSelection selection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	public String[] splitStringToArray(String str, String ignorChars) {
		String[] result = null;
		try {
			// System.out.println("Splitting string: " + str + " to array."
			// + System.currentTimeMillis());
			result = str.split("(" + ignorChars + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return result;
	}

	public int[] splitStringToIntArray(String str, String ignorChars) {
		String[] result = null;
		try {
			// System.out.println("Splitting string: " + str + " to array."
			// + System.currentTimeMillis());
			result = str.split("(" + ignorChars + ")");
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		int[] intArr = new int[result.length];
		for (int i = 0; i < result.length; i++) {
			intArr[i] = Integer.parseInt(result[i]);
		}
		return intArr;
	}

	public String[] splitStringToArray(String str) {
		return splitStringToArray(str, "?!^");
	}

	public void typeText(String text) throws Exception {
		Robot robot = new Robot();
		byte[] bytes = text.getBytes();
		for (byte b : bytes) {
			int code = b;
			// keycode only handles [A-Z] (which is ASCII decimal [65-90])
			if (code > 96 && code < 123) {
				code = code - 32;
				robot.delay(100);
				robot.keyPress(code);
				robot.keyRelease(code);
			} else if (code == 58) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (code == 92) {
				robot.keyPress(KeyEvent.VK_BACK_SLASH);
				robot.keyRelease(KeyEvent.VK_BACK_SLASH);
			} else if (code == 46) {
				robot.keyPress(KeyEvent.VK_PERIOD);
				robot.keyRelease(KeyEvent.VK_PERIOD);
			} else if (code == 45) {
				robot.keyPress(KeyEvent.VK_MINUS);
				robot.keyRelease(KeyEvent.VK_MINUS);
			}

		}
	}

	public boolean searchForTextInFile(String text, URL url) throws Exception {
		report.report("Text to find is: " + text);
		boolean found = false;
		scanner = new Scanner(url.openStream());
		try {
			while (scanner.hasNextLine()) {
				String lineTex = scanner.nextLine();
				// System.out.println(scanner.nextLine());
				// report.report(scanner.nextLine());
				if (lineTex.contains(text)) {
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			report.report("Text: " + text + " was not found");
			e.printStackTrace();
		}
		if (found == false) {
			report.report("Text: " + text + " was not found");
		}
		return found;

	}

	public String resolveAprostophes(String item) {
		if (!item.contains("'")) {
			return "'" + item + "'";
		}
		StringBuilder finalString = new StringBuilder();
		finalString.append("concat('");
		finalString.append(item.replace("'", "',\"'\",'"));
		finalString.append("')");
		return finalString.toString();
	}

	public String printStringArray(String[] str) {
		return printStringArray(str, "|");
	}

	public String printStringArray(String[] str, String seperator) {
		String output = "";
		for (int i = 0; i < str.length; i++) {
			output = output + seperator + str[i];
		}
		System.out.println("Strings are:" + output);
		report.report("String are:" + output);
		return output;
	}

	public String getLineFromTextFile(File file, String textToFind)
			throws IOException {
		String textLine = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.contains(textToFind)) {
				textLine = line;
				// System.out.println("line found:" + line);
				break;
			}

		}
		br.close();
		if (textLine == null) {
			System.out.println("Line with text: " + textToFind
					+ " was not found");
		}

		return textLine;
	}

	public String[] getHtmlElementFromHtmlFile(String xpathToFind,
			String fileContent) throws ParserConfigurationException,
			XPathExpressionException, XPatherException {
		TagNode tagNode = new HtmlCleaner().clean(fileContent);
		Object[] segments = tagNode.evaluateXPath(xpathToFind);
		String[] arr = new String[segments.length];
		for (int i = 0; i < segments.length; i++) {
			TagNode segment = (TagNode) segments[i];
			System.out.println(segment.getText());
			arr[i] = segment.getText().toString();
			System.out.println();
		}
		return arr;
	}

	public String[] getStringsfromXmlByXpath(String xpathToFind,
			Document document) throws Exception {
		String[] arr = null;

		List<String[]> segments = netService.getListFromXmlNode(document,
				xpathToFind);

		return arr;
	}

	public String getFileContent(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkIfFileExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("File: " + path + " was not found");
			return false;
		} else
			System.out.println("File exist");
		return true;

	}

	public String[] trimLowerCaseAndRemoveChars(String[] strArr) {
		for (int i = 0; i < strArr.length; i++) {
			strArr[i] = strArr[i].trim();
			strArr[i] = strArr[i].toLowerCase();
			strArr[i] = strArr[i].replace("?", "");
			strArr[i] = strArr[i].replace("!", "");
			strArr[i] = strArr[i].replace(",", "");
			strArr[i] = strArr[i].replace(".", "");
			strArr[i] = strArr[i].replace("-", " ");
			strArr[i] = strArr[i].replace(";", "");
			strArr[i] = strArr[i].replace("\\", "");
			strArr[i] = strArr[i].replace("\"", "");
			strArr[i] = strArr[i].trim();
		}

		return strArr;
	}

	public void writeArrayistToCSVFile(String path, List<String[]> list)
			throws IOException {
		String csv = path;
		CSVWriter writer = new CSVWriter(new FileWriter(csv));
		writer.writeAll(list);

		writer.close();
	}

	public void writeListToSmbFile(String path, List<String> list,
			NtlmPasswordAuthentication auth) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		String nl = System.getProperty("line.separator");
		for (String element : list) {
//		    out.writeUTF(element);
		    out.writeBytes(element);
		    out.writeBytes(nl);
		}
		byte[] bytes = baos.toByteArray();
		
		
		SmbFile smbFile = new SmbFile(path, auth);
		SmbFileOutputStream output = new SmbFileOutputStream(smbFile);
		output.write(bytes);
		

	}

	public List<String[]> getListFromLogEntries(LogEntries logEntries) {
		return getListFromLogEntries(logEntries, null, true);
	}

	public List<String[]> getListFromLogEntries(LogEntries logEntries,
			String filter, boolean useFilter) {
		List<String[]> strList = new ArrayList<String[]>();
		for (LogEntry entry : logEntries) {

			String[] str = new String[] { entry.getMessage(),
					String.valueOf(entry.getTimestamp()),
					entry.getLevel().toString() };
			if (filter != null && entry.getMessage().contains(filter)
					|| useFilter == false && filter == null) {
				strList.add(str);
			}
		}
		return strList;
	}

	public void copyFileToFolder(String sourcePath, String destinationPath)
			throws IOException {
		try {
			FileUtils.copyFileToDirectory(new File(sourcePath), new File(
					destinationPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTextForText(int chars) {

		String lipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
		return lipsum.substring(0, chars);
	}
}
