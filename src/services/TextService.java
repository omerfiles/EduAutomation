package services;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jsystem.framework.system.SystemObjectImpl;

@Service
public class TextService extends SystemObjectImpl {

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
}
