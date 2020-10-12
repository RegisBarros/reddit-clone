package com.redditclone.services;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class MailContentBuilder {
    private final TemplateEngine templateEngine;

    String build(String message) {
        var context = new Context();
        context.setVariable("message", message);

        return templateEngine.process("mailTemplate", context);
    }
}
