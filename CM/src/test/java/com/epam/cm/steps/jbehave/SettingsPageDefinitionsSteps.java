package com.epam.cm.steps.jbehave;

import com.epam.cm.dto.CredentialsDTO;
import com.epam.cm.dto.SettingsDTO;
import com.epam.cm.steps.serenity.LoginPageSteps;
import com.epam.cm.steps.serenity.SettingsPageSteps;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;

import static org.hamcrest.Matchers.is;

/**
 * Created by Lev_Serba on 12/12/2016.
 */
public class SettingsPageDefinitionsSteps {
    @Steps
    SettingsPageSteps settingsSteps;
    @Steps
    LoginPageSteps loginPageSteps;

    @Given("user on the settings page logged as speaker: $examplesTable")
        public void goToSettingsPageAsSpeaker(ExamplesTable table){
        CredentialsDTO user = table.getRowsAs(CredentialsDTO.class).get(0);
        loginPageSteps.unsignedUserInHomePage();
        loginPageSteps.clickOnAccountMenu();
        loginPageSteps.typeLoginAndPassword(user);
        loginPageSteps.clickSignInButton();
        loginPageSteps.clickSettingsOption();
    }

    @When("user click on the Edit link next to Email")
    public void clickEdtLinkNextToEmail(){
        settingsSteps.clickEditLinkNextToEmail();
    }


    @When("type incorrect values in New email field: $examplesTable")
    public void typeIncorrectEmail(ExamplesTable table){
        boolean replaceNamedParameters = true;
        String email = table.getRowAsParameters(0, replaceNamedParameters).valueAs("wrongEmail", String.class);
        SettingsDTO settingsDTO = new SettingsDTO(){
            {
                setEmail(email);
            }
        };
        settingsSteps.typeEmail(settingsDTO);
    }


    @When("user click on the Edit link next to Name")
    public void clickEditLinkNextToName(){
        settingsSteps.clickEditLinkNextToName();
    }

    @When("leaves 'Last name' field empty")
    public void leaveLastNameEmpty(){
        settingsSteps.leaveLastNameInputEmpty();
    }

    @When("clicks name save button")
    public void clickNameSaveBtn(){
        settingsSteps.clickNameSaveBtn();
    }

    @When("clicks email save button")
    public void clickEmailSaveBtn(){
        settingsSteps.clickEmailSaveBtn();
    }

    @Then("Current Email and New Email fields are visible")
    public void areCurrentEmailAndNewEmailFieldsVisible(){
        Assert.assertTrue(settingsSteps.areCurrentEmailAndNewEmailFieldsVisible());
    }

    @Then("user see a warning message saying '$errorMsg'")
    public void userSeeWarningMassage(String errorMsg){
        Assert.assertTrue(settingsSteps.isErrorMsgShown(errorMsg));
    }


    @Then("'Name' field has following elements: '$firstName', '$lastName' fields 'Save' and 'Cancel' buttons")
    public void checkNameFieldElements(String firstName, String lastName){
        System.out.println();

        Assert.assertThat(firstName,is(settingsSteps.getFirstNameLbl()));
        Assert.assertThat(lastName,is(settingsSteps.getLastNameLbl()));
        Assert.assertTrue(settingsSteps.isSaveBtnVisible());
        Assert.assertTrue(settingsSteps.isCancelBtnVisble());
    }

    @Then("Empty field is highlighted in red and  message saying '$msg' is shown")
    public void lastNameIsHighlightedAndMsgAppears(String msg){
        Assert.assertTrue(settingsSteps.isLastNameHighlighted());
        Assert.assertThat(msg,is(settingsSteps.getLastNameErrorMsg()));
    }

}
