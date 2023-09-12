package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PraxisstundeUpdateDelegate implements JavaDelegate {

    protected static Logger LOG = LoggerFactory.getLogger(PraxisstundeUpdateDelegate.class);

    public void execute(DelegateExecution execution) throws Exception {
        String fahrstundeTyp = (String) execution.getVariable("fahrstundeTyp");
        int anzahlFahrstunden = (int) execution.getVariable("anzahlFahrstunden");

        if(fahrstundeTyp.equals("landfahrt")) {
            int anzahlUeberlandfahrten = (int) execution.getVariable("anzahlUeberlandfahrten");
            anzahlUeberlandfahrten += 1;
            execution.setVariable("anzahlUeberlandfahrten", anzahlUeberlandfahrten);
        }
        if(fahrstundeTyp.equals("autobahnfahrt")) {
            int anzahlAutobahnfahrten = (int) execution.getVariable("anzahlAutobahnfahrten");
            anzahlAutobahnfahrten += 1;
            execution.setVariable("anzahlAutobahnfahrten", anzahlAutobahnfahrten);
        }

        if(fahrstundeTyp.equals("nachtfahrt")) {
            int anzahlNachtfahrten = (int) execution.getVariable("anzahlNachtfahrten");
            anzahlNachtfahrten += 1;
            execution.setVariable("anzahlNachtfahrten", anzahlNachtfahrten);
        }
        anzahlFahrstunden += 1;

        execution.setVariable("anzahlFahrstunden", anzahlFahrstunden);
    }
}
