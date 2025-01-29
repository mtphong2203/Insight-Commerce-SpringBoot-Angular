package com.maiphong.insightcommerce.dtos.email;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    private String templateName;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private Map<String, Object> variables;

}
