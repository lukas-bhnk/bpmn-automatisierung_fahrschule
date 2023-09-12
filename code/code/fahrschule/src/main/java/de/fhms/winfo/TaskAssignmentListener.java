package de.fhms.winfo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskAssignmentListener implements TaskListener {

    private final static Logger LOGGER = Logger.getLogger(TaskAssignmentListener.class.getName());

    @Autowired
    private EmailService emailService;

    public void notify(DelegateTask delegateTask) {

        String assignee = delegateTask.getAssignee();
        String taskId = delegateTask.getId();

        if (assignee != null) {
            // Get User Profile from User Management:
            IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
            User user = identityService.createUserQuery().userId(assignee).singleResult();

            if (user != null) {
                // get email address from user profile:
                String recipient = user.getEmail();

                if (recipient != null && !recipient.isEmpty()) {
                    String subject = "Task assigned: " + delegateTask.getName();
                    String message = "Please complete the Task: http://localhost:8080/camunda/app/tasklist/default/#/?searchQuery=%5B%5D&filter=1b07490b-7631-11ec-a327-00059a3c7a00&sorting=%5B%7B%22sortBy%22:%22created%22,%22sortOrder%22:%22desc%22%7D%5D&task=" + taskId;
                    emailService.sendEmail(recipient, subject, message);
                } else {
                    LOGGER.warning("Not sending email to user " + assignee + "', user has no email address.");
                }
            } else {
                LOGGER.warning("Not sending email to user " + assignee + "', user is not enrolled with identity service.");
            }
        }
    }
}
