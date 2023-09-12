package de.fhms.winfo;

import org.camunda.bpm.engine.delegate.*;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service("requestProcessor")
public class ProcessRequestDelegate implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger("Fahrschule");

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Processing request by '"
                     + execution.getVariable("fahrschueler") + "'...");
    }
}
