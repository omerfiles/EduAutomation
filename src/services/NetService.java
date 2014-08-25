package services;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import org.hamcrest.core.IsInstanceOf;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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

	public NodeList getNodesFromXml(String expression,Document document) throws Exception {
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
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

}
