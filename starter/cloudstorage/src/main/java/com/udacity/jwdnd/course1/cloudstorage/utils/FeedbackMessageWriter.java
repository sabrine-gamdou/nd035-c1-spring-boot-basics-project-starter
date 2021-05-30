package com.udacity.jwdnd.course1.cloudstorage.utils;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class FeedbackMessageWriter {


    public void redirectMessages(RedirectAttributes redirectAttributes, String attributeName,
                                 String message){
        redirectAttributes.addFlashAttribute(attributeName,true);
        redirectAttributes.addFlashAttribute(attributeName+"Message",message);
        
    }
}
