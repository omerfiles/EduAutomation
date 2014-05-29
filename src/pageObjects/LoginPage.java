package pageObjects;

import junit.framework.Assert;
import drivers.GenericWebDriver;

public abstract class LoginPage extends GenericPage {

	private String pageUrl;

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public LoginPage(GenericWebDriver webDriver) {
		super(webDriver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GenericPage waitForPageToLoad() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected abstract GenericPage OpenPage(String url) throws Exception;

}
