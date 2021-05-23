package com.udacity.jwdnd.course1.cloudstorage.pages;


import com.udacity.jwdnd.course1.cloudstorage.utils.WebElementUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(css = "#logout")
    private WebElement logout;

    private WebElementUtils webElementUtils;

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.webElementUtils = new WebElementUtils();
        this.webElementUtils.setDriver(this.driver);
    }

    public void logout(){
        webElementUtils.delay(this.logout);
        webElementUtils.clickButton(logout);
    }

}
