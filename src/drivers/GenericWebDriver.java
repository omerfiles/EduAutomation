package drivers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.ProxyServer;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;
//import org.browsermob.proxy.ProxyServer;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.applitools.eyes.Eyes;
import com.applitools.eyes.RectangleSize;
import com.google.common.base.Predicate;

import Enums.AutoParams;
import Enums.ByTypes;
import Enums.TestRunnerType;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;
import services.DbService;
import services.GenericService;
import services.NetService;
import services.Reporter;
import services.TestResultService;
import services.TextService;
import junit.framework.SystemTestCaseImpl;

@Service
public abstract class GenericWebDriver extends GenericService {

	protected static final Logger logger = LoggerFactory
			.getLogger(GenericWebDriver.class);
	private String sutUrl = null;
	private String sutSubDomain = null;
	private String institutionnName = null;

	private String CIServerName = null;

	protected RemoteWebDriver webDriver;
	protected int timeout = 10;
	private String browserName;
	private boolean initialized;
	// private Config configuration;
	protected DbService dbService;
	protected String remoteMachine;
	protected boolean enableConsoleLog;
	protected boolean useProxy;

	String scrFileExt = "jpg";

	Proxy proxy;
	ProxyServer server;

	@Autowired
	private services.Configuration configuration;

	private TextService textService;

	@Autowired
	protected services.Reporter reporter;

	@Autowired
	TestResultService testResultService;

	protected String logsFolder;
	private boolean failureAdded;

	Eyes eyes = new Eyes();
	boolean eyesOpen = false;

	// This is your api key, make sure you use it in all your tests.

	// abstract public void init(String remoteUrl, String folderName)
	// throws Exception;

	abstract public void init(String remoteUrl, boolean startProxy)
			throws Exception;

