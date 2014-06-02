package drivers;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.Configuration;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import services.DbService;
import jsystem.framework.report.Reporter;
import jsystem.framework.system.SystemObjectImpl;
import junit.framework.SystemTestCaseImpl;

@Service
public abstract class GenericWebDriver extends SystemTestCaseImpl {

	protected static final Logger logger = LoggerFactory
			.getLogger(GenericWebDriver.class);
	public String sutUrl = null;
	protected RemoteWebDriver webDriver;
	private int timeout = 10;
	private String browserName;
	private boolean initialized;
	// private Config configuration;
	protected DbService dbService;
	@Autowired
	private services.Configuration configuration;

	protected String logsFolder;

	abstract public void init(String remoteUrl, String folderName)
			throws Exception;

	public void init() throws Exception {
		init(configuration.getProperty("remote.machine"), null);
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
			report.addLink("link to url", url);
			webDriver.get(url);

		} catch (Exception e) {
			Assert.fail("Open url failed ." + e.toString());
		}

	}

	public void navigate(String url) throws Exception {
		report.startLevel("Navigating to: " + url,
				Reporter.EnumReportLevel.CurrentPlace);
		webDriver.navigate().to(url);

		report.stopLevel();
	}

	public void maximize() throws Exception {
		webDriver.manage().window().maximize();
	}

	public String getElementProperty(WebElement element, String propertyname)
			throws Exception {
		String value = null;
		value = element.getAttribute(propertyname);
		return value;
	}

	public WebElement waitForElement(String idValue, String byType,
			int timeout, boolean isElementMandatory) throws Exception {

		report.startLevel("waiting for element " + idValue + " by trpe "
				+ byType + " for " + timeout + " seconds");
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			if (byType.equals("id")) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(idValue)));
				element = webDriver.findElement(By.id(idValue));
			} else {
				if (byType.equals("xpath")) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(idValue)));
					element = webDriver.findElement(By.xpath(idValue));
				} else {
					if (byType.equals("className")) {
						wait.until(ExpectedConditions
								.visibilityOfElementLocated(By
										.className(idValue)));
						element = webDriver.findElement(By.className(idValue));
					} else {
						if (byType.equals("name")) {
							wait.until(ExpectedConditions
									.visibilityOfElementLocated(By
											.name(idValue)));
							element = webDriver.findElement(By.name(idValue));
						} else {
							if (byType.equals("linkText")) {
								wait.until(ExpectedConditions
										.visibilityOfElementLocated(By
												.linkText(idValue)));
								element = webDriver.findElement(By
										.linkText(idValue));
							} else {
								if (byType.equals("partialLinkText")) {
									wait.until(ExpectedConditions
											.visibilityOfElementLocated(By
													.partialLinkText(idValue)));
									element = webDriver.findElement(By
											.partialLinkText(idValue));
								}
							}
						}

					}

				}
			}
		} catch (Exception e) {
			if (isElementMandatory == true) {
				// printScreen("Element " + idValue +
				// " not found. See screen shot");
				Assert.fail("Element: " + idValue + " was not found after "
						+ timeout + "seconds");
				// failCause.append("Element: " + idValue + " was not found");
			}

		} finally {

			if (isElementMandatory == true && element == null) {

				Assert.fail("Element: " + idValue + " not found");
			}
			report.stopLevel();
			return element;
		}
	}

	public WebElement waitForElement(String idValue, String byType)
			throws Exception {
		return waitForElement(idValue, byType, timeout, true);
	}

	public WebElement waitForElement(String idValue, String byType,
			boolean isElementMandatory, int timeout) throws Exception {
		return waitForElement(idValue, byType, timeout, isElementMandatory);
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

			Assert.fail("Element not found or element is not Clickable");
		}
	}

	public void assertTextBy(String idValue, String byType, String text)
			throws Exception {
		report.startLevel("Asserting " + text + " in " + idValue,
				Reporter.EnumReportLevel.CurrentPlace);
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
			report.report("text to compare was: " + current);
			Assert.assertEquals(text, current);
			// Assert.assertTrue("Check text failed. Actual text was: " +
			// current + ". Text to compare was: " + text,
			// current.equals(text));
			report.report("Asserting " + text + " in " + idValue);
		} catch (Exception e) {
			// printScreen();
			// printScreen("Text assertion failed");
			Assert.fail("Text assertion failed. Xpath was: " + idValue
					+ " . Text to assert was: " + text + ". Actual text was: "
					+ current);
		}

		report.stopLevel();
	}

	public void closeBrowser() throws Exception {
		if (initialized == true) {
			try {

				report.report("Closing browser: " + this.getBrowserName());
				deleteCookiesAndCache();
				webDriver.close();

			} catch (Exception e) {
				report.report("Closing " + this.getBrowserName() + "failed. "
						+ e.toString());
			}
		}

	}

	public void refresh() throws Exception {
		webDriver.navigate().refresh();
	}

	public void deleteCookiesAndRefresh() throws Exception {

		webDriver.manage().deleteAllCookies();
		webDriver.navigate().refresh();
	}

	public void deleteCookiesAndCache() throws Exception {
		webDriver.manage().deleteAllCookies();

	}

	public void sendKey(Keys keys, int iterations) throws Exception {
		for (int i = 0; i < iterations; i++) {
			// printScreen(Integer.toString(i));
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

		sendKey(keys);
		webDriver.switchTo().window(currentWindow);
	}

	public String switchToFrame(String frameName) throws Exception {
		String currentWindow = webDriver.getWindowHandle();
		webDriver.switchTo().frame(frameName);
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
		// List<WebElement> framesList=
		// webDriver.findElements(By.xpath("//iframe[@class='FB_UI_Dialog']"));
		report.report("Before the loop");
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

	public void switchToNewWindow() throws Exception {
		for (String winHandle : webDriver.getWindowHandles()) {
			webDriver.switchTo().window(winHandle);
		}
	}

	public void checkElementNotExist(String xpath, String message)
			throws Exception {
		boolean elementFound = false;
		try {

			WebDriverWait wait = new WebDriverWait(webDriver, timeout, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(xpath)));
			waitForElement(xpath, "xpath");
			elementFound = true;
			// printScreen("Element was found when it should not");

		} catch (Exception e) {

		} finally {

			Assert.assertEquals("Element with xpath " + xpath
					+ " found, when it should not. " + message, elementFound,
					false);
		}
	}

	public void checkElementNotExist(String xpath) throws Exception {
		checkElementNotExist(xpath, null);
	}

	public WebElement getElement(By by) {
		WebElement element = webDriver.findElement(by);
		return element;
	}

	public void closeAlertByAccept() {
		WebDriverWait wait = new WebDriverWait(webDriver, 20, 1000);

		if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
			webDriver.switchTo().alert().accept();
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
			report.report("Could not get alert text. might have timed out");
		}
		return text;
	}

	public String printScreen() throws Exception {
		return printScreen("", null);
	}

	public String printScreen(String message, String level) throws Exception {
		// File screenShot;
		// log levels: 0-only failes tests, 1-save all screenshots

		String sig = dbService.sig();
		String path = null;
		String newFileName = null;
		try {
			// if (level.equals(configuration.getProperty("logLevel"))) {
			WebDriver driver = webDriver;
			driver = new Augmenter().augment(driver);
			byte[] decodedScreenshot = org.apache.commons.codec.binary.Base64
					.decodeBase64(((TakesScreenshot) driver).getScreenshotAs(
							OutputType.BASE64).getBytes());
			// InetAddress inetAddress = InetAddress.getLocalHost();

			newFileName = System.getProperty("user.dir") + "/log//current/"
					+ "ScreenShot" + message.replace(" ", "") + sig + ".png";
			// path = System.getProperty("user.dir") + "//" + "log//current/";
			path = configuration.getProperty("logserver") + "//"
					+ configuration.getProperty("screenshotFolder");
			path = path + sig + ".png";
			FileOutputStream fos = new FileOutputStream(new File(newFileName));
			fos.write(decodedScreenshot);
			fos.close();

		} catch (Exception e) {
			report.report("Taking the screenshot failed: " + e.getStackTrace());
		} finally {
			report.addLink("Screenshot", path);

			return path;
		}

	}

	public WebElement getTableTdByName(String tableId,String text) throws Exception {
		WebElement result = null;
		WebElement table = waitForElement(tableId, "xpath");
		List<WebElement> allrows = table.findElements(By.tagName("tr"));
		for (WebElement row : allrows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (WebElement cell : cells) {
				System.out.println(cell.getText());
				if (cell.getText().equals(text)) {
					result = cell;
				}
			}
		}
		return result;
	}

	public int getTableRowIndexByParam(String tableId, String tdIndex,
			String tdText, String byType) throws Exception {
		String result = "";
		int index = 0;
		try {
			while (result != null) {
				String xpath = "//table[@id='" + tableId
						+ "']//tbody//tr[@role='row'][" + index + "]//td["
						+ tdIndex + "]//div";
				System.out.println(xpath);
				String text = waitForElement(xpath, byType, false, 10)
						.getText();
				if (text.equals(tdText)) {
					result = text;
					System.out.println("text is:" + text);
				}
				index++;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			Assert.fail();
		} finally {
			return index;
		}
	}

	public void clickOnTableCell(String tableId, int xIndex, int yIndex)
			throws Exception {
		waitForElement(
				"//table[@id='" + tableId + "']//tr[" + yIndex + "]//td["
						+ xIndex + "]", "xpath").click();
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
		return webDriver.getCurrentUrl();
	
	}

}
