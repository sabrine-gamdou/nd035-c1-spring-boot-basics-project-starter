package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.utils.WebElementUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    private WebElementUtils webElementUtils;

    private final WebDriver driver;

    public CredentialsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.webElementUtils = new WebElementUtils();
        this.webElementUtils.setDriver(this.driver);
    }

    public void createCredential(String url, String username, String password){
        webElementUtils.clickButton(credentialsTab);
        webElementUtils.clickButton(addCredentialButton);
        webElementUtils.enterValue(this.credentialUrl,url);
        webElementUtils.enterValue(this.credentialUsername,username);
        webElementUtils.enterValue(this.credentialPassword,password);
        webElementUtils.clickButton(credentialSubmit);
        webElementUtils.clickButton(credentialsTab);
    }

    public void editCredential(String url, String username, String password){
        webElementUtils.clickButton(credentialsTab);
        webElementUtils.clickButton(editCredentialButton);
        webElementUtils.enterValue(this.editCredentialUrl,url);
        webElementUtils.enterValue(this.editCredentialUsername,username);
        webElementUtils.enterValue(this.editCredentialPassword,password);
        webElementUtils.clickButton(editCredentialSubmit);
        webElementUtils.clickButton(credentialsTab);
    }

    public void deleteCredential(){
        webElementUtils.clickButton(credentialsTab);
        webElementUtils.clickButton(deleteCredentialButton);
        webElementUtils.clickButton(deleteCredentialSubmit);
        webElementUtils.clickButton(credentialsTab);
    }

    public String getDisplayedCredentialUrl(){
        webElementUtils.delay(this.displayedCredentialUrl);
        return this.displayedCredentialUrl.getText();
    }

    public String getDisplayedCredentialUsername(){
        webElementUtils.delay(this.displayedCredentialUsername);
        return this.displayedCredentialUsername.getText();
    }

    public String getDisplayedCredentialPassword(){
        webElementUtils.delay(this.displayedCredentialPassword);
        return this.displayedCredentialPassword.getText();
    }

    public String getDisplayedDecryptedCredentialPassword(){
        webElementUtils.clickButton(credentialsTab);
        webElementUtils.clickButton(editCredentialButton);
        webElementUtils.delay(this.editCredentialPassword);
        return this.editCredentialPassword.getAttribute("value");
    }

}
