package com.udacity.jwdnd.course1.cloudstorage.pages;


import com.udacity.jwdnd.course1.cloudstorage.service.utils.WebElementUtilsService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HomePage {

    @FindBy(css = "#logout")
    private WebElement logout;

    @Autowired
    private WebElementUtilsService webElementUtilsService;

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.webElementUtilsService = new WebElementUtilsService();
        this.webElementUtilsService.setDriver(this.driver);
    }

    public void logout(){
        webElementUtilsService.delay(this.logout);
        webElementUtilsService.clickButton(logout);
    }

}
