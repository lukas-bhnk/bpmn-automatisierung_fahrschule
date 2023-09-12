package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppInfoDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    public void execute(DelegateExecution execution) throws Exception {
        String customer = (String) execution.getVariable("vorname");
        String to = (String) execution.getVariable("fahrschuelerEmail");
        String userName = (String) execution.getVariable("userName");
        String userPassword = (String) execution.getVariable("userPassword");
        String subject = "Einlogdaten Fahrschul-App";
        String message = "Hallo " + customer + ",\n\n" +
                         "deine Einlog-Daten für die Fahrschul-App sind:"+
                         "Username:" + userName + "\n" +
                         "Passwort:" + userPassword + "\n\n" +
                         "Viele Grüße,\nDeine Fahrschule Müller";
        emailService.sendEmail(to, subject, message);
    }
}
