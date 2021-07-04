package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessages;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private UserService userService;

    public SignupController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String addUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) throws InterruptedException {
        String signUpError = null;
        String signupSuccessMessage = null;
        if(!userService.isUsernameAvailable(user.getUsername())){
            signUpError = FeedbackMessages.USERNAME_USED;
        }
        if(signUpError == null){
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signUpError = FeedbackMessages.USER_SIGNUP_ERROR;
            }
        }

        if (signUpError == null){
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("signupSuccessMessage", FeedbackMessages.USER_SIGNED_UP_SUCCESSFULLY);
            return "redirect:/login";
        }else{
            model.addAttribute("signupError", signUpError);
        }

        return "signup";
    }
}
