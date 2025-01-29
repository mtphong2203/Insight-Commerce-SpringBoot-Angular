package com.maiphong.insightcommerce.services;

import org.springframework.scheduling.annotation.Async;

import com.maiphong.insightcommerce.dtos.email.EmailRequestDTO;

public interface IEmailService {
    @Async
    void sendEmailAsync(EmailRequestDTO requestDTO);
}
