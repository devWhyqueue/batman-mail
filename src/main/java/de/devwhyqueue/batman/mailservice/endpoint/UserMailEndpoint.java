package de.devwhyqueue.batman.mailservice.endpoint;

import de.devwhyqueue.batman.mailservice.model.MailData;
import de.devwhyqueue.batman.mailservice.security.AuthoritiesConstants;
import de.devwhyqueue.batman.mailservice.service.MailService;
import de.devwhyqueue.batman.mailservice.service.exception.MailException;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.SYSTEM + "\")")
  public ResponseEntity<Void> sendActivationMail(@Valid @RequestBody MailData mailData) {
    try {
      this.mailService.sendActivationEmail(mailData);
      return ResponseEntity.ok().build();
    } catch (MailException e) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
          "Could not send activation mail!");
    }
  }
}
