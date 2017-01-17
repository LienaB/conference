package com.epam.cm.pages;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

import org.openqa.selenium.WebDriver;


public class MyTalksPage extends AnyPage {

    private final String RowByName = "//*[@class='data-table__row ng-scope']/../div[contains(.,'%s')]";
    private final String STATUS = "//*[@class='data-table__row ng-scope']/../div[contains(.,'%s')]/div[@class='data-table__column data-table__column_status-talk ng-binding']";
    private final String TITLE = "//*[@class='data-table__row ng-scope']/div[contains(.,'%s')]/a";

    @FindBy(xpath = "//*[@class='my-talks__header']/a")
    WebElementFacade submitNewTalkBtn;
    // no my info
    @FindBy(xpath = "//control-popup/div/div")
    WebElementFacade popUpMyInfoNotField;
    @FindBy(xpath = "//*[@class='pop-up']/p")
    WebElementFacade errorMsgMyInfoNotFieldText;
    @FindBy(xpath = "//*[@class='btn pop-up__button']")
    WebElementFacade errorMsgMyInfoSubmitBtn;
    @FindBy(xpath = "//*[@class='btn pop-up__button']")
    WebElementFacade okMyInfoNotFieldBtn;
    @FindBy(xpath = "//*[@class='pop-up-wrapper']/div/button[@class='pop-up__close']")
    WebElementFacade cancelMyInfoBtn;
    // big pop up invalid
    @FindBy(xpath = "//*[@class='btn pop-up__button']")
    WebElementFacade newTalkBigPopUpSubmitBtn;
    @FindBy(xpath = "//*[@class='btn btn_right btn_small']")
    WebElementFacade okBtnAfterInvalidData;
    @FindBy(xpath = "//*[@class='pop-up-wrapper']/div/p")
    WebElementFacade alertMsg;
    @FindBy(xpath = "//*[@class='pop-up-wrapper']/div/p[1]")
    WebElementFacade firstNotSavingMsg;
    @FindBy(xpath = "//*[@class='pop-up-wrapper']/div/p[2]")
    WebElementFacade secondNotSavingMsg;
    @FindBy(xpath = "//*[@class='pop-up-wrapper']/div/p[3]")
    WebElementFacade thirdNotSavingMsg;
    @FindBy(xpath = "//*[@class='pop-up-button-wrapper']/button[@class='btn pop-up__button']")
    WebElementFacade yesNotSavingBtn;
    @FindBy(xpath = "//*[@class='pop-up-button-wrapper']/button[@class='btn btn__cancel pop-up__button']")
    WebElementFacade noNotSavingBtn;
    @FindBy(xpath = "//newtalk/div[3]/div/button")
    WebElementFacade cancelInvalidDataBtn;
    @FindBy(xpath = "//*[@class='pop-up-wrapper']/div/button")
    WebElementFacade cancelBigPopUpBtn;
    // big pop-up fields
    @FindBy(xpath = "//*[@id='new-talk-title']")
    WebElementFacade titleField;
    @FindBy(xpath = "//*[@id='new-talk-desc']")
    WebElementFacade descriptionField;
    @FindBy(xpath = "//*[@class='pop-up pop-up_big']/form/select[1]")
    WebElementFacade topicDropDown;
    @FindBy(xpath = "//*[@class='pop-up pop-up_big']/form/select[2]")
    WebElementFacade typeDropDown;
    @FindBy(xpath = "//*[@class='pop-up pop-up_big']/form/select[3]")
    WebElementFacade languageDropDown;
    @FindBy(xpath = "//*[@class='pop-up pop-up_big']/form/select[4]")
    WebElementFacade levelDropDown;
    @FindBy(xpath = "//*[@id='new-talk-add-inf']")
    WebElementFacade additionalInfoField;

    //filters
    @FindBy(xpath = "//*[@id='my-talk-status']")
    WebElementFacade filterByStatus;
    @FindBy(xpath = "//*[@id='my-talk-topic']")
    WebElementFacade filterByTopic;
    @FindBy(xpath = "//*[@ng-model='$ctrl.myDate_start']")
    WebElementFacade filterByStartedDate;
    @FindBy(xpath = "//*[@ng-model='$ctrl.myDate_finish']")
    WebElementFacade filterByFinishDate;
    @FindBy(xpath = "//*[@class='my-talk-settings__button-wrapper']/input[@value='apply']")
    WebElementFacade myTalksApplyBtn;
    @FindBy(xpath = "//*[@class='my-talk-settings__button-wrapper']/input[@value='reset']")
    WebElementFacade myTalksResetBtn;

