package drivers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import Enums.ByTypes;
import services.DbService;

@Service
public class IEWebDriver extends GenericWebDriver {

	
//	For IE 11 only, you will need to set a registry entry on the target computer so that the driver can maintain a connection to
//	the instance of Internet Explorer it creates. For 32-bit Windows installations, the key you must examine in the registry 
//	editor is HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE. For 64-bit Windows 
//	installations, the key is HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE. 
//	Please note that the FEATURE_BFCACHE subkey may or may not be present, and should be created if it is not present. Important: Inside this key,
//	create a DWORD value named iexplore.exe with the value of 0.
	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		setTimeout(30);
		setBrowserName("Internet Exporer");
		setInitialized(true);
		dbService = new DbService();
		report.report("Remote url from pom file is: " + remoteUrl);
		logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
//			report.startLevel("Initializing IEWebDriver",
//					Reporter.EnumReportLevel.CurrentPlace);

			DesiredCapabilities capabilities = DesiredCapabilities
					.internetExplorer();
			capabilities.setCapability(
					CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION,
					true);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			// webDriver = new RemoteWebDriver( capabilities);
			setPageLoadTimeOut();
			setScriptLoadTimeOut();
			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

	
	@Override
	public void waitForElementAndClick(String idValue, ByTypes byType)
			throws Exception {
		waitForElement(idValue, byType, timeout, true).click();
	}
	
	

	@Override
	public String switchToNewWindow(int windowId) throws Exception {
		Thread.sleep(10000);
		Set<String> winhandles = webDriver.getWindowHandles();
		List<String> windows = new ArrayList<String>();
		windows.addAll(winhandles);
		System.out.println("before switch: " + webDriver.getWindowHandle());
		String oldWindow = webDriver.getWindowHandle();
		webDriver.switchTo().window(windows.get(windowId));
		System.out.println("after switch: " + webDriver.getWindowHandle());
		return oldWindow;

	}
}
