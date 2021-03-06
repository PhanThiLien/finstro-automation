package com.finstro.automation.pages;

import com.finstro.automation.appium.driver.AppiumBaseDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class RegisterPage {
	
	private AppiumBaseDriver driver;
	
	@AndroidFindBy(uiAutomator="new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().resourceId(\"au.com.finstro.finstropay:id/have_account_link\"))")
    private WebElement loginPageLink;
	
	@AndroidFindBy(id="au.com.finstro.finstropay:id/tvAgreement")
    private WebElement agreement;
    
    public RegisterPage(AppiumBaseDriver driver){
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver.getDriver()), this);
    }

    public LoginPage toLoginPage() throws Exception {
        driver.clickByPosition(loginPageLink,"right");
        return new LoginPage(driver);
    }
    public boolean isActive() throws Exception {
        return driver.waitForElementDisplayed(agreement,15);
    }
}