    // organiser
    @FindBy(xpath = "//*[@class='data-table__row ng-scope']/descendant::div[contains(.,'New')]")
    WebElementFacade tableRow;
    @FindBy(xpath = "//*[@id=\"$ctrl.reviewTalkId\"]/div[1]/div/form/textarea[3]")
    WebElementFacade orgComments;

    //approve
    @FindBy(xpath = "")
    WebElementFacade approveBtn;


    //reject
    @FindBy(xpath = "//button[@ng-click='$ctrl.reject()']")
    WebElementFacade rejectBtn;

    //inProgress
    @FindBy(xpath = "")
    WebElementFacade inProgressBtn;

    public WebElementFacade findRow(String name){
        WebElementFacade row = find(By.xpath(String.format(RowByName,name)));
        return row;
    }

   /* public void clickRejectBtn(){
        rejectBtn.click();
    }*/

    public String getStatus(String name) {
       // waitABit(3000);
        return find(By.xpath(String.format(STATUS, name))).getText();
    }

    public void clickTitle(String name){
        WebElementFacade titleTable = find(By.xpath(String.format(TITLE, name)));
        titleTable.click();
    }

    public void clickRejectBtn(){
        rejectBtn.click();
    }

    public MyTalksPage(WebDriver driver) {
        super(driver);
    }

    public void clickSubmitNewTalk() {
        submitNewTalkBtn.click();
    }

    public String getErrorNoMyInfoMsgText() {
        return errorMsgMyInfoNotFieldText.getText();
    }

    public void clickCancelNoMyInfoPopUpBtn() {
        cancelMyInfoBtn.click();
    }

    /*public void clickOKBtn() {
        errorMsgMyInfoSubmitBtn.click();
    }
    */
    public void clickBigPopUpSbmBtn() {
        newTalkBigPopUpSubmitBtn.click();
    }

    public void clickErrorMyInfoOkBtn() {
        okMyInfoNotFieldBtn.click();
    }

    public String getEmptyFieldsAlertMsg() {
        return alertMsg.getText();
    }

    public String getFirstInfoMsgText() {
        return firstNotSavingMsg.getText();
    }

    public String getSecondInfoMsgText() {
        return secondNotSavingMsg.getText();
    }

    public String getThirdInfoMsgText() {
        return thirdNotSavingMsg.getText();
    }

    public void clickYesBtn() {
        yesNotSavingBtn.click();
    }

    public void clickNoBtn() {
        noNotSavingBtn.click();
    }

    public void clickCancelBigPopUpBtn() {
        cancelBigPopUpBtn.click();
    }

    public void clickCancelInvalidData() {
        cancelInvalidDataBtn.click();
    }

    public void setOrgComments(String comments){
        orgComments.clear();
        orgComments.type(comments);
    }

    public void setTitleField(String title) {
        titleField.clear();
        titleField.type(title);
    }

    public int getTitleLength(){
        return titleField.getValue().length();
    }

    public boolean isTitleHighlighted() {
        return titleField.getAttribute("class").contains("invalid");
    }

    public void setDescriptionField(String description) {
        descriptionField.clear();
        descriptionField.type(description);
    }

    public int getDescriptionLength(){
        return descriptionField.getValue().length();
    }

    public boolean isDescriptionHighlighted() {
        return descriptionField.getAttribute("class").contains("invalid");
    }

    public void setAdditionalInfoField(String additionalInfo) {
        additionalInfoField.clear();
        additionalInfoField.type(additionalInfo);
    }

    public int getAdditionalInfoLength(){
        return additionalInfoField.getValue().length();
    }

    public boolean isAdditionalInfoHighlighted() {
        return additionalInfoField.getAttribute("class").contains("invalid");
    }

    public void selectTopic(String topic) {
        topicDropDown.selectByIndex(Integer.parseInt(topic));
    }

    public boolean isTopicHighLighted() {
        return topicDropDown.getAttribute("class").contains("invalid");
    }

    public void selectType(String type) {
        typeDropDown.selectByIndex(Integer.parseInt(type));
    }

    public boolean isTypeHighlighted() {
        return typeDropDown.getAttribute("class").contains("invalid");
    }

    public void selectLanguage(String language) {
        languageDropDown.selectByIndex(Integer.parseInt(language));
    }

    public boolean isLanguageHighlighted() {
        return languageDropDown.getAttribute("class").contains("invalid");
    }

    public void selectLevel(String level) {
        levelDropDown.selectByIndex(Integer.parseInt(level));
    }

    public boolean isLevelHighlighted() {
        return levelDropDown.getAttribute("class").contains("invalid");
    }
}
