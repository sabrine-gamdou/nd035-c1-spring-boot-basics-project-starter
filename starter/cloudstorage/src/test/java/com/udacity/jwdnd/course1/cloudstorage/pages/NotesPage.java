package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.utils.WebElementUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotesPage {

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

    /* Add,edit,delete buttons on the notes home tab*/
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

    private WebElementUtils webElementUtils;

    private final WebDriver driver;

    public NotesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.webElementUtils = new WebElementUtils();
        this.webElementUtils.setDriver(this.driver);
    }

    public void createNote(String title, String description){
        webElementUtils.clickButton(notesTab);
        webElementUtils.clickButton(addNoteButton);
        webElementUtils.enterValue(this.noteTitle,title);
        webElementUtils.enterValue(this.noteDescription,description);
        webElementUtils.clickButton(noteSubmit);
        webElementUtils.clickButton(notesTab);
    }

    public void editNote(String title, String description){
        webElementUtils.clickButton(notesTab);
        webElementUtils.clickButton(editNoteButton);
        webElementUtils.enterValue(this.editNoteTitle,title);
        webElementUtils.enterValue(this.editNoteDescription,description);
        webElementUtils.clickButton(editNoteSubmit);
        webElementUtils.clickButton(notesTab);
    }

    public void deleteNote(){
        webElementUtils.clickButton(notesTab);
        webElementUtils.clickButton(deleteNoteButton);
        webElementUtils.clickButton(deleteNoteSubmit);
        webElementUtils.clickButton(notesTab);
    }

    public String getDisplayedNoteTitle(){
        webElementUtils.delay(this.displayedNoteTitle);
        return this.displayedNoteTitle.getText();
    }

    public String getDisplayedNoteDescription(){
        webElementUtils.delay(this.displayedNoteDescription);
        return this.displayedNoteDescription.getText();
    }

}
