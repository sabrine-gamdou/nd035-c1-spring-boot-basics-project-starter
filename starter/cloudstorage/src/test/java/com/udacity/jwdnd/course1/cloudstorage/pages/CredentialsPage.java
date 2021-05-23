package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.service.utils.WebElementUtilsService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CredentialsPage {

    /* Credentials home tab */
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    /* URL elements on create and edit tabs */
    @FindBy(css = "#credential-url")
    private WebElement credentialUrl;

    @FindBy(css = "#edit-credential-url")
    private WebElement editCredentialUrl;

    /* Username elements on create and edit tabs */
    @FindBy(css = "#credential-username")
    private WebElement credentialUsername;

    @FindBy(css = "#edit-credential-username")
    private WebElement editCredentialUsername;

    /* Password elements on create and edit tabs */
    @FindBy(css = "#credential-password" )
    private WebElement credentialPassword;

    @FindBy(css = "#edit-credential-password")
    private WebElement editCredentialPassword;

    /* Add,edit,delete buttons on the credentials home tab */
    @FindBy(id = "add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "edit-credential-button")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-credential-button")
    private WebElement deleteCredentialButton;

    /* Displayed credential data on the credentials home tab*/
    @FindBy(css = "#displayed-credential-url")
    private WebElement displayedCredentialUrl;

    @FindBy(css = "#displayed-credential-username")
    private WebElement displayedCredentialUsername;

    @FindBy(css = "#displayed-credential-password")
    private WebElement displayedCredentialPassword;

    /* Submit buttons on create,edit,delete windows */
    @FindBy(css = "#credential-submit")
    private WebElement credentialSubmit;

    @FindBy(css = "#edit-credential-submit")
    private WebElement editCredentialSubmit;

    @FindBy(css = "#delete-credential-submit")
    private WebElement deleteCredentialSubmit;

    @Autowired
    private WebElementUtilsService webElementUtilsService;

    private final WebDriver driver;

    public CredentialsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.webElementUtilsService = new WebElementUtilsService();
        this.webElementUtilsService.setDriver(this.driver);
    }

    public void createCredential(String url, String username, String password){
        webElementUtilsService.clickButton(credentialsTab);
        webElementUtilsService.clickButton(addCredentialButton);
        webElementUtilsService.enterValue(this.credentialUrl,url);
        webElementUtilsService.enterValue(this.credentialUsername,username);
        webElementUtilsService.enterValue(this.credentialPassword,password);
        webElementUtilsService.clickButton(credentialSubmit);
        webElementUtilsService.clickButton(credentialsTab);
    }

    public void editCredential(String url, String username, String password){
        webElementUtilsService.clickButton(credentialsTab);
        webElementUtilsService.clickButton(editCredentialButton);
        webElementUtilsService.enterValue(this.editCredentialUrl,url);
        webElementUtilsService.enterValue(this.editCredentialUsername,username);
        webElementUtilsService.enterValue(this.editCredentialPassword,password);
        webElementUtilsService.clickButton(editCredentialSubmit);
        webElementUtilsService.clickButton(credentialsTab);
    }

    public void deleteCredential(){
        webElementUtilsService.clickButton(credentialsTab);
        webElementUtilsService.clickButton(deleteCredentialButton);
        webElementUtilsService.clickButton(deleteCredentialSubmit);
        webElementUtilsService.clickButton(credentialsTab);
    }

    public String getDisplayedCredentialUrl(){
        webElementUtilsService.delay(this.displayedCredentialUrl);
        return this.displayedCredentialUrl.getText();
    }

    public String getDisplayedCredentialUsername(){
        webElementUtilsService.delay(this.displayedCredentialUsername);
        return this.displayedCredentialUsername.getText();
    }

    public String getDisplayedCredentialPassword(){
        webElementUtilsService.delay(this.displayedCredentialPassword);
        return this.displayedCredentialPassword.getText();
    }

    public String getDisplayedDecryptedCredentialPassword(){
        webElementUtilsService.clickButton(credentialsTab);
        webElementUtilsService.clickButton(editCredentialButton);
        webElementUtilsService.delay(this.editCredentialPassword);
        return this.editCredentialPassword.getAttribute("value");
    }

}
