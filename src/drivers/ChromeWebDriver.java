package drivers;

import java.net.URL;
import java.util.Arrays;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import services.DbService;

public class ChromeWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		System.out.println("remote url in chrome webdriver: " + remoteUrl);
		setBrowserName("chrome");
		setInitialized(true);
		dbService = new DbService();
//		setRemoteMachine(remoteUrl);

		logsFolder = folderName;
		try {

			report.startLevel("Initializing ChromeWebDriver",
					Reporter.EnumReportLevel.CurrentPlace);

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");

			options.addArguments("--start-maximized");
//			options.addArguments("--disable-user-media-security=true");
			 options.addArguments("--use-fake-ui-for-media-stream");
			// options.addArguments("--use-fake-device-for-media-stream");

			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			// capabilities.setCapability("platform", "Windows 2003");
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			// webDriver = new RemoteWebDriver( capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

}
