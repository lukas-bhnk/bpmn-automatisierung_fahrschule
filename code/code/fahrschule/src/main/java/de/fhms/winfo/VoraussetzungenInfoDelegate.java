package de.fhms.winfo;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoraussetzungenInfoDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    public void execute(DelegateExecution execution) throws Exception {
        String userId = (String) execution.getVariable("fahrlehrer");
        String fahrschueler = (String) execution.getVariable("fahrschueler");
        IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        String subject = "Voraussetzungen Fahrschüler: " + fahrschueler;
        String message = "Hallo, " + ",\n\n" +
                         "der o.g. fahrschüler hat leider nicht alle Mindestvoraussetzungen zur Prüfung im System erfüllt. \n" +
                "Der Fahrschüler benötigt für die Theorieprüfung mind. 12 Theoriestunden inkl. zwei Theoriestunden mit Zusatzstoff. Zudem wird der Stand von mind. 90% in der Theorie-App benötigt. \n" +
                "Aktueller Stand des Fahrschülers: \n" + (int) execution.getVariable("anzahlTheoriestunden") + " Theoriestunden \n" +
                (int) execution.getVariable("anzahlZusatzstunden") + " Zusatzstunden \n" +
                (long) execution.getVariable("appProzent") + "% in der App \n \n \n" +
                "Der Fahrschüler benötigt für die Praxisprüfung mind. 12 Praxisstunden, davon müssen 5 Landfahrten, 4 Autobahnfahrten und 3 Nachtfahrten enthalten sein.\n" +
                "Aktueller Stand des Fahrschülers: \n" + (int) execution.getVariable("anzahlFahrstunden") + " Fahrstunden \n" +
                (int) execution.getVariable("anzahlUeberlandfahrten") + " Landfahrten \n" +
                (int) execution.getVariable("anzahlAutobahnfahrten") + " Autobahnfahrten \n" +
                (int) execution.getVariable("anzahlNachtfahrten") + " Nachtfahrten \n" +
                "\n\n" +
                         "Viele Grüße,\n Fahrschule Müller";
        emailService.sendEmail(user.getEmail(), subject, message);
    }
}
