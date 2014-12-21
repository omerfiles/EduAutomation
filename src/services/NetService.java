package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import jcifs.smb.NtlmAuthenticator;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.hamcrest.core.IsInstanceOf;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.primitives.Chars;

import jsystem.framework.system.SystemObjectImpl;
import junit.framework.Assert;

@Service
public class NetService extends SystemObjectImpl {

	public NetService() {
	};

	private Map<String, String> map;

	public Document getXmlFromString(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(
					new StringReader(xmlString)));
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("Test failed during getting xml form sring");
		} finally {

		}
		return document;
	}

	public Document getXmlFromFile(String filePath) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			// Assert.fail("Test failed during getting xml form sring");
		} finally {

		}
		return document;
	}

	public Document getXMLDocFromSQLXML(SQLXML sqlxml) throws Exception {
		InputStream binaryStream = sqlxml.getBinaryStream();
		DocumentBuilder parser = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document result = parser.parse(binaryStream);
		return result;
	}

	public List<String[]> getListFromXmlNode(Document document, String Xpath)
			throws Exception {

		List<String[]> arrList = new ArrayList<String[]>();
		// XPathFactory xPathfactory = XPathFactory.newInstance();
		// XPath xpath = xPathfactory.newXPath();
		// XPathExpression expr = xpath.compile(Xpath);
		// NodeList nl=(NodeList)expr.evaluate(document,XPathConstants.NODESET);
		NodeList nl = document.getChildNodes();

		NodeList childNodes = nl.item(0).getChildNodes();

		NodeList detailsNodeList = childNodes.item(2).getChildNodes();
		System.out.println("length of details node list is: "
				+ detailsNodeList.getLength());
		for (int j = 0; j < detailsNodeList.getLength(); j++) {
			if (detailsNodeList.item(j).getAttributes() != null
					&& detailsNodeList.item(j).getAttributes()
							.getNamedItem("code") != null) {
				String[] arr = new String[4];
				arr[0] = detailsNodeList.item(j).getAttributes()
						.getNamedItem("code").toString();
				// arr[1] = detailsNodeList.item(j).getAttributes()
				// .getNamedItem("structure").toString();
				arr[1] = detailsNodeList.item(j).getChildNodes().item(0)
						.getAttributes().getNamedItem("length").toString();
				arr[1] = arr[1].replace("length=", "");
				arr[1] = arr[1].replace("\"", "");
				arr[2] = detailsNodeList.item(j).getChildNodes().item(0)
						.getAttributes().getNamedItem("offset").toString();
				arr[2] = arr[2].replace("offset=", "");
				arr[2] = arr[2].replace("\"", "");
				arr[3] = detailsNodeList.item(j).getChildNodes().item(0)
						.getTextContent();
				arr[3] = arr[3].replace("*", "");
				arrList.add(arr);
				// detailsNodeList.item(j).getChildNodes().item(1).getAttributes().getNamedItem("offset");
			}
		}

		return arrList;
	}

	public NodeList getNodesFromXml(String expression, Document document)
			throws Exception {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
				document, XPathConstants.NODESET);
		return nodeList;

	}

	public List<String[]> getListFromJson(String jsonString,
			String firstArrayName, String secondArrayName, String[] names)
			throws Exception {
		List<String[]> arrList = new ArrayList<String[]>();
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONArray jsonArray = jsonObject.getJSONArray(firstArrayName);
		for (int i = 0; i < jsonArray.length(); i++) {
			String[] str = new String[names.length];
			JSONObject detailsObj = jsonArray.getJSONObject(i);
			JSONArray feedbacks = detailsObj.getJSONArray(secondArrayName);
			for (int j = 0; j < feedbacks.length(); j++) {

				JSONObject feedbackObject = feedbacks.getJSONObject(j);
				str = new String[names.length];
				for (int y = 0; y < names.length; y++) {

					str[y] = feedbackObject.getString(names[y]);

				}
				if (str[0] != null) {
					arrList.add(str);
				}

			}

		}

		return arrList;
	}

	public NodeList getNodeChilds(Node node) throws Exception {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {

		}
		return nl;

	}

	public static void listAllAttributes(Element element) {

		System.out
				.println("List attributes for node: " + element.getNodeName());

		// get a map containing the attributes of this node
		NamedNodeMap attributes = element.getAttributes();

		// get the number of nodes in this map
		int numAttrs = attributes.getLength();

		for (int i = 0; i < numAttrs; i++) {
			Attr attr = (Attr) attributes.item(i);

			String attrName = attr.getNodeName();
			String attrValue = attr.getNodeValue();

			System.out.println("Found attribute: " + attrName + " with value: "
					+ attrValue);

		}
	}

	public void compareLists(List<String[]> listA, List<String[]> listB)
			throws Exception {

	}

	public void sentHttpRequest(String request, String requestMethod)
			throws Exception {
		report.report("Request is: " + request);
		URL url = new URL(request);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod(requestMethod);
		OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
		// System.out.println(httpCon.getResponseCode()) ;
		report.report("Get response code: " + httpCon.getResponseCode());
		System.out.println(httpCon.getResponseMessage());
		report.report("Get response message: " + httpCon.getResponseMessage());
		out.close();

	}

	public void sentHttpRequest(String request) throws Exception {
		sentHttpRequest(request, "POST");

	}

	public void updateSlaveStatus(String slaveName, String text)
			throws IOException, UnsupportedEncodingException {
		// String statusFolder = "\\\\10.1.0.66\\slavesStatus\\";
		String smbFolder = "smb://10.1.0.83/slavesStatus/" + slaveName + ".txt";
		// NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
		// "automation", "tamar2010");
		NtlmPasswordAuthentication auth = getAuth();
		SmbFile smbFile = new SmbFile(smbFolder, auth);

		SmbFileOutputStream outputStream = new SmbFileOutputStream(smbFile);
		outputStream.write(text.getBytes());

		// if file does not exist, create file
		// File file = new File(statusFolder + slaveName + ".txt");
		// if (file.exists()) {
		// System.out.println("file exist");
		// PrintWriter writer = new PrintWriter(statusFolder + slaveName
		// + ".txt", "UTF-8");
		//
		// writer.print(text);
		// writer.close();
		// } else {
		// System.out.println("file dose not exist. creating file");
		// PrintWriter writer = new PrintWriter(statusFolder + slaveName
		// + ".txt", "UTF-8");
		// writer.print(text);
		// writer.close();
		// }
		// if file exist, update status

	}

	public boolean checkAllSlaveStatus() throws Exception {
		// read all files from \\\\10.1.0.66\\slavesStatus\\ and check that all
		// has "true" in them
		TextService textService = new TextService();
		boolean status = false;
		// File folder = new File("\\\\10.1.0.66\\slavesStatus\\");
		String path = "smb://10.1.0.83/slavesStatus/";
		String[] listOfFiles;
		StringBuilder builder = null;
		NtlmPasswordAuthentication auto = getAuth();
		SmbFile smbFile = new SmbFile(path, auto);
		listOfFiles = smbFile.list();
		boolean[] slavesStatus = new boolean[listOfFiles.length];
		while (checkBooleanArr(slavesStatus) == false) {
			for (int i = 0; i < listOfFiles.length; i++) {
				// read status from file
				// String text = textService.getTextFromFile(
				// listOfFiles[i].getAbsolutePath(),
				// Charset.defaultCharset());
				// System.out.println("Text is: "+text);
				// SmbFileInputStream fileInputStream=new
				// SmbFileInputStream(path+listOfFiles[i]);

				// String text=fileInputStream.toString();

				try {
					builder = new StringBuilder();
					SmbFile file = new SmbFile(path + listOfFiles[i], auto);
					builder = readFileContent(file, builder);
				} catch (Exception e) {

				}
				String text = builder.toString();

				System.out.println("Text is:" + text);
				if (text.equals("ready")) {
					System.out
							.println("Slave: " + listOfFiles[i] + " is ready");
					slavesStatus[i] = true;
				} else {
					System.out.println("slave " + listOfFiles[i]
							+ " not ready. Sleeping for 5 seconds");
					Thread.sleep(5000);
					slavesStatus[i] = false;
				}
			}
		}

		System.out.println("All servers are ready");
		return true;
	}

	private StringBuilder readFileContent(SmbFile file, StringBuilder builder)
			throws IOException {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(
				new SmbFileInputStream(file)));
		String lineReader = null;
		try {
			while ((lineReader = reader.readLine()) != null) {
				builder.append(lineReader);
			}
		} catch (Exception e) {

		} finally {
			reader.close();
		}
		return builder;
	}

	boolean checkBooleanArr(boolean[] arr) {
		boolean arrStatus = true;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == false) {
				arrStatus = false;
			}
		}
		return arrStatus;
	}

	public NtlmPasswordAuthentication getAuth() {
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
				"automation", "tamar2010");
		return auth;
	}
}
