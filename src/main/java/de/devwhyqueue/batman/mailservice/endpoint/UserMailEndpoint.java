package de.devwhyqueue.batman.mailservice.endpoint;

import de.devwhyqueue.batman.mailservice.model.MailData;
import de.devwhyqueue.batman.mailservice.service.MailService;
import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserMailEndpoint {

  private MailService mailService;

  public UserMailEndpoint(MailService mailService) {
    this.mailService = mailService;
  }

  @PostMapping("/users/activate")
  public ResponseEntity<Void> sendActivationMail(@Valid @RequestBody MailData mailData) {
    try {
      this.mailService.sendActivationEmail(mailData);
      return ResponseEntity.ok().build();
    } catch (MailException | MessagingException e) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
          "Could not send activation mail!");
    }
  }
}
