package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationInfoDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    public void execute(DelegateExecution execution) throws Exception {
        String customer = (String) execution.getVariable("vorname");
        String to = (String) execution.getVariable("fahrschuelerEmail");
        String subject = "Registriegungsbestätigung und TÜV-Information";
        String message = "Hallo " + customer + ",\n\n" +
                         "hiermit bestätigen wir dir gerne deine Registrierung bei unserer Fahrschule. "+
                         "Wir möchten dich auch im Vorhinein darüber informieren, dass du deinen TÜV Antrag stellts. Das dazugehörige Formular kannst du bei deiner nächsten Theoriestunde bei uns in der Fahrschule  einfach mitnehmen.\n\n" +
                         "Viele Grüße,\nDeine Fahrschule Müller";
        emailService.sendEmail(to, subject, message);
    }
}
