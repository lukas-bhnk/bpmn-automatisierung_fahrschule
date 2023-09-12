/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fhms.winfo;

import org.camunda.bpm.client.spring.SpringTopicSubscription;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.spring.boot.starter.ClientProperties;
import org.camunda.bpm.client.spring.event.SubscriptionInitializedEvent;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Configuration
public class HandlerConfiguration {

  @Autowired
  private EmailService emailService;
  
  protected static Logger LOG = LoggerFactory.getLogger(HandlerConfiguration.class);

  protected String workerId;

  public HandlerConfiguration(ClientProperties properties) {
    workerId = properties.getWorkerId();
  }

  @ExternalTaskSubscription("alterChecker")
  @Bean
  public ExternalTaskHandler alterChecker() {
    return (externalTask, externalTaskService) -> {

      Date date = (Date) externalTask.getVariable("geburtsdatum");
      String fuehrerscheinklasse = (String) externalTask.getVariable("fuehrerscheinklasse");
      LocalDate birthDate = date.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDate();
      LocalDate now = LocalDate.now();
      long years = ChronoUnit.YEARS.between(birthDate, now);
      boolean boolAlter;

      if( (years < 17 && fuehrerscheinklasse.equals("B")) || (years < 16)){
        boolAlter = false;
      }
      else{
        boolAlter = true;
      }


      // complete the external task
      externalTaskService.complete(externalTask, Variables.putValue("alterInOrdnung", boolAlter));

      LOG.info("{}: Die External Task {} hat das Alter überprüft!", workerId, externalTask.getId());
    };
  }

  @ExternalTaskSubscription("anmeldungInfo")
  @Bean
  public ExternalTaskHandler anmeldungsInfo() {
    return (externalTask, externalTaskService) -> {
      String vorname = (String) externalTask.getVariable("vorname");
      String to = (String) externalTask.getVariable("fahrschuelerEmail");
      String subject = "Registriegungsbestätigung und TÜV-Information";
      String message = "Hallo " + vorname + ",\n\n" +
              "hiermit bestätigen wir dir gerne deine Registrierung bei unserer Fahrschule. "+
              "Wir möchten dich auch im Vorhinein darüber informieren, dass du deinen TÜV Antrag stellts. Das dazugehörige Formular kannst du bei deiner nächsten Theoriestunde bei uns in der Fahrschule  einfach mitnehmen.\n\n" +
              "Viele Grüße,\nDeine Fahrschule Müller";
      emailService.sendEmail(to, subject, message);
      externalTaskService.complete(externalTask);
      LOG.info("{}: Die External Task {} hat das Alter erfolgreich verifiziert!", workerId, externalTask.getId());
    };
  }

  @ExternalTaskSubscription("ablehnungInfo")
  @Bean
  public ExternalTaskHandler ablehnungsInfo() {
    return (externalTask, externalTaskService) -> {
      String customer = (String) externalTask.getVariable("vorname");
      String to = (String) externalTask.getVariable("fahrschuelerEmail");
      String subject = "Absage - Anmeldung Fahrschule";
      String message = "Hallo " + customer + ",\n\n" +
              "leider müssen wir dir mitteilen, dass du zu jung bist, um dich für die Fahrschule bei uns anzumelden."+
              "Das Mindestalter für die Anmeldung bei uns ist für die Füherscheinklasse BF17 16 und für die Führerscheinklasse B 17.\n\n" +
              "Viele Grüße,\nDeine Fahrschule Müller";
      emailService.sendEmail(to, subject, message);
      externalTaskService.complete(externalTask);
      LOG.info("{}: Die External Task {} hat die Anfrage aufgrund des Alters abgelehnt!", workerId, externalTask.getId());
    };
  }

  @EventListener(SubscriptionInitializedEvent.class)
  public void catchSubscriptionInitEvent(SubscriptionInitializedEvent event) {

    SpringTopicSubscription topicSubscription = event.getSource();
    if (!topicSubscription.isAutoOpen()) {

      // open topic in case it is not opened already
      topicSubscription.open();

      LOG.info("Subscription with topic name '{}' has been opened!",
          topicSubscription.getTopicName());
    }
  }

}
