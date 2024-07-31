package com.server.sftp_using_spring_integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SftpService {

    private static final Logger logger = LoggerFactory.getLogger(SftpService.class);

    @Autowired
    private MessagingTemplate messagingTemplate;

    public void sendFile(File file) {
        try {
            Message<File> message = MessageBuilder.withPayload(file).build();
            messagingTemplate.send(message);
            logger.info("File {} uploaded successfully.", file.getName());
        } catch (Exception e) {
            logger.error("Failed to upload file {}. Error: {}", file.getName(), e.getMessage(), e);
            throw new RuntimeException("Failed to upload file via SFTP", e);
        }
    }
}
