package com.server.sftp_using_spring_integration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;

@Configuration
public class SftpConfig {

    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port}")
    private int sftpPort;

    @Value("${sftp.user}")
    private String sftpUser;

    @Value("${sftp.password}")
    private String sftpPassword;

    @Value("${sftp.remote-directory}")
    private String sftpRemoteDirectory;

    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost(sftpHost);
        factory.setPort(sftpPort);
        factory.setUser(sftpUser);
        factory.setPassword(sftpPassword);
        factory.setAllowUnknownKeys(true);
        return factory;
    }

    @Bean
    public MessageChannel sftpChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "sftpChannel")
    public MessageHandler sftpMessageHandler() {
        FileTransferringMessageHandler<?> handler = new FileTransferringMessageHandler<>(sftpSessionFactory());
        handler.setRemoteDirectoryExpressionString("'" + sftpRemoteDirectory + "'");
        handler.setLoggingEnabled(true);
        return handler;
    }

    @Bean
    public MessagingTemplate messagingTemplate() {
        return new MessagingTemplate(sftpChannel());
    }
}
