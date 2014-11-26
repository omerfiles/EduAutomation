package drivers;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Enums.AutoParams;
import Enums.ByTypes;
import Enums.TestRunnerType;
import services.DbService;
import services.TestResultService;
import services.TextService;
import jsystem.framework.report.Reporter;
import junit.framework.SystemTestCaseImpl;

@Service
public abstract class GenericWebDriver extends SystemTestCaseImpl {

	protected static final Logger logger = LoggerFactory
			.getLogger(GenericWebDriver.class);
	private String sutUrl = null;
	private String sutSubDomain = null;
	private String institutionnName = null;

	protected RemoteWebDriver webDriver;
	protected int timeout = 10;
	private String browserName;
	private boolean initialized;
	// private Config configuration;
	protected DbService dbService;
	protected String remoteMachine;
	protected boolean enableConsoleLog;

	@Autowired
	private services.Configuration configuration;

	private TextService textService;

	private services.Reporter reporter;

	TestResultService testResultService;

	protected String logsFolder;
	private boolean failureAdded;

	abstract public void init(String remoteUrl, String folderName)
			throws Exception;

	public void init(TestResultService testResultService) throws Exception {
		this.testResultService = testResultService;
		// String remoteMachine = null;

		// getting remote machine from maven command line
		// remoteMachine = System.getProperty("machine");
		// if (remoteMachine != null) {
		// System.out.println("got remote machine from maven cmd: "+remoteMachine);
		// }
		//
		// // getting remote machine from pom profile while executing tests
		// using
		// // maven/Jenkins
		//
		// if (remoteMachine == null) {
		// remoteMachine = configuration.getProperty("remote.machine");
		// }
		// if (remoteMachine == null) {
		// // getting remote machine from pom file
		// remoteMachine = System.getProperty("remote.machine");
		// }
		// if (remoteMachine == null) {
		// Assert.fail("Remote machine value is null");
		// }
		// if (enableConsoleLog == true) {
		// enableConsoleLog = true;
		// }
		textService = new TextService();
		remoteMachine = configuration.getAutomationParam(
				AutoParams.remoteMachine.toString(), "machine");
		setSutUrl(configuration.getAutomationParam(
				AutoParams.sutUrl.toString(), "suturl"));
		setSutSubDomain(configuration.getProperty("institution.name"));
		setInstitutionName(configuration.getProperty("institution.name"));

		init(remoteMachine, null);
	}

	public String getSutUrl() {
		return sutUrl;
	}

	public void setSutUrl(String sutUrl) {
		this.sutUrl = sutUrl;
	}

	public void getFucus() throws Exception {
		webDriver.switchTo().window(webDriver.getWindowHandle());
	}

	public void openUrl(String url) throws Exception, TimeoutException {

		try {
			webDriver.get(url);
		} catch (UnhandledAlertException e) {
			System.out.println(e.toString());
			closeAlertByAccept();
		} catch (Exception e) {
			System.out.println(e.toString());

		}

	}

	public void navigate(String url) throws Exception {

		webDriver.navigate().to(url);
	}

	// public void maximize() throws Exception {
	// webDriver.manage().window().maximize();
	// }

	public String getElementProperty(WebElement element, String propertyname)
			throws Exception {
		String value = null;
		value = element.getAttribute(propertyname);
		return value;
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory) throws Exception {
		return waitForElement(idValue, byType, timeout, isElementMandatory,
				null);
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory, String message)
			throws Exception {

		return waitForElement(idValue, byType, timeout, isElementMandatory,
				message, 1000);
	}

	@SuppressWarnings("finally")
	public WebElement waitForElement(String idValue, ByTypes byType,
			int timeout, boolean isElementMandatory, String message, int sleepMS)
			throws Exception {
		// System.out.println("waiting for element " + idValue + " by trpe "
		// + byType + " for " + timeout + " seconds");
		reporter.startLevel("waiting for element " + idValue + " by type "
				+ byType + " for " + timeout + " seconds");
		WebElement element = null;

		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, sleepMS);

