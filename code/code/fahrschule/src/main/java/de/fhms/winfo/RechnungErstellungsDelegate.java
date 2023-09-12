package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("rechnungErstellungsService")
public class RechnungErstellungsDelegate implements JavaDelegate {
    private static final int rechnungBetragFahrstunde = 35;
    private static final int grundgebuehr = 350;

    protected static Logger LOG = LoggerFactory.getLogger(RechnungErstellungsDelegate.class);


    public void execute(DelegateExecution execution) throws Exception {
        int anzahlFahrstunden = 0;
        int frakturierteFahrstunden;
        int offeneGrundgebuehr = 0;
        int offeneFahrstundengebuehr;
        boolean frakturierteGrundgebuehr;
        int rechnungGrundgebuehr = 0;
        int bezahlteFahrstunden;

        offeneGrundgebuehr = (int) execution.getVariable("offeneGrundgebuehr");
        frakturierteGrundgebuehr = (boolean) execution.getVariable("frakturierteGrundgebuehr");
        frakturierteFahrstunden = (int) execution.getVariable("frakturierteFahrstunden");
        bezahlteFahrstunden = (int) execution.getVariable("gezahlteFahrstunden");
        anzahlFahrstunden = (int) execution.getVariable("anzahlFahrstunden");
        offeneFahrstundengebuehr = (int) execution.getVariable("offeneFahrstundengebuehr");



        if (!frakturierteGrundgebuehr) {
            frakturierteGrundgebuehr = true;
            // grundgebühr 350€
            offeneGrundgebuehr += grundgebuehr;
            rechnungGrundgebuehr += grundgebuehr;
            execution.setVariable("frakturierteGrundgebuehr", true);
        }
        int rechnungsFahrstunden = 0;
        if (anzahlFahrstunden - frakturierteFahrstunden > 0) {
            rechnungsFahrstunden = anzahlFahrstunden - frakturierteFahrstunden;
        }

        int rechnungsbetragFahrstunden = rechnungsFahrstunden * rechnungBetragFahrstunde;
        int rechnungsbetrag = rechnungsbetragFahrstunden + rechnungGrundgebuehr;
        if(rechnungsbetrag == 0) {
            LOG.warn("In diesem Monat beträgt der Rechnungsbetrag 0€, daher wird keine Rechnung versendet! ");
            execution.setVariable("rechnungBetrag", rechnungsbetrag);
            throw new BpmnError("no-amount");
        }

        execution.setVariable("frakturierteFahrstunden", frakturierteFahrstunden + rechnungsFahrstunden);
        execution.setVariable("gezahlteFahrstunden", bezahlteFahrstunden);
        execution.setVariable("rechnungFahrstunden", rechnungsFahrstunden);
        execution.setVariable("rechnungBetrag", rechnungsbetrag);
        execution.setVariable("offeneFahrstundengebuehr", offeneFahrstundengebuehr + rechnungsbetragFahrstunden);
        execution.setVariable("rechnungGundgebuehr", rechnungGrundgebuehr);
        execution.setVariable("rechnungBetragFahrstunde", rechnungBetragFahrstunde);
        execution.setVariable("offeneGrundgebuehr", offeneGrundgebuehr);


    }
}
