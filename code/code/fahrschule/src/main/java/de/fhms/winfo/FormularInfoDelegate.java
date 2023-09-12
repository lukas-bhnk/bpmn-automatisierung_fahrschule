package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormularInfoDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    public void execute(DelegateExecution execution) throws Exception {
        String customer = (String) execution.getVariable("vorname");
        String to = (String) execution.getVariable("fahrschuelerEmail");
        String userName = (String) execution.getVariable("userName");
        String userPassword = (String) execution.getVariable("userPassword");
        String subject = "Formular - Information";
        String message = "Hallo " + customer + ",\n\n" +
                         "leider müssen wir dir mitteilen, dass wir dein Anmeldungsformular nicht weiter bearbeiten können." + "\n" +
                         "Dein abgegebendes Formular  weist Feheler auf und wir können dieses daher nicht zur Anmeldung berücksichtigen. " + "\n" +
                         "Du kannst gerne ein neues korrektes und vollständiges Formular erstellen. Bei Rückfragen zu den Fehlern stehen wir dir gerne zur Verfügung. \n\n" +
                         "Viele Grüße,\n Fahrschule Müller";
        emailService.sendEmail(to, subject, message);
    }
}
