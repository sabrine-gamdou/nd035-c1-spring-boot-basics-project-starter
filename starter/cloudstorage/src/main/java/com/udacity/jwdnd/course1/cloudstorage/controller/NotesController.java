package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private final NoteService noteService;
    private final UserService userService;

    public String noteError = null;
    public String noteSuccess = null;

    public NotesController(NoteService noteService, UserService userService){
        this.noteService = noteService;
        this.userService = userService;
    }

    /* It creates a new model object. */
    @PostMapping
    public String createNote(@ModelAttribute Note note, Model model,
                             Authentication authentication){
        this.noteSuccess = null;
        this.noteError = null;
        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = noteService.createNote(note);
        if( rowsAdded < 0 ){
            noteError = "There was an error creating the note, please try again.";
        }
        if(noteError == null){
            model.addAttribute("noteSuccess", "Note created successfully!");
        }else{
            model.addAttribute("noteError", noteError);
        }
        return "redirect:/home";
    }

    /* It updates model object. */
    @PutMapping
    public String updateNote(@ModelAttribute Note note, Model model,
                            Authentication authentication){
        this.noteError = null;
        this.noteSuccess = null;
        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsUpdated = noteService.updateNote(note);
        if (rowsUpdated < 0){
            this.noteError = "There was an error for updating a note. Please try again";
        }
        if (this.noteError == null) {
            model.addAttribute("noteSuccess", "Note updated successfully!");
        } else {
            model.addAttribute("noteError", this.noteError);
        }
        return "redirect:/home";
    }

    /* It deletes model object. */
    @DeleteMapping
    public String deleteNote(@ModelAttribute Note note, Model model,
                             Authentication authentication){
        this.noteError = null;
        this.noteSuccess = null;
        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int noteId = note.getNoteId();
        System.out.println("This is the ID: " + noteId);
        int rowsUpdated = noteService.deleteNote(noteId);
        if (rowsUpdated < 0){
            this.noteError = "There was an error deleting a note. Please try again";
        }
        if (this.noteError == null) {
            model.addAttribute("noteSuccess", "You successfully deleted a note!");
        } else {
            model.addAttribute("noteError", this.noteError);
        }

        return "redirect:/home";
    }


}
