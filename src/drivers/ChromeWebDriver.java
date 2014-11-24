package drivers;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import services.DbService;
import services.TestResultService;

public class ChromeWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		System.out.println("remote url in chrome webdriver: " + remoteUrl);
		setBrowserName("chrome");
		setInitialized(true);
		dbService = new DbService();
		// setRemoteMachine(remoteUrl);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		logsFolder = folderName;
		try {

			System.out.println("Initializing ChromeWebDriver");

			if (enableConsoleLog == true) {
				LoggingPreferences logPrefs = new LoggingPreferences();
				logPrefs.enable(LogType.BROWSER, Level.ALL);
				capabilities.setCapability(CapabilityType.LOGGING_PREFS,
						logPrefs);
			}

			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");

			options.addArguments("--start-maximized");
			options.addArguments("--use-fake-ui-for-media-stream");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			setPageLoadTimeOut();
			setScriptLoadTimeOut();

		} catch (UnreachableBrowserException e) {
			System.out.println("Browser unreachable");
			// try to start selenium grid node
			try {
				Process proc = Runtime
						.getRuntime()
						.exec("java -jar seleniumFiles/selenium-server-standalone-2.43.0.jar -Dwebdriver.chrome.driver='seleniumFiles/chromedriver.exe' -Dwebdriver.ie.driver='seleniumFiles/IEDriverServer.exe'  -role node  -hub http://10.1.0.56:4444/grid/register");
				// Then retreive the process output
				InputStream in = proc.getInputStream();
				InputStream err = proc.getErrorStream();

				webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
						capabilities);

				// webDriver = new RemoteWebDriver(new URL(remoteUrl +
				// "/wd/hub"),
				// capabilities);
			} catch (Exception ex) {
				System.out.println(e.toString());
			}
		}

		catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

}
