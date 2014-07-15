package services;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import jsystem.framework.system.SystemObjectImpl;

@Service
public class TextService extends SystemObjectImpl {

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
			return new String(encoded, encoding);
		}
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

	public String[] splitStringToArray(String str) {
		String[] result = str.split("(?!^)");
		return result;
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
		report.report("Text to find is: "+text);
		boolean found = false;
		scanner = new Scanner(url.openStream());
		try {
			while (scanner.hasNextLine()) {
				String lineTex=scanner.nextLine();
//				System.out.println(scanner.nextLine());
//				report.report(scanner.nextLine());
				if (lineTex.contains(text) ) {
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			report.report("Text: "+text +" was not found");
			e.printStackTrace();
		}
		if(found==false){
			report.report("Text: "+text +" was not found");
		}
		return found;

	}
}
