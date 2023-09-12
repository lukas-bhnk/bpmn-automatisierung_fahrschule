package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class StartVariablesListener implements ExecutionListener {

    public void notify(DelegateExecution delegateExecution) {
        delegateExecution.setVariable("anzahlFahrstunden", 0);
        delegateExecution.setVariable("anzahlTheoriestunden", 0);
        delegateExecution.setVariable("anzahlZusatzstunden", 0);
        delegateExecution.setVariable("gezahlteFahrstunden", 0);
        delegateExecution.setVariable("offeneGrundgebuehr", 0);
        delegateExecution.setVariable("frakturierteFahrstunden", 0);
        delegateExecution.setVariable("frakturierteGrundgebuehr", false);
        delegateExecution.setVariable("rechnungFahrstunden", 0);
        delegateExecution.setVariable("rechnungBetrag", 0);
        delegateExecution.setVariable("rechnungGundgebuehr", 0);
        delegateExecution.setVariable("rechnungBetragFahrstunde", 0);
        delegateExecution.setVariable("anzahlUeberlandfahrten", 0);
        delegateExecution.setVariable("anzahlNachtfahrten", 0);
        delegateExecution.setVariable("anzahlAutobahnfahrten", 0);
        delegateExecution.setVariable("offeneFahrstundengebuehr", 0);




    }
}
