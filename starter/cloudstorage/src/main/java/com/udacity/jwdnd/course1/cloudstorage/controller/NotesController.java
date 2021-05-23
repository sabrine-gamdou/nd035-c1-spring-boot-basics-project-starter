package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private final NoteService noteService;
    private final UserService userService;

    public String noteError = null;
    public String noteErrorMessage = null;
    public String noteSuccess = null;
    public String noteSuccessMessage = null;

    public NotesController(NoteService noteService, UserService userService){
        this.noteService = noteService;
        this.userService = userService;
    }

    /* It creates a new model object. */
    @PostMapping
    public String createNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes,
                             Authentication authentication){
        this.noteError = null;
        this.noteErrorMessage = null;
        this.noteSuccess = null;
        this.noteSuccessMessage = null;
        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = noteService.createNote(note);
        if( rowsAdded < 0 ){
            this.noteErrorMessage = "There was an error creating the note, please try again.";
        }
        if(noteError == null){
            redirectAttributes.addFlashAttribute("noteSuccess",true);
            redirectAttributes.addFlashAttribute("noteSuccessMessage", "Note created successfully!");
        }else{
            redirectAttributes.addFlashAttribute("noteError", true);
            redirectAttributes.addFlashAttribute("noteErrorMessage",this.noteErrorMessage);
        }
        return "redirect:/home";
    }

    /* It updates model object. */
    @PutMapping
    public String updateNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes,
                            Authentication authentication){
        this.noteError = null;
        this.noteErrorMessage = null;
        this.noteSuccess = null;
        this.noteSuccessMessage = null;

        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsUpdated = noteService.updateNote(note);
        if (rowsUpdated < 0){
            this.noteErrorMessage = "There was an error for updating a note. Please try again";
        }
        if(noteError == null){
            redirectAttributes.addFlashAttribute("noteSuccess",true);
            redirectAttributes.addFlashAttribute("noteSuccessMessage", "Note updated successfully!");
        }else{
            redirectAttributes.addFlashAttribute("noteError", true);
            redirectAttributes.addFlashAttribute("noteErrorMessage",this.noteErrorMessage);
        }
        return "redirect:/home";
    }

    /* It deletes model object. */
    @DeleteMapping
    public String deleteNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes,
                             Authentication authentication){
        this.noteError = null;
        this.noteErrorMessage = null;
        this.noteSuccess = null;
        this.noteSuccessMessage = null;

        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int noteId = note.getNoteId();
        System.out.println("This is the ID: " + noteId);
        int rowsUpdated = noteService.deleteNote(noteId);
        if (rowsUpdated < 0){
            this.noteErrorMessage = "There was an error deleting a note. Please try again";
        }
        if(noteError == null){
            redirectAttributes.addFlashAttribute("noteSuccess",true);
            redirectAttributes.addFlashAttribute("noteSuccessMessage", "Note deleted successfully!");
        }else{
            redirectAttributes.addFlashAttribute("noteError", true);
            redirectAttributes.addFlashAttribute("noteErrorMessage",this.noteErrorMessage);
        }

        return "redirect:/home";
    }


}