			switch (byType) {
			case className:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.className(idValue)));
				element = webDriver.findElement(By.className(idValue));
				;
				break;
			case linkText:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.linkText(idValue)));
				element = webDriver.findElement(By.linkText(idValue));
				break;
			case id:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(idValue)));
				element = webDriver.findElement(By.id(idValue));
				;
				break;
			case name:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.name(idValue)));
				element = webDriver.findElement(By.name(idValue));
				;
				break;
			case partialLinkText:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.partialLinkText(idValue)));
				element = webDriver.findElement(By.partialLinkText(idValue));
				break;
			case xpath:
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(idValue)));
				element = webDriver.findElement(By.xpath(idValue));
				break;
			}

		} catch (UnhandledAlertException e) {
			closeAlertByAccept();
		} catch (NoSuchElementException e) {

			if (isElementMandatory == true) {
				Assert.fail("Exception when waiting for element:" + idValue
						+ ".| " + e.toString() + " Description: " + message);
				failureAdded = true;
			}

		} catch (TimeoutException e) {
			if (isElementMandatory == true) {
				testResultService.addFailTest("Element " + idValue
						+ " was not found after the specified timeout: "
						+ timeout + " Description of element:" + message);
				failureAdded = true;
			}
		} catch (InvalidElementStateException e) {
			testResultService.addFailTest("Element " + idValue
					+ " was in ivalid state " + " Description of element:"
					+ message);
			failureAdded = true;
		}

		catch (Exception e) {
			System.out.println("Unknown exception was found:" + e.toString()
					+ " while watining for element with description: "
					+ message);
		}

		finally {
			if (isElementMandatory == true && element == null) {
				// if (message != null) {
				// // System.out.println(message);
				// }
				if (failureAdded == false) {
					testResultService.addFailTest("Element: " + idValue
							+ " not found. Description:" + message);
				}
				Assert.fail("Element: " + idValue + " not found " + message);

			}
			return element;
		}
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			String message) throws Exception {
		return waitForElement(idValue, byType, timeout, true, message);
	}

	public WebElement waitForElement(String idValue, ByTypes byType)
			throws Exception {
		return waitForElement(idValue, byType, this.timeout, true, null);
	}

	public void waitForElementAndClick(String idValue, ByTypes byType)
			throws Exception {
		waitForElement(idValue, byType, timeout, true, null).click();
	}

	public void waitForElementAndSendEnter(String idValue, ByTypes byType)
			throws Exception {
		waitForElement(idValue, byType, timeout, true, null).sendKeys(
				Keys.ENTER);
	}

	public WebElement waitForElement(String idValue, ByTypes byType,
			boolean isElementMandatory, int timeout) throws Exception {
		return waitForElement(idValue, byType, timeout, isElementMandatory,
				null);
	}

	public void sendKey(Keys keys) throws Exception {
		webDriver.switchTo().activeElement().sendKeys(keys);
	}

	public void sendKey(String keys) throws Exception {
		webDriver.switchTo().activeElement().sendKeys(keys);
	}

	public void checkElementEnabledAndClickable(String xpath) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			webDriver.findElement(By.xpath(xpath));
		} catch (Exception e) {

			// Assert.fail("Element not found or element is not Clickable");
			testResultService
					.addFailTest("Element not found or element is not Clickable");
		}
	}

	public void waitUntilElementExist(String idValue, ByTypes byType,
			int timeOut) throws Exception {

		boolean exist = false;
		int elapsedTime = 0;
		while (elapsedTime < timeOut) {
			try {
				WebDriverWait wait = new WebDriverWait(webDriver, timeOut, 200);
				wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.xpath(idValue)));
				webDriver.findElement(By.xpath(idValue));
				exist = true;
				break;
			} catch (Exception e) {
				Thread.sleep(100);
			}

		}

	}

	public void assertTextBy(String idValue, String byType, String text)
			throws Exception {
		String current = null;
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 2000);
			if (byType == "xpath") {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(idValue)));
				element = webDriver.findElement(By.xpath(idValue));
			} else if (byType == "id") {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(idValue)));
				element = webDriver.findElement(By.id(idValue));
			}
			current = element.getText();
			System.out.println("text to compare was: " + current);
			// Assert.assertEquals(text, current);
			testResultService.assertEquals(text, current);
			// Assert.assertTrue("Check text failed. Actual text was: " +
			// current + ". Text to compare was: " + text,
			// current.equals(text));
		} catch (Exception e) {
			// Assert.fail("Text assertion failed. Xpath was: " + idValue
			// + " . Text to assert was: " + text + ". Actual text was: "
			// + current);
		}

	}

	public void quitBrowser() throws Exception {
		System.out.println("Quit broswer");
		if (initialized == true) {
			try {

				// deleteCookiesAndCache();

				webDriver.quit();

			} catch (Exception e) {
				System.out.println("Closing " + this.getBrowserName()
						+ "failed. " + e.toString());
			}
		}

	}

	public void closeBrowser() throws Exception {
		try {

			deleteCookiesAndCache();

			webDriver.close();

		} catch (Exception e) {
			System.out.println("Closing " + this.getBrowserName() + "failed. "
					+ e.toString());
		}
	}

	public void refresh() throws Exception {
		webDriver.navigate().refresh();
	}

	public void deleteCookiesAndRefresh() throws Exception {

		try {
			webDriver.manage().deleteAllCookies();
			webDriver.navigate().refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out
					.println("Failed when trying to delete cookies and refresh");
			e.printStackTrace();
		}
	}

	public void deleteCookiesAndCache() throws Exception {
		webDriver.manage().deleteAllCookies();

	}

	public void sendKey(Keys keys, int iterations) throws Exception {
		for (int i = 0; i < iterations; i++) {
			sendKey(keys);

		}
	}

	public void swithcToFrameAndSendKeys(String xpathExpression, String keys,
			String frameId) throws Exception {
		swithcToFrameAndSendKeys(xpathExpression, keys, false, frameId);
	}

	public void swithcToFrameAndSendKeys(String xpathExpression, String keys,
			boolean clear, String frameId) throws Exception {
		String currentWindow = webDriver.getWindowHandle();
		webDriver.switchTo().frame(frameId);
		// webDriver.findElement(By.xpath(xpathExpression)).click();
		// webDriver.findElement(By.xpath(xpathExpression)).sendKeys(keys);

		WebElement element = webDriver.findElement(By.xpath(xpathExpression));

		element.click();
		if (clear == true) {
			element.clear();
		}
		//
		// sendKey(keys);
		// element.sendKeys(keys);
		element.sendKeys(keys);
		webDriver.switchTo().window(currentWindow);
	}

	public String switchToFrame(String frameName) throws Exception {
		String currentWindow = null;
		try {
			currentWindow = webDriver.getWindowHandle();
			WebDriverWait wait = new WebDriverWait(webDriver, timeout);
			wait.until(ExpectedConditions
					.frameToBeAvailableAndSwitchToIt(frameName));
		} catch (TimeoutException e) {
			// Assert.fail("Frame waw not found");
			System.out.println(e.toString());
			testResultService.addFailTest("Frame was not found", true);
		} finally {

		}
		return currentWindow;
	}

	public String switchToFrame(WebElement element) throws Exception {
		String currentWindow = webDriver.getWindowHandle();
		webDriver.switchTo().frame(element);
		return currentWindow;
	}

	public String switchToFrame(int index) throws Exception {
		String currentWindow = webDriver.getWindowHandle();
		webDriver.switchTo().frame(index);
		return currentWindow;
	}

	public void getFrameNames() throws Exception {
		webDriver.switchTo().frame(1);
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("feedform_user_message")));
		WebElement element = webDriver.findElement(By
				.id("feedform_user_message"));
		element.sendKeys("Test Frame");

		// WebElement element=
		// webDriver.findElement(By.xpath("//input[@name='publish',@value='send',@type='submit']"));

		element.click();

	}

	public String switchToPopUp(int index) throws Exception {
		String parentWindowhandle = webDriver.getWindowHandle();
		WebDriver popup = null;
		Set<String> openWindowsList = webDriver.getWindowHandles();
		String popupWindowHandle = null;
		int i = 0;
		for (String windowHandle : openWindowsList) {
			if (!windowHandle.equals(parentWindowhandle))
				if (i == index) {
					popupWindowHandle = windowHandle;
					break;
				}
			i++;
		}
		webDriver.switchTo().window(popupWindowHandle);
		return parentWindowhandle;
	}

	public String switchToPopup() throws Exception {
		String parentWindowhandle = webDriver.getWindowHandle();
		WebDriver popup = null;
		Set<String> openWindowsList = webDriver.getWindowHandles();
		String popupWindowHandle = null;
		for (String windowHandle : openWindowsList) {
			if (!windowHandle.equals(parentWindowhandle))
				popupWindowHandle = windowHandle;
		}
		webDriver.switchTo().window(popupWindowHandle);
		return parentWindowhandle;
	}

	public void switchToMainWindow(String windowName) throws Exception {
		webDriver.switchTo().window(windowName);

	}

	public String switchToNewWindow() throws Exception {
		return switchToNewWindow(1);
	}

	public String switchToNewWindow(int windowId) throws Exception {

		Set<String> winhandles = webDriver.getWindowHandles();
		List<String> windows = new ArrayList<String>();
		windows.addAll(winhandles);
		System.out.println("before switch: " + webDriver.getWindowHandle());
		String oldWindow = webDriver.getWindowHandle();
		webDriver.switchTo().window(windows.get(windowId));
		System.out.println("after switch: " + webDriver.getWindowHandle());
		return oldWindow;

	}

	public void checkElementNotExist(String xpath, String message)
			throws Exception {
		boolean elementFound = false;
		try {
			WebElement element = waitForElement(xpath, ByTypes.xpath, false,
					timeout);
			if (element != null) {
				elementFound = true;
				printScreen(message);
			}

		} catch (Exception e) {
			System.out.println("Exceptin found during checkElementNotExist "
					+ e.toString());
		} finally {

			testResultService.assertTrue("Element with xpath " + xpath
					+ " found when it should not", elementFound == false);
		}
	}

	public void checkElementNotExist(String xpath) throws Exception {
		checkElementNotExist(xpath, "Element with xpath: " + xpath
				+ " was found when it should not");
	}

	public WebElement getElement(By by) {
		WebElement element = webDriver.findElement(by);
		return element;
	}

	public void switchToAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);

			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				webDriver.switchTo().alert();
			}
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
			Assert.fail("Alert not found");
		}
	}

	public void closeAlertByAccept() {
		try {
			System.out.println("Closing alert");
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				webDriver.switchTo().alert().accept();
			}
			System.out.println("Finished closing alert");
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			System.out.println("Alert was not found after 10 seconds");
		}

	}

	public void closeAlertByDismiss() {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);

		if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
			webDriver.switchTo().alert().dismiss();
		}
	}

	public String getAlertText(int timeOut) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeOut, 1000);
		String text = null;
		try {
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				Alert alert = webDriver.switchTo().alert();
				text = alert.getText();
			}
		} catch (Exception e) {
			System.out
					.println("Could not get alert text. might have timed out");
		}
		return text;
	}

	public String printScreen() throws Exception {
		return printScreen("", null);
	}

	public String printScreen(String message) throws Exception {
		return printScreen(message, null);
	}

	public String printScreen(String message, String level) throws Exception {
		// File screenShot;
		// log levels: 0-only failes tests, 1-save all screenshots

		String sig = dbService.sig();
		String path = null;
		String newFileName = null;
		try {
			WebDriver driver = webDriver;
			driver = new Augmenter().augment(driver);
			byte[] decodedScreenshot = org.apache.commons.codec.binary.Base64
					.decodeBase64(((TakesScreenshot) driver).getScreenshotAs(
							OutputType.BASE64).getBytes());
			TestRunnerType runner = getTestRunner();

			// If test is running using jenkins ci
			if (runner == TestRunnerType.CI) {

				newFileName = configuration.getProperty("logserver") + "\\\\"
						+ configuration.getProperty("screenshotFolder")
						+ "\\\\ScreenShot" + message.replace(" ", "") + sig
						+ ".png";
				System.out.println("File path is :" + newFileName);

				path = "http://"
						+ configuration.getProperty("logserver").replace("\\",
								"") + "/"
						+ configuration.getProperty("screenshotFolder")
						+ "/ScreenShot" + message.replace(" ", "") + sig
						+ ".png";

			} else if (runner == TestRunnerType.local) {
				newFileName = System.getProperty("user.dir") + "/log//current/"
						+ "ScreenShot" + message.replace(" ", "") + sig
						+ ".png";
				path = System.getProperty("user.dir") + "//" + "log//current//"
						+ "ScreenShot" + message.replace(" ", "") + sig
						+ ".png";
			}
			FileOutputStream fos = new FileOutputStream(new File(newFileName));
			fos.write(decodedScreenshot);
			fos.close();
			System.out.println(path);

		} catch (Exception e) {
			System.out.println("Taking the screenshot failed: " + e.toString());
		}

		return path;

	}

	public WebElement getTableTdByName(String tableId, String text)
			throws Exception {
		WebElement result = null;
		WebElement table = waitForElement(tableId, ByTypes.xpath);
		List<WebElement> allrows = table.findElements(By.tagName("tr"));
		for (WebElement row : allrows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (WebElement cell : cells) {
				System.out.println(cell.getText());
				if (cell.getText().contains(text)) {
					result = cell;
					break;
				}
			}
		}
		return result;
	}

	public void pasteTextFromClipboard(WebElement element) throws Exception {
		element.click();
		element.sendKeys(Keys.chord(Keys.CONTROL, "v"));

	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String getUrl() throws Exception {
		System.out.println(webDriver.getCurrentUrl());
		return webDriver.getCurrentUrl();

	}

	public DbService getDbService() {
		return dbService;
	}

	public void clickOnElement(WebElement td) {
		td.click();

	}

	public void quit() {
		// TODO Auto-generated method stub

	}

	public void switchToTopMostFrame() {
		webDriver.switchTo().defaultContent();
	}

	public void switchToMainWindow() throws Exception {
		webDriver.switchTo().window(
				(String) webDriver.getWindowHandles().toArray()[0]);

	}

	public void dragAndDropElement(WebElement from, WebElement to) {

		(new Actions(webDriver)).dragAndDrop(from, to).perform();
	}

	public void maximize() {
		webDriver.manage().window().maximize();
	}

	public WebElement getChildElementByXpath(WebElement element, String xpath) {
		WebElement chileElement = element.findElement(By.xpath(xpath));
		return chileElement;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getSutSubDomain() {
		return sutSubDomain;
	}

	public void setSutSubDomain(String sutSubDomain) {
		this.sutSubDomain = sutSubDomain;
	}

	public void setInstitutionName(String name) {
		this.institutionnName = name;
	}

	public String getIntitutionName() {
		return this.institutionnName;
	}

	public String getCssValue(WebElement element, String cssParam)
			throws Exception {
		String value = null;
		try {
			value = element.getCssValue(cssParam);
		} catch (Exception e) {
			testResultService.addFailTest("Element with css param " + cssParam
					+ " was null");
		}
		return value;
	}

	public void getElementLocation(WebElement element) {
		Point p = element.getLocation();
		System.out.println("X is: " + p.getX() + " and Y is: " + p.getY());
	}

	public void setElementSelected(WebElement element) throws Exception {
		element.click();

	}

	public TestRunnerType getTestRunner() {
		// if test is run in debug/development
		TestRunnerType testRunner = null;
		if (System.getProperty("remote.machine") != null) {
			testRunner = TestRunnerType.CI;
		} else if (System.getProperty("remote.machine") == null) {
			testRunner = TestRunnerType.local;
		}
		return testRunner;
	}

	public String getElementHTML(WebElement element) throws Exception {
		String html = element.getAttribute("innerHTML");
		System.out.println("Element HTMl is: " + html);

		return html;
	}

	public JSONObject getWebDriverJson() throws Exception {
		// String hub = "grid_server_host"; //IP or hostname of GRID

		int port = 4444; // port no.

		HttpHost host = new HttpHost("10.1.0.56", port);

		DefaultHttpClient client = new DefaultHttpClient();

		String url = host + "/grid/api/testsession?session=";

		URL session = new URL(url
				+ ((RemoteWebDriver) webDriver).getSessionId());

		BasicHttpEntityEnclosingRequest req;

		req = new BasicHttpEntityEnclosingRequest("POST",
				session.toExternalForm());

		org.apache.http.HttpResponse response = client.execute(host, req);

		JSONObject object = new JSONObject(EntityUtils.toString(response
				.getEntity()));

		// String proxyID = (String) object.get("proxyId");

		// String node = (proxyID.split("//")[1].split(":")[0]);

		return object;
	}

	public String getRemoteMachine() {
		return remoteMachine;
	}

	public void setRemoteMachine(String remoteMachine) {
		this.remoteMachine = remoteMachine;
	}

	public void clickOnElementWithOffset(WebElement element, int X_offset,
			int Y_offset) {
		Actions builder = new Actions(webDriver);
		Action action = builder.moveToElement(element, X_offset, Y_offset)
				.click().build();
		action.perform();
	}

	public boolean isEnableConsoleLog() {
		return enableConsoleLog;
	}

	public void setEnableConsoleLog(boolean enableConsoleLog) {
		this.enableConsoleLog = enableConsoleLog;
	}

	public LogEntries getConsoleLogEntries() {
		LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);

		// for (LogEntry entry : logEntries) {
		// System.out.println(entry.getMessage());
		// // do something useful with the data
		// }
		return logEntries;
	}

	public void hoverOnElement(WebElement element) throws Exception {
		Actions builder = new Actions(webDriver);
		builder.moveToElement(element).perform();

	}

	public void executeJsScript(String script) throws Exception {
		// ScriptEngineManager factory = new ScriptEngineManager();
		// // create a JavaScript engine
		// ScriptEngine engine = factory.getEngineByName("JavaScript");
		// engine.eval(script);
		// evaluate JavaScript code from String
		((JavascriptExecutor) webDriver).executeScript(script);

	}

	public void waitForJSFunctionToEnd(String function) {
		String script = "var callback = arguments[arguments.length - 1];"
				+ "callback(" + function + "());";

		try {
			webDriver.manage().timeouts()
					.setScriptTimeout(15, TimeUnit.SECONDS);
			((JavascriptExecutor) webDriver).executeAsyncScript(script);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean waitUntilElementClickable(String xpath, int timeout) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout);
		WebElement element = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath(xpath)));

		if (element == null) {
			testResultService.addFailTest("Element was not clickable after "
					+ timeout);
			return false;
		} else {
			return true;
		}

	}

	public boolean checNodeIsON(String nodeIp) throws Exception {
		System.out
				.println("Check the HUB usability - before test suite starts");
		String gridHub = "localhost";
		HtmlUnitDriver hubConsoleHtmlDriver = new HtmlUnitDriver();
		String hubStatusUrl = "http://" + gridHub + ":4444/grid/console";
		hubConsoleHtmlDriver.get(hubStatusUrl);
		String remoteProxyNodeXpath = "//p[@class='proxyid']";
		boolean nodeIsOn = false;

		try {
			List<WebElement> elements = hubConsoleHtmlDriver.findElements(By
					.xpath(remoteProxyNodeXpath));
			for (int i = 0; i < elements.size(); i++) {
				String nodeDetails = elements.get(i).getText();
				if (nodeDetails.contains(nodeIp)) {
					nodeIsOn = true;
					break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Node is not running");
		}
		return nodeIsOn;

	}

	public void addValuesToCookie(String cookieName, String value) {
		try {
			Cookie cookie = new Cookie(cookieName, value);
			webDriver.manage().addCookie(cookie);
			System.out.println("Cookie added");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RemoteWebDriver getWebDriver() {
		return webDriver;
	}

	public void setReporter(services.Reporter reporter) {
		this.reporter = reporter;
	}

	public void setTestResultService(TestResultService testResultService) {
		this.testResultService = testResultService;

	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue) throws Exception {
		boolean selected = false;
		{
			try {
				// TODO - add webdriver wait
				WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
				wait.until(ExpectedConditions.elementToBeClickable(By
						.id(comboboxName)));
				Select select = new Select(webDriver.findElement(By
						.id(comboboxName)));
				List<WebElement> options = select.getOptions();
				for (int i = 0; i < options.size(); i++) {
					if (options.get(i).getText().contains(optionValue)) {
						select.selectByIndex(i);
						System.out.println("option " + optionValue
								+ " selected");
						selected = true;
						break;

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				printScreen("problem selecting from combo box");
				testResultService.addFailTest(
						"problem selecting from combo box", true);
				e.printStackTrace();
			}
			System.out.println("Selected " + comboboxName + ": " + selected);
		}
		// TODO Auto-generated method stub

	}

	public void printConsoleLogs(String logFilter, boolean useFllter)
			throws Exception {
		try {
			textService = new TextService();
			LogEntries logEntries = getConsoleLogEntries();
			List<String[]> logList = textService.getListFromLogEntries(
					logEntries, logFilter, useFllter);
			String consoleLogPath = "files/consoleOutput/consoleLog"
					+ dbService.sig() + ".csv";
			textService.writeArrayistToCSVFile(consoleLogPath, logList);
			System.out
					.println("Console log can be found in: " + consoleLogPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSelectedValueFromComboBox(String comboBoxId) {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(comboBoxId)));
		Select select = new Select(webDriver.findElement(By.id(comboBoxId)));

		WebElement option = select.getFirstSelectedOption();

		return option.getText();
	}

	public void setPageLoadTimeOut() {
		webDriver.manage().timeouts().pageLoadTimeout(timeout+10, TimeUnit.SECONDS);

	}

	public void setScriptLoadTimeOut() {
		webDriver.manage().timeouts()
				.setScriptTimeout(timeout, TimeUnit.SECONDS);
	}

}
