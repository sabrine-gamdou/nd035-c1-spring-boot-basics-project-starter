package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessageWriter;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessages;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FilesController {

    private final FileService fileService;
    private final UserService userService;

    private final long MAXIMUM_FILESIZE = 134217728L;

    private String filesize = (MAXIMUM_FILESIZE / (1024 * 1024)) + "";

    private FeedbackMessageWriter feedbackMessageWriter;

    private String fileError = null;
    private String fileErrorMessage = null;
    private String fileSuccess = null;
    private String fileSuccessMessage = null;

    public FilesController(FileService fileService, UserService userService){
        this.fileService = fileService;
        this.userService = userService;
        feedbackMessageWriter = new FeedbackMessageWriter();
    }

    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Integer fileId) {

        File file = fileService.findFileById(fileId);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + file.getFilename());

        ByteArrayResource resource = new ByteArrayResource((file.getFileData()));
        return ResponseEntity.ok()
                .headers(header)
                .body(resource);
    }

    @PostMapping("/upload")
    public String createFile(@RequestParam("fileUpload") MultipartFile multipartFile, RedirectAttributes redirectAttributes,
                             Authentication authentication) throws IOException, SizeLimitExceededException {
        this.fileError = null;
        this.fileErrorMessage = null;
        this.fileSuccess = null;
        this.fileSuccessMessage = null;

        int userId = userService.getUser(authentication.getName()).getUserId();
        if ( multipartFile.getSize() > this.MAXIMUM_FILESIZE){
            throw new SizeLimitExceededException(FeedbackMessages.FILE_SIZE_LIMIT_EXCEEDED);
        }
        if (this.fileService.isFileNameAvailable(multipartFile, userId) && !multipartFile.isEmpty()) {
            try {
                fileService.createFile(new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                        multipartFile.getSize(), userId, multipartFile.getBytes()));
                feedbackMessageWriter.redirectMessages(redirectAttributes,"fileSuccess",
                        FeedbackMessages.FILE_CREATED_SUCCESSFULLY);
                return "redirect:/home";

            } catch (Exception e) {
                feedbackMessageWriter.redirectMessages(redirectAttributes,"fileError",
                        e.toString());
                return "redirect:/home";
            }
        } else if (multipartFile.isEmpty()) {
            feedbackMessageWriter.redirectMessages(redirectAttributes,"fileError",
                    FeedbackMessages.FILE_SHOULD_NOT_BE_EMPTY);
            return "redirect:/home";
        } else {
            feedbackMessageWriter.redirectMessages(redirectAttributes,"fileError",
                    FeedbackMessages.FILE_NAME_ALREADY_USED);
            return "redirect:/home";
        }
    }

    @DeleteMapping
    public String deleteFile(@ModelAttribute File file, RedirectAttributes redirectAttributes,
                             Authentication authentication){
        this.fileError = null;
        this.fileErrorMessage = null;
        this.fileSuccess = null;
        this.fileSuccessMessage = null;

        file.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = fileService.deleteFile(file.getFileId());
        if( rowsAdded < 0 ){
            fileErrorMessage = FeedbackMessages.FILE_DELETION_ERROR;
        }
        if(fileError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"fileSuccess",
                    FeedbackMessages.FILE_DELETED_SUCCESSFULLY);
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"fileError",
                    this.fileErrorMessage);
        }

        return "redirect:/home";
    }

    @ExceptionHandler({SizeLimitExceededException.class, MaxUploadSizeExceededException.class})
    public String handleException(RedirectAttributes redirectAttributes){
        this.fileError = null;
        this.fileErrorMessage = null;

        fileErrorMessage = FeedbackMessages.FILE_SIZE_LIMIT_EXCEEDED+ ". Please upload file with size smaller than " + this.filesize + " MB";
        feedbackMessageWriter.redirectMessages(redirectAttributes,"fileError",
                this.fileErrorMessage);

        return "redirect:/home";
    }
}
