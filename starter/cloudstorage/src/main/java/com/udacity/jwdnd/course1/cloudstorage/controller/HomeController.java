package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, CredentialService credentialService,
                          FileService fileService, UserService userService,
                          EncryptionService encryptionService){
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(Model model, Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        model.addAttribute("notes",noteService.getNotes(user));
        model.addAttribute("credentials", credentialService.getCredentials(user));
        model.addAttribute("files", fileService.getFiles(user));
        model.addAttribute("encryptionService",encryptionService);

        return "home";
    }


}
