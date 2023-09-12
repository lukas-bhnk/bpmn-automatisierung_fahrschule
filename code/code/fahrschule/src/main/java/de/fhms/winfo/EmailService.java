package de.fhms.winfo;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());

    @Value("${email.host}")    private String host;
    @Value("${email.sender}")  private String sender;

    public void sendEmail(String to, String subject, String message) {

        try {
            Email email = new SimpleEmail();
            email.setHostName(host);
            email.setFrom(sender);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(to);
            email.send();
            LOGGER.info("Email \'" + subject + "\' sent to  " + to + ".");
        } catch (Exception e) {
            LOGGER.warning("Could not send email:" + e.getMessage());
        }
    }
}
