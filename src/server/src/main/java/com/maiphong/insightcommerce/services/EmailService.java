package com.maiphong.insightcommerce.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.maiphong.insightcommerce.dtos.email.EmailRequestDTO;
import com.maiphong.insightcommerce.exceptions.EmailException;

import jakarta.mail.internet.MimeMessage;

@Service
@Transactional
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender,
            TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmailAsync(EmailRequestDTO requestDTO) {
        if (requestDTO.getTo() == null || requestDTO.getTo().isEmpty()) {
            throw new IllegalArgumentException("Email recipient is required");
        }

        if (requestDTO.getSubject() == null || requestDTO.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Email subject is required");
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(requestDTO.getTo());
            helper.setSubject(requestDTO.getSubject());
            helper.setText(generateEmailBody(requestDTO.getTemplateName(), requestDTO), true);

            if (requestDTO.getCc() != null && !requestDTO.getCc().isEmpty()) {
                helper.setCc(requestDTO.getCc());
            }

            if (requestDTO.getBcc() != null && !requestDTO.getBcc().isEmpty()) {
                helper.setBcc(requestDTO.getBcc());
            }

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send email: " + e.getMessage());
        }
    }

    private String generateEmailBody(String templateName, EmailRequestDTO request) {
        Context context = new Context();

        context.setVariables(request.getVariables());

        return templateEngine.process(templateName, context);
    }

}
