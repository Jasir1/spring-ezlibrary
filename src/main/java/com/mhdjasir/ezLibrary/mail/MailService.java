package com.mhdjasir.ezLibrary.mail;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.mhdjasir.ezLibrary.dto.AccountCredentialMailDTO;
import com.mhdjasir.ezLibrary.dto.ForgotPasswordMailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String mailSenderUsername;

    public void sendAccountCredentialMail(String subject, String to, String name, String password) {
        try {
            File templateFile = ResourceUtils.getFile("classpath:user-credentials.hbs");
            String context = Files.readString(templateFile.toPath());

            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(context);
            AccountCredentialMailDTO emailTemplateDTO = new AccountCredentialMailDTO(name, to, password);
            String html = template.apply(emailTemplateDTO);

            sendMail(subject,to,html);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendForgotPasswordMail(String subject, String to, String name, String resetLink) {
        try {
            File templateFile = ResourceUtils.getFile("classpath:forgot-password.hbs");
            String context = Files.readString(templateFile.toPath());

            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(context);
            ForgotPasswordMailDTO emailTemplateDTO = new ForgotPasswordMailDTO(name, resetLink);
            String html = template.apply(emailTemplateDTO);

            sendMail(subject,to,html);

        } catch ( IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMail(String subject, String to, String html){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(mailSenderUsername);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            //FileSystemResource file = new FileSystemResource(new File("path/to/pdf"));
            //helper.addAttachment("File Name",file);

            mailSender.send(mimeMessage);

        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }
}
