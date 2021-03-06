package de.devwhyqueue.batman.mailservice.service;

import de.devwhyqueue.batman.mailservice.model.MailData;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MailService {

  private final Logger log = LoggerFactory.getLogger(MailService.class);

  @Value("${spring.mail.username}")
  private String from;

  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;

  public MailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
    this.javaMailSender = javaMailSender;
    this.templateEngine = templateEngine;
  }

  public void sendActivationEmail(MailData mailData) throws de.devwhyqueue.batman.mailservice.service.exception.MailException {
    log.debug("Sending activation email to '{}'", mailData.getEmail());
    sendEmailFromTemplate(mailData, "activationMail", "Stauseepokal - Aktiviere dein Konto");
  }

  private void sendEmailFromTemplate(MailData mailData, String templateName, String subject)
      throws de.devwhyqueue.batman.mailservice.service.exception.MailException {
      Context context = new Context();
      context.setVariable("mailData", mailData);
      String content = templateEngine.process(templateName, context);
      sendEmail(mailData.getEmail(), subject, content, false, true);

  }

  private void sendEmail(String to, String subject, String content, boolean isMultipart,
      boolean isHtml) throws de.devwhyqueue.batman.mailservice.service.exception.MailException {
    log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart, isHtml, to, subject, content);

    try {
      // Prepare message using a Spring helper
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart,
          StandardCharsets.UTF_8.name());
      message.setTo(to);
      message.setFrom(from);
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);
      log.debug("Sent email to User '{}'", to);
    }catch (MessagingException | MailException e) {
      log.error("Could not send mail! Message was: {}", e.getMessage());
      throw new de.devwhyqueue.batman.mailservice.service.exception.MailException();
    }
  }
}
