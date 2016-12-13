package com.epam.cm.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import org.apache.commons.lang3.text.StrBuilder;
import org.openqa.selenium.WebDriver;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Lev_Serba on 12/12/2016.
 */
public class SettingsPage extends AnyPage {

    @FindBy(xpath = "//*[@class='settings__block']/div[2]")
    private WebElementFacade emailEditLink;
    @FindBy(xpath = "//*[@class='edit-email__fields-wrapper']/label[1]")
    private WebElementFacade currentEmailLabel;
    @FindBy(xpath = "//*[@class='edit-email__fields-wrapper']/input[@name='currentEmail']")
    private WebElementFacade currentEmailInput;
    @FindBy(xpath = "//*[@class='edit-email__fields-wrapper']/label[2]")
    private WebElementFacade newEmailLabel;
    @FindBy(xpath = "//*[@class='edit-email__fields-wrapper']/input[@name='newEmail']")
    private WebElementFacade newEmailInput;
    @FindBy(xpath = "//*[@class='edit-email__buttons-wrapper']/input[1]")
    private WebElementFacade emailSaveBtn;
    @FindBy(xpath = "//*[@class='edit-email__result edit-email__result_error ng-binding']")
    private WebElementFacade emailErrorMsg;


    public SettingsPage(WebDriver driver) {
        super(driver);
    }

    public void clickEditLinkNextToEmail() {
        emailEditLink.withTimeoutOf(5, SECONDS).waitUntilClickable().click();
    }

    public boolean  isCurrentEmailFieldVisible() {
        waitFor(currentEmailInput);
        if (currentEmailLabel.getText().equalsIgnoreCase("Current Email") &&
                currentEmailInput.isCurrentlyVisible()) {
            return true;
        }
        return false;
    }

    public boolean isNewEmailFieldVisible() {
        waitFor(newEmailInput);
        if (newEmailLabel.getText().equalsIgnoreCase("New Email") &&
                newEmailInput.isCurrentlyVisible()) {
            return true;
        }
        return false;
    }

    public void typeEmail(String email) {
        newEmailInput.withTimeoutOf(5, SECONDS).waitUntilVisible().clear();
        newEmailInput.type(email);
    }

    public void clickSaveBtn() {
        waitABit(4000);
        emailSaveBtn.withTimeoutOf(5, SECONDS).waitUntilClickable().click();
    }

    public String getErrorMsg() {
        String errorMsg = emailErrorMsg.withTimeoutOf(5,SECONDS).waitUntilVisible().getText();
        return errorMsg;
    }
}