	public void init() throws Exception {
		// this.testResultService = testResultService;
		try {
			eyes.setApiKey("tsN45rbyinZ1084MxMVSzumAgD106Qn3MOpBcr101hiyVEpSY110");

			textService = new TextService();
			remoteMachine = configuration.getAutomationParam(
					AutoParams.remoteMachine.toString(), "machine");
			setSutUrl(configuration.getAutomationParam(
					AutoParams.sutUrl.toString(), "suturl"));
			setSutSubDomain(configuration.getProperty("institution.name"));
			setInstitutionName(configuration.getProperty("institution.name"));

			init(remoteMachine, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			getUnexpectedAlertDetails();
			// closeAlertByAccept();
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

		long startTime = System.currentTimeMillis();

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
			System.out.println("Closing alert and trying again");
			// closeAlertByAccept();
			getUnexpectedAlertDetails();
			// waitForElement(idValue, byType);
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
					+ " was in invalid state " + " Description of element:"
					+ message);
			failureAdded = true;
		} catch (WebDriverException e) {
			printScreen("Web driver exception found");
			System.out.println(e.toString());
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
				String idValueForPrintScreen = idValue.replaceAll("/", "_");

				printScreen("Element " + idValueForPrintScreen + " _not_found ");
				Assert.fail("Element: " + idValue + " not found " + message);

			}
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			if (element != null) {
				reporter.report("Element " + idValue + " found after: "
						+ elapsedTime + " ms");
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
		if (eyesOpen) {
			eyes.close();
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
			testResultService.addFailTest("Frame was not found", true, true);
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
		// boolean elementFound = false;
		// try {
		// WebElement element = waitForElement(xpath, ByTypes.xpath, false,
		// timeout);
		// if (element != null) {
		// elementFound = true;
		// printScreen(message);
		// }
		//
		// } catch (Exception e) {
		// System.out.println("Exceptin found during checkElementNotExist "
		// + e.toString());
		// } finally {
		//
		// testResultService.assertTrue("Element with xpath " + xpath
		// + " found when it should not", elementFound == false);
		// }
		WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By
					.xpath(xpath)));
			WebElement element = waitForElement(xpath, ByTypes.xpath, 10, false);
			if (element != null) {
				testResultService.addFailTest("Element with xpath: " + xpath
						+ " was found when it should not");
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Element with xpath: " + xpath
					+ " was found when it should not");
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
			// TestRunnerType runner = TestRunnerType.CI;
			// If test is running using jenkins ci
			if (runner == TestRunnerType.CI) {

				newFileName = configuration.getGlobalProperties("logserver")
						+ "\\\\"
						+ configuration.getProperty("screenshotFolder")
						+ "\\ScreenShot" + message.replace(" ", "") + sig
						+ ".png";
				System.out.println("File path is :" + newFileName);

				path = "http://"
						+ configuration.getGlobalProperties("logserver")
								.replace("\\", "") + "/"
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

			if (runner == TestRunnerType.CI) {
				// **printscreen using smbFile
				NetService netService = new NetService();
				String sFileName = "scr_" + dbService.sig(8)
						+ message.replace(" ", "") + ".png";
				SmbFile smbFile = new SmbFile("smb://"
						+ configuration.getLogerver()
						+ "/automationScreenshots/" + sFileName,
						netService.getAuth());
				SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(
						smbFile);
				// FileOutputStream fos = new FileOutputStream(new
				// File(newFileName));
				// fos.write(decodedScreenshot);
				smbFileOutputStream.write(decodedScreenshot);
				smbFileOutputStream.close();
				System.out.println("http://newjenkins/automationScreenshots/"
						+ sFileName);
			} else {
				newFileName = System.getProperty("user.dir") + "/log//current/"
						+ "ScreenShot" + message.replace(" ", "") + sig
						+ ".png";
				path = System.getProperty("user.dir") + "//" + "log//current//"
						+ "ScreenShot" + message.replace(" ", "") + sig
						+ ".png";

				FileOutputStream fos = new FileOutputStream(new File(
						newFileName));
				fos.write(decodedScreenshot);
			}

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
		try {
			webDriver.manage().window().maximize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WebElement getChildElementByXpath(WebElement element, String xpath) {
		WebElement chileElement = element.findElement(By.xpath(xpath));
		return chileElement;
	}

	public List<WebElement> getChildElementsByXpath(WebElement element,
			String xpath) {
		return element.findElements(By.xpath(xpath));
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
		Thread.sleep(2000);

	}

	public void hoverOnElement(WebElement element, int x, int y)
			throws Exception {
		Actions builder = new Actions(webDriver);
		builder.moveToElement(element, x, y).perform();
		Thread.sleep(2000);

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

	// public void setReporter(services.Reporter reporter) {
	// this.reporter = reporter;
	// }

	public void setTestResultService(TestResultService testResultService) {
		this.testResultService = testResultService;

	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue) throws Exception {
		waitUntilComboBoxIsPopulated(comboboxName);
		selectElementFromComboBox(comboboxName, optionValue, ByTypes.id, false);
	}

	public void selectElementFromComboBoByIndex(String comboboxName, int index)
			throws Exception {
		selectElementFromComboBoByIndex(comboboxName, ByTypes.id, index);
	}

	public void selectElementFromComboBoByIndex(String comboboxName,
			ByTypes byType, int index) throws Exception {
		boolean selected = false;
		{
			try {
				Select select = new Select(waitForElement(comboboxName, byType));
				List<WebElement> options = select.getOptions();
				select.selectByIndex(index);

			}

			catch (UnhandledAlertException e) {
				getUnexpectedAlertDetails();
			}

			catch (Exception e) {
				printScreen("problem selecting from combo box");
				testResultService.addFailTest(
						"problem selecting from combo box", true, true);
				e.printStackTrace();
			}

			System.out.println("Selected " + comboboxName + ": " + selected);
		}
	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue, boolean contains) throws Exception {
		selectElementFromComboBox(comboboxName, optionValue, ByTypes.id,
				contains);
	}

	public void selectElementFromComboBox(String comboboxName,
			String optionValue, ByTypes byType, boolean contains)
			throws Exception {
		boolean selected = false;

		String[] optionValues = null;
		{
			try {
				Select select = new Select(waitForElement(comboboxName, byType));
				// waitForElement(comboboxName, ByTypes.id);
				List<WebElement> options = select.getOptions();
				optionValues = new String[options.size()];

				for (int j = 0; j < options.size(); j++) {
					optionValues[j] = (options.get(j).getText());
				}
				for (int i = 0; i < options.size(); i++) {

					if (contains == false) {
						if (options.get(i).getText().equals(optionValue)) {
							select.selectByIndex(i);
							System.out.println("option " + optionValue
									+ " selected");
							selected = true;
							break;
						}
					} else {
						if (options.get(i).getText().contains(optionValue)) {
							select.selectByIndex(i);
							System.out.println("option " + optionValue
									+ " selected");
							selected = true;
							break;
						}
					}
				}
				if (selected == false) {
					testResultService.addFailTest(optionValue
							+ " was not found in the combo box", true, true);
				}
			}

			catch (UnhandledAlertException e) {
				getUnexpectedAlertDetails();
			}

			catch (Exception e) {
				System.out.println(e.toString());
				System.out.println("Options were: "
						+ textService.printStringArray(optionValues));
				printScreen("problem selecting from combo box");
				testResultService.addFailTest(
						"problem selecting from combo box", true, true);
				e.printStackTrace();
			}

			System.out.println("Selected " + comboboxName + ": " + selected);
		}
	}

	public List<String> printConsoleLogs(String logFilter, boolean useFllter)
			throws Exception {
		List<String> logList = null;
		try {
			textService = new TextService();
			LogEntries logEntries = getConsoleLogEntries();
			logList = textService.getListFromLogEntries(logEntries, logFilter,
					useFllter);
			// TD DO change to SMB auth
			NetService netService = new NetService();

			// String tempCsvFile = "files/csvFiles/temp" + dbService.sig(6);
			// SmbFile sFile = new SmbFile(tempCsvFile);
			// textService.writeArrayistToCSVFile(tempCsvFile, logList);
			NtlmPasswordAuthentication auto = netService.getAuth();

			String path = "smb://" + configuration.getLogerver()
					+ "/automationLogs/consoleLog" + dbService.sig() + ".csv";
			textService.writeListToSmbFile(path, logList, netService.getAuth());

			// SmbFileOutputStream outputStream = new
			// SmbFileOutputStream(smbFile);
			// outputStream.write(b);
			// textService.writeArrayistToCSVFile(path, logList);
			System.out.println("Console log can be found in: " + path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logList;
	}

	public String getSelectedValueFromComboBox(String comboBoxId)
			throws Exception {
		WebDriverWait wait = new WebDriverWait(webDriver, timeout + 10, 1000);
		WebElement option = null;
		try {
			waitUntilComboBoxIsPopulated(comboBoxId);
			wait.until(ExpectedConditions.elementToBeClickable(By
					.id(comboBoxId)));
			Select select = new Select(webDriver.findElement(By.id(comboBoxId)));

			option = select.getFirstSelectedOption();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("getting value from combo box: "
					+ comboBoxId + " failed");
		}

		return option.getText();
	}

	public void setPageLoadTimeOut() {
		webDriver.manage().timeouts()
				.pageLoadTimeout(timeout + 10, TimeUnit.SECONDS);

	}

	public void setScriptLoadTimeOut() {
		webDriver.manage().timeouts()
				.setScriptTimeout(timeout, TimeUnit.SECONDS);
	}

	public String getPopUpText() throws Exception {

		String alertText = null;
		try {

			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				alertText = webDriver.switchTo().alert().getText();
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			testResultService.addFailTest("Alert was not found", true, false);
		}
		return alertText;

	}

	public void waitUntilComboBoxIsPopulated(String comboBoxId)
			throws Exception {
		final Select combo = new Select(waitForElement(comboBoxId, ByTypes.id));
		try {
			new FluentWait<WebDriver>(webDriver)
					.withTimeout(20, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS)
					.until(new Predicate<WebDriver>() {

						public boolean apply(WebDriver webdriver) {
							// TODO Auto-generated method stub
							return (!combo.getOptions().isEmpty());
						}
					});
		} catch (UnhandledAlertException e) {
			// TODO Auto-generated catch block
			getUnexpectedAlertDetails();
		} catch (TimeoutException e) {
			testResultService
					.addFailTest("Combobox is not filled after 20 seconds");
		}
	}

	public void getUnexpectedAlertDetails() throws Exception {

		// get alert text
		String alertText = getAlertText(5);
		System.out.println("Alert text was: " + alertText);
		List<String> consoleLog = printConsoleLogs(null, false);
		for (int i = 0; i < consoleLog.size(); i++) {
			System.out.println(consoleLog.get(i).toString());
		}
		// get console log
		// printscreen alert

	}

	public WebElement findElementByXpath(String value, ByTypes byType) {
		return webDriver.findElement(By.xpath(value));
	}

	public void HoverOnElementAndmoveToComboBoxElementAndSelectValue(
			WebElement hoverElement, String comboboxName, String value)
			throws Exception {
		Actions actions = new Actions(webDriver);
		actions.moveToElement(hoverElement)
				.moveToElement(webDriver.findElementById(comboboxName))
				.clickAndHold().perform();
		selectElementFromComboBox(comboboxName, value);

	}

	public int getWindowWidth() {
		return webDriver.manage().window().getSize().getWidth();
	}

	public int getWindowHeight() {
		return webDriver.manage().window().getSize().getHeight();
	}

	public void initEyes(String appName, String testName) {
		try {
			// initEyesTest(appName, testName);
			eyes.setSaveNewTests(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Eyes init OK");
		eyesOpen = true;
	}

	// public void initEyes(String appName, String testName,
	// RectangleSize rectangleSize) {
	// try {
	// if (rectangleSize == null) {
	// initEyesTest(appName, testName);
	// } else {
	// initEyesTest(appName, testName, rectangleSize);
	// }
	//
	// eyes.setSaveNewTests(true);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println("Eyes init OK");
	// eyesOpen = true;
	// }

	// public void eyesCheckPage(String text) {
	// eyes.checkWindow(text);
	// }

	public void highlightElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",

		element, "color: blue; border: 2px solid red;");
	}

	public void waitForJqueryToFinish() throws Exception {
		try {
			long startime = System.currentTimeMillis();
			// System.out.println("startted: "+System.currentTimeMillis());
			new WebDriverWait(webDriver, 1800)
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver driver) {
							// JavascriptExecutor js = (JavascriptExecutor)
							// driver;
							// return (Boolean) js
							// .executeScript("return jQuery.active == 0");
							return runJavascript("return jQuery.active == 0");

						}
					});
			long finishTime = System.currentTimeMillis();
			finishTime = finishTime - startime;
			System.out.println("finished. took: " + finishTime + "ms");
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			printScreen();
			e.printStackTrace();
		}
	}

	public boolean runJavascript(String script) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		return (Boolean) js.executeScript(script);
	}

	public void failTest() {
		testResultService.addFailTest("Failed by webdriver");
		reporter.report("fail in webdriver");
	}

	public void checkForBrokenImages() {
		List<WebElement> imageElements = webDriver.findElementsByTagName("img");
		for (int i = 0; i < imageElements.size(); i++) {
			isImageLoaded(imageElements.get(i));
		}
	}

	public boolean isImageLoaded(WebElement image) {
		Boolean imageLoaded = (Boolean) ((JavascriptExecutor) webDriver)
				.executeScript(
						"return arguments[0].complete && type of arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
						image);
		System.out.println("image not found");
		return imageLoaded;
	}

	public void setBrowserWidth(int width) throws Exception {
		try {
			int height = webDriver.manage().window().getSize().getHeight();
			webDriver.manage().window().setSize(new Dimension(width, height));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public void initEyesTest(String appName, String testName) {
	// eyes.open(webDriver, appName, testName);
	// }
	// public void initEyesTest(String appName, String testName,
	// RectangleSize rectangleSize) {
	//
	// eyes.open(webDriver, appName, testName, rectangleSize);
	// }

	public void waitUntilTextIsLoadedInElement(final String xpath)
			throws Exception {
		new WebDriverWait(webDriver, 60)
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver w) {
						return w.findElement(By.xpath(xpath)).getText()
								.length() > 0;
					}

				});
	}

	public void waitUntilTextIsClearedFromElement(final String xpath)
			throws Exception {
		new WebDriverWait(webDriver, 60)
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver w) {
						return w.findElement(By.xpath(xpath)).getText()
								.length() == 0;
					}

				});
	}

	public void startProxyServer() {
		String PROXY = "localhost:4040";
		server = new ProxyServer(4040);
		server.start();
		proxy = server.seleniumProxy();
		proxy.setHttpProxy(PROXY).setSslProxy(PROXY);

	}

	public void startProxyLister(String site) {
		server.newHar(site);
	}

	public void stopProxyListen() throws IOException {
		Har har = server.getHar();
		FileOutputStream fos = new FileOutputStream("files/proxyOutput.txt");
		har.writeTo(fos);
		server.stop();
	}

	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public List<WebElement> getElementsByXpath(String xpath) throws Exception {
		return webDriver.findElements(By.xpath(xpath));
	}

	public String getBrowserVersion() {
		Capabilities capabilities = webDriver.getCapabilities();
		String version = capabilities.getVersion();
		return version;
	}

	public Screenshot takeElementScreenShot(WebElement element)
			throws Exception {
		Screenshot screenshot = new AShot().takeScreenshot(webDriver, element);
		BufferedImage image = screenshot.getImage();
		File file = new File(System.getProperty("user.dir") + "/log//current/"
				+ element.getAttribute("name") + dbService.sig(6) + ".jpg");
		ImageIO.write(image, "jpg", file);

		return screenshot;
	}

	public void saveImage(String message, BufferedImage bufferedImage)
			throws Exception {
		String fileExt = "jpg";
		TestRunnerType runner = getTestRunner();
		String sig = dbService.sig();
		String newFileName;
		String path;
		// runner=runnerType.CI;

		try {
			if (runner == TestRunnerType.CI) {

				newFileName = configuration.getGlobalProperties("logserver")
						+ "\\\\"
						+ configuration.getProperty("screenshotFolder")
						+ "\\ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
				System.out.println("File path is :" + newFileName);

				path = "http://"
						+ configuration.getGlobalProperties("logserver")
								.replace("\\", "") + "/"
						+ configuration.getProperty("screenshotFolder")
						+ "/ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;

			} else if (runner == TestRunnerType.local) {
				newFileName = System.getProperty("user.dir") + "/log//current/"
						+ "ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
				path = System.getProperty("user.dir") + "//" + "log//current//"
						+ "ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
			}

			if (runner == TestRunnerType.CI) {
				// **printscreen using smbFile
				NetService netService = new NetService();
				String sFileName = "scr_" + dbService.sig(8)
						+ message.replace(" ", "") + "." + scrFileExt;
				SmbFile smbFile = new SmbFile("smb://"
						+ configuration.getLogerver()
						+ "/automationScreenshots/" + sFileName,
						netService.getAuth());
				SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(
						smbFile);
				// FileOutputStream fos = new FileOutputStream(new
				// File(newFileName));
				// fos.write(decodedScreenshot);
				// smbFileOutputStream.write(decodedScreenshot);
				smbFileOutputStream.close();

				ImageIO.write(bufferedImage, scrFileExt, smbFileOutputStream);
				System.out.println("http://newjenkins/automationScreenshots/"
						+ sFileName);
			} else {
				newFileName = System.getProperty("user.dir") + "/log//current/"
						+ "ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;
				path = System.getProperty("user.dir") + "//" + "log//current//"
						+ "ScreenShot" + message.replace(" ", "") + sig + "."
						+ scrFileExt;

				File file = new File(path);
				// FileOutputStream fos = new FileOutputStream(new File(
				// newFileName));
				// fos.write(decodedScreenshot);
				ImageIO.write(bufferedImage, scrFileExt, file);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Reporter getReporter() {
		return this.reporter;
	}

	public void switchToNextTab() {
		List<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
		// System.out.println("Number of tabs: "+tabs.size());
		webDriver.switchTo().window(tabs.get(1));

	}

	public services.Configuration getConfiguration() {
		return configuration;
	}

	public String waitForSpecificCurrentUrl(String currentUrl, String prefix)
			throws Exception {
		for (int i = 0; i < timeout; i++) {
			currentUrl = getUrl();
			// System.out.println("currentUrl: "+currentUrl);
			if (currentUrl.contains(prefix)) {
				break;
			} else {
				sleep(1);
			}
		}
		return currentUrl;
	}

}
