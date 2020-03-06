package de.devwhyqueue.batman.mailservice.service.exception;

public class MailException extends Exception {

  public MailException() {
    super("Could not send mail!");
  }
}
