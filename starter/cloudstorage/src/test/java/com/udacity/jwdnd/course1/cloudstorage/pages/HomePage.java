package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    /*2. Write Tests for Note Creation, Viewing, Editing, and Deletion.

        Write a test that deletes a note and verifies that the
        note is no longer displayed.*/
    @FindBy(css = "#logout")
    private WebElement logout;


    /* Notes */
    /* Notes home tab */
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    /* Title Elements on create and edit tabs*/
    @FindBy(css = "#note-title")
    private WebElement noteTitle;

    @FindBy(css = "#edit-note-title")
    private WebElement editNoteTitle;

    /* Description Elements on create and edit windows*/
    @FindBy(css = "#note-description")
    private WebElement noteDescription;

    @FindBy(css = "#edit-note-description")
    private WebElement editNoteDescription;

    /* Add,edit,delete Elements on the notes home tab*/
    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteButton;

    /* Displayed notes data on the notes home tab*/
    @FindBy(css = "#displayed-note-title")
    private WebElement displayedNoteTitle;

    @FindBy(css = "#displayed-note-description")
    private WebElement displayedNoteDescription;

    /* Submit buttons on create,edit,delete windows */
    @FindBy(id = "note-submit")
    private WebElement noteSubmit;

    @FindBy(css = "#edit-note-submit")
    private WebElement editNoteSubmit;

    @FindBy(css = "#delete-note-submit")
    private WebElement deleteNoteSubmit;

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout(){
        delay(this.logout);
        clickButton(logout);
    }

    public void createNote(String title, String description){
        clickButton(notesTab);
        clickButton(addNoteButton);
        enterValue(this.noteTitle,title);
        enterValue(this.noteDescription,description);
        clickButton(noteSubmit);
        clickButton(notesTab);
    }

    public void editNote(String title, String description){
        clickButton(notesTab);
        clickButton(editNoteButton);
        enterValue(this.editNoteTitle,title);
        enterValue(this.editNoteDescription,description);
        clickButton(editNoteSubmit);
        clickButton(notesTab);
    }

    public void deleteNote(){
        clickButton(notesTab);
        clickButton(deleteNoteButton);
        clickButton(deleteNoteSubmit);
        clickButton(notesTab);
    }

    public String getDisplayedNoteTitle(){
        delay(this.displayedNoteTitle);
        return this.displayedNoteTitle.getText();
    }

    public String getDisplayedNoteDescription(){
        delay(this.displayedNoteDescription);
        return this.displayedNoteDescription.getText();
    }

    private void enterValue(WebElement webElement, String value){
        delay(webElement);
        webElement.clear();
        webElement.sendKeys(value);
    }

    private void clickButton(WebElement webElement){
        delay(webElement);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
    }

    private void delay(WebElement webElement){
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement)).click();
    }

}
