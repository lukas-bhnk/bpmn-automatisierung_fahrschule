package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RechnungVersendungsDelegate implements JavaDelegate {

    @Autowired
    private EmailService emailService;

    public void execute(DelegateExecution execution) throws Exception {
        String vorname = (String) execution.getVariable("vorname");
        String to = (String) execution.getVariable("fahrschuelerEmail");
        int rechnungFahrstunden = (int) execution.getVariable("rechnungFahrstunden");
        int rechnungGrundgebuehr = (int) execution.getVariable("rechnungGundgebuehr");
        int rechnungBetragFahrstunde = (int) execution.getVariable("rechnungBetragFahrstunde");
        int rechnungBetrag = (int) execution.getVariable("rechnungBetrag");


        String subject = "Rechnung - Fahrschule";
        String message = "Hallo " + vorname + ",\n\n" +
                "hiermit stellen wir unsere folgenden Leistungen dir in Rechnung: \n"+
                rechnungFahrstunden + " Fahrstunden mit je " + rechnungBetragFahrstunde + " ergibt " + (rechnungFahrstunden * rechnungBetragFahrstunde) + " (in Eur) \n"+
                "Grundgebühr: " + rechnungGrundgebuehr +  " (in Eur)\n" +
                "gesamter Rechnungsbetrag: " + rechnungBetrag + " (in Eur)\n\n "  +
                "Die Grundgebühr ist einmalig für die erste Rechnung zu zahlen. Sie enthält alle Theoriestunden, der Zugang zur Fahrschul-App sowie weitere Verwaltungskosten für die Fahrschule. " + "\n\n" +
                "Viele Grüße,\nDeine Fahrschule Müller";
        emailService.sendEmail(to, subject, message);

    }
}
