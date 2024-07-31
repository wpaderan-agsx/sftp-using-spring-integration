package com.server.sftp_using_spring_integration.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.sftp_using_spring_integration.service.SftpService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/sftp")
public class SftpController {

    private static final Logger logger = LoggerFactory.getLogger(SftpController.class);

    @Autowired
    private SftpService sftpService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload-", ".zip");
            file.transferTo(tempFile);
            sftpService.sendFile(tempFile);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            logger.error("Error occurred while processing the file. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        } catch (Exception e) {
            logger.error("Error occurred during file upload. Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}
